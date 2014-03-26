***REMOVED***
/*
 * © Copyright IBM Corp. 2013
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
 * Collection of callback functions needed to register SBT widgets.
 *
 * @author Benjamin Jakobus
 */


/**
 * Callback for creating the plugin header.
 *
 * @param unknown $args
 */
function ibm_sbtk_header($args = array()) {
	$settings = new SBTSettings();
	$store = SBTCredentialStore::getInstance();

	if (($settings->getAuthenticationMethod() == 'oauth1' || $settings->getAuthenticationMethod() == 'oauth2') && $store->getOAuthAccessToken() == null &&
	(!isset($_COOKIE['IBMSBTKOAuthLogin']) || $_COOKIE['IBMSBTKOAuthLogin'] != 'yes')) {
		return;
	}
	
	$endpoints = $settings->getEndpoints();

	if ($endpoints == null || empty($endpoints)) {
		return;
	}

	$plugin = new SBTPlugin($endpoints[0]['name']);		
	$plugin->createHeader();
}
