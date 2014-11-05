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
Ext.define('Ssp.view.tools.profile.Person', {
    extend: 'Ext.form.FieldContainer',
    alias: 'widget.profileperson',
    mixins: ['Deft.mixin.Injectable', 'Deft.mixin.Controllable'],
    inject: {
        columnRendererUtils: 'columnRendererUtils',
        textStore: 'sspTextStore'
    },
    width: '100%',
    height: '100%',
    minHeight: 200,
    
    initComponent: function(){
        var me = this;
        Ext.apply(me, {
            fieldLabel: '',
            layout: 'hbox',
            margin: '0 0 0 0',
            height: '150',
            defaultType: 'displayfield',
            fieldDefaults: {
                msgTarget: 'side'
            },
            
            items: [{
                xtype: 'image',
                fieldLabel: '',
                src: Ssp.util.Constants.DEFAULT_NO_STUDENT_PHOTO_URL,
                itemId: 'studentPhoto',
                width: 160
            }, {
                xtype: 'fieldset',
                border: 0,
                padding: '0 0 0 5',
                title: '',
                defaultType: 'displayfield',
				flex: 1,
                defaults: {
                    anchor: '100%',
                    height: 16,
                    margin: 0
                },
                items: [{
                    fieldLabel: '',
                    hideLabel: true,
                    name: 'primaryEmailAddressField',
                    itemId: 'primaryEmailAddressField'
                }, {
                    fieldLabel: me.textStore.getValueByCode('ssp.label.dob'),
                    name: 'birthDate',
                    itemId: 'birthDate',
                    labelWidth: 30
                }, {
                    fieldLabel: 'Type',
                    name: 'studentType',
                    itemId: 'studentType',
                    labelWidth: 32
                }, , {
                    fieldLabel: 'Status',
                    name: 'programStatus',
                    itemId: 'programStatus',
                    labelWidth: 45
                }, {
                    fieldLabel: 'Status Reason',
                    name: 'programStatusReason',
                    itemId: 'programStatusReason',
                    hidden: true
                }, {
                    fieldLabel: 'Academic Program',
                    name: 'academicPrograms',
                    itemId: 'academicPrograms',
                    labelAlign: 'top',
					fieldCls: 'wrappable-cell',
					width: '98%'
                }, {
                    fieldLabel: 'Plan Name',
                    name: 'mapName',
                    itemId: 'mapName',
                    labelAlign: 'top',
					fieldCls: 'wrappable-cell',
					width: '98%'
                }, {
                    fieldLabel: 'Plan Owner',
                    name: 'advisor',
                    itemId: 'advisor',
                    labelWidth: 70,
					fieldCls: 'wrappable-cell',
					width: '98%'
                }]
            
            }]
        
        });
        
        return me.callParent(arguments);
    }
    
});
