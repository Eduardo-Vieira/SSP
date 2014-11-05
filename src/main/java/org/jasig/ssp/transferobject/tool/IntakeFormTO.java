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
package org.jasig.ssp.transferobject.tool;

import java.util.List;
import java.util.Map;

import javax.validation.Valid;

import org.jasig.ssp.model.PersonCompletedItem;
import org.jasig.ssp.model.tool.IntakeForm;
import org.jasig.ssp.transferobject.PersonChallengeTO;
import org.jasig.ssp.transferobject.PersonDemographicsTO;
import org.jasig.ssp.transferobject.PersonCompletedItemTO;
import org.jasig.ssp.transferobject.PersonEducationGoalTO;
import org.jasig.ssp.transferobject.PersonEducationLevelTO;
import org.jasig.ssp.transferobject.PersonEducationPlanTO;
import org.jasig.ssp.transferobject.PersonFundingSourceTO;
import org.jasig.ssp.transferobject.PersonTO;
import org.jasig.ssp.transferobject.TransferObject;

/**
 * IntakeForm transfer object
 */
public class IntakeFormTO implements TransferObject<IntakeForm> { // NOPMD

	@Valid
	private PersonTO person;

	@Valid
	private PersonDemographicsTO personDemographics;

	@Valid
	private PersonEducationGoalTO personEducationGoal;

	@Valid
	private PersonEducationPlanTO personEducationPlan;

	@Valid
	private List<PersonEducationLevelTO> personEducationLevels;

	@Valid
	private List<PersonFundingSourceTO> personFundingSources;

	@Valid
	private List<PersonChallengeTO> personChallenges;
	
	@Valid
	private List<PersonCompletedItemTO> personChecklist;

	private Map<String, Object> referenceData;

	public IntakeFormTO() {
		super();
	}

	public IntakeFormTO(final IntakeForm model) {
		super();
		from(model);
	}

	@Override
	public final void from(final IntakeForm model) { // NOPMD
		if (model.getPerson() != null) {
			person = new PersonTO(model.getPerson());
		}

		if (model.getPerson().getDemographics() != null) {
			personDemographics = new PersonDemographicsTO(model.getPerson()
					.getDemographics());
			if (personDemographics.getPersonId() == null) {
				personDemographics.setPersonId(person.getId());
			}
		}

		if (model.getPerson().getEducationGoal() != null) {
			personEducationGoal = new PersonEducationGoalTO(model.getPerson()
					.getEducationGoal());
			if (personEducationGoal.getPersonId() == null) {
				personEducationGoal.setPersonId(person.getId());
			}
		}

		if (model.getPerson().getEducationPlan() != null) {
			personEducationPlan = new PersonEducationPlanTO(model.getPerson()
					.getEducationPlan());
			if (personEducationPlan.getPersonId() == null) {
				personEducationPlan.setPersonId(person.getId());
			}
		}
		if (model.getPerson().getCompletedItems() != null 
				&& !model.getPerson().getCompletedItems().isEmpty()) {
			personChecklist = PersonCompletedItemTO.toTOList(model.getPerson().getCompletedItems());
			for (PersonCompletedItemTO completedItem : getPersonChecklist()) {
				completedItem.setPersonId(model.getPerson().getId());
			}
		}
		if ((model.getPerson().getEducationLevels() != null)
				&& !(model.getPerson().getEducationLevels().isEmpty())) {
			personEducationLevels = PersonEducationLevelTO
					.toTOList(model.getPerson().getEducationLevels());
		}

		if ((model.getPerson().getFundingSources() != null)
				&& !(model.getPerson().getFundingSources().isEmpty())) {
			personFundingSources = PersonFundingSourceTO.toTOList(model
					.getPerson().getFundingSources());
		}

		if ((model.getPerson().getChallenges() != null)
				&& !(model.getPerson().getChallenges().isEmpty())) {
			personChallenges = PersonChallengeTO.toTOList(model
					.getPerson().getChallenges());
		}
	}

	public PersonTO getPerson() {
		return person;
	}

	public void setPerson(final PersonTO person) {
		this.person = person;
	}

	public PersonDemographicsTO getPersonDemographics() {
		return personDemographics;
	}

	public void setPersonDemographics(
			final PersonDemographicsTO personDemographics) {
		this.personDemographics = personDemographics;
	}

	public PersonEducationGoalTO getPersonEducationGoal() {
		return personEducationGoal;
	}

	public void setPersonEducationGoal(
			final PersonEducationGoalTO personEducationGoal) {
		this.personEducationGoal = personEducationGoal;
	}

	public List<PersonEducationLevelTO> getPersonEducationLevels() {
		return personEducationLevels;
	}

	public void setPersonEducationLevels(
			final List<PersonEducationLevelTO> personEducationLevels) {
		this.personEducationLevels = personEducationLevels;
	}

	public PersonEducationPlanTO getPersonEducationPlan() {
		return personEducationPlan;
	}

	public void setPersonEducationPlan(
			final PersonEducationPlanTO personEducationPlan) {
		this.personEducationPlan = personEducationPlan;
	}

	public List<PersonFundingSourceTO> getPersonFundingSources() {
		return personFundingSources;
	}

	public void setPersonFundingSources(
			final List<PersonFundingSourceTO> personFundingSources) {
		this.personFundingSources = personFundingSources;
	}

	public List<PersonChallengeTO> getPersonChallenges() {
		return personChallenges;
	}

	public void setPersonChallenges(
			final List<PersonChallengeTO> personChallenges) {
		this.personChallenges = personChallenges;
	}

	public Map<String, Object> getReferenceData() {
		return referenceData;
	}

	public void setReferenceData(final Map<String, Object> referenceData) {
		this.referenceData = referenceData;
	}

	public List<PersonCompletedItemTO> getPersonChecklist() {
		return personChecklist;
	}

	public void setPersonChecklist(List<PersonCompletedItemTO> personChecklist) {
		this.personChecklist = personChecklist;
	}
}

