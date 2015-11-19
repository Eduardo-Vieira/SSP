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

import java.util.List;

import org.jasig.ssp.dao.reference.BlurbDao;
import org.jasig.ssp.dao.reference.EnrollmentStatusDao;
import org.jasig.ssp.model.reference.Blurb;
import org.jasig.ssp.model.reference.EnrollmentStatus;
import org.jasig.ssp.service.reference.BlurbService;
import org.jasig.ssp.service.reference.ConfigService;
import org.jasig.ssp.service.reference.EnrollmentStatusService;
import org.jasig.ssp.util.sort.PagingWrapper;
import org.jasig.ssp.util.sort.SortingAndPaging;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service 
@Transactional
public class BlurbServiceImpl extends
		AbstractReferenceService<Blurb>
		implements BlurbService {
	
	@Autowired
	transient private BlurbDao dao;

	@Autowired
	transient private ConfigService configService;

	protected void setDao(final BlurbDao dao) {
		this.dao = dao;
	}

	@Override
	protected BlurbDao getDao() {
		return dao;
	}

	@Override
	public Blurb save(Blurb obj) throws org.jasig.ssp.service.ObjectNotFoundException ,org.jasig.ssp.web.api.validation.ValidationException {
		Blurb blurb = dao.get(obj.getId());
		if(blurb == null)
		{
			throw new org.jasig.ssp.web.api.validation.ValidationException("Blurb with PK "+obj.getId()+" does not exist.");
		}
		if(!blurb.getCode().equals(obj.getCode()) || !blurb.getName().equals(obj.getName()))
		{
			throw new org.jasig.ssp.web.api.validation.ValidationException("You can't edit a blurb's code or name.");
		}
		return super.save(obj);
	}
	@Override
	public PagingWrapper<Blurb> getAll(
			SortingAndPaging sAndP, String code) {
		return dao.getAll(sAndP, code, null);
	}
	@Override
	public PagingWrapper<Blurb> getAll(
			SortingAndPaging sAndP, String code, String langCode) {
		if (langCode==null) {
			langCode = configService.getByNameNullOrDefaultValue("defaultLanguage");
		}
		if (langCode==null) {
			langCode = "eng";
		}
		return dao.getAll(sAndP,code,langCode);
	}

}
