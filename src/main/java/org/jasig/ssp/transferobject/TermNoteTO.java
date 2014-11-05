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
package org.jasig.ssp.transferobject;

import org.jasig.ssp.model.TermNote;
import org.jasig.ssp.model.external.Term;

public class TermNoteTO extends AbstractAuditableTO<TermNote> {

	private String studentNotes;
	
	private String contactNotes;
	
	private String termCode;
	
	private Boolean isImportant = false;
	
	public TermNoteTO() {
		super();
	}	

	public TermNoteTO(TermNote model) {
		super();
		from(model);
	}	

	@Override
	public void from(TermNote model) {
		super.from(model);
		this.setContactNotes(model.getContactNotes());
		this.setIsImportant(model.getIsImportant());
		this.setStudentNotes(model.getStudentNotes());
		this.setTermCode(model.getTermCode());
	}
	
	public String getStudentNotes() {
		return studentNotes;
	}

	public void setStudentNotes(String studentNotes) {
		this.studentNotes = studentNotes;
	}

	public String getContactNotes() {
		return contactNotes;
	}

	public void setContactNotes(String contactNotes) {
		this.contactNotes = contactNotes;
	}

	public String getTermCode() {
		return termCode;
	}

	public void setTermCode(String termCode) {
		this.termCode = termCode;
	}

	public Boolean getIsImportant() {
		return isImportant;
	}

	public void setIsImportant(Boolean isImportant) {
		this.isImportant = isImportant;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((contactNotes == null) ? 0 : contactNotes.hashCode());
		result = prime * result
				+ ((isImportant == null) ? 0 : isImportant.hashCode());
		result = prime * result
				+ ((studentNotes == null) ? 0 : studentNotes.hashCode());
		result = prime * result
				+ ((termCode == null) ? 0 : termCode.hashCode());
		return result;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if(obj.getClass().equals(Term.class)){
				return ((Term)obj).getCode().equals(termCode);
		}
			
		if (getClass() != obj.getClass())
			return false;
		TermNoteTO other = (TermNoteTO) obj;
		if (contactNotes == null) {
			if (other.contactNotes != null)
				return false;
		} else if (!contactNotes.equals(other.contactNotes))
			return false;
		if (isImportant == null) {
			if (other.isImportant != null)
				return false;
		} else if (!isImportant.equals(other.isImportant))
			return false;
		if (studentNotes == null) {
			if (other.studentNotes != null)
				return false;
		} else if (!studentNotes.equals(other.studentNotes))
			return false;
		if (termCode == null) {
			if (other.termCode != null)
				return false;
		} else if (!termCode.equals(other.termCode))
			return false;
		return true;
	}
	

}
