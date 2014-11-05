/*
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
Ext.define('Ssp.controller.admin.journal.AssociateTrackStepsAdminViewController', {
	extend: 'Ssp.controller.admin.journal.AssociateJournalAdminViewController',
    config: {
        associatedItemType: 'journalStep',
        parentItemType: 'journalTrack',
        parentIdAttribute: 'journalTrackId',
        associatedItemIdAttribute: 'journalStepId'
    },
	constructor: function(){
		this.callParent(arguments);
		
		this.clear();
		//this.getParentItems();
		
		var params = {status: "ACTIVE", limit: "-1"};
		this.getParentItemsWithParams(params);
		
		return this;
	}	
});