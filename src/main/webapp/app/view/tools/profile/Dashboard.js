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
Ext.define('Ssp.view.tools.profile.Dashboard', {
    extend: 'Ext.form.Panel',
    alias: 'widget.profiledashboard',
    mixins: ['Deft.mixin.Injectable', 'Deft.mixin.Controllable'],
    controller: 'Ssp.controller.tool.profile.ProfilePersonViewController',
    inject: {
		authenticatedPerson: 'authenticatedPerson',
		textStore: 'sspTextStore'
    },
    width: '100%',
    height: '100%',
    
    initComponent: function(){
        var me = this;
		
        Ext.apply(me, {
            border: 0,
            bodyPadding: 10,
            defaults: {
                layout: {
                    type: 'hbox',
                    align: 'stretch'
                },
                flex: 1,
                padding: 0,
                border: 0
            },
            
            items: [{
                xtype: 'container',
                margin: 0,
                items: [{
                    xtype: 'profileperson',
                    flex: 3
                }, {
                    xtype: 'tbspacer',
                    width: 10
                }, {
                    xtype: 'profileservicereasons',
					flex: 1
                }, {
                    xtype: 'tbspacer',
                    width: 10
                }, {
                    xtype: 'profilespecialservicegroups',
					flex: 1
                }, {
                    xtype: 'tbspacer',
                    width: 10
                }, {
                    xtype: 'profilereferralsources',
					flex: 1
                }]
            }, {
                xtype: 'evaluatedsuccessindicatorgroups'
            }]
        });
        
        return me.callParent(arguments);
    }
    
});
