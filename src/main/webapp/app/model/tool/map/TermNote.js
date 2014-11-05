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
Ext.define('Ssp.model.tool.map.TermNote', {
    extend: 'Ssp.model.AbstractBase',
    fields: [{name:'studentNotes',type:'string'},
             {name:'contactNotes',type:'string'},
             {name:'termCode',type:'string'},
             {name:'isImportant',type:'boolean'}
             ],
    getData: function(){
		var me = this;
		var obj = {};
		var fields = Ssp.model.tool.map.TermNote.getFields();
		fields.forEach(function(field){
			if(field.name != 'active'){
				obj[field.name] = me.get(field.name);
			}
		})
		return obj;
	}		        		  
});