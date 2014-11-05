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
package org.jasig.mygps.web;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.List;
import java.util.UUID;

import org.jasig.ssp.model.Person;
import org.jasig.ssp.service.impl.SecurityServiceInTestEnvironment;
import org.jasig.ssp.transferobject.reference.ChallengeTO;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.transaction.TransactionConfiguration;
import org.springframework.transaction.annotation.Transactional;

/**
 * {@link MyGpsChallengeController} tests
 * 
 * @author jon.adams
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("../../ssp/web/ControllerIntegrationTests-context.xml")
@TransactionConfiguration
@Transactional
public class MyGpsChallengeControllerIntegrationTest {

	@Autowired
	private MyGpsChallengeController controller;

	private static final UUID CHALLENGE_ID = UUID
			.fromString("7c0e5b76-9933-484a-b265-58cb280305a5");

	private static final String CHALLENGE_NAME = "Finances - Education";

	@Autowired
	private transient SecurityServiceInTestEnvironment securityService;

	/**
	 * Setup the security service with the administrator user.
	 */
	@Before
	public void setUp() {
		securityService.setCurrent(new Person(Person.SYSTEM_ADMINISTRATOR_ID));
	}

	/**
	 * Test the {@link MyGpsChallengeController#search(String)} action results.
	 * 
	 * @throws Exception
	 *             Thrown if the controller throws any exceptions.
	 */
	@Test
	public void testControllerSearch() throws Exception {
		List<ChallengeTO> list = controller.search(CHALLENGE_NAME);

		assertEquals(
				"Search with a specific name should have returned one Challenge.",
				1, list.size());

		ChallengeTO challenge = list.get(0);

		assertEquals(
				"Switch with a specific name did not return the expected Challenge.",
				CHALLENGE_ID, challenge.getId());

		assertNotNull("ModifiedBy id should not have been empty.",
				challenge.getModifiedBy());
		assertTrue("ShowInStudentIntake should have been true.",
				challenge.isShowInStudentIntake());

		assertFalse(
				"Referrals should not have been empty for the specified Challenge.",
				challenge.getChallengeChallengeReferrals().isEmpty());

		assertEquals("Referrals list size and calculated count did not match.",
				challenge.getChallengeChallengeReferrals().size(),
				challenge.getReferralCount());
	}
}
