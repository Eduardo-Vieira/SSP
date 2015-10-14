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
package org.jasig.ssp.dao.reference;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;
import org.jasig.ssp.dao.AuditableCrudDao;
import org.jasig.ssp.model.ObjectStatus;
import org.jasig.ssp.model.reference.Blurb;
import org.jasig.ssp.util.sort.PagingWrapper;
import org.jasig.ssp.util.sort.SortDirection;
import org.jasig.ssp.util.sort.SortingAndPaging;
import org.springframework.stereotype.Repository;

/**
 * Data access class for the Blurb reference entity.
 */
@Repository
public class BlurbDao extends AbstractReferenceAuditableCrudDao<Blurb>
		implements AuditableCrudDao<Blurb> {

	public BlurbDao() {
		super(Blurb.class);
	}
	
	public PagingWrapper<Blurb> getAll(
			final SortingAndPaging sAndP, String code, String langCode) {

		Criteria criteria = createCriteria();
		if (code != null) {
			code = code.replace('*', '%');
			criteria.add(Restrictions.like("code", code));
		}
		if (langCode != null) {
			criteria.add(Restrictions.eq("lang", langCode));
		}
		sAndP.addAll(criteria);
		return super.processCriteriaWithStatusSortingAndPaging(criteria,
				sAndP);
	}
	
}
