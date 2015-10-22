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
Ext.define('Ssp.controller.person.SpecialServiceGroupsViewController', {
    extend: 'Deft.mvc.ViewController',
    mixins: [ 'Deft.mixin.Injectable' ],
    inject: {
    	apiProperties: 'apiProperties',
    	columnRendererUtils: 'columnRendererUtils',
    	formRendererUtils: 'formRendererUtils',
    	person: 'currentPerson',
    	store: 'specialServiceGroupsAllUnpagedStore',
    	service: 'specialServiceGroupService',
        itemSelectorInitializer: 'itemSelectorInitializer',
        textStore: 'sspTextStore'
    },
	init: function() {
		var me=this;
				
		me.service.getAll({
			success: me.getAllSuccess,
			failure: me.getAllFailure,
			scope: me
		});
		
		return me.callParent(arguments);
    },
    
	getAllSuccess: function( r, scope ){
		var me=scope;
    	var selectedSpecialServiceGroups = me.columnRendererUtils.getSelectedIdsForMultiSelect( me.person.get('specialServiceGroups') );

        me.store.loadData(r.rows);
        me.store.clearFilter(true);
        me.formRendererUtils.applyAssociativeStoreFilter(me.store, selectedSpecialServiceGroups);
		
		var assigned = me.textStore.getValueByCode('ssp.message.special-service-groups.assigned','Assigned to the Student');
		var available = me.textStore.getValueByCode('ssp.message.special-service-groups.available','Available Service Groups');
        me.itemSelectorInitializer.defineAndAddSelectorField(me.getView(), selectedSpecialServiceGroups, {
            itemId: 'specialServiceGroupsItemSelector',
            name: 'specialServiceGroups',
            fieldLabel: '<div style="float:right; width: 48%; ">' + assigned + '</div><div style="width: 50%;">' + available + '</div>',
			labelAlign: 'top',
			labelSeparator: ' ',
            store: me.store
        });

	},
	
    getAllFailure: function( response, scope ){
    	var me=scope;  	
    }

});