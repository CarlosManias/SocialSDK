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
package com.ibm.sbt.services.client.connections.common;

import com.ibm.sbt.services.client.base.BaseEntity;
import com.ibm.sbt.services.client.base.BaseService;
import com.ibm.sbt.services.client.base.datahandlers.DataHandler;

/**
 * @author mewallace
 *
 */
public class Link extends BaseEntity {
	
	public Link() {
	}
	
	public Link(String title, String href) {
		setTitle(title);
		setHref(href);
	}
	
	public Link(BaseService svc, DataHandler<?> dataHandler) {
		super(svc, dataHandler);
	}
	
	public String getHref() {
		return getAsString(CommonXPath.href);
	}
	
	public void setHref(String href) {
		setAsString(CommonXPath.href, href);
	}
	
	public String getTitle() {
		return getAsString(CommonXPath.title);
	}
	
	public void setTitle(String title) {
		setAsString(CommonXPath.title, title);
	}
	
	public long getSize() {
		return getAsLong(CommonXPath.size);
	}
	
	public void setSize(long size) {
		setAsLong(CommonXPath.size, size);
	}
	
	public String getType() {
		return getAsString(CommonXPath.type);
	}

	public void setType(String type) {
		setAsString(CommonXPath.type, type);
	}
}
