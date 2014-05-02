/*
 * © Copyright IBM Corp. 2014
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
package com.ibm.sbt.sample.app;

import java.io.ByteArrayOutputStream;

import com.ibm.commons.xml.DOMUtil;
import com.ibm.sbt.services.client.ClientServicesException;
import com.ibm.sbt.services.client.base.transformers.TransformerException;
import com.ibm.sbt.services.client.connections.files.Comment;
import com.ibm.sbt.services.client.connections.files.File;
import com.ibm.sbt.services.client.connections.files.FileService;


/**
 * @author mwallace
 *
 */
public class FileServiceApp extends BaseApp {
	
	private FileService fileService;
	
	public FileServiceApp(String url, String user, String password) {
		super(url, user, password);
	}
	
    public FileService getFileService() {
    	if (fileService == null) {
    		fileService = new FileService(getBasicEndpoint());
    	}
    	return fileService;
    }
    
    public Comment addCommentToFile(String fileId, String comment, String userId) throws ClientServicesException, TransformerException {
    	return getFileService().addCommentToFile(fileId, comment, userId, null);
    }
    
	/**
	 * Demo.
	 * 
	 * @param args
	 */
	public static void main(String[] args) {
		if (args.length < 3) {
			System.out.println("Usage: java com.ibm.sbt.sample.app.FileServiceApp <url> <user> <password>");
			return;
		}
		
		String url = args[0];
		String user = args[1];
		String password = args[2];
		
		FileServiceApp fsa = null;
		try {
			fsa = new FileServiceApp(url, user, password);
			
			FileService fileService = fsa.getFileService();
			File file = fileService.getFile("087ad154-df65-43c2-8c4e-383c68337724", "6097b4ce-39dc-4db1-9d4f-4d89ff125c69", null);
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			long size = fileService.downloadFile(baos, file, null);
			
			System.out.println(DOMUtil.getXMLString(file.getDataHandler().getData()));
			System.out.println("Downloaded: "+size);
			
		} catch (Exception e) {
			e.printStackTrace();
		}

	}
	
}
