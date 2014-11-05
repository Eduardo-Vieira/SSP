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
import java.util.ArrayList;
import java.util.List;

import net.sf.jasperreports.engine.JRException;

import org.jasig.ssp.model.ObjectStatus;
import org.jasig.ssp.service.ObjectNotFoundException;
import org.jasig.ssp.util.service.stub.Stubs;
import org.jasig.ssp.util.service.stub.Stubs.PersonFixture;
import org.jasig.ssp.util.service.stub.Stubs.ProgramStatusFixture;
import org.jasig.ssp.util.service.stub.Stubs.StudentTypeFixture;
import org.jasig.ssp.util.service.stub.Stubs.TermFixture;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mock.web.MockHttpServletResponse;

import com.google.common.base.Predicate;
import com.google.common.collect.Lists;

public class EarlyAlertStudentOutcomeReportControllerIntegrationTest extends
		AbstractReportControllerIntegrationTest {



	@Autowired
	private transient EarlyAlertStudentOutcomeReportController controller;

	/**
	 * {@link #testGetEarlyAlertStudentOutcomeReportWithFilters()}, 
	 * Test to make sure all the filters are implemented properly.
	 */
	@Test
	public void testGetEarlyAlertStudentOutcomeReportWithFilters()
			throws IOException, ObjectNotFoundException, JRException {
		final MockHttpServletResponse response = new MockHttpServletResponse();
		
		controller.getEarlyAlertStudentOutcomeReport(response,"earlyAlertOutcome",
				ObjectStatus.ACTIVE,//Object Status
				null,//roster status
				Stubs.HomeDepartmentFixture.MATHEMATICS.title(), //home department
				PersonFixture.COACH_1.id(),
				null,
				Lists.newArrayList(StudentTypeFixture.ILP.id()),
				Lists.newArrayList(Stubs.ServiceReasonFixture.TEST_SERVICE_REASON.id()),
				ProgramStatusFixture.ACTIVE.id(), 
				Lists.newArrayList(Stubs.SpecialServiceGroupFixture.TEST_SSG.id()), 
				Lists.newArrayList(Stubs.EarlyAlertOutcomeFixture.WAITING_FOR_RESPONSE.id()),
				TermFixture.FALL_2012.code(), 
				null,
				null,
				"csv");

		// "body" is the actual results and the header that describes its columns.
		// This is as opposed to rows which precede the header, which describe
		// the filtering criteria
		final List<String> expectedReportBodyLines = new ArrayList<String>(4);
		expectedReportBodyLines.add("FIRST,MIDDLE,LAST,STUDENT ID,EMAIL(SCHOOL),OUTCOME,COUNSELOR");
		expectedReportBodyLines.add(",,,,,,");
		expectReportBodyLines(expectedReportBodyLines, response, null);
	}

	@Test
	public void testGetEarlyAlertStudentOutcomeReportWithNoFilter()
			throws IOException, ObjectNotFoundException, JRException {

		sessionFactory.getCurrentSession().flush();

		final MockHttpServletResponse response = new MockHttpServletResponse();

		controller.getEarlyAlertStudentOutcomeReport(response,"earlyAlertOutcome",
				null,
				null,
				null,
				null,
				null,
				null,
				null,
				null,
				null,
				null,
				null,
				null,
				null,
				"csv");
		final List<String> expectedReportBodyLines = new ArrayList<String>(4);
		//TODO Understand why no filters does not bring back a result!
		expectedReportBodyLines.add("FIRST,MIDDLE,LAST,STUDENT ID,EMAIL(SCHOOL),OUTCOME,COUNSELOR");
		expectedReportBodyLines.add("test,Mumford,coach1student3,coach1student3,coach1student3@unicon.net,Duplicate Early Alert Notice,test coach1");
		expectedReportBodyLines.add("test,Mumford,coach1student1,coach1student1,coach1student1@unicon.net,Not an Early Alert Class,test coach1");
		expectedReportBodyLines.add("test,Mumford,coach1student2,coach1student2,coach1student2@unicon.net,Not an Early Alert Class,test coach1");
		expectedReportBodyLines.add("James,A,Gosling,student0,test@sinclair.edu,Student Did Not Responsd,Alan Turing");
		expectedReportBodyLines.add("test,Mumford,coach1student2,coach1student2,coach1student2@unicon.net,Student Did Not Responsd,test coach1");
		expectedReportBodyLines.add("test,Mumford,coach1student3,coach1student3,coach1student3@unicon.net,Student Did Not Responsd,test coach1");
		
		expectedReportBodyLines.add("test,Mumford,coach1student4,coach1student4,coach1student4@unicon.net,Student Did Not Responsd,test coach1");
		expectedReportBodyLines.add("test,Mumford,coach1student1,coach1student1,coach1student1@unicon.net,Student Responded,test coach1");
		expectedReportBodyLines.add("test,Mumford,coach1student2,coach1student2,coach1student2@unicon.net,Waiting for Response,test coach1");
		expectedReportBodyLines.add("test,Mumford,coach1student3,coach1student3,coach1student3@unicon.net,Waiting for Response,test coach1");
		expectReportBodyLines(expectedReportBodyLines, response, null);
	}
	
	/**
	 * {@link #testGetEarlyAlertStudentOutcomeReportWithFilters()}, 
	 * Test to make sure all the filters are implemented properly.
	 */
	@Test
	public void testGetEarlyAlertStudentOutreachReportWithFilters()
			throws IOException, ObjectNotFoundException, JRException {
		final MockHttpServletResponse response = new MockHttpServletResponse();
		
		controller.getEarlyAlertStudentOutcomeReport(response,"earlyAlertOutreachIds",
				ObjectStatus.ACTIVE,//Object Status
				null,//roster status
				Stubs.HomeDepartmentFixture.MATHEMATICS.title(), //home department
				PersonFixture.COACH_1.id(),
				null,
				Lists.newArrayList(StudentTypeFixture.ILP.id()),
				Lists.newArrayList(Stubs.ServiceReasonFixture.TEST_SERVICE_REASON.id()),
				ProgramStatusFixture.ACTIVE.id(),
				Lists.newArrayList(Stubs.SpecialServiceGroupFixture.TEST_SSG.id()),
				Lists.newArrayList(Stubs.EarlyAlertOutcomeFixture.WAITING_FOR_RESPONSE.id()),
				TermFixture.FALL_2012.code(),
				null,
				null,
				"csv");

		// "body" is the actual results and the header that describes its columns.
		// This is as opposed to rows which precede the header, which describe
		// the filtering criteria
		final List<String> expectedReportBodyLines = new ArrayList<String>(4);
		expectedReportBodyLines.add("FIRST,MIDDLE,LAST,STUDENT ID,EMAIL(SCHOOL),OUTREACH,COUNSELOR");
		expectedReportBodyLines.add(",,,,,,");
		expectReportBodyLines(expectedReportBodyLines, response, null);
	}

	@Test
	public void testGetEarlyAlertStudentReachReportWithNoFilter()
			throws IOException, ObjectNotFoundException, JRException {

		sessionFactory.getCurrentSession().flush();

		final MockHttpServletResponse response = new MockHttpServletResponse();

		controller.getEarlyAlertStudentOutcomeReport(response,"earlyAlertOutreachIds",
				null,
				null,
				null,
				null,
				null,
				null,
				null,
				null,
				null,
				null,
				null,
				null,
				null,
				"csv");
		final List<String> expectedReportBodyLines = new ArrayList<String>(4);
		//TODO Understand why no filters does not bring back a result!
		expectedReportBodyLines.add("FIRST,MIDDLE,LAST,STUDENT ID,EMAIL(SCHOOL),OUTREACH,COUNSELOR");
		expectedReportBodyLines.add("test,Mumford,coach1student1,coach1student1,coach1student1@unicon.net,In Person,test coach1");
		expectedReportBodyLines.add("test,Mumford,coach1student2,coach1student2,coach1student2@unicon.net,Letter,test coach1");
		expectedReportBodyLines.add("test,Mumford,coach1student3,coach1student3,coach1student3@unicon.net,Letter,test coach1");
		expectReportBodyLines(expectedReportBodyLines, response, null);
	}
	
	@Override
	protected Predicate<String> afterHeader() {
		return afterLineContaining("Early Alert Student Outcome Report");
	}

}
