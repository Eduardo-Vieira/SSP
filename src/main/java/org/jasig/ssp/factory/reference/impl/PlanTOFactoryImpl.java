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
package org.jasig.ssp.factory.reference.impl;

import org.apache.commons.lang.StringUtils;
import org.jasig.ssp.dao.PlanDao;
import org.jasig.ssp.factory.AbstractAuditableTOFactory;
import org.jasig.ssp.factory.reference.PlanCourseTOFactory;
import org.jasig.ssp.factory.reference.PlanElectiveCourseTOFactory;
import org.jasig.ssp.factory.reference.PlanTOFactory;
import org.jasig.ssp.factory.reference.TermNoteTOFactory;
import org.jasig.ssp.model.Plan;
import org.jasig.ssp.model.PlanCourse;
import org.jasig.ssp.model.PlanElectiveCourse;
import org.jasig.ssp.model.TermNote;
import org.jasig.ssp.service.ObjectNotFoundException;
import org.jasig.ssp.service.PersonService;
import org.jasig.ssp.service.TemplateService;
import org.jasig.ssp.service.reference.TransferGoalService;
import org.jasig.ssp.transferobject.PlanCourseTO;
import org.jasig.ssp.transferobject.PlanElectiveCourseTO;
import org.jasig.ssp.transferobject.PlanTO;
import org.jasig.ssp.transferobject.TermNoteTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.UUID;


@Service
@Transactional(readOnly = true)
public class PlanTOFactoryImpl extends AbstractAuditableTOFactory<PlanTO, Plan>
		implements PlanTOFactory {

	public PlanTOFactoryImpl() {
		super(PlanTO.class, Plan.class);
	}

	@Autowired
	private transient PlanDao dao;

	@Autowired
	private PersonService personService;
	
	@Autowired
	private PlanCourseTOFactory planCourseTOFactory;

	@Autowired
	private PlanElectiveCourseTOFactory planElectiveCourseTOFactory;
	
	@Autowired
	private TermNoteTOFactory termNoteTOFactory;

    @Autowired
    private TemplateService templateService;

    @Autowired
	private TransferGoalService transferGoalService;

	@Override
	protected PlanDao getDao() {
		return dao;
	}
	
	public PersonService getPersonService() {
		return personService;
	}

	public void setPersonService(PersonService personService) {
		this.personService = personService;
	}
	
	@Override
	public Plan from(PlanTO tObject) throws ObjectNotFoundException {
		Plan model = super.from(tObject);
		model.setOwner(getPersonService().get(UUID.fromString(tObject.getOwnerId())));
		model.setPerson(getPersonService().get(UUID.fromString(tObject.getPersonId())));
		model.setName(tObject.getName());
		model.setAcademicGoals(tObject.getAcademicGoals());
		model.setAcademicLink(tObject.getAcademicLink());
		model.setCareerLink(tObject.getCareerLink());
		model.setContactEmail(tObject.getContactEmail());
		model.setContactName(tObject.getContactName());
		model.setContactNotes(tObject.getContactNotes());
		model.setContactPhone(tObject.getContactPhone());
		model.setContactTitle(tObject.getContactTitle());
		model.setIsF1Visa(tObject.getIsF1Visa());
		model.setIsFinancialAid(tObject.getIsFinancialAid());
		model.setIsImportant(tObject.getIsImportant());
		model.setStudentNotes(tObject.getStudentNotes());
        model.setProgramCode(tObject.getProgramCode());
		model.setCatalogYearCode(tObject.getCatalogYearCode());
		model.getTermNotes().clear();
		model.setIsValid(tObject.getIsValid());

        if (StringUtils.isNotBlank(tObject.getBasedOnTemplateId())) {
            model.setTemplateBasedOn(templateService.get(UUID.fromString(tObject.getBasedOnTemplateId())));
        }

        List<TermNoteTO> termNotes = tObject.getTermNotes();
		for (TermNoteTO termNoteTO : termNotes) {
			if(termNoteTO!=null)
			{
				TermNote noteModel = getTermNoteTOFactory().from(termNoteTO);
				model.getTermNotes().add(noteModel);
				noteModel.setPlan(model);
			}
		}		

		model.getPlanCourses().clear();
		List<PlanCourseTO> planCourses = tObject.getPlanCourses();
		for (PlanCourseTO planCourseTO : planCourses) {
			PlanCourse planCourse = getPlanCourseTOFactory().from(planCourseTO);
			planCourse.setPlan(model);
			planCourse.setPerson(model.getPerson());
			model.getPlanCourses().add(planCourse);
		}

		model.getPlanElectiveCourses().clear();
		List<PlanElectiveCourseTO> planElectiveCourses = tObject.getPlanElectiveCourses();
		for (PlanElectiveCourseTO planElectiveCourseTO : planElectiveCourses) {
			PlanElectiveCourse planElectiveCourse = planElectiveCourseTOFactory.from(planElectiveCourseTO);
			planElectiveCourse.setPlan(model);
			model.getPlanElectiveCourses().add(planElectiveCourse);
		}

		if (StringUtils.isNotBlank(tObject.getTransferGoalId())) {
			model.setTransferGoal(this.transferGoalService.get(UUID.fromString(tObject.getTransferGoalId())));
		} else {
			model.setTransferGoal(null);
		}
		return model;
	}

	public PlanCourseTOFactory getPlanCourseTOFactory() {
		return planCourseTOFactory;
	}

	public void setPlanCourseTOFactory(PlanCourseTOFactory planCourseTOFactory) {
		this.planCourseTOFactory = planCourseTOFactory;
	}
		
	public TermNoteTOFactory getTermNoteTOFactory() {
		return termNoteTOFactory;
	}

	public void setTermNoteTOFactory(TermNoteTOFactory termNoteTOFactory) {
		this.termNoteTOFactory = termNoteTOFactory;
	}
}
