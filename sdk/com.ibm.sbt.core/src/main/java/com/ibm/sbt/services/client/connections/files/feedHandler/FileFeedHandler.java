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
package com.ibm.sbt.services.client.connections.files.feedHandler;

import static com.ibm.sbt.services.client.base.ConnectionsConstants.nameSpaceCtx;

import org.w3c.dom.Document;
import org.w3c.dom.Node;

import com.ibm.commons.xml.xpath.XPathExpression;
import com.ibm.sbt.services.client.Response;
import com.ibm.sbt.services.client.base.AtomXPath;
import com.ibm.sbt.services.client.base.IFeedHandler;
import com.ibm.sbt.services.client.base.datahandlers.XmlDataHandler;
import com.ibm.sbt.services.client.connections.files.File;
import com.ibm.sbt.services.client.connections.files.FileList;
import com.ibm.sbt.services.client.connections.files.FileService;

public class FileFeedHandler implements IFeedHandler{

	private FileService service; 
	
	public FileFeedHandler() {
		// TODO Auto-generated constructor stub
	} 
	
	public FileFeedHandler(FileService service) {
		this.service = service;
	}
	
	/**
	 * @param dataHolder
	 * @return file
	 */
	@Override
    public File createEntity(Response dataHolder) {
        return createEntityFromData((Node)dataHolder.getData());
    }

	/**
	 * @param data
	 * @return file
	 */
    @Override
    public File createEntityFromData(Object data) {
    	XPathExpression xpath = (data instanceof Document) ? (XPathExpression)AtomXPath.singleEntry.getPath() : null;
    	XmlDataHandler handler = new XmlDataHandler((Node)data, nameSpaceCtx, xpath);
        File file = new File(service, handler);
        return file;
    }

    /**
	 * @param dataHolder
	 * @return filelist
	 */
    @Override
    public FileList createEntityList(Response dataHolder) {
        return new FileList((Response) dataHolder, this);
    }

    /**
	 * @return service
	 */
    @Override
    public FileService getService() {
        return service;
    }
}
