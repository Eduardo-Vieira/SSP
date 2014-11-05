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
package org.jasig.mygps.model.transferobject;

import java.io.Serializable;
import java.util.List;
import java.util.UUID;

import org.jasig.ssp.model.reference.AbstractReference;
import org.jasig.ssp.util.SspStringUtils;

public class FormQuestionTO implements Serializable {

	private static final long serialVersionUID = -4773730489429626123L;

	private UUID id;

	private String label;

	private String type;

	private boolean readOnly;

	private boolean required;

	private String value;

	private List<String> values;

	private String maximumLength;

	private List<FormOptionTO> options;

	private String validationExpression;

	private String visibilityExpression;

	private String availabilityExpression;

	public UUID getId() {
		return id;
	}

	public void setId(UUID id) {
		this.id = id;
	}

	public String getLabel() {
		return label;
	}

	public void setLabel(String label) {
		this.label = label;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public boolean isReadOnly() {
		return readOnly;
	}

	public void setReadOnly(boolean readOnly) {
		this.readOnly = readOnly;
	}

	public boolean isRequired() {
		return required;
	}

	public void setRequired(boolean required) {
		this.required = required;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public void setValueUUID(UUID value) {
		this.value = value.toString();
	}

	public void setValueAbstractReference(AbstractReference value) {
		if (null == value) {
			this.value = null;
		} else {
			if (null == value.getId()) {
				throw new IllegalArgumentException("An object must have an id");
			} else {
				this.value = value.getId().toString();
			}
		}
	}

	public void setValueBoolean(boolean value) {
		this.value = SspStringUtils.stringFromBoolean(value);
	}

	public void setValueInt(int value) {
		this.value = String.valueOf(value);
	}

	public List<String> getValues() {
		return values;
	}

	public void setValues(List<String> values) {
		this.values = values;
	}

	public String getMaximumLength() {
		return maximumLength;
	}

	public void setMaximumLength(String maximumLength) {
		this.maximumLength = maximumLength;
	}

	public List<FormOptionTO> getOptions() {
		return options;
	}

	public void setOptions(List<FormOptionTO> options) {
		this.options = options;
	}

	public String getValidationExpression() {
		return validationExpression;
	}

	public void setValidationExpression(String validationExpression) {
		this.validationExpression = validationExpression;
	}

	public String getVisibilityExpression() {
		return visibilityExpression;
	}

	public void setVisibilityExpression(String visibilityExpression) {
		this.visibilityExpression = visibilityExpression;
	}

	public String getAvailabilityExpression() {
		return availabilityExpression;
	}

	public void setAvailabilityExpression(String availabilityExpression) {
		this.availabilityExpression = availabilityExpression;
	}

	public FormOptionTO getFormOptionByValue(String value) {
		for (FormOptionTO formOptionTO : getOptions()) {
			if (formOptionTO.getValue().toUpperCase()
					.equals(value.toUpperCase())) {
				return formOptionTO;
			}
		}

		return null;
	}

	public FormOptionTO getFormOptionById(UUID formOptionId) {
		for (FormOptionTO formOptionTO : getOptions()) {
			if (formOptionTO.getId().equals(formOptionId)) {
				return formOptionTO;
			}
		}

		return null;
	}
}
