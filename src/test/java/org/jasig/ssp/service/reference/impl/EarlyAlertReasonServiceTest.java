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
package org.jasig.ssp.service.reference.impl; // NOPMD by jon.adams

import static org.easymock.EasyMock.createMock;
import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.isA;
import static org.easymock.EasyMock.replay;
import static org.easymock.EasyMock.verify;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.UUID;

import org.jasig.ssp.dao.reference.EarlyAlertReasonDao;
import org.jasig.ssp.model.ObjectStatus;
import org.jasig.ssp.model.reference.EarlyAlertReason;
import org.jasig.ssp.service.ObjectNotFoundException;
import org.jasig.ssp.util.sort.PagingWrapper;
import org.jasig.ssp.util.sort.SortingAndPaging;
import org.jasig.ssp.web.api.validation.ValidationException;
import org.junit.Before;
import org.junit.Test;

/**
 * Test the EarlyAlertReason service implementation class
 * 
 * @author jon.adams
 */
public class EarlyAlertReasonServiceTest {

	private transient EarlyAlertReasonServiceImpl service;

	private transient EarlyAlertReasonDao dao;

	/**
	 * Test setup
	 */
	@Before
	public void setUp() {
		service = new EarlyAlertReasonServiceImpl();
		dao = createMock(EarlyAlertReasonDao.class);

		service.setDao(dao);
	}

	/**
	 * Test the {@link EarlyAlertReasonServiceImpl#getAll(SortingAndPaging)}
	 * method.
	 */
	@Test
	public void testGetAll() {
		final List<EarlyAlertReason> daoAll = new ArrayList<EarlyAlertReason>();
		daoAll.add(new EarlyAlertReason());

		expect(dao.getAll(isA(SortingAndPaging.class))).andReturn(
				new PagingWrapper<EarlyAlertReason>(daoAll));

		replay(dao);

		final Collection<EarlyAlertReason> all = service.getAll(
				new SortingAndPaging(ObjectStatus.ACTIVE)).getRows();
		assertFalse("List should not have been empty.", all.isEmpty());
		verify(dao);
	}

	/**
	 * Test the {@link EarlyAlertReasonServiceImpl#get(UUID)} method.
	 * 
	 * @throws ObjectNotFoundException
	 *             Should not be thrown in this test since it uses mocked
	 *             objects.
	 */
	@Test
	public void testGet() throws ObjectNotFoundException {
		final UUID id = UUID.randomUUID();
		final EarlyAlertReason daoOne = new EarlyAlertReason(id);

		expect(dao.get(id)).andReturn(daoOne);

		replay(dao);

		assertNotNull("Get method should have returned a non-null instance.",
				service.get(id));
		verify(dao);
	}

	/**
	 * Test the {@link EarlyAlertReasonServiceImpl#save(EarlyAlertReason)}
	 * method.
	 * 
	 * @throws ObjectNotFoundException
	 *             Should not be thrown in this test since it uses mocked
	 *             objects.
	 * @throws ValidationException
	 *             If there were any validation errors.
	 */
	@Test
	public void testSave() throws ObjectNotFoundException, ValidationException {
		final UUID id = UUID.randomUUID();
		final EarlyAlertReason daoOne = new EarlyAlertReason(id);

		expect(dao.save(daoOne)).andReturn(daoOne);

		replay(dao);

		assertNotNull("Save method return model should not have been null.",
				service.save(daoOne));
		verify(dao);
	}

	/**
	 * Test the {@link EarlyAlertReasonServiceImpl#delete(UUID)} method.
	 * 
	 * @throws ObjectNotFoundException
	 *             Should not be thrown in this test since it uses mocked
	 *             objects.
	 */
	@Test
	public void testDelete() throws ObjectNotFoundException {
		final UUID id = UUID.randomUUID();
		final EarlyAlertReason daoOne = new EarlyAlertReason(id);

		expect(dao.get(id)).andReturn(daoOne);
		expect(dao.save(daoOne)).andReturn(daoOne);
		expect(dao.get(id)).andThrow(
				new ObjectNotFoundException(id, "EarlyAlertReason"));

		replay(dao);

		service.delete(id);

		try {
			final EarlyAlertReason daoTwo = service.get(id);
			assertNull(
					"Recently deleted object should not have been able to be reloaded.",
					daoTwo);
		} catch (final ObjectNotFoundException e) {
			// expected exception
			assertNotNull(
					"Recently deleted object should not have been found when attempting to reload.",
					e);
		}

		verify(dao);
	}
}
