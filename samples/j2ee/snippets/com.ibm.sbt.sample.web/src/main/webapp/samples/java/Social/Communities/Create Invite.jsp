<!-- /*
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
 */-->
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<%@page import="java.io.PrintWriter"%>
<%@page import="java.util.Collection"%>
<%@page import="com.ibm.commons.runtime.Application"%>
<%@page import="com.ibm.commons.runtime.Context"%>
<%@page	import="com.ibm.sbt.services.client.connections.communities.Community"%>
<%@page import="com.ibm.sbt.services.client.connections.communities.CommunityService"%>
<%@page import="com.ibm.sbt.services.client.connections.communities.Invite"%>
<%@page	import="com.ibm.sbt.services.client.connections.communities.InviteList"%>

<%@page import="com.ibm.sbt.services.client.connections.communities.CommunityList"%>
<%@page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<html>
<head>
<title>SBT JAVA Sample - Create Invite</title>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
</head>

<body>
	<h4>Create Invite</h4>
	<div id="content">
	<%
		try {
			CommunityService svc = new CommunityService();
			CommunityList communities = svc.getMyCommunities();
			if(communities.size()>0){
				String communityId = communities.get(0).getCommunityUuid();
				String userId = Context.get().getProperty("sample.id2");
				Invite invite = new Invite(svc);
				invite.setCommunityUuid(communityId);
				invite.setUserid(userId);
				invite = svc.createInvite(invite);
				out.println("Invite created for community with id"+invite.getCommunityUuid()+"<br>");
				out.println("Invite Id "+ invite.getInviteUuid()+"<br>");
				out.println("Invite Url "+ invite.getInviteUrl()+"<br>");
				out.println("Community Url "+ invite.getCommunityUrl()+"<br>");
				out.println("Invite sent to user with name "+invite.getContributor().getName()+"<br>");
				out.println("Invite sent to user with userid "+invite.getContributor().getUserid()+"<br>");
				out.println("Invite sent to user with email "+invite.getContributor().getEmail()+"<br>");
			}
			else{
				out.println("No communities exist to send invite");
			}
		} catch (Throwable e) {
			out.println("<pre>");
			out.println(e.getMessage());
			out.println("</pre>");
		}
	%>
	</div>
</body>
</html>