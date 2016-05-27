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
package org.jasig.ssp.model;

import javax.persistence.Entity;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

@Entity
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
@Table(name="map_plan_elective_course_electives")
public class PlanElectiveCourseElective extends AbstractMapElectiveCourse implements Cloneable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -5473230782807660690L;
	
	@NotNull
	@ManyToOne()
	@JoinColumn(name = "map_plan_elective_course_id", updatable = false, nullable = false)
	private PlanElectiveCourse planElectiveCourse;
	

	public PlanElectiveCourse getPlanElectiveCourse() {
		return planElectiveCourse;
	}

	public void setPlanElectiveCourse(PlanElectiveCourse planElectiveCourse) {
		this.planElectiveCourse = planElectiveCourse;
	}

	public PlanElectiveCourse getParent() {
		return planElectiveCourse;
	}
	
	@Override
	protected PlanElectiveCourseElective clone() throws CloneNotSupportedException {
		PlanElectiveCourseElective clone = new PlanElectiveCourseElective();
		cloneCommonFields(clone);
		return clone;
	}

}
