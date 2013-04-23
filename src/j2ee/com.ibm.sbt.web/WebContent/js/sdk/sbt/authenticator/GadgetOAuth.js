/*
 * � Copyright IBM Corp. 2012
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); 
 * you may not use this file except in compliance with the License. 
 * You may obtain a copy of the License at:
 * 
 * http://www.apache.org/licenses/LICENSE-2.0 
 * 
 * Unless required by applicable law or agreed to in writing, software 
 * distributed under the License is distributed on an "AS IS" BASIS, 
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or 
 * implied. See the License for the specific language governing 
 * permissions and limitations under the License.
 */

/**
 * Social Business Toolkit SDK. 
 * Definition of the authentication mechanism for OAuth 1.0.
 */
define([ "sbt/declare", "sbt/lang", "sbt/dom" ], function(declare,lang,dom) {

    /**
     * OpenSocial OAuth authentication.
     * 
     * This class triggers the authentication for a service.
     */
    return declare("sbt.authenticator.GadgetOAuth", null, {

        constructor : function(args) {
            lang.mixin(this, args || {});
        },

        /**
         * Method that authenticates the current user
         */
        authenticate : function(options) {
            var onOpen = function() {
            	alert("It's open");
            };
            var onClose = function() {
            	alert("It's closed");
            	options.callback();
            };
            var windowOptions = 'height=700,width=650';
            var popup = new gadgets.oauth.Popup(this.url, windowOptions, onOpen, onClose);
            
            popup.createOpenerOnClick()();
        }
    });
});