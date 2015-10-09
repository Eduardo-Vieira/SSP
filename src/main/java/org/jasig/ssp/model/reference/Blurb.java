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

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.NotEmpty;
import org.jasig.ssp.model.Auditable;

/**
 * RegistrationLoad reference object.
 */
@Entity
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
public class Blurb
		extends AbstractReference
		implements Auditable {

	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	
	@Column(nullable = false, length = 80)
	@NotNull
	@NotEmpty
	private String code;
	
	@Column(nullable = false, length = 80)
	@NotNull
	@NotEmpty
	private String value;
	
	@Column(name="lang", nullable = true, length = 3)
	@NotNull
	@NotEmpty
	private String lang;	

	/**
	 * Constructor
	 */
	public Blurb() {
		super();
	}


	@Override
	protected int hashPrime() {
		return 353;
	}


	public String getCode() {
		return code;
	}


	public void setCode(String code) {
		this.code = code;
	}

	public String getValue() {
		return value;
	}


	public void setValue(String value) {
		this.value = value;
	}
	
	public String getLang() {
		return value;
	}


	public void setLang(String LangCode) {
		this.lang = value;
	}


}
