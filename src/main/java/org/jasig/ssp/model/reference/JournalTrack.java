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

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import javax.persistence.Entity;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;

import org.jasig.ssp.model.Auditable;

/**
 * JournalTrack reference object.
 */
@Entity
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
public class JournalTrack
		extends AbstractReference
		implements Auditable {

	private static final long serialVersionUID = 2719277716161933677L;

	public static final UUID JOURNALTRACK_EARLYALERT_ID = UUID
			.fromString("B2D07B38-5056-A51A-809D-81EA2F3B27BF");

	private int sortOrder;

	/**
	 * Journal steps. Changes to this side of the relationship are not
	 * persisted.
	 */
	@OneToMany(mappedBy = "journalTrack")
	@OrderBy("sortOrder")
	private List<JournalTrackJournalStep> journalTrackJournalSteps = new ArrayList<JournalTrackJournalStep>(
			0);

	/**
	 * Empty constructor
	 */
	public JournalTrack() {
		super();
	}

	/**
	 * Constructor
	 * 
	 * @param id
	 *            Identifier; required
	 */

	public JournalTrack(final UUID id) {
		super(id);
	}

	/**
	 * Constructor
	 * 
	 * @param id
	 *            Identifier; required
	 * @param name
	 *            Name; required; max 100 characters
	 */

	public JournalTrack(final UUID id, final String name) {
		super(id, name);
	}

	/**
	 * Gets the sort order
	 * 
	 * @return the sort order
	 */
	public int getSortOrder() {
		return sortOrder;
	}

	public void setSortOrder(final int sortOrder) {
		this.sortOrder = sortOrder;
	}

	public List<JournalTrackJournalStep> getJournalTrackJournalSteps() {
		return journalTrackJournalSteps;
	}

	public void setJournalTrackJournalSteps(
			final List<JournalTrackJournalStep> journalTrackJournalSteps) {
		this.journalTrackJournalSteps = journalTrackJournalSteps;
	}

	@Override
	protected int hashPrime() {
		return 227;
	}

	@Override
	final public int hashCode() { // NOPMD by jon.adams on 5/3/12 11:48 AM
		return hashPrime() * super.hashCode()
				* hashField("sortOrder", sortOrder);
	}
}