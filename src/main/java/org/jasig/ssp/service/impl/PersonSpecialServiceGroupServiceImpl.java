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

import org.apache.commons.lang.StringUtils;
import org.jasig.ssp.dao.PersonSpecialServiceGroupDao;
import org.jasig.ssp.model.ObjectStatus;
import org.jasig.ssp.model.Person;
import org.jasig.ssp.model.PersonSpecialServiceGroup;
import org.jasig.ssp.service.AbstractPersonAssocAuditableService;
import org.jasig.ssp.service.PersonSpecialServiceGroupService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;


@Service
public class PersonSpecialServiceGroupServiceImpl extends AbstractPersonAssocAuditableService<PersonSpecialServiceGroup>
		implements PersonSpecialServiceGroupService {

	private static final Logger LOGGER = LoggerFactory.getLogger(PersonSpecialServiceGroupServiceImpl.class);

	@Autowired
	private transient PersonSpecialServiceGroupDao dao;

	@Override
	protected PersonSpecialServiceGroupDao getDao() {
		return dao;
	}

	@Override
	public List<String> getAllSSGCodesForPerson(final Person person, final ObjectStatus objectStatus) {
		if (person != null) {
			return dao.getAllCodesForPersonId(person.getId(), objectStatus);
		} else {
			LOGGER.debug("Can't get Special Service Groups by Code for Person that is empty!");
			return null;
		}
	}

	@Override
	public void deleteAllForPerson(final Person person) {
	    if (person != null) {
	        dao.deleteAllForPerson(person.getId());
        } else {
            LOGGER.debug("Can't delete Special Service Groups for Person that is empty!");
        }
	}

	@Override
	public void deleteByCode(final String code, final Person person) {
		if (person != null && StringUtils.isNotBlank(code)) {
		    dao.deleteForPersonByCode(code, person.getId());
		} else {
            LOGGER.debug("Can't delete Special Service Groups for Person and/or Code that is empty!");
        }
	}
}
