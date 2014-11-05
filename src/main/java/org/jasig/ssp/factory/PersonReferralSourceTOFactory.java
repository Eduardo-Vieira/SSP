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
package org.jasig.ssp.factory;

import java.util.Collection;
import java.util.Set;

import org.jasig.ssp.model.Person;
import org.jasig.ssp.model.PersonReferralSource;
import org.jasig.ssp.model.reference.ReferralSource;
import org.jasig.ssp.service.ObjectNotFoundException;
import org.jasig.ssp.transferobject.PersonReferralSourceTO;
import org.jasig.ssp.transferobject.reference.ReferenceLiteTO;

public interface PersonReferralSourceTOFactory extends
		TOFactory<PersonReferralSourceTO, PersonReferralSource> {

	PersonReferralSource fromLite(
			final ReferenceLiteTO<ReferralSource> lite,
			final Person person) throws ObjectNotFoundException;

	Set<PersonReferralSource> fromLites(
			final Collection<ReferenceLiteTO<ReferralSource>> lites,
			final Person person) throws ObjectNotFoundException;

	void updateSetFromLites(
			final Set<PersonReferralSource> updateSet,
			final Collection<ReferenceLiteTO<ReferralSource>> lites,
			final Person person) throws ObjectNotFoundException;
}
