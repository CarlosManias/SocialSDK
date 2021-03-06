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
 
dojo.provide("sbt._bridge.ui.OAuthDialog");

/**
 * Social Business Toolkit SDK.
 * 
 * Definition of a dojo based dialog for OAuth 1.0.
 */
define('sbt/_bridge/ui/OAuthDialog',['dijit/Dialog'], function(Dialog) {
	return {
		show: function(url, width, height) {
			var frameUrl = url + "?loginUi=dialog";
			var style = "width: " + width + "px; height=" + height + "px";
			var d = new Dialog({
				title: "Authentication",
				content: "<iframe src='"+frameUrl+"' style='" + style + "'></iframe>",
				style: style
	        });
	        d.show();
		}
	};
});
