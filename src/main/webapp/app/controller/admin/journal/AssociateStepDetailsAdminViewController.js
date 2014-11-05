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
Ext.define('Ssp.controller.admin.journal.AssociateStepDetailsAdminViewController', {
	extend: 'Ssp.controller.admin.journal.AssociateJournalAdminViewController',
    config: {
        associatedItemType: 'journalStepDetail',
        parentItemType: 'journalStep',
        parentIdAttribute: 'journalStepId',
        associatedItemIdAttribute: 'journalStepDetailId',
        nodeSortFunction: function(rec1, rec2){
        	if(rec1.objectStatus != rec2.objectStatus)
        		return rec1.objectStatus == 'ACTIVE' ? -1:1;
        	return rec1.sortOrder - rec2.sortOrder;
        }},
	constructor: function(){
		var me=this;
		me.callParent(arguments);
		me.clear();
		
		var params = {status: "ACTIVE", limit: "-1"};
		this.getParentItemsWithParams(params);
		
		//me.getParentItems();	
		return me;
	}
});