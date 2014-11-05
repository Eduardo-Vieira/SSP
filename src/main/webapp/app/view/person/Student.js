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
Ext.define('Ssp.view.person.Student', {
    extend: 'Ext.form.Panel',
    alias: 'widget.student',
    mixins: ['Deft.mixin.Injectable', 'Deft.mixin.Controllable'],
    controller: 'Ssp.controller.person.StudentViewController',
    inject: {
        textStore: 'sspTextStore'
    },
    width: '100%',
    height: '100%',
    layout: {
        align: 'stretch',
        type: 'hbox'
    },
    bodyPadding: 5,
    
    initComponent: function(){
        var me = this;
        
        Ext.applyIf(me, {
            border: 0,
            bodyPadding: 0,
            
            fieldDefaults: {
                msgTarget: 'side',
                labelAlign: 'right',
                labelWidth: 100
            },
			items: [
                {
                    xtype: 'fieldcontainer',
                    flex: 0.35,
					padding: '0 10 0 0',
					margin: '0 0 0 0',
                    fieldLabel: '',
					layout: {
						type: 'vbox'
					},
					items: [
                        {
                            xtype: 'editperson'
                        }
                    ]
                },
                {
                    xtype: 'fieldcontainer',
                    flex: 0.35,
                    fieldLabel: '',
					padding: '0 10 0 0',
					margin: '0 0 0 0',
					layout: {
						type: 'vbox'
					},
					items: [
                        {
                            xtype: 'personcoach'
                        },
						{
                    xtype: 'fieldset',
					padding: '0 10 0 0',
                    title: 'Student Intake',
					border: 0,
                    layout: {
						flex: .90
                    },
                    items: [{
                        xtype: 'studentIntakeRequest',
                        //flex: .50
                    },{
                            fieldLabel: 'Student Intake Request Last Sent ',
                            name: 'studentIntakeRequestDateInCaseload',
                            itemId: 'studentIntakeRequestDateInCaseload',
                            xtype: 'displayfield',
                            labelWidth: 195,
                            renderer: Ext.util.Format.dateRenderer('m/d/Y')
                        }, {
                            fieldLabel: 'Student Intake Request Completed',
                            name: 'studentIntakeCompleteDateInCaseload',
                            itemId: 'studentIntakeCompleteDateInCaseload',
                            xtype: 'displayfield',
                            labelWidth: 195,
                            value: 'test',
                            renderer: Ext.util.Format.dateRenderer('m/d/Y')
                        
                        }]
            }
						
                    ]
                },
                {
                    xtype: 'fieldcontainer',
                    fieldLabel: '',
					padding: ' 0 0 0 0',
					margin: '0 0 0 0',
					flex: 0.30,
					layout: {
						type: 'vbox'
					},
					items: [
                        {
                    xtype: 'personappointment'
                }, {
                    xtype: 'personanticipatedstartdate'
                }
                    ]
                }
            ]
			
            
        });
        
        me.callParent(arguments);
    }
    
});
