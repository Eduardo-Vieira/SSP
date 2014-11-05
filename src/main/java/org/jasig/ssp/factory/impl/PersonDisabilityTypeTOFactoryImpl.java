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
package org.jasig.ssp.factory.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.jasig.ssp.dao.PersonDisabilityTypeDao;
import org.jasig.ssp.factory.AbstractAuditableTOFactory;
import org.jasig.ssp.factory.PersonDisabilityTypeTOFactory;
import org.jasig.ssp.model.PersonDisabilityType;
import org.jasig.ssp.service.ObjectNotFoundException;
import org.jasig.ssp.service.PersonService;
import org.jasig.ssp.service.reference.DisabilityTypeService;
import org.jasig.ssp.transferobject.PersonDisabilityTypeTO;

@Service
@Transactional(readOnly = true)
public class PersonDisabilityTypeTOFactoryImpl extends
		AbstractAuditableTOFactory<PersonDisabilityTypeTO, PersonDisabilityType>
		implements PersonDisabilityTypeTOFactory {

	public PersonDisabilityTypeTOFactoryImpl() {
		super(PersonDisabilityTypeTO.class, PersonDisabilityType.class);
	}

	@Autowired
	private transient PersonDisabilityTypeDao dao;

	@Autowired
	private transient PersonService personService;

	@Autowired
	private transient DisabilityTypeService disabilityTypeService;

	@Override
	protected PersonDisabilityTypeDao getDao() {
		return dao;
	}

	@Override
	public PersonDisabilityType from(final PersonDisabilityTypeTO tObject)
			throws ObjectNotFoundException {
		final PersonDisabilityType model = super.from(tObject);

		model.setDescription(tObject.getDescription());

		model.setDisabilityType((tObject.getDisabilityTypeId() == null) ? null :
				disabilityTypeService.get(tObject.getDisabilityTypeId()));

		if (tObject.getPersonId() != null) {
			model.setPerson(personService.get(tObject.getPersonId()));
		}

		return model;
	}
}