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
Ext.define('Ssp.controller.person.EditPersonViewController', {
    extend: 'Deft.mvc.ViewController',
    mixins: [ 'Deft.mixin.Injectable' ],
    inject: {
    	appEventsController: 'appEventsController',
        person: 'currentPerson',
        personLite: 'personLite',
        personService: 'personService',
     	formUtils: 'formRendererUtils',
        configStore: 'configStore'
    },
    control: {
    	retrieveFromExternalButton: {
    		selector: '#retrieveFromExternalButton',
    		listeners: {
                click: 'onRetrieveFromExternalClick'
            }       		
    	},
 		
    	firstNameField: {
    		selector: '#firstName',
    		listeners: {
                change: 'onStudentNameChange'
            }
    	},
    	
    	middleNameField: {
    		selector: '#middleName',
    		listeners: {
                change: 'onStudentNameChange'
            }    		
    	},
    	
    	lastNameField: {
    		selector: '#lastName',
    		listeners: {
                change: 'onStudentNameChange'
            }
    	}, 
    	
    	usernameField: {
    		selector: '#username',
    		listeners: {
                change: 'onStudentNameChange'
            }
    	}, 
    	
    	studentIdField: {
    		selector: '#studentId',
    		listeners: {
                validityChange: 'onStudentIdValidityChange'
            }
    	},
    	
    	view: {
			afterlayout: {
				fn: 'onAfterLayout',
				single: true
			}
    	},
    	
    	homePhoneField: '#homePhone',
    	workPhoneField: '#workPhone',
    	homePhoneField: '#homePhone',
    	primaryEmailAddressField: '#primaryEmailAddress',
    	secondaryEmailAddressField: '#secondaryEmailAddress'
    },  
	init: function() {
		var me=this;
		if(!me.getView().instantCaseloadAssignment){
	    	var disabled = me.configStore.getConfigByName('syncStudentPersonalDataWithExternalData');		
			var displayRetrieveFromExternalButton = me.configStore.getConfigByName('allowExternalRetrievalOfStudentDataFromCaseloadAssignment');
	    	// alias the studentId field and provide validation
			var studentIdField = me.getStudentIdField();
			Ext.apply(studentIdField, {
		                  minLength: me.configStore.getConfigByName('studentIdMinValidationLength'),
		                  minLengthText: me.configStore.getConfigByName('studentIdMinValidationErrorText'),
		                  maxLength: me.configStore.getConfigByName('studentIdMaxValidationLength'),
		                  maxLengthText: me.configStore.getConfigByName('studentIdMaxValidationErrorText'),
		                  vtype: 'studentIdValidator',
		                  vtypeText: me.configStore.getConfigByName('studentIdValidationErrorText')
	                     });		
			
			// when editing a student, 
			if (me.person.get('id') != "")
			{
				// set the retrieve from SSI button visbility
				me.getRetrieveFromExternalButton().setVisible( false );
			
				// disable fields if the external configuration mode is enabled
				me.getFirstNameField().setDisabled(disabled);
				me.getMiddleNameField().setDisabled(disabled);
				me.getLastNameField().setDisabled(disabled);
				studentIdField.setDisabled(disabled);
				me.getHomePhoneField().setDisabled(disabled);
				me.getWorkPhoneField().setDisabled(disabled);
				me.getPrimaryEmailAddressField().setDisabled(disabled);
				me.getSecondaryEmailAddressField().setDisabled(disabled);
			}
			
			
			me.getView().getForm().reset();
			me.getView().loadRecord( me.person );
			// use config to determine if the retrieveFromExternalButton should be visible
			if (me.person.get('id') == "")
			{
				me.getRetrieveFromExternalButton().setVisible( displayRetrieveFromExternalButton );			
				// enable the retrieveFromExternalButton if the studentId field is valid
				me.setRetrieveFromExternalButtonDisabled( !studentIdField.isValid() );		
			}else{
				me.getRetrieveFromExternalButton().setVisible(false);
			}
		}
		
		me.appEventsController.assignEvent({
            eventName: 'onRetrieveFromExternal',
            callBackFunc: me.onRetrieveFromExternalClick,
            scope: me
        });
		
		return me.callParent(arguments);
    },
	
	
	destroy: function(){
        var me = this;
        
        me.appEventsController.removeEvent({
            eventName: 'onRetrieveFromExternal',
            callBackFunc: me.onRetrieveFromExternalClick,
            scope: me
        });
        
        return me.callParent(arguments);
    },
	
    
    setForInstantCaseloadAssignment: function(schoolId) {
		var me=this;
    	var disabled = true;		
		var displayRetrieveFromExternalButton = me.configStore.getConfigByName('allowExternalRetrievalOfStudentDataFromCaseloadAssignment');
    	// alias the studentId field and provide validation
		var studentIdField = me.getStudentIdField();
		Ext.apply(studentIdField, {
	                  minLength: me.configStore.getConfigByName('studentIdMinValidationLength'),
	                  minLengthText: me.configStore.getConfigByName('studentIdMinValidationErrorText'),
	                  maxLength: me.configStore.getConfigByName('studentIdMaxValidationLength'),
	                  maxLengthText: me.configStore.getConfigByName('studentIdMaxValidationErrorText'),
	                  vtype: 'studentIdValidator',
	                  vtypeText: me.configStore.getConfigByName('studentIdValidationErrorText')
                     });		
				
		
			// disable fields if the external configuration mode is enabled
			me.getFirstNameField().setDisabled(disabled);
			me.getMiddleNameField().setDisabled(disabled);
			me.getLastNameField().setDisabled(disabled);
			studentIdField.setDisabled(disabled);
			me.getHomePhoneField().setDisabled(disabled);
			me.getWorkPhoneField().setDisabled(disabled);
			me.getPrimaryEmailAddressField().setDisabled(disabled);
			me.getSecondaryEmailAddressField().setDisabled(disabled);
			me.getUsernameField().setDisabled(disabled);
			
			me.getRetrieveFromExternalButton().setVisible( false );
			me.getHomePhoneField().setVisible( false );
			me.getWorkPhoneField().setVisible( false );
			me.getPrimaryEmailAddressField().setVisible( false );
			me.getSecondaryEmailAddressField().setVisible( false );
			me.getUsernameField().setVisible( false );
		
		
		
		me.getView().getForm().reset();
		var studentIdField = me.getStudentIdField();
		studentIdField.value = schoolId;
		me.onRetrieveFromExternalClick();
    },
    
    onAfterLayout: function(){
		var me = this;
    	if(me.getView().instantCaseloadAssignment == true){
    		this.setForInstantCaseloadAssignment(me.getView().schoolIdValue);
    	}
    },
	
	handleNull: function(value, defaultValue){
		if(defaultValue == null || defaultValue == undefined)
			defaultValue = "";
		if(value == null || value == undefined || value == 'null')
			return defaultValue;
		return value;
	},
    
    onStudentNameChange: function( comp, newValue, oldValue, eOpts){
    	var me=this;
    	me.person.set(comp.name,newValue);
    	if(me.getView().instantCaseloadAssignment)
    		me.appEventsController.getApplication().fireEvent('instantStudentNameChange');
    	else
    		me.appEventsController.getApplication().fireEvent('studentNameChange');
    },
    
    onStudentIdValidityChange: function(comp, isValid, eOpts){
    	var me=this;
        me.setRetrieveFromExternalButtonDisabled( !isValid );
    },
    
    setRetrieveFromExternalButtonDisabled: function( enabled ){
    	this.getRetrieveFromExternalButton().setDisabled( enabled );
    },
    
    onRetrieveFromExternalClick: function( button ){
    	var me=this;
    	var studentIdField = me.getStudentIdField();
    	var schoolId = studentIdField.value;
    	if ( studentIdField.isValid() )
    	{
    		if (schoolId != "")
    		{
    			me.getView().setLoading( true );
    			me.personService.getBySchoolId( schoolId,{
    				success: me.getBySchoolIdSuccess,
    				failure: me.getBySchoolIdFailure,
    				scope: me
    			});
    		}
    	}else{
    		Ext.Msg.alert('SSP Error','Please correct the errors in your form.');
    	}
    },
    
    getBySchoolIdSuccess: function( r, scope ){
		var me=scope;
		var model = new Ssp.model.Person();
		me.getView().setLoading( false );
		if ( r != null)
		{
			if(r.id == null || r.id === '')
			{
				me.getView().getForm().reset();
				model.populateFromExternalData( r );
				me.person.data = model.data;
				me.getView().loadRecord( me.person );
				if(!me.getView().instantCaseloadAssignment)
					me.formUtils.loadDisplay('mainview', 'caseloadassignment', true, {flex:1});
			}
			//If we find an internal record, reload the screen in 'Edit' mode.
			else
			{
				
				var model = new Ssp.model.Person();
				me.person.data = model.data;
				me.personLite.set('id', r.id);
		    	me.personService.get( r.id, {success:me.getPersonSuccess, 
					  failure:me.getBySchoolIdFailure, 
					  scope: me} );	
			}

		}else{
			Ext.Msg.alert('SSP Notification','There were no records found with the provided ID. Please try a different value.');
		}
    },    
    getPersonSuccess: function( response, scope ){
    	var me=scope;
    	var person = new Ssp.model.Person();
		me.getView().setLoading( false );
    	me.person.data = person.data;
    	me.person.populateFromGenericObject(response);
    	if(!me.getView().instantCaseloadAssignment)
    		me.formUtils.loadDisplay('mainview', 'caseloadassignment', true, {flex:1});				
    },    
    getBySchoolIdFailure: function( response, scope ){
    	var me=scope;
		var dialogOpts = {
			buttons: Ext.Msg.OK,
			icon: Ext.Msg.ERROR,
			fn: Ext.emptyFn,
			title: 'Student Not Found',
			msg: 'No matching student found for the id provided',
			scope: me
		};
		
		if(response.status == 404 && response.statusText == 'Not Found')
		{
			
			Ext.Msg.show(dialogOpts);
		}
		
    	me.getView().setLoading( false );
    }
});