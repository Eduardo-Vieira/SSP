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
package org.jasig.ssp.web.api;

import org.apache.commons.lang.StringUtils;
import org.jasig.ssp.factory.external.ExternalPersonPlanStatusTOFactory;
import org.jasig.ssp.factory.external.MapStatusReportLiteTOFactory;
import org.jasig.ssp.factory.reference.PlanLiteTOFactory;
import org.jasig.ssp.factory.reference.PlanTOFactory;
import org.jasig.ssp.model.AbstractMapElectiveCourse;
import org.jasig.ssp.model.ObjectStatus;
import org.jasig.ssp.model.Person;
import org.jasig.ssp.model.Plan;
import org.jasig.ssp.model.SubjectAndBody;
import org.jasig.ssp.security.SspUser;
import org.jasig.ssp.service.MapStatusReportService;
import org.jasig.ssp.service.MapStatusService;
import org.jasig.ssp.service.MessageService;
import org.jasig.ssp.service.ObjectNotFoundException;
import org.jasig.ssp.service.PersonService;
import org.jasig.ssp.service.PlanService;
import org.jasig.ssp.service.RequestTrustService;
import org.jasig.ssp.service.SecurityService;
import org.jasig.ssp.service.external.ExternalPersonPlanStatusService;
import org.jasig.ssp.service.external.TermService;
import org.jasig.ssp.service.reference.ConfigService;
import org.jasig.ssp.service.reference.TemplateElectiveCourseDetailService;
import org.jasig.ssp.transferobject.AbstractMapElectiveCourseTO;
import org.jasig.ssp.transferobject.PagedResponse;
import org.jasig.ssp.transferobject.PlanCourseTO;
import org.jasig.ssp.transferobject.PlanElectiveCourseElectiveTO;
import org.jasig.ssp.transferobject.PlanElectiveCourseTO;
import org.jasig.ssp.transferobject.PlanLiteTO;
import org.jasig.ssp.transferobject.PlanOutputTO;
import org.jasig.ssp.transferobject.PlanTO;
import org.jasig.ssp.transferobject.ServiceResponse;
import org.jasig.ssp.transferobject.external.AbstractPlanStatusReportTO;
import org.jasig.ssp.transferobject.external.ExternalPersonPlanStatusTO;
import org.jasig.ssp.util.security.DynamicPermissionChecking;
import org.jasig.ssp.util.sort.PagingWrapper;
import org.jasig.ssp.util.sort.SortingAndPaging;
import org.jasig.ssp.web.api.validation.ValidationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;


@Controller
@RequestMapping("/1/person/{personId}/map/plan")
public class PlanController  extends AbstractBaseController {

	private static final Logger LOGGER = LoggerFactory
			.getLogger(PlanController.class);

	@Override
	protected Logger getLogger() {
		return LOGGER;
	}
	
	@Autowired
	private PlanService service;
	
	@Autowired
	private PersonService personService;
	
	@Autowired
	private TermService termService;
	
	@Autowired
	private PlanTOFactory factory;
	
	@Autowired
	private PlanLiteTOFactory liteFactory;
	
	@Autowired
	private transient SecurityService securityService;

	@Autowired
	private transient RequestTrustService requestTrustService;
	
	@Autowired
	private transient ConfigService configService;
	
	@Autowired
	private transient MessageService messageService;

	@Autowired
	private transient MapStatusService mapStatusService;

	@Autowired
	private transient MapStatusReportService mapStatusReportService;
	
	@Autowired
	private transient ExternalPersonPlanStatusService externalPlanStatusService;

	
	@Autowired
	private ExternalPersonPlanStatusTOFactory planStatusFactory;
	
	@Autowired
	private MapStatusReportLiteTOFactory mapStatusReportTOFactory;

	@Autowired
	private TemplateElectiveCourseDetailService templateElectiveCourseDetailService;
 
	/**
	 * Retrieves the specified list from persistent storage.
	 * 
	 * @param personId
	 *            The specific id to use to lookup the associated data.
	 * @return The specified instance if found.
	 * @throws ObjectNotFoundException
	 *             If specified object could not be found.
	 * @throws ValidationException
	 *             If that specified data is not invalid.
	 */
	@RequestMapping(method = RequestMethod.GET)
	@DynamicPermissionChecking
	public @ResponseBody
	PagedResponse<PlanTO> get(final @PathVariable UUID personId,
			final @RequestParam(required = false) ObjectStatus status,
			final @RequestParam(required = false) Integer start,
			final @RequestParam(required = false) Integer limit,
			final HttpServletRequest request) throws ObjectNotFoundException,
			ValidationException {

		assertStandardMapReadApiAuthorization(request);

		// Run getAll
		final PagingWrapper<Plan> data = getService().getAllForStudent(
				SortingAndPaging.createForSingleSortWithPaging(
						status == null ? ObjectStatus.ALL : status, start,
						limit, null, null, null),personId);

		return new PagedResponse<PlanTO>(true, data.getResults(), getFactory()
				.asTOList(data.getRows()));		
	}

	/**
	 * Retrieves the specified list from persistent storage.
	 * 
	 * @param id
	 *            The specific id to use to lookup the associated data.
	 * @return The specified instance if found.
	 * @throws ObjectNotFoundException
	 *             If specified object could not be found.
	 * @throws ValidationException
	 *             If that specified data is not invalid.
	 */
	@RequestMapping(value="/{id}", method = RequestMethod.GET)
	@DynamicPermissionChecking
	public @ResponseBody
	PlanTO getPlan(final @PathVariable UUID personId,
				   final @PathVariable UUID id,
				   final HttpServletRequest request) throws ObjectNotFoundException,
			ValidationException {
		assertStandardMapReadApiAuthorization(request);
		Plan model = getService().get(id);
		return validatePlan(new PlanTO(model));
	}	
	/**
	 * Retrieves the current entity from persistent storage.
	 * 
	 * @param personId
	 *            The specific id to use to lookup the associated data.
	 * @return The specified instance if found.
	 * @throws ObjectNotFoundException
	 *             If specified object could not be found.
	 * @throws ValidationException
	 *             If that specified data is not invalid.
	 */	
	@DynamicPermissionChecking
	@RequestMapping(value="/current", method = RequestMethod.GET)
	public @ResponseBody
	PlanTO getCurrentForStudent(final @PathVariable UUID personId,
								final HttpServletRequest request) throws ObjectNotFoundException,
			ValidationException {
		assertStandardMapReadApiAuthorization(request);
		final Plan model = getService().getCurrentForStudent(personId);
		if (model == null) {
			return null;
		}
		return validatePlan(new PlanTO(model));
	}

	/**
	 * Retrieves the specified list from persistent storage.  
	 * 
	 * @param personId
	 *            The specific id to use to lookup the associated data.
	 * @return The specified instance if found.
	 * @throws ObjectNotFoundException
	 *             If specified object could not be found.
	 * @throws ValidationException
	 *             If that specified data is not invalid.
	 */
	@RequestMapping(value="/summary", method = RequestMethod.GET)
	@DynamicPermissionChecking
	public @ResponseBody
	PagedResponse<PlanLiteTO> getSummary(final @PathVariable UUID personId,
			final @RequestParam(required = false) ObjectStatus status,
			final @RequestParam(required = false) Integer start,
			final @RequestParam(required = false) Integer limit,
			final HttpServletRequest request) throws ObjectNotFoundException,
			ValidationException {
		assertStandardMapReadApiAuthorization(request);
		// Run getAll
		final PagingWrapper<Plan> data = getService().getAllForStudent(
				SortingAndPaging.createForSingleSortWithPaging(
						status == null ? ObjectStatus.ALL : status, start,
						limit, null, null, null),personId);

		return new PagedResponse<PlanLiteTO>(true, data.getResults(), getLiteFactory()
				.asTOList(data.getRows()));		
	}
	
	/**
	 * Persist a new instance of the specified object.
	 * <p>
	 * Must not include an id.
	 * 
	 * @param obj
	 *            New instance to persist.
	 * @return Original instance plus the generated id.
	 * @throws ObjectNotFoundException
	 *             If specified object could not be found.
	 * @throws ValidationException
	 *             If the specified data contains an id (since it shouldn't).
	 * @throws CloneNotSupportedException 
	 */
	@PreAuthorize("hasRole('ROLE_PERSON_MAP_WRITE')")
	@RequestMapping(method = RequestMethod.POST)
	public @ResponseBody
	PlanTO create(@Valid @RequestBody final PlanTO obj) throws ObjectNotFoundException,
			ValidationException, CloneNotSupportedException {

		if (obj.getId() != null) {
			throw new ValidationException(
					"It is invalid to send an entity with an ID to the create method. Did you mean to use the save method instead?");
		}
		clearPlanElectiveCourseIds(obj);
		setElectiveCourseData(obj);

		Plan model = getFactory().from(obj);
		model = getService().copyAndSave(model);

		if (null != model) {
			final Plan createdModel = getFactory().from(obj);
			if (null != createdModel) {
				return validatePlan(new PlanTO(model));
			}
		}
		return null;
	}
	
	/**
	 * Returns an html page valid for printing
	 * <p>
	 *
	 * 
	 * @param planOutputDataTO
	 *            instance to print.
	 * @return html text strem
	 * @throws ObjectNotFoundException
	 *             If specified object could not be found.
	 */
	@PreAuthorize("hasRole('ROLE_PERSON_READ') or hasRole('ROLE_PERSON_MAP_READ')")
	@RequestMapping(value = "/print", method = RequestMethod.POST)
	public @ResponseBody
	String print(final HttpServletResponse response,
			 @RequestBody final PlanOutputTO planOutputDataTO) throws ObjectNotFoundException {
		
		SubjectAndBody message = service.createOutput(planOutputDataTO);
		if(message != null)
			return message.getBody();
		
		return null;
	}

	
	@PreAuthorize("hasRole('ROLE_PERSON_READ') or hasRole('ROLE_PERSON_MAP_READ')")
	@RequestMapping(value = "/printCurrent", method = RequestMethod.POST)
	public @ResponseBody
	String printCurrent(final HttpServletResponse response, final @PathVariable UUID personId,
			 @RequestBody final PlanOutputTO planOutputDataTO) throws ObjectNotFoundException {

		Plan currentPlan = service.getCurrentForStudent(personId);
		PlanTO planTO = getFactory().from(currentPlan);
		planOutputDataTO.setPlan(planTO);
		
		SubjectAndBody message = service.createOutput(planOutputDataTO);
		if(message != null)
			return message.getBody();
		
		return null;
	}
	
	/**
	 * Returns an html page valid for printing
	 * <p>
	 *
	 * 
	 * @param planOutputDataTO
	 *            instance to print.
	 * @return html text strem
	 * @throws ObjectNotFoundException
	 *             If specified object could not be found.
	 * @throws ValidationException
	 */
	@PreAuthorize("hasRole('ROLE_PERSON_READ') or hasRole('ROLE_PERSON_MAP_READ')")
	@RequestMapping(value = "/emailCurrent", method = RequestMethod.POST)
	public @ResponseBody
	String email(final HttpServletResponse response, final @PathVariable UUID personId,
			 @RequestBody final PlanOutputTO planOutputDataTO) throws ObjectNotFoundException, ValidationException {
		
		
		Plan currentPlan = service.getCurrentForStudent(personId);
		PlanTO planTO = getFactory().from(currentPlan);
		planOutputDataTO.setPlan(planTO);
		
		SubjectAndBody messageText = service.createOutput(planOutputDataTO);
		if(messageText == null)
			return null;
		Person person = personService.get(UUID.fromString(planOutputDataTO.getPlan().getPersonId()));
		Set<String> watcherAddresses = new HashSet<String>(person.getWatcherEmailAddresses());
		watcherAddresses.addAll(org.springframework.util.StringUtils.commaDelimitedListToSet(planOutputDataTO.getEmailCC()));
	    
	    messageService.createMessage(planOutputDataTO.getEmailTo(),org.springframework.util.StringUtils.arrayToCommaDelimitedString(watcherAddresses
	    				.toArray(new String[watcherAddresses.size()])),
							messageText);
		
		return "Map Plan has been queued.";
	}	
	/**
	 * Returns an html page valid for printing
	 * <p>
	 *
	 * 
	 * @param planOutputDataTO
	 *            instance to print.
	 * @return html text strem
	 * @throws ObjectNotFoundException
	 *             If specified object could not be found.
	 * @throws ValidationException
	 */
	@PreAuthorize("hasRole('ROLE_PERSON_READ') or hasRole('ROLE_PERSON_MAP_READ')")
	@RequestMapping(value = "/email", method = RequestMethod.POST)
	public @ResponseBody
	String email(final HttpServletResponse response,
			 @RequestBody final PlanOutputTO planOutputDataTO) throws ObjectNotFoundException, ValidationException {
		
		
		SubjectAndBody messageText = service.createOutput(planOutputDataTO);
		if(messageText == null)
			return null;

		Person person = personService.get(UUID.fromString(planOutputDataTO.getPlan().getPersonId()));
		Set<String> watcherAddresses = new HashSet<String>(person.getWatcherEmailAddresses());
		watcherAddresses.addAll(org.springframework.util.StringUtils.commaDelimitedListToSet(planOutputDataTO.getEmailCC()));
	    messageService.createMessage(planOutputDataTO.getEmailTo(), 
	    		org.springframework.util.StringUtils.arrayToCommaDelimitedString(watcherAddresses
	    				.toArray(new String[watcherAddresses.size()])),
							messageText);
		
		return "Map Plan has been queued.";
	}

	/**
	 * Persist any changes to the plan instance.
	 * 
	 * @param id
	 *            Explicit id to the instance to persist.
	 * @param obj
	 *            Full instance to persist.
	 * @return The update data object instance.
	 * @throws ObjectNotFoundException
	 *             If specified object could not be found.
	 * @throws ValidationException
	 *             If the specified id is null.
	 * @throws CloneNotSupportedException 
	 */
	@PreAuthorize("hasRole('ROLE_PERSON_MAP_WRITE')")
	@RequestMapping(value = "/{id}", method = RequestMethod.PUT)
	public @ResponseBody
	PlanTO save(@PathVariable final UUID id, @Valid @RequestBody final PlanTO obj)
			throws ValidationException, ObjectNotFoundException, CloneNotSupportedException {
		if (id == null) {
			throw new ValidationException(
					"You submitted without an id to the save method.  Did you mean to create?");
		}

		if (obj.getId() == null) {
			obj.setId(id);
		}
		final Plan oldPlan = getService().get(id);
		final Person oldOwner = oldPlan.getOwner();
		
		SspUser currentUser = getSecurityService().currentlyAuthenticatedUser();

		setElectiveCourseData(obj);
		
		//If the currently logged in user is not the owner of this plan
		//we need to create a clone then save it.
		if(currentUser.getPerson().getId().equals(oldOwner.getId()))
		{
			final Plan model = getFactory().from(obj);
			Plan savedPlan = getService().save(model);
			if (null != model) {
				return validatePlan(new PlanTO(savedPlan));
			}
		}
		else
		{
			obj.setId(null);
			clearPlanElectiveCourseIds(obj);
			Plan model = getFactory().from(obj);
			final Plan clonedPlan = getService().copyAndSave(model);
			if (null != clonedPlan) {
				return validatePlan(new PlanTO(clonedPlan));
			}
		}

		return null;
	}

	/**
	 * Marks the specified plan instance with a status of
	 * {@link ObjectStatus#INACTIVE}.
	 * 
	 * @param id
	 *            The id of the data instance to mark deleted.
	 * @return Success boolean.
	 * @throws ObjectNotFoundException
	 *             If specified object could not be found.
	 */
	@PreAuthorize("hasRole('ROLE_PERSON_MAP_WRITE')")
	@RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
	public @ResponseBody
	ServiceResponse delete(@PathVariable final UUID id)
			throws ObjectNotFoundException {
		getService().delete(id);
		return new ServiceResponse(true);
	}
	
	/**
	 * Validate the plan instance.
	 * 
	 * @param plan
	 *            Full instance of plan object.
	 * @return The validated data object instance.
	 * @throws ObjectNotFoundException
	 *             If specified object could not be found.
	 */
	@PreAuthorize("hasRole('ROLE_PERSON_MAP_WRITE')")
	@RequestMapping(value = "/validate", method = RequestMethod.POST)
	public @ResponseBody
	PlanTO validatePlan(final HttpServletResponse response,
			 @RequestBody final PlanTO plan)
			throws ObjectNotFoundException {
		PlanTO validatedTO = getService().validate(plan);
		return validatedTO;
	}
	
	/**
	 * Return plan status for given student.
	 * 
	 * @param personId
	 *            Explicit personId to the instance to persist.
	 * @return The current plan status of the student.
	 */
	@DynamicPermissionChecking
	@RequestMapping(value = "/planstatus", method = RequestMethod.GET)
	public @ResponseBody
	ExternalPersonPlanStatusTO getPlanStatus(
			final HttpServletRequest request,
			final HttpServletResponse response,
			@PathVariable final UUID personId)
			throws ObjectNotFoundException {
		assertStandardMapReadApiAuthorization(request);
		if(personId == null){
			return null;
		}
		String schoolId = null;
		Person student = personService.get(personId);
		schoolId = student.getSchoolId();
		//TODO not the cleanest way to handle but clientside generates 500 error in console
		// Currently plan status is not required.
		try{
			return planStatusFactory.from(externalPlanStatusService.getBySchoolId(schoolId));
		}catch(Exception exp)
		{
			return null;
		}
	}

	@DynamicPermissionChecking
	@RequestMapping(value = "/calculatedPlanstatus", method = RequestMethod.GET)
	public @ResponseBody
	AbstractPlanStatusReportTO getCalculatedPlanStatus(
			final HttpServletRequest request,
			final HttpServletResponse response,
			@PathVariable final UUID personId)
			throws ObjectNotFoundException {
		assertStandardMapReadApiAuthorization(request);
		if(personId == null){
			return null;
		}
		
		return mapStatusService.getByPersonId(personId);
	}
	private void setElectiveCourseData(PlanTO planTO) {
		for (PlanCourseTO planCourseTO : planTO.getPlanCourses()) {
			if (StringUtils.isNotBlank(planCourseTO.getOriginalFormattedCourse())) {
				PlanElectiveCourseTO planElectiveCourseTO = findPlanElectiveCourse(planCourseTO.getOriginalFormattedCourse(), planTO.getPlanElectiveCourses());
				if (planElectiveCourseTO!=null && planCourseTO.getFormattedCourse().equals(planElectiveCourseTO.getFormattedCourse())) {
					setElectiveCourseData(planCourseTO, planElectiveCourseTO);
				} else {
					PlanElectiveCourseElectiveTO planElectiveCourseElectiveTO = findPlanElectiveCourseElective(planCourseTO.getFormattedCourse(), planElectiveCourseTO.getPlanElectiveCourseElectives());
					if (planElectiveCourseElectiveTO!=null) {
						setElectiveCourseData(planCourseTO, planElectiveCourseElectiveTO);
					}
				}
			} else {
				PlanElectiveCourseTO planElectiveCourseTO = findPlanElectiveCourse(planCourseTO.getFormattedCourse(), planTO.getPlanElectiveCourses());
				if (planElectiveCourseTO!=null) {
					planCourseTO.setOriginalFormattedCourse(planCourseTO.getFormattedCourse());
				}
			}
		}
	}

	private void setElectiveCourseData(PlanCourseTO planCourseTO, AbstractMapElectiveCourseTO electiveCourseTO) {
		planCourseTO.setCourseCode(electiveCourseTO.getCourseCode());
		planCourseTO.setCourseDescription(electiveCourseTO.getCourseDescription());
		planCourseTO.setCourseTitle(electiveCourseTO.getCourseTitle());
		planCourseTO.setCreditHours(electiveCourseTO.getCreditHours());
	}

	private PlanElectiveCourseTO findPlanElectiveCourse(String formattedCourse, List<PlanElectiveCourseTO> planElectiveCourseTOs ) {
		for (PlanElectiveCourseTO planElectiveCourseTO : planElectiveCourseTOs) {
			if (formattedCourse.equals(planElectiveCourseTO.getFormattedCourse()))
			return planElectiveCourseTO;
		}
		return null;
	}
	private PlanElectiveCourseElectiveTO findPlanElectiveCourseElective (String formattedCourse, List<PlanElectiveCourseElectiveTO> planElectiveCourseElectiveTOs ) {
		for (PlanElectiveCourseElectiveTO planElectiveCourseElectiveTO : planElectiveCourseElectiveTOs) {
			if (formattedCourse.equals(planElectiveCourseElectiveTO.getFormattedCourse()))
				return planElectiveCourseElectiveTO;
		}
		return null;
	}
	private void clearPlanElectiveCourseIds(PlanTO planTO) {
		for (PlanElectiveCourseTO planElectiveCourseTO : planTO.getPlanElectiveCourses()) {
			planElectiveCourseTO.setId(null);
			for (PlanElectiveCourseElectiveTO planElectiveCourseElectiveTO : planElectiveCourseTO.getPlanElectiveCourseElectives()) {
				planElectiveCourseElectiveTO.setId(null);
			}
		}
	}

	private PlanTO validatePlan(PlanTO plan) throws ObjectNotFoundException{
		String schoolId = null;
		if(StringUtils.isNotBlank(plan.getPersonId())){
			Person student = personService.get(UUID.fromString(plan.getPersonId()));
			schoolId = student.getSchoolId();
		}
		return getService().validate(plan);
	}

	private void assertStandardMapReadApiAuthorization(HttpServletRequest request)
			throws AccessDeniedException {
		if ( securityService.hasAuthority("ROLE_PERSON_READ") ||
				securityService.hasAuthority("ROLE_PERSON_MAP_READ") ) {
			return;
		}
		try {
			requestTrustService.assertHighlyTrustedRequest(request);
		} catch ( AccessDeniedException e ) {
			throw new AccessDeniedException("Untrusted request with"
					+ " insufficient permissions.", e);
		}
	}

	public PlanService getService() {
		return service;
	}

	public void setService(PlanService service) {
		this.service = service;
	}

	public PlanTOFactory getFactory() {
		return factory;
	}

	public void setFactory(PlanTOFactory factory) {
		this.factory = factory;
	}

	public SecurityService getSecurityService() {
		return securityService;
	}

	public void setSecurityService(SecurityService securityService) {
		this.securityService = securityService;
	}

	public PlanLiteTOFactory getLiteFactory() {
		return liteFactory;
	}

	public void setLiteFactory(PlanLiteTOFactory liteFactory) {
		this.liteFactory = liteFactory;
	}
}
