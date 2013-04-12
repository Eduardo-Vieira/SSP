/*
 * Licensed to Jasig under one or more contributor license
 * agreements. See the NOTICE file distributed with this work
 * for additional information regarding copyright ownership.
 * Jasig licenses this file to you under the Apache License,
 * Version 2.0 (the "License"); you may not use this file
 * except in compliance with the License. You may obtain a
 * copy of the License at:
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on
 * an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */
Ext.define('Ssp.controller.tool.map.SemesterPanelContainerViewController', {
    extend: 'Deft.mvc.ViewController',
    mixins: [ 'Deft.mixin.Injectable' ],
    inject:{
		appEventsController: 'appEventsController',
    	futureTermsStore:'futureTermsStore',
    	mapPlanService:'mapPlanService',
		formUtils: 'formRendererUtils',
		person: 'currentPerson',
        personLite: 'personLite',
    },
    
	control: {
	    	view: {
				afterrender: 'onViewReady'
	    	},
	},
	semesterStores: [],
	init: function() {
		var me=this;
		var id = me.personLite.get('id');
	    me.resetForm();
	    
	    if (id != "") {
			me.getView().setLoading(true);
			var serviceResponses = {
                failures: {},
                successes: {},
                responseCnt: 0,
                expectedResponseCnt: 1
            }
	    	 me.mapPlanService.get(id, {
	             success: me.newServiceSuccessHandler('map', me.getMapPlanServiceSuccess, serviceResponses),
	             failure: me.newServiceFailureHandler('map', me.getMapPlanServiceFailure, serviceResponses),
	             scope: me
	         });
	    }
		return me.callParent(arguments);
    },
    resetForm: function() {
        var me = this;
        me.getView().getForm().reset();
    },
    
    newServiceSuccessHandler: function(name, callback, serviceResponses) {
        var me = this;
        return me.newServiceHandler(name, callback, serviceResponses, function(name, serviceResponses, response) {
            serviceResponses.successes[name] = response;
        });
    },

    newServiceFailureHandler: function(name, callback, serviceResponses) {
        var me = this;
        return me.newServiceHandler(name, callback, serviceResponses, function(name, serviceResponses, response) {
            serviceResponses.failures[name] = response;
        });
    },

    newServiceHandler: function(name, callback, serviceResponses, serviceResponsesCallback) {
        return function(r, scope) {
            var me = scope;
            serviceResponses.responseCnt++;
            if ( serviceResponsesCallback ) {
                serviceResponsesCallback.apply(me, [name, serviceResponses, r]);
            }
            if ( callback ) {
                callback.apply(me, [ serviceResponses ]);
            }
            me.afterServiceHandler(serviceResponses);
        };
    },

    getMapPlanServiceSuccess: function(serviceResponses) {
        var me = this;
        var mapResponse = serviceResponses.successes.map.rows;
		if(mapResponse == null || mapResponse.length == 0)
			me.getMapPlanServiceFailure();
        //me.map.populateFromGenericObject(mapResponse);
    },

    getMapPlanServiceFailure: function() {
		var me = this;
    	me.futureTermsStore.addListener("load", me.createNewPlan, me);
    	me.futureTermsStore.load();
    },
	onViewReady: function(){
		
	},
	createNewPlan:function(){
		me = this;
		var view  = me.getView().getComponent("semestersets");
    	var terms = me.futureTermsStore.getRange(0);
		terms = me.formUtils.valueSortByField( terms, 'startDate' );
		if(terms.length == 0){
			return me.callParent(arguments);
		}
		var startYear =  terms[0].get("reportYear");
		var endYear = startYear + 5;
		var currentYear = startYear;
		var termsets = [];
		termsets[startYear] = [];
		i = k = 0;
		terms.forEach(function(term){
			if(termsets.hasOwnProperty(term.get("reportYear"))){
				termsets[term.get("reportYear")].push(term);
			}else if(term.get("reportYear") > endYear){
				return termsets;
			}else{
				k = 0;
				termsets[term.get("reportYear")]=[term];
			};
		});
		termsets.forEach(function(termSet){
			var yearView = view.add(new Ext.form.FieldSet({
				xtype : 'fieldset',
				border: 0,
				title : '',
				padding : '2 2 2 2',
				margin : '0 0 0 0',
				itemId : 'year' + termSet[0].get("reportYear"),
				autoScroll: true,
				flex : 1,
			}));
			termSet.forEach(function(term){
				yearView.add(me.createSemesterPanel(term.get("name") + "("  + term.get('code') + ")"));
			});
		});
    },
	
	createSemesterPanel: function(semesterName){
		var semesterStore = new Ssp.store.SemesterCourses();
		me.semesterStores.push(semesterStore);
		return new Ssp.view.tools.map.SemesterPanel({
			title:semesterName,
			itemId:semesterName,
			store: semesterStore
		});
	},
	afterServiceHandler: function(serviceResponses) {
        var me = this;
        if ( serviceResponses.responseCnt >= serviceResponses.expectedResponseCnt ) {
            me.getView().setLoading(false);
        }
    },

	destroy: function() {
        var me=this;
		me.futureTermsStore.removeListener("load", me.createNewPlan, me);
        return me.callParent( arguments );
    },
	
    
});
