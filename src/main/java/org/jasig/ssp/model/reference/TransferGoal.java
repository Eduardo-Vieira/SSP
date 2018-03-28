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

import org.jasig.ssp.model.Auditable;

import javax.persistence.Entity;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.validation.constraints.NotNull;
import java.util.UUID;

/**
 * Tag reference object.
 */
@Entity
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
public class TransferGoal
		extends AbstractReference
		implements Auditable, Cloneable {

	//private static final long serialVersionUID  = 7607015313995397895L;

	/**
	 *
	 */
	private static final long serialVersionUID = 1951851647836192289L;

	/**
	 * Constructor
	 */
	public TransferGoal() {
		super();
	}

	/**
	 * Constructor
	 *
	 * @param id
	 *            Identifier; required
	 */

	public TransferGoal(@NotNull final UUID id) {
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
	 */
	public TransferGoal(@NotNull final UUID id, @NotNull final String name,
                        final String description) {
		super(id, name, description);
	}
	
	@Override
	protected int hashPrime() {
		return 375;
	}
	
	@Override
	public int hashCode() { 
		int result = hashPrime() * super.hashCode();
		return result;
	}
	// default hashCode okay if no extra fields are added

	public TransferGoal clone() {
		TransferGoal clone = new TransferGoal();
		clone.setId(this.getId());
		clone.setName(this.getName());
		clone.setDescription(this.getDescription());
		clone.setCreatedBy(this.getCreatedBy());
		clone.setCreatedDate(this.getCreatedDate());
		clone.setModifiedBy(this.getModifiedBy());
		clone.setModifiedDate(this.getModifiedDate());
		clone.setObjectStatus(this.getObjectStatus());
		return clone;
	}
}
