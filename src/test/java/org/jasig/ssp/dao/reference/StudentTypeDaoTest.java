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
package org.jasig.ssp.dao.reference;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.fail;

import java.util.Collection;
import java.util.UUID;

import org.jasig.ssp.model.ObjectStatus;
import org.jasig.ssp.model.Person;
import org.jasig.ssp.model.reference.StudentType;
import org.jasig.ssp.service.ObjectNotFoundException;
import org.jasig.ssp.service.impl.SecurityServiceInTestEnvironment;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.transaction.TransactionConfiguration;
import org.springframework.transaction.annotation.Transactional;

/**
 * StudentType DAO test on {@link StudentTypeDao}
 * 
 * @author jon.adams
 * 
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("../dao-testConfig.xml")
@TransactionConfiguration(defaultRollback = false)
@Transactional
public class StudentTypeDaoTest {

	private static final Logger LOGGER = LoggerFactory
			.getLogger(StudentTypeDaoTest.class);

	@Autowired
	private transient StudentTypeDao dao;

	@Autowired
	private transient SecurityServiceInTestEnvironment securityService;

	@Before
	public void setUp() {
		securityService.setCurrent(new Person(Person.SYSTEM_ADMINISTRATOR_ID));
	}

	/**
	 * Tests {@link StudentTypeDao#save(StudentType)}
	 * 
	 * @throws ObjectNotFoundException
	 *             If object could not be found.
	 */
	@Test
	public void testSaveNew() throws ObjectNotFoundException {
		UUID saved;

		StudentType obj = new StudentType();
		obj.setName("new name");
		obj.setObjectStatus(ObjectStatus.ACTIVE);
		dao.save(obj);

		assertNotNull("Generated ID should not have been null.", obj.getId());
		saved = obj.getId();

		LOGGER.debug(obj.toString());

		obj = dao.get(saved);
		assertNotNull("Saved object should not have been null.", obj);
		assertNotNull("Saved object ID should not have been null.", obj.getId());
		assertNotNull("Saved object Name should not have been null.",
				obj.getName());

		final Collection<StudentType> all = dao.getAll(ObjectStatus.ACTIVE)
				.getRows();
		assertNotNull("GetAll() results should not have been null.", all);
		assertFalse("GetAll() results should not have been empty.",
				all.isEmpty());
		assertList(all);

		dao.delete(obj);
	}

	/**
	 * Test {@link StudentTypeDao#get(UUID)} when parameters are invalid.
	 * 
	 * @throws ObjectNotFoundException
	 *             Expected that this exception is thrown.
	 */
	@Test(expected = ObjectNotFoundException.class)
	public void testNull() throws ObjectNotFoundException {
		dao.get(UUID.randomUUID());

		fail("Exception should have been thrown.");
	}

	private void assertList(final Collection<StudentType> objects) {
		for (final StudentType object : objects) {
			assertNotNull("List object should not have been null.",
					object.getId());
		}
	}

	/**
	 * Test automatic UUID generation
	 */
	@Test
	public void uuidGeneration() {
		// arrange
		final StudentType obj = new StudentType();
		obj.setName("new name");
		obj.setObjectStatus(ObjectStatus.ACTIVE);

		final StudentType obj2 = new StudentType();
		obj2.setName("new name");
		obj2.setObjectStatus(ObjectStatus.ACTIVE);

		// act/assert
		assertNotNull("Save result should not have been null.", dao.save(obj));
		assertNotNull("Save result should not have been null.", dao.save(obj2));

		// cleanup
		dao.delete(obj);
		dao.delete(obj2);
	}
}