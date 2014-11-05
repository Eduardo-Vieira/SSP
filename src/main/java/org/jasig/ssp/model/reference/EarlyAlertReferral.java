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
package org.jasig.ssp.model.reference;

import java.util.UUID;

import javax.annotation.Nullable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.validation.constraints.NotNull;

import org.jasig.ssp.model.Auditable;

/**
 * EarlyAlertReferral reference object.
 * 
 * @author jon.adams
 */
@Entity
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
public class EarlyAlertReferral
		extends AbstractReference
		implements Auditable {

	private static final long serialVersionUID = -1740796259941040664L;

	@Column(nullable = false)
	@NotNull
	private short sortOrder = 0; // NOPMD by jon.adams on 5/4/12 1:43 PM

	@Column(nullable = false)
	@NotNull
	private String acronym;
	
	@Column(nullable = true,  length = 100)
	@Nullable
	private String recipientEmailAddress;
	
	@Column(nullable = true,  length = 512)
	@Nullable
	private String carbonCopy;

	/**
	 * Constructor
	 */
	public EarlyAlertReferral() {
		super();
	}

	/**
	 * Constructor
	 * 
	 * @param id
	 *            Identifier; required
	 */

	public EarlyAlertReferral(@NotNull final UUID id) {
		super(id);
	}

	/**
	 * Constructor
	 * 
	 * @param id
	 *            Identifier; required
	 * @param name
	 *            Name; required; max 80 characters
	 * @param description
	 *            Description; max 64000 characters
	 * @param sortOrder
	 *            Default sort order when displaying objects to the user
	 * @param acronym
	 *            acronym (a.k.a. code)
	 */
	public EarlyAlertReferral(@NotNull final UUID id,
			@NotNull final String name,
			final String description, final short sortOrder, // NOPMD by jon
			final String acronym) {
		super(id, name, description);
		this.sortOrder = sortOrder;
		this.acronym = acronym;
	}

	/**
	 * Gets the default sort order when displaying an item list to the user
	 * 
	 * @return the sortOrder
	 */
	public short getSortOrder() { // NOPMD by jon.adams on 5/4/12 1:43 PM
		return sortOrder;
	}

	/**
	 * Sets the default sort order when displaying an item list to the user
	 * 
	 * @param sortOrder
	 *            the sortOrder to set
	 */
	public void setSortOrder(final short sortOrder) { // NOPMD by jon on 5/4/12
														// 11:16
		this.sortOrder = sortOrder;
	}

	/**
	 * @return the acronym (a.k.a. code)
	 */
	public String getAcronym() {
		return acronym;
	}

	/**
	 * @param acronym
	 *            the acronym (a.k.a. code) to set
	 */
	public void setAcronym(@NotNull final String acronym) {
		this.acronym = acronym;
	}

	public String getRecipientEmailAddress() {
		return recipientEmailAddress;
	}

	public void setRecipientEmailAddress(String recipientEmailAddress) {
		this.recipientEmailAddress = recipientEmailAddress;
	}

	public String getCarbonCopy() {
		return carbonCopy;
	}

	public void setCarbonCopy(String carbonCopy) {
		this.carbonCopy = carbonCopy;
	}

	/**
	 * Unique (amongst all Models in the system) prime for use by
	 * {@link #hashCode()}
	 */
	@Override
	protected int hashPrime() {
		return 157;
	}

	@Override
	public int hashCode() { // NOPMD by jon.adams on 5/3/12 11:48 AM
		int result = hashPrime();

		// AbstractAuditable properties
		result *= super.hashCode();

		result *= hashField("sortOrder", sortOrder);
		result *= hashField("acronym", acronym);
		result *= hashField("recipientEmailAddress", recipientEmailAddress);
		result *= hashField("carbonCopy", carbonCopy);

		return result;
	}
}