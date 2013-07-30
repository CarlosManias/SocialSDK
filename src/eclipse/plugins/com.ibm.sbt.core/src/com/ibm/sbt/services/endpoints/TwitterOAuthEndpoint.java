/*
 * � Copyright IBM Corp. 2013
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

import com.ibm.sbt.services.client.ClientService;
import com.ibm.sbt.services.client.ClientServicesException;
import com.ibm.sbt.services.client.smartcloud.SmartCloudService;

/**
 * @author Vimal Dhupar
 *
 */

public class TwitterOAuthEndpoint extends OAuthEndpoint {
	
private static final int authenticationErrorCode = 403;
	
    public TwitterOAuthEndpoint() {
    }

    @Override
	public ClientService getClientService() throws ClientServicesException {
    	return new SmartCloudService(this);
    }
    
    @Override
	public boolean isHeaderAllowed(String headerName, String serviceUrl){
    	if (headerName.equalsIgnoreCase("Origin"))
		{
			return false;
		}
		return true;

    }
    @Override
	public int getAuthenticationErrorCode(){
    	return authenticationErrorCode;
    }
}
