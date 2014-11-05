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

import org.jasig.ssp.dao.AuditableCrudDao;
import org.jasig.ssp.model.reference.Campus;
import org.springframework.stereotype.Repository;

/**
 * Data access class for the Campus reference entity.
 * 
 * @author jon.adams
 */
@Repository
public class CampusDao extends AbstractReferenceAuditableCrudDao<Campus>
		implements AuditableCrudDao<Campus> {

	/**
	 * Constructor that initializes the instance with the specific type for use
	 * by the base class methods.
	 */
	public CampusDao() {
		super(Campus.class);
	}
}
