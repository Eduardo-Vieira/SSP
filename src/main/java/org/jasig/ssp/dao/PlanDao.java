/**
 * Licensed to Apereo under one or more contributor license
 * agreements. See the NOTICE file distributed with this work
 * for additional information regarding copyright ownership.
 * Apereo licenses this file to you under the Apache License,
 * Version 2.0 (the "License"); you may not use this file
 * except in compliance with the License.  You may obtain a
 * copy of the License at the following location:
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */
package org.jasig.ssp.dao;

import com.google.common.collect.Lists;
import org.apache.commons.lang.StringUtils;
import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.jasig.ssp.model.ObjectStatus;
import org.jasig.ssp.model.Person;
import org.jasig.ssp.model.Plan;
import org.jasig.ssp.model.reference.ProgramStatus;
import org.jasig.ssp.service.reference.ConfigService;
import org.jasig.ssp.transferobject.PersonLiteTO;
import org.jasig.ssp.transferobject.reports.*;
import org.jasig.ssp.util.hibernate.BatchProcessor;
import org.jasig.ssp.util.hibernate.NamespacedAliasToBeanResultTransformer;
import org.jasig.ssp.util.sort.PagingWrapper;
import org.jasig.ssp.util.sort.SortingAndPaging;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.util.CollectionUtils;

import java.util.*;

@Repository
public class PlanDao extends AbstractPlanDao<Plan> implements AuditableCrudDao<Plan> {


	@Autowired
	private transient ConfigService configService;
	
	public PlanDao() {
		super(Plan.class);
	}

	@SuppressWarnings("unchecked")
	public List<Plan> getAllForStudent(UUID id)
	{
		List<Plan> plans = createCriteria()
		.add(Restrictions.eq("person.id", id)).list();
		return plans;
	}
	
	public Plan getActivePlanForStudent(UUID id)
	{
		Plan activePlan = (Plan) createCriteria()
		.add(Restrictions.eq("person.id", id))
		.add(Restrictions.eq("objectStatus", ObjectStatus.ACTIVE))
		.uniqueResult();
		return activePlan;
	}

	public List<Plan> getActivePlanForStudents(List<UUID> ids)
	{
		if ( ids == null || ids.size() < 1 ) {
			return new <Plan>ArrayList();
		}

		final BatchProcessor<UUID, Plan> processor = new BatchProcessor<>(ids);
		do {
			final Criteria criteria = createCriteria().add(Restrictions.eq("objectStatus", ObjectStatus.ACTIVE));
			processor.process(criteria, "person.id");

		} while ( processor.moreToProcess() );

		return processor.getUnsortedUnpagedResultsAsList();
	}

	public PagingWrapper<Plan> getAllForStudent(final SortingAndPaging sAndP,UUID personId) {
		Criteria criteria = createCriteria();
		criteria.add(Restrictions.eq("person.id",personId));
		return processCriteriaWithStatusSortingAndPaging(criteria,
				sAndP);
	}

	public int markOldPlansAsInActive(Plan plan) {
		int updatedEntities = 0;
		String markOldPlansAsInActiveBaseQuery = "update Plan p set p.objectStatus = :objectStatus where p.person = :person and p != :plan";
		updatedEntities += createHqlQuery( markOldPlansAsInActiveBaseQuery )
				.setInteger("objectStatus", ObjectStatus.INACTIVE.ordinal() )
				.setEntity("person", plan.getPerson())
				.setEntity("plan", plan)
				.executeUpdate();
		return updatedEntities;
	}
	
	@SuppressWarnings("unchecked")
	public List<PlanAdvisorCountTO> getPlanCountByOwner(SearchPlanTO form){
		Criteria criteria = createCriteria();
		
		if(form.getDateFrom() != null)
			criteria.add(Restrictions.ge("modifiedDate", form.getDateFrom()));
		if(form.getDateTo() != null)
			criteria.add(Restrictions.lt("modifiedDate", form.getDateTo()));
		
		criteria.add(Restrictions.eq("objectStatus", ObjectStatus.ACTIVE));

		criteria.createAlias("owner", "owner");
		criteria.setProjection(Projections.projectionList().
				add(Projections.countDistinct("id").as("plan_entityCount")).
				// cannot just group by owner, else you get a N+1
				// http://stackoverflow.com/questions/4330480/prevent-hibernate-n1-selects-when-grouping-by-an-entity
				// (plus EntityStudentCountByCoachTO doesn't map Persons, just AuditPersons)
				add(Projections.groupProperty("owner.id").as("plan_coachId")).
				add(Projections.groupProperty("owner.firstName").as("plan_coachFirstName")).
				add(Projections.groupProperty("owner.lastName").as("plan_coachLastName")));
		
		List<EntityStudentCountByCoachTO> activePlansByCoaches = criteria.setResultTransformer(
						new NamespacedAliasToBeanResultTransformer(
								EntityStudentCountByCoachTO.class, "plan_")).list();
		
		criteria = createCriteria();
		
		if(form.getDateFrom() != null)
			criteria.add(Restrictions.ge("modifiedDate", form.getDateFrom()));
		if(form.getDateTo() != null)
			criteria.add(Restrictions.lt("modifiedDate", form.getDateTo()));
		
		criteria.add(Restrictions.eq("objectStatus", ObjectStatus.INACTIVE));

		criteria.createAlias("owner", "owner");
		criteria.setProjection(Projections.projectionList().
				add(Projections.countDistinct("id").as("plan_entityCount")).
				// cannot just group by owner, else you get a N+1
				// http://stackoverflow.com/questions/4330480/prevent-hibernate-n1-selects-when-grouping-by-an-entity
				// (plus EntityStudentCountByCoachTO doesn't map Persons, just AuditPersons)
				add(Projections.groupProperty("owner.id").as("plan_coachId")).
				add(Projections.groupProperty("owner.firstName").as("plan_coachFirstName")).
				add(Projections.groupProperty("owner.lastName").as("plan_coachLastName")));
		
		List<EntityStudentCountByCoachTO> inactivePlansByCoaches = criteria.setResultTransformer(
						new NamespacedAliasToBeanResultTransformer(
								EntityStudentCountByCoachTO.class, "plan_")).list();
		
		Map<UUID,PlanAdvisorCountTO> results = new HashMap<UUID,PlanAdvisorCountTO>();
		for(EntityStudentCountByCoachTO inactivePlan:inactivePlansByCoaches){
			if(results.containsKey(inactivePlan.getCoach().getId())){
				PlanAdvisorCountTO result = results.get(inactivePlan.getCoach().getId());
				result.setInactivePlanCount(inactivePlan.getEntityCount());
			}else{
				PlanAdvisorCountTO result = new PlanAdvisorCountTO();
				result.setCoachName(inactivePlan.getCoach().getFirstName() + " " + inactivePlan.getCoach().getLastName());
				result.setInactivePlanCount(inactivePlan.getEntityCount());
				results.put(inactivePlan.getCoach().getId(), result);
			}
		}
		
		for(EntityStudentCountByCoachTO activePlan:activePlansByCoaches){
			if(results.containsKey(activePlan.getCoach().getId())){
				PlanAdvisorCountTO result = results.get(activePlan.getCoach().getId());
				result.setActivePlanCount(activePlan.getEntityCount());
			}else{
				PlanAdvisorCountTO result = new PlanAdvisorCountTO();
				result.setCoachName(activePlan.getCoach().getFirstName() + " " + activePlan.getCoach().getLastName());
				result.setActivePlanCount(activePlan.getEntityCount());
				results.put(activePlan.getCoach().getId(), result);
			}
		}
		List<PlanAdvisorCountTO> sortedResults = Lists.newArrayList(results.values());
		Collections.sort(sortedResults, PlanAdvisorCountTO.COACH_NAME_COMPARATOR);
		return sortedResults;
	}	
	
	
	@SuppressWarnings("unchecked")
	public List<PlanCourseCountTO> getPlanCountByCourse(SearchPlanTO form){

		final StringBuilder selectAndFrom = new  StringBuilder("select count(distinct pc.id) as plan_studentCount, ")
				.append("pc.courseCode as plan_courseCode, ")
				.append("pc.formattedCourse as plan_formattedCourse, ")
				.append("pc.courseTitle as plan_courseTitle, ")
				.append("pc.termCode as plan_termCode ")
				.append("from Plan p, PlanCourse pc, Person person");

		final StringBuilder where = new StringBuilder(" where p.objectStatus = :objectStatus and pc.plan.id = p.id and p.person.id = person.id ");

		final boolean isPlanStatusFilter = form.getPlanStatus() != null;
		boolean calculateMapPlanStatus = false;
		if ( isPlanStatusFilter ) {
			calculateMapPlanStatus = Boolean.parseBoolean(configService.getByNameEmpty("calculate_map_plan_status").trim().toLowerCase());
			if ( calculateMapPlanStatus ) {
				selectAndFrom.append(", MapStatusReport msr ");
				where.append(" and msr.planStatus = :planStatus ");
				where.append(" and msr.plan = p and msr.person.id = person.id");
			} else {
				selectAndFrom.append(", ExternalPersonPlanStatus ps ");
				where.append(" and ps.status = :planStatus ");
				where.append(" and ps.schoolId = person.schoolId ");
			}
		}

		buildQueryCourseFilters(form, selectAndFrom, where);
		final String queryStr = selectAndFrom.append(where).append(" group by pc.courseCode, pc.formattedCourse, pc.courseTitle, pc.termCode").toString();
		
		Query query = createHqlQuery(queryStr).setInteger("objectStatus", ObjectStatus.ACTIVE.ordinal() );
		bindSearchPlanQueryParams(form,  query);
		List<PlanCourseCountTO> planCoursesCount = query.setResultTransformer(new NamespacedAliasToBeanResultTransformer(
								PlanCourseCountTO.class, "plan_")).list();
		
		return planCoursesCount;
	}

	@SuppressWarnings("unchecked")
	public List<PlanStudentStatusTO> getPlanStudentStatusByCourse(SearchPlanTO form){

		final StringBuilder selectAndFrom = new StringBuilder("select ")
				.append("distinct person.schoolId as plan_studentId, ")
				.append("pc.formattedCourse as plan_formattedCourse, ")
				.append("pc.courseTitle as plan_courseTitle, ")
				.append("p.objectStatus as plan_planObjectStatus, ");

		final boolean calculateMapPlanStatus = Boolean.parseBoolean(configService.getByNameEmpty("calculate_map_plan_status").trim().toLowerCase());
		final boolean isPlanStatusFilter = form.getPlanStatus() != null;

		// subqueries b/c you can't do outer joins with Hibernate Theta-style joins, but if no plan status filter is
		// specified we want to return plans w/o status
		if ( calculateMapPlanStatus ) {
			selectAndFrom.append("(select msr.planStatus from MapStatusReport msr where msr.plan.id = p.id and msr.person.id = person.id")
					.append(isPlanStatusFilter ? " and msr.planStatus = :planStatus" : "")
					.append(") as plan_planStatus, ");
			selectAndFrom.append("(select msr.planNote from MapStatusReport msr where msr.plan.id = p.id and msr.person.id = person.id")
					.append(isPlanStatusFilter ? " and msr.planStatus = :planStatus" : "")
					.append(") as plan_statusDetails ");
		} else {
			selectAndFrom.append("(select ps.status from ExternalPersonPlanStatus ps where ps.schoolId = person.schoolId")
					.append(isPlanStatusFilter ? " and ps.status = :planStatus" : "")
					.append(") as plan_planStatus, ");
			selectAndFrom.append("(select ps.statusReason from ExternalPersonPlanStatus ps where ps.schoolId = person.schoolId")
					.append(isPlanStatusFilter ? " and ps.status = :planStatus" : "")
					.append(") as plan_statusDetails ");
		}

		selectAndFrom.append(" from Plan p, Person person, PlanCourse pc ");
		final StringBuilder where = new StringBuilder(" where p.objectStatus = :objectStatus and pc.plan.id = p.id and p.person.id = person.id ");
		buildQueryCourseFilters(form, selectAndFrom, where);
		final String queryStr = selectAndFrom.append(where).toString();

		Query query = createHqlQuery(queryStr).setInteger("objectStatus", ObjectStatus.ACTIVE.ordinal() );
		bindSearchPlanQueryParams(form, query);
		List<PlanStudentStatusTO> planStudentStatus = query.setResultTransformer(new NamespacedAliasToBeanResultTransformer(
				PlanStudentStatusTO.class, "plan_")).list();
		
		return planStudentStatus;
	}

	@SuppressWarnings("unchecked")
	public List<PlanStudentCoursesCountTO> getPlanStudentCoursesCount(SearchPlanTO form, ProgramStatus activeStatus) {
		final StringBuilder selectAndFrom = new StringBuilder("SELECT ")
				.append(" p.schoolId as person_schoolId, p.username as person_username, p.firstName as person_firstName, ")
				.append(" p.lastName as person_lastName, p.primaryEmailAddress as person_primaryEmailAddress, ")
				.append(" count(distinct pc.id) as person_plannedCourses ");

		selectAndFrom.append(" from Person as p left join p.plans plan ")
				.append(" join plan.planCourses as pc ")
				.append(" left join p.programStatuses as pps ");
		final StringBuilder where = new StringBuilder(" where plan.objectStatus = :planObjectStatus ");
		where.append(" and pps.programStatus.id = :programStatusId ")
                .append(" and pps.objectStatus = :ppsObjectStatus ")
                .append(" and pps.expirationDate is null ");

		buildQueryCourseFilters(form, selectAndFrom, where);
		final String queryStr = selectAndFrom.append(where)
                .append(" group by p.schoolId, p.username, p.firstName, ")
                .append(" p.lastName, p.primaryEmailAddress ")
				.append(" order by p.lastName, p.firstName ").toString();

		Query query = createHqlQuery(queryStr).setInteger("planObjectStatus", ObjectStatus.ACTIVE.ordinal() );
        query.setInteger("ppsObjectStatus", ObjectStatus.ACTIVE.ordinal() );
		query.setParameter("programStatusId", activeStatus.getId());
		bindSearchPlanQueryParams(form, query);
		List<PlanStudentCoursesCountTO> planStudentCoursesCountTOs = query.setResultTransformer(new NamespacedAliasToBeanResultTransformer(
				PlanStudentCoursesCountTO.class, "person_")).list();

		return planStudentCoursesCountTOs;
	}

	private void buildQueryCourseFilters(SearchPlanTO form, StringBuilder selectAndFrom, StringBuilder where) {

		final boolean isNonOperationalCourseFilter = StringUtils.isNotBlank(form.getSubjectAbbreviation()) || StringUtils.isNotBlank(form.getNumber());
		if ( isNonOperationalCourseFilter ) {
			selectAndFrom.append(", ExternalCourse ec");
			where.append(" and ec.code = pc.courseCode ");
		}

		if(StringUtils.isNotBlank(form.getSubjectAbbreviation()))
		{
			where.append(" and ec.subjectAbbreviation = :subjectAbbreviation ");
		}

		if(StringUtils.isNotBlank(form.getNumber()))
		{
			where.append(" and ec.number = :courseNumber ");
		}

		if(!form.getTermCodes().isEmpty())
		{
			where.append(" and pc.termCode in :termCodes ");
		}

		if(StringUtils.isNotBlank(form.getFormattedCourse()))
		{
			where.append(" and pc.formattedCourse = :formattedCourse ");
		}

	}

	private void bindSearchPlanQueryParams(SearchPlanTO form, Query hqlQuery) {

		if(StringUtils.isNotBlank(form.getSubjectAbbreviation()))
		{
			hqlQuery.setString("subjectAbbreviation", form.getSubjectAbbreviation());
		}

		if(StringUtils.isNotBlank(form.getNumber()))
		{
			hqlQuery.setString("courseNumber", form.getNumber());
		}

		if(StringUtils.isNotBlank(form.getFormattedCourse()))
		{
			hqlQuery.setString("formattedCourse", form.getFormattedCourse());
		}

		if(!form.getTermCodes().isEmpty())
		{
			hqlQuery.setParameterList("termCodes", form.getTermCodes());
		}

		if(form.getPlanStatus() != null)
		{
			hqlQuery.setString("planStatus", form.getPlanStatus().toString());
		}

	}

	@SuppressWarnings("unchecked")
	public List<MapStatusReportPerson> getAllActivePlanIds() {
		String getAllActivePlanIdQuery = "select new org.jasig.ssp.transferobject.reports.MapStatusReportPerson(plan.id, plan.person.id, plan.person.schoolId, plan.programCode,plan.catalogYearCode,plan.person.firstName,plan.person.lastName,plan.person.coach.id,plan.owner.id) "
									   + "from org.jasig.ssp.model.Plan plan "
									   + "where plan.objectStatus = :objectStatus and plan.person.objectStatus = :objectStatus";
		Query query = createHqlQuery(getAllActivePlanIdQuery);
		List<MapStatusReportPerson> result  = query.setInteger("objectStatus", ObjectStatus.ACTIVE.ordinal()).list();
									   
		return result;
	}

	@SuppressWarnings("unchecked")
	public List<MapPlanStatusReportCourse> getAllPlanCoursesForStatusReport(
			UUID planId) {
		String getAllPlanCoursesForStatusReportTO = "select new org.jasig.ssp.transferobject.reports.MapPlanStatusReportCourse(pc.termCode, pc.formattedCourse, pc.courseCode, pc.courseTitle, pc.creditHours, pc.originalFormattedCourse) "
												  + " from PlanCourse pc where pc.plan.id = :planId and objectStatus = :objectStatus";
		Query query = createHqlQuery(getAllPlanCoursesForStatusReportTO).setParameter("planId", planId).setInteger("objectStatus", ObjectStatus.ACTIVE.ordinal());
		return query.list();
	}

	public Person getOwnerForPlan(UUID id) {
		String query = "Select p.owner from org.jasig.ssp.model.Plan p where p.id = :id";
		return (Person) createHqlQuery(query).setParameter("id", id).uniqueResult();
	}

	public List<PersonLiteTO> getAllPlanOwners() {
		String getAllPlanOwners = "select new org.jasig.ssp.transferobject.PersonLiteTO" +
				"(p.id, p.firstName, p.lastName) " +
				" from Person p where p.id in (select distinct owner.id from Plan)" +
				" order by p.lastName, p.firstName";
		Query query = createHqlQuery(getAllPlanOwners);
		return query.list();
	}

	public List<MapTransferGoalReportTO> getTransferGoalReport(List<UUID> transferGoalIds, List<UUID> planOwnerIds,
															   UUID programStatus, String planExists,
															   String catalogYearCode, Date modifiedDateFrom,
															   Date modifiedDateTo){
		StringBuilder sb = new StringBuilder("select distinct new org.jasig.ssp.transferobject.reports.MapTransferGoalReportTO(" +
				" plan.person.schoolId, plan.person.firstName, plan.person.lastName, plan.person.primaryEmailAddress," +
				" plan.createdDate, plan.owner.firstName, plan.owner.lastName, plan.transferGoal.name, plan.isPartial, " +
				" plan.catalogYearCode, plan.objectStatus)");
//		sb.append(" from org.jasig.ssp.model.Plan plan");
        sb.append(" from Plan plan");
        if (programStatus!=null) {
//            sb.append(" join org.jasig.ssp.model.PersonProgramStatus pps ");
            sb.append(" join plan.person.programStatuses pps ");
        }
		boolean whereAdded = false;
		if (!CollectionUtils.isEmpty(transferGoalIds)) {
			sb.append((whereAdded?" and " : " where "));
			whereAdded = true;
			sb.append(" plan.transferGoal.id in (:transferGoalIds) ");
		}
		if (!CollectionUtils.isEmpty(planOwnerIds)) {
			sb.append((whereAdded?" and " : " where "));
			whereAdded = true;
			sb.append(" plan.owner.id in (:planOwnerIds) ");
		}
		if (programStatus!=null) {
			sb.append((whereAdded?" and " : " where "));
			whereAdded = true;
//			sb.append(" plan.person.id = pps.personId ");
			sb.append(" pps.programStatus.id = :programStatusId ");
			sb.append(" and pps.objectStatus = 1 ");
            sb.append(" and pps.expirationDate is null ");
		}
		if (StringUtils.isNotEmpty(planExists)) {
			sb.append((whereAdded?" and " : " where "));
			whereAdded = true;
			sb.append(" plan.objectStatus = :objectStatus");
		}
		if (StringUtils.isNotEmpty(catalogYearCode)) {
			sb.append((whereAdded?" and " : " where "));
			whereAdded = true;
			sb.append(" plan.catalogYearCode = :catalogYearCode");
		}
		if (modifiedDateFrom!=null) {
			sb.append((whereAdded?" and " : " where "));
			whereAdded = true;
			sb.append(" plan.modifiedDate >= (:modifiedDateFrom)");
		}
		if (modifiedDateTo!=null) {
			sb.append((whereAdded?" and " : " where "));
			whereAdded = true;
			sb.append(" plan.modifiedDate <= (:modifiedDateTo)");
		}
        sb.append(" order by plan.person.lastName, plan.person.firstName");

		Query query = createHqlQuery(sb.toString());

		if (!CollectionUtils.isEmpty(transferGoalIds)) {
			query.setParameterList("transferGoalIds", transferGoalIds);
		}
		if (!CollectionUtils.isEmpty(planOwnerIds)) {
			query.setParameterList("planOwnerIds", planOwnerIds);
		}
        if (programStatus!=null) {
			query.setParameter("programStatusId", programStatus);
		}
		if (StringUtils.isNotEmpty(planExists)) {
			query.setParameter("objectStatus",
                    (planExists.equals("ACTIVE")?ObjectStatus.ACTIVE:ObjectStatus.INACTIVE));
		}
		if (StringUtils.isNotEmpty(catalogYearCode)) {
			query.setParameter("catalogYearCode", catalogYearCode);
		}
		if (modifiedDateFrom!=null) {
			query.setParameter("modifiedDateFrom", modifiedDateFrom);
		}
		if (modifiedDateTo!=null) {
			query.setParameter("modifiedDateTo", modifiedDateTo);
		}
		return query.list();
	}
}
