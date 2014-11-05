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

import java.util.List;
import java.util.UUID;

import org.jasig.ssp.dao.StrengthDao;
import org.jasig.ssp.model.Strength;
import org.jasig.ssp.model.Person;
import org.jasig.ssp.security.SspUser;
import org.jasig.ssp.service.AbstractRestrictedPersonAssocAuditableService;
import org.jasig.ssp.service.StrengthService;
import org.jasig.ssp.util.sort.SortingAndPaging;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Strength service implementation
 */
@Service
@Transactional
public class StrengthServiceImpl
		extends AbstractRestrictedPersonAssocAuditableService<Strength>
		implements StrengthService {

	@Autowired
	transient private StrengthDao dao;

	@Override
	protected StrengthDao getDao() {
		return dao;
	}

	@Override
	public List<Strength> getStrengthsForPersonIfNoneSelected(
			final List<UUID> selectedIds, final Person person,
			final SspUser requester, final String sessionId,
			final SortingAndPaging sAndP) {
		if (selectedIds == null || selectedIds.isEmpty()) {
			return (List<Strength>) getAllForPerson(person, requester, sAndP)
					.getRows();
		}

		return get(selectedIds, requester, sAndP);
	}
}