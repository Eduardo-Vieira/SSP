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
package org.jasig.ssp.service.reference.impl;

import java.util.UUID;

import org.jasig.ssp.dao.reference.ConfidentialityDisclosureAgreementDao;
import org.jasig.ssp.model.reference.ConfidentialityDisclosureAgreement;
import org.jasig.ssp.service.reference.ConfidentialityDisclosureAgreementService;
import org.jasig.ssp.transferobject.PagedResponse;
import org.jasig.ssp.transferobject.reference.ConfidentialityDisclosureAgreementTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.ResponseBody;

@Service
@Transactional
public class ConfidentialityDisclosureAgreementServiceImpl extends
		AbstractReferenceService<ConfidentialityDisclosureAgreement>
		implements ConfidentialityDisclosureAgreementService {

	@Autowired
	transient private ConfidentialityDisclosureAgreementDao dao;

	protected void setDao(final ConfidentialityDisclosureAgreementDao dao) {
		this.dao = dao;
	}

	@Override
	protected ConfidentialityDisclosureAgreementDao getDao() {
		return dao;
	}

	@Override
	public int setEnabled(UUID id) {
		return getDao().setEnabled(id);
		//return 0;
	}

	@Override
	public @ResponseBody
	ConfidentialityDisclosureAgreementTO getLive() {
		// TODO Auto-generated method stub
		return getDao().getLiveCDA();
	}

	@Override
	public ConfidentialityDisclosureAgreement save(ConfidentialityDisclosureAgreement t) {
		// TODO Auto-generated method stub
		
		return getDao().save(t);
	}
}
