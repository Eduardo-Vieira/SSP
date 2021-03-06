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

package org.jasig.ssp.dao.external;

import java.util.ArrayList;
import java.util.List;
import org.hibernate.Criteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.jasig.ssp.model.external.ExternalStudentAcademicProgram;
import org.jasig.ssp.util.hibernate.BatchProcessor;
import org.springframework.stereotype.Repository;


@Repository
public class ExternalStudentAcademicProgramDao extends
		AbstractExternalDataDao<ExternalStudentAcademicProgram> {

	public ExternalStudentAcademicProgramDao()
	{
		super(ExternalStudentAcademicProgram.class);
	}
	
	public List<ExternalStudentAcademicProgram> getAcademicProgramsBySchoolId(String schoolId){
		Criteria criteria = createCriteria();
		criteria.add(Restrictions.eq("schoolId", schoolId));
		criteria.addOrder(Order.asc("degreeName"));
		
		return (List<ExternalStudentAcademicProgram>)criteria.list();
	}

	public List<ExternalStudentAcademicProgram> getAcademicProgramsBySchoolIds(final List<String> schoolIds) {

		if ( schoolIds == null || schoolIds.size() < 1 ) {
			return new <ExternalStudentAcademicProgram>ArrayList();
		}

		final BatchProcessor<String, ExternalStudentAcademicProgram> processor = new BatchProcessor<>(schoolIds);
		do {
			final Criteria criteria = createCriteria();
			criteria.addOrder(Order.asc("degreeName"));
			processor.process(criteria, "schoolId");

		} while ( processor.moreToProcess() );

		return processor.getUnsortedUnpagedResultsAsList();
	}
}
