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
Ext.define('Ssp.controller.tool.documents.UploadDocumentsViewController', {
    extend: 'Deft.mvc.ViewController',
    mixins: [ 'Deft.mixin.Injectable' ],
    inject: {
    	apiProperties: 'apiProperties',
        appEventsController: 'appEventsController',
    	formUtils: 'formRendererUtils',
    	person: 'currentPerson',
    	model: 'currentStudentDocument',
    	confidentialityLevelsStore: 'confidentialityLevelsAllUnpagedStore'
    },
    config: {
    	containerToLoadInto: 'tools',
    	formToDisplay: 'documents'
    	
    },

    control: {
		'saveButton': {
			click: 'onSaveClick'
		} ,   	
		
		'cancelButton': {
			click: 'onCancelClick'
		}   	
    },
    
	init: function() {
		var me=this;
		me.confidentialityLevelsStore.clearFilter(true);
		me.confidentialityLevelsStore.load();
		me.formUtils.applyAssociativeStoreFilter(me.confidentialityLevelsStore, me.model.get('confidentialityLevelId'));
		me.getView().getForm().loadRecord(me.model);
		return this.callParent(arguments);
    },
 
	
	onCancelClick: function(button){
		this.displayMain();
	},
	
	onSaveClick: function(button){
		this.save();
	},	
	save: function(){
		var me=this;
		var url = me.getBaseUrl(me.person.get('id'));
		if(me.getView().getForm().isValid() && !me.model.get('id'))
        {
			me.getView().getForm().submit(
            {
                url: url,
                method : 'POST',
                waitMsg: 'Uploading Student Document...',
                success: function (fp, o) 
                {
                    	Ext.Msg.alert('Success', 'File uploaded successfully');
                    	me.displayMain();
                },
                failure: function (fp,o)
                {
                	if(o.result.message)
                	{
                		Ext.Msg.alert('Error',o.result.message);
                		return;
                	}
                	if(o.result.exception && o.result.exception.match('MaxUploadSizeExceededException'))
                	{
                		//the configured max size is inside the second set of parens in the exception message...  We will
                		//parse it out and give it to the user.
                		var  startPos = o.result.exception.indexOf('(') + 1;
                		var  endPos = o.result.exception.indexOf(')',startPos);
                		startPos = o.result.exception.indexOf('(',endPos) +1;
                		endPos = o.result.exception.indexOf(')',startPos);
                		var size = Math.round(o.result.exception.substring(startPos,endPos)/1000)
                		Ext.Msg.alert('Error','The file you are trying to upload is too large.  Max file size is '+size+'kb');
                		return;
                	}
                	if(o.result.exception)
                	{
                		Ext.Msg.alert('Error','There has been an error uploading your file.  Please contact the system administrator.');
                		return;
                	}                	
                }
            });
        }
		else
		{
			var success = function ()
			{
            	Ext.Msg.alert('Success', 'Document information updated successfully');
            	me.displayMain();	
            };
            var failure = function()
            {
            	Ext.Msg.alert('Updating document failed.  Please contact the system administrator.');
            };		
            me.getView().getForm().updateRecord();
			// update
    		me.apiProperties.makeRequest({
    			url: url+'/'+ me.model.get('id'), 
    			method: 'PUT',
    			jsonData: me.model.data,
    			successFunc: success,
    			failureFunc: failure,
    			scope: me
    		});	
		}
	},	
	displayMain: function(){
		var comp = this.formUtils.loadDisplay(this.getContainerToLoadInto(), this.getFormToDisplay(), true, {});
	},
    getBaseUrl: function(id){
		var me=this;
		var baseUrl = me.apiProperties.createUrl( me.apiProperties.getItemUrl('studentDocument') );
		baseUrl = baseUrl.replace('{id}', id);
		return baseUrl;
    }
});