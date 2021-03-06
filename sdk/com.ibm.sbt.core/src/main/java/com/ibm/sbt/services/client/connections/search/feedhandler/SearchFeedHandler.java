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
package com.ibm.sbt.services.client.connections.search.feedhandler;

import static com.ibm.sbt.services.client.base.ConnectionsConstants.nameSpaceCtx;

import org.w3c.dom.Document;
import org.w3c.dom.Node;

import com.ibm.commons.xml.xpath.XPathExpression;
import com.ibm.sbt.services.client.Response;
import com.ibm.sbt.services.client.base.BaseEntity;
import com.ibm.sbt.services.client.base.BaseService;
import com.ibm.sbt.services.client.base.IFeedHandler;
import com.ibm.sbt.services.client.base.datahandlers.EntityList;
import com.ibm.sbt.services.client.connections.search.Result;
import com.ibm.sbt.services.client.connections.search.ResultList;
import com.ibm.sbt.services.client.connections.search.SearchService;
import com.ibm.sbt.services.client.connections.search.SearchXPath;

/**
 * Feed handler for Search Service
 * 
 * @author Manish Kataria
 */


public class SearchFeedHandler implements IFeedHandler{
	
	private final SearchService service;
	
	/**
	 * Constructor
	 * 
	 * @param service
	 */
	public SearchFeedHandler(SearchService service){
		this.service = service;
	}

	@Override
	public BaseEntity createEntity(Response response) {
		Node data = (Node)response.getData();
		return createEntityFromData(data);
	}

	@Override
	public BaseEntity createEntityFromData(Object data) {
		Node node = (Node)data;
		XPathExpression expr = (data instanceof Document) ? (XPathExpression)SearchXPath.entry.getPath() : null;
		//XmlDataHandler handler = new XmlDataHandler(node, nameSpaceCtx, expr);
		Result result = new Result(service, (Node)data, nameSpaceCtx, expr);
		return result;
	}

	@Override
	public EntityList<? extends BaseEntity> createEntityList(Response requestData) {
		return new ResultList((Response)requestData, this);
	}

	@Override
	public BaseService getService() {
		return service;
	}

}
