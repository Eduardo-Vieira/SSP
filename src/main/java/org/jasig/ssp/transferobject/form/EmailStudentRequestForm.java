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
package org.jasig.ssp.transferobject.form;

import java.io.Serializable;
import java.util.UUID;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@JsonIgnoreProperties(ignoreUnknown = true)
public class EmailStudentRequestForm extends AbstractEmailForm implements Serializable {

	private static final long serialVersionUID = 1L;

	private UUID studentId;
	
	private UUID confidentialityLevelId;
	
	private Boolean createJournalEntry;

	private static final Logger LOGGER = LoggerFactory
			.getLogger(EmailStudentRequestForm.class);
	
	@Override
	protected Logger getLogger(){
		return LOGGER;
	}
	
	public UUID getStudentId() {
		return studentId;
	}

	public void setStudentId(UUID studentId) {
		this.studentId = studentId;
	}
	
	public Boolean hasStudentId(){
		return this.studentId == null ? false:true;
	}

	public UUID getConfidentialityLevelId() {
		return confidentialityLevelId;
	}

	public void setConfidentialityLevelId(UUID confidentialityLevelId) {
		this.confidentialityLevelId = confidentialityLevelId;
	}

	public Boolean getCreateJournalEntry() {
		return createJournalEntry;
	}

	public void setCreateJournalEntry(Boolean createJournalEntry) {
		this.createJournalEntry = createJournalEntry;
	}
}