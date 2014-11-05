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
package org.jasig.ssp.web.api.reports;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.UUID;

import javax.servlet.http.HttpServletResponse;

import net.sf.jasperreports.engine.JRException;

import org.jasig.ssp.factory.PersonTOFactory;
import org.jasig.ssp.model.ObjectStatus;
import org.jasig.ssp.security.permissions.Permission;
import org.jasig.ssp.service.ObjectNotFoundException;
import org.jasig.ssp.service.PersonService;
import org.jasig.ssp.service.external.TermService;
import org.jasig.ssp.service.reference.JournalStepDetailService;
import org.jasig.ssp.service.reference.ProgramStatusService;
import org.jasig.ssp.service.reference.ReferralSourceService;
import org.jasig.ssp.service.reference.ServiceReasonService;
import org.jasig.ssp.service.reference.SpecialServiceGroupService;
import org.jasig.ssp.service.reference.StudentTypeService;
import org.jasig.ssp.transferobject.reports.BaseStudentReportTO;
import org.jasig.ssp.transferobject.reports.PersonSearchFormTO;
import org.jasig.ssp.util.sort.PagingWrapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.google.common.collect.Maps;

public class DistanceLearningReportController extends ReportBaseController<BaseStudentReportTO> {
	private static String REPORT_URL = "/reports/journalSessionDetails.jasper";
	private static String REPORT_FILE_TITLE = "Journal_Session_Details_Report";
	private static String JOURNAL_SESSION_DETAILS = "journalSessionDetails";
	
	private static final Logger LOGGER = LoggerFactory
			.getLogger(DistanceLearningReportController.class);

	@Autowired
	private transient PersonService personService;
	@Autowired
	private transient PersonTOFactory personTOFactory;
	@Autowired
	private transient SpecialServiceGroupService ssgService;

	@Autowired
	private transient TermService termService;
	@Autowired
	private transient ProgramStatusService programStatusService;
	@Autowired
	protected transient StudentTypeService studentTypeService;	
	@Autowired
	protected transient ServiceReasonService serviceReasonService;	
	

	// @Autowired
	// private transient PersonTOFactory factory;

	@InitBinder
	public void initBinder(final WebDataBinder binder) {
		final SimpleDateFormat dateFormat = new SimpleDateFormat(DEFAULT_DATE_FORMAT,
				Locale.US);
		dateFormat.setLenient(false);
		binder.registerCustomEditor(Date.class, new CustomDateEditor(
				dateFormat, true));
	}

	@RequestMapping(method = RequestMethod.GET)
	@PreAuthorize(Permission.SECURITY_REPORT_READ)
	@ResponseBody
	public void getDistanceLearningReport(
			final HttpServletResponse response,
			final @RequestParam(required = false) ObjectStatus status,
			final @RequestParam(required = false) UUID coachId,			
			final @RequestParam(required = false) List<UUID> specialServiceGroupIds,
			final @RequestParam(required = false) List<UUID> studentTypeIds,
			final @RequestParam(required = false) List<UUID> serviceReasonIds,
			final @RequestParam(required = false) Date createDateFrom,
			final @RequestParam(required = false) Date createDateTo,
			final @RequestParam(required = false) String termCode,
			final @RequestParam(required = false) String homeDepartment,
			final @RequestParam(required = false, defaultValue = DEFAULT_REPORT_TYPE) String reportType)
			throws ObjectNotFoundException, IOException {
		
		final Map<String, Object> parameters = Maps.newHashMap();
		final PersonSearchFormTO personSearchForm = new PersonSearchFormTO();
		
		SearchParameters.addCoach(coachId, parameters, personSearchForm, personService, personTOFactory);
		
		SearchParameters.addReferenceLists(studentTypeIds, 
				specialServiceGroupIds, 
				null,
				serviceReasonIds,
				parameters, 
				personSearchForm, 
				studentTypeService, 
				ssgService, 
				null,
				serviceReasonService);
		
		
		SearchParameters.addDateRange(createDateFrom, 
				createDateTo, 
				termCode, 
				parameters, 
				personSearchForm, 
				termService);
		
		SearchParameters.addReferenceTypes(null,
				null, 
				false,
				null,
				homeDepartment,
				parameters, 
				personSearchForm, 
				programStatusService, 
				null);

		
		final PagingWrapper<BaseStudentReportTO> people = personService.getStudentReportTOsFromCriteria(
				personSearchForm, SearchParameters.getReportPersonSortingAndPagingAll(status));
		
		List<BaseStudentReportTO> compressedReports = processStudentReportTOs(people);
		SearchParameters.addStudentCount(compressedReports, parameters);
		renderReport(response, parameters, compressedReports, REPORT_URL, reportType, REPORT_FILE_TITLE);

	}

	@Override
	protected Logger getLogger() {
		return LOGGER;
	}
}
