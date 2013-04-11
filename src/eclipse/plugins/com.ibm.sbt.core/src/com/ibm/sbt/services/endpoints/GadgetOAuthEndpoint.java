/*
 * © Copyright IBM Corp. 2012
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

package com.ibm.sbt.services.endpoints;

import com.ibm.commons.runtime.Context;
import com.ibm.commons.runtime.RuntimeConstants;
import com.ibm.sbt.security.authentication.oauth.consumer.oauth_10a.servlet.OAClientAuthentication;
import com.ibm.sbt.services.endpoints.js.JSReference;

/**
 * Bean that provides authentication via OAuth.
 * 
 * @author mwallace
 */
public class GadgetOAuthEndpoint extends OAuth2Endpoint {

	/*
	 * (non-Javadoc)
	 * @see com.ibm.sbt.services.endpoints.AbstractEndpoint#getAuthenticator(java.lang.String)
	 */
	@Override
	public JSReference getAuthenticator(String endpointName) {
		JSReference reference = new JSReference("sbt/authenticator/GadgetOAuth");
		Context ctx = Context.get();
		StringBuilder b = new StringBuilder();
		RuntimeConstants.get().appendBaseProxyUrl(b, ctx);
		b.append("/");
		b.append(OAClientAuthentication.URL_PATH);
		b.append('/');
		b.append(endpointName);
		String url = b.toString();
		reference.getProperties().put("url", url);
		return reference;
	}

	/*
	 * (non-Javadoc)
	 * @see com.ibm.sbt.services.endpoints.AbstractEndpoint#getTransport(java.lang.String, java.lang.String)
	 */
	@Override
	public JSReference getTransport(String endpointName, String moduleId) {
		JSReference reference = new JSReference("sbt/GadgetTransport");
		String serviceName = getServiceName();
		if (serviceName != null) {
			reference.getProperties().put("serviceName", serviceName);
		}
		return reference;
	}
}
