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
package com.ibm.sbt.services.client.connections.activity.feedHandler;

import static com.ibm.sbt.services.client.base.ConnectionsConstants.nameSpaceCtx;

import org.w3c.dom.Document;
import org.w3c.dom.Node;

import com.ibm.commons.xml.xpath.XPathExpression;
import com.ibm.sbt.services.client.Response;
import com.ibm.sbt.services.client.base.IFeedHandler;
import com.ibm.sbt.services.client.base.datahandlers.XmlDataHandler;
import com.ibm.sbt.services.client.connections.activity.ActivityService;
import com.ibm.sbt.services.client.connections.activity.Member;
import com.ibm.sbt.services.client.connections.activity.MemberList;
import com.ibm.sbt.services.client.connections.activity.model.ActivityXPath;

/**
 * Feed Handler for Member
 * @author Vimal Dhupar
 *
 */
public class MemberFeedHandler implements IFeedHandler {

	private final ActivityService service;
	
	/**
	 * Constructor
	 * 
	 * @param service
	 */
	public MemberFeedHandler(ActivityService service){
		this.service = service;
	}
	
	/**
	 * @param requestData
	 * @return activity member
	 */
	@Override
	public Member createEntity(Response requestData) {
		Node data = (Node)requestData.getData();
		return createEntityFromData(data);
	}
	
	/**
	 * @param data object
	 * @return activity member
	 */
	@Override
	public Member createEntityFromData(Object data) {
		Node node = (Node)data;
		XPathExpression expr = (data instanceof Document) ? (XPathExpression)ActivityXPath.Entry.getPath() : null;
		XmlDataHandler handler = new XmlDataHandler(node, nameSpaceCtx, expr);
		Member member = new Member(service, handler);
		return member;
	}

	/**
	 * @param data object
	 * @return Collection of activity members
	 */
	@Override
	public MemberList createEntityList(Response requestData) {
		return new MemberList(requestData, this);
	}

	/**
	 * @return ActivityService
	 */
	@Override
	public ActivityService getService() {
		return service;
	}

}
