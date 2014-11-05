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
import org.jasig.ssp.dao.reference.CategoryDao;
import org.jasig.ssp.factory.reference.AbstractReferenceTOFactory;
import org.jasig.ssp.factory.reference.CategoryTOFactory;
import org.jasig.ssp.model.reference.Category;
import org.jasig.ssp.transferobject.reference.CategoryTO;

@Service
@Transactional(readOnly = true)
public class CategoryTOFactoryImpl extends
		AbstractReferenceTOFactory<CategoryTO, Category>
		implements CategoryTOFactory {

	public CategoryTOFactoryImpl() {
		super(CategoryTO.class, Category.class);
	}

	@Autowired
	private transient CategoryDao dao;

	@Override
	protected CategoryDao getDao() {
		return dao;
	}

}
