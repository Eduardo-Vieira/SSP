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
package org.jasig.ssp.util.hibernate;


import org.hibernate.dialect.SQLServerDialect;
import org.hibernate.exception.spi.ViolatedConstraintNameExtracter;

/**
 * Extensions to {@link SQLServerDialect} to work around SQLServer-specific
 * issues with SSP.
 */
public class ExtendedSQLServerDialect extends SQLServerDialect {

	public ExtendedSQLServerDialect() {
		super();
	}

	private static final ViolatedConstraintNameExtracter MS_SQL_EXTRACTER
			= new SQLServerViolatedConstraintNameExtractor();

	/**
	 * Provides a {@link ViolatedConstraintNameExtracter} that tries to return
	 * the name of the violated constraint (or unique index). Note: The default
	 * (super) implementation always returns null.
	 */
	@Override
	public ViolatedConstraintNameExtracter getViolatedConstraintNameExtracter() {
		return MS_SQL_EXTRACTER;
	}

}
