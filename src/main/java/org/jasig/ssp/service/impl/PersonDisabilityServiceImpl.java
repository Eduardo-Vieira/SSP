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
package org.jasig.ssp.service.impl;

import java.util.UUID;

import org.jasig.ssp.dao.PersonDisabilityDao;
import org.jasig.ssp.model.ObjectStatus;
import org.jasig.ssp.model.Person;
import org.jasig.ssp.model.PersonDisability;
import org.jasig.ssp.service.ObjectNotFoundException;
import org.jasig.ssp.service.PersonDisabilityService;
import org.jasig.ssp.util.sort.PagingWrapper;
import org.jasig.ssp.util.sort.SortingAndPaging;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PersonDisabilityServiceImpl implements PersonDisabilityService {

	@Autowired
	private transient PersonDisabilityDao dao;

	@Override
	public PagingWrapper<PersonDisability> getAll(final SortingAndPaging sAndP) {
		return dao.getAll(sAndP);
	}

	@Override
	public PersonDisability get(final UUID id) throws ObjectNotFoundException {
		return dao.get(id);
	}

	@Override
	public PersonDisability forPerson(final Person person) {
		return person.getDisability();
	}

	@Override
	public PersonDisability create(final PersonDisability obj) {
		return dao.save(obj);
	}

	@Override
	public PersonDisability save(final PersonDisability obj)
			throws ObjectNotFoundException {
		return dao.save(obj);
	}

	@Override
	public void delete(final UUID id) throws ObjectNotFoundException {
		final PersonDisability current = get(id);

		if ((null != current)
				&& !ObjectStatus.INACTIVE.equals(current.getObjectStatus())) {
			current.setObjectStatus(ObjectStatus.INACTIVE);
			save(current);
		}
	}
}