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
Ext.define('Ssp.view.admin.forms.campus.EditCampusEarlyAlertRouting',{
	extend: 'Ext.form.Panel',
	alias : 'widget.editcampusearlyalertrouting',
    mixins: [ 'Deft.mixin.Injectable',
              'Deft.mixin.Controllable'],
    controller: 'Ssp.controller.admin.campus.EditCampusEarlyAlertRoutingViewController',
    inject: {
    	earlyAlertReasonsStore: 'earlyAlertReasonsAllUnpagedStore',
    	coachesStore: 'coachesStore',
    	personService: 'personService'
    },
	title: 'Edit Routing Group',
	initComponent: function() {
        var me=this;
		Ext.applyIf(me, {
		    fieldDefaults: {
		        msgTarget: 'side',
		        labelAlign: 'right',
		        labelWidth: 125
		    },
            items: [{
			        xtype: 'combobox',
			        name: 'earlyAlertReasonId',
			        itemId: 'earlyAlertReasonCombo',
			        fieldLabel: 'Early Alert Reason',
			        emptyText: 'Select One',
			        store: me.earlyAlertReasonsStore,
			        valueField: 'id',
			        displayField: 'name',
			        typeAhead: true,
			        queryMode: 'local',
			        allowBlank: true,
			        width: 500
				},{
                    xtype: 'textfield',
                    fieldLabel: 'Group Name',
                    width: 500,
                    name: 'groupName',
                    allowBlank: true,
                    maxLength: 255
                },{
                    xtype: 'textfield',
                    fieldLabel: 'Group Email',
                    name: 'groupEmail',
                    width: 500,
                    vtype:'email',
			        maxLength: 100,
			        allowBlank: true
                },{
		            xtype: 'combobox',
		            store: me.coachesStore,
		            itemId: 'personCombo',
                    name: 'personCombo',
		            displayField: 'fullName',
		            emptyText: 'Select One',
		            valueField:'id',
		            typeAhead: true,
		            fieldLabel: 'Person',
		            queryMode: 'local',
		            width: 300
		        }],
            
            dockedItems: [{
       		               xtype: 'toolbar',
       		               items: [{
		       		                   text: 'Save',
		       		                   xtype: 'button',
		       		                   action: 'save',
		       		                   itemId: 'saveButton',
		       		                   formBind: false
		       		               }, '-', {
		       		                   text: 'Cancel',
		       		                   xtype: 'button',
		       		                   action: 'cancel',
		       		                   itemId: 'cancelButton'
		       		               }]
       		           }]
        });

        return me.callParent(arguments);
    }	
});