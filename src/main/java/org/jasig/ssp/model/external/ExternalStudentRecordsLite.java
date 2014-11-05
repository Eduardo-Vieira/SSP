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
package org.jasig.ssp.model.external;

import java.io.Serializable;
import java.util.List;

public class ExternalStudentRecordsLite extends AbstractExternalData implements
		ExternalData, Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -5994592133348666761L;
	private List<ExternalStudentFinancialAidAwardTerm> financialAidAcceptedTerms;
	private List<ExternalStudentFinancialAidFile> financialAidFiles;

	private ExternalStudentTranscript gpa;
	private List<ExternalStudentAcademicProgram> programs;
	private ExternalStudentFinancialAid financialAid;
	/**
	 * @return the gpa
	 */
	public ExternalStudentTranscript getGPA() {
		return gpa;
	}
	/**
	 * @param gpa the gpa to set
	 */
	public void setGPA(ExternalStudentTranscript gpa) {
		this.gpa = gpa;
	}
	/**
	 * @return the programs
	 */
	public List<ExternalStudentAcademicProgram> getPrograms() {
		return programs;
	}
	/**
	 * @param programs the programs to set
	 */
	public void setPrograms(List<ExternalStudentAcademicProgram> programs) {
		this.programs = programs;
	}
	/**
	 * @return the financialAid
	 */
	public ExternalStudentFinancialAid getFinancialAid() {
		return financialAid;
	}
	/**
	 * @param financialAid the financialAid to set
	 */
	public void setFinancialAid(ExternalStudentFinancialAid financialAid) {
		this.financialAid = financialAid;
	}
	
	/**
	 * @return the financialAidAcceptedTerms
	 */
	public List<ExternalStudentFinancialAidAwardTerm> getFinancialAidAcceptedTerms() {
		return financialAidAcceptedTerms;
	}
	/**
	 * @param financialAidAcceptedTerms the financialAidAcceptedTerms to set
	 */
	public void setFinancialAidAcceptedTerms(
			List<ExternalStudentFinancialAidAwardTerm> financialAidAcceptedTerms) {
		this.financialAidAcceptedTerms = financialAidAcceptedTerms;
	}
	public List<ExternalStudentFinancialAidFile> getFinancialAidFiles() {
		return financialAidFiles;
	}
	public void setFinancialAidFiles(
			List<ExternalStudentFinancialAidFile> financialAidFiles) {
		this.financialAidFiles = financialAidFiles;
	}

}
