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
package com.ibm.sbt.services.client.connections.communities.feedhandler;

import static com.ibm.sbt.services.client.base.ConnectionsConstants.nameSpaceCtx;

import org.w3c.dom.Document;
import org.w3c.dom.Node;

import com.ibm.commons.xml.xpath.XPathExpression;
import com.ibm.sbt.services.client.Response;
import com.ibm.sbt.services.client.base.IFeedHandler;
import com.ibm.sbt.services.client.base.datahandlers.XmlDataHandler;
import com.ibm.sbt.services.client.connections.communities.CommunityService;
import com.ibm.sbt.services.client.connections.communities.Invite;
import com.ibm.sbt.services.client.connections.communities.InviteList;
import com.ibm.sbt.services.client.connections.communities.model.CommunityXPath;

/**
 * 
 * @author Swati Singh
 *
 */
public class InviteFeedHandler implements IFeedHandler {

	private final CommunityService service;
	
	/**
	 * Constructor
	 * 
	 * @param service
	 */
	public InviteFeedHandler(CommunityService service){
		this.service = service;
	}
	
	/**
	 * @param requestData
	 * @return Invite
	 */
	@Override
	public Invite createEntity(Response requestData) {
		Node data = (Node)requestData.getData();
		return createEntityFromData(data);
	}
	
	/**
	 * @param data object
	 * @return Invite
	 */
	@Override
	public Invite createEntityFromData(Object data) {
		Node node = (Node)data;
		XPathExpression expr = (data instanceof Document) ? (XPathExpression)CommunityXPath.entry.getPath() : null;
		XmlDataHandler handler = new XmlDataHandler(node, nameSpaceCtx, expr);
		Invite invite = new Invite(service, handler);
		return invite;
	}

	/**
	 * @param data object
	 * @return Collection of community Invitations
	 */
	@Override
	public InviteList createEntityList(Response requestData) {
		return new InviteList((Response)requestData, this);
	}

	/**
	 * @return CommunityService
	 */
	@Override
	public CommunityService getService() {
		return service;
	}

}
