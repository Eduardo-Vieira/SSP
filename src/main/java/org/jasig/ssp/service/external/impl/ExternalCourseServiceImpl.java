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
package org.jasig.ssp.service.external.impl;

import java.util.List;

import org.jasig.ssp.dao.external.ExternalCourseDao;
import org.jasig.ssp.model.external.ExternalCourse;
import org.jasig.ssp.service.external.ExternalCourseService;
import org.jasig.ssp.transferobject.external.SearchExternalCourseTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Term service implementation
 * 
 * @author jon.adams
 */
@Service
@Transactional
public class ExternalCourseServiceImpl extends AbstractExternalReferenceDataService<ExternalCourse>
		implements ExternalCourseService {

	@Autowired
	transient private ExternalCourseDao dao;

	@Override
	protected ExternalCourseDao getDao() {
		return dao;
	}

	protected void setDao(final ExternalCourseDao dao) {
		this.dao = dao;
	}

	@Override
	public Boolean validateCourseForTerm(String courseCode, String termCode) {
		//Shirt Circut:  As per SSP-1923.  If the is table is empty
		//we assume the implementor does not want this validation and there for we will
		//not spam the UI with validation errors in this case
		if(!hasCourseTerms())
		{
			 return true;
		}
		return dao.validateCourseForTerm( courseCode,  termCode);
	}

	@Override
	public Boolean hasCourseTerms() {
		return dao.hasCourseTerms();
	}

	@Override
	public List<ExternalCourse> search(SearchExternalCourseTO form) {
		return dao.search(form);
	}

	@Override
	public List<String> getAllTagsForCourse(String courseCode) {
		return dao.getTagsForCourse(courseCode);
	}

	@Override
	public void flushCache() {
		dao.flushAndLoadCache();
	}
	
	@Override
	public List<String> getValidCourseCodesForTerm(String termCode, List<String> courseCodes){
		return dao.getValidCourseCodesForTerm(termCode, courseCodes);
	}
}