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
Ext.define('Ssp.service.SearchChallengeReferralService', {  
    extend: 'Ssp.service.AbstractService',   		
    mixins: [ 'Deft.mixin.Injectable'],
    inject: {
    	apiProperties: 'apiProperties',
    	store: 'searchChallengeReferralStore'
    },
    initComponent: function() {
		return this.callParent( arguments );
    },
    
    getBaseUrl: function(){
		var me=this;
		return me.apiProperties.createUrl( me.apiProperties.getItemUrl(me.store.getBaseUrlName()) );
    },

	searchWithParams: function(params, callbacks) {
		var me=this;

		me.store.removeAll();

		// Set params in the url for Search Store
		// because the params need to be applied prior to load and not in a params
		// definition from the load method or the paging
		// toolbar applied to the SearchView will not
		// apply the params when using next or previous page

		queryStr = "";
		for (var paramName in params) {
			// TODO url encoding?
			if ( queryStr && (params[paramName] || !params[paramName] == '')) {
				queryStr += "&";
			}
			if(params[paramName] || !params[paramName] == '')
			{
				queryStr += paramName + "=" + params[paramName];
			}
		}
		
		
		Ext.apply(me.store.getProxy(),{url: me.getBaseUrl()+'?'+queryStr});

		me.store.load({
			params: {

			},
			callback: function(records, operation, success) {
				if (success)
				{
					if (callbacks != null)
					{
						callbacks.success( records, callbacks.scope );
					}
				}else{
					if (callbacks != null)
					{
						callbacks.failure( records, callbacks.scope );
					}
				}
			},
			scope: me
		});
		
	},

    
    search: function( 
    		 categoryId,
    		 challengeId,
    		 searchPhrase,
    		callbacks ){
    	var me = this;
		me.searchWithParams({
   		 categoryId: categoryId,
		 challengeId: challengeId,
		 searchPhrase: searchPhrase
		}, callbacks);
    }
});
