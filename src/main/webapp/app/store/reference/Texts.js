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
 * specific language governing permissions and limDitations
 * under the License.
 */
Ext.define('Ssp.store.reference.Texts', {
    extend: 'Ssp.store.reference.AbstractReferences',
    model: 'Ssp.model.reference.Text',
    constructor: function(){
    	this.callParent(arguments);
    	Ext.apply(this.getProxy(),{url: this.getProxy().url + this.apiProperties.getItemUrl('blurb'),
    		autoLoad: true,extraParams: this.extraParams});
    },
    getValueByCode:function(code)
    {
    	var me=this;
    	var index = me.findExact('code',code);
    	if(index >= 0)
    	 return me.getAt(index).get('value');
    	return null;
    }

});
