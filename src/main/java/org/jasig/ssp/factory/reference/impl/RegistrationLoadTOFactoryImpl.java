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
package org.jasig.ssp.factory.reference.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.jasig.ssp.dao.reference.MilitaryAffiliationDao;
import org.jasig.ssp.dao.reference.RegistrationLoadDao;
import org.jasig.ssp.factory.reference.AbstractReferenceTOFactory;
import org.jasig.ssp.factory.reference.MilitaryAffiliationTOFactory;
import org.jasig.ssp.factory.reference.RegistrationLoadTOFactory;
import org.jasig.ssp.model.reference.MilitaryAffiliation;
import org.jasig.ssp.model.reference.RegistrationLoad;
import org.jasig.ssp.transferobject.reference.MilitaryAffiliationTO;
import org.jasig.ssp.transferobject.reference.RegistrationLoadTO;

@Service
@Transactional(readOnly = true)
public class RegistrationLoadTOFactoryImpl extends
		AbstractReferenceTOFactory<RegistrationLoadTO, RegistrationLoad>
		implements RegistrationLoadTOFactory {

	public RegistrationLoadTOFactoryImpl() {
		super(RegistrationLoadTO.class, RegistrationLoad.class);
	}

	@Autowired
	private transient RegistrationLoadDao dao;

	@Override
	protected RegistrationLoadDao getDao() {
		return dao;
	}

}
 