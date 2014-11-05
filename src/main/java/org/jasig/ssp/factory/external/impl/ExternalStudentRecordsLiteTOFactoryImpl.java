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
package org.jasig.ssp.factory.external.impl;

import org.jasig.ssp.dao.external.ExternalDataDao;
import org.jasig.ssp.factory.external.ExternalStudentRecordsLiteTOFactory;
import org.jasig.ssp.model.external.ExternalPerson;
import org.jasig.ssp.model.external.ExternalStudentRecordsLite;
import org.jasig.ssp.transferobject.external.ExternalPersonLiteTO;
import org.jasig.ssp.transferobject.external.ExternalStudentRecordsLiteTO;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class ExternalStudentRecordsLiteTOFactoryImpl extends
		AbstractExternalDataTOFactory<ExternalStudentRecordsLiteTO, ExternalStudentRecordsLite> implements
		ExternalStudentRecordsLiteTOFactory {

	
	public ExternalStudentRecordsLiteTOFactoryImpl() {
		super(ExternalStudentRecordsLiteTO.class, ExternalStudentRecordsLite.class);
	}
	
	
	public ExternalStudentRecordsLiteTOFactoryImpl(
			Class<ExternalStudentRecordsLiteTO> toClass,
			Class<ExternalStudentRecordsLite> mClass) {
		super(toClass, mClass);
	}

	@Override
	protected ExternalDataDao<ExternalStudentRecordsLite> getDao() {
		// TODO Auto-generated method stub
		return null;
	}

}
