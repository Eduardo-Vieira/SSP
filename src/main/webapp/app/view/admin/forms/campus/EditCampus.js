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
Ext.define('Ssp.view.admin.forms.campus.EditCampus',{
	extend: 'Ext.form.Panel',
	alias : 'widget.editcampus',
    mixins: [ 'Deft.mixin.Injectable',
              'Deft.mixin.Controllable'],
    controller: 'Ssp.controller.admin.campus.EditCampusViewController',
    inject: {
    	store: 'coachesStore'
    },
	title: 'Edit Campus',
	initComponent: function() {
		var me=this;
        Ext.applyIf(me, {
            items: [
                {
                    xtype: 'textfield',
                    fieldLabel: 'Name',
                    anchor: '100%',
                    name: 'name',
					maxLength: 80,
                    allowBlank: false,
                    required: true
                },{
                    xtype: 'textareafield',
                    fieldLabel: 'Description',
                    anchor: '100%',
                    name: 'description'
				},{
                    xtype: 'textfield',
                    fieldLabel: 'Code',
                    name: 'code',
					maxLength: 10,
                    allowBlank: true,
                    required: false
                },{
			        xtype: 'combobox',
			        name: 'earlyAlertCoordinatorId',
			        itemId: 'earlyAlertCoordinatorCombo',
			        fieldLabel: 'Early Alert Coordinator',
			        emptyText: 'Select One',
			        store: me.store,
			        valueField: 'id',
			        displayField: 'fullName',
			        typeAhead: true,
			        queryMode: 'local',
			        allowBlank: false,
			        width: 300
                },{
                    xtype: 'oscheckbox',
                    fieldLabel: 'Active',
                    name: 'objectStatus'
                }],
	            
	            dockedItems: [{
	       		               xtype: 'toolbar',
	       		               items: [{
			       		                   text: 'Save',
			       		                   xtype: 'button',
			       		                   action: 'save',
			       		                   itemId: 'saveButton',
										   formBind: true
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