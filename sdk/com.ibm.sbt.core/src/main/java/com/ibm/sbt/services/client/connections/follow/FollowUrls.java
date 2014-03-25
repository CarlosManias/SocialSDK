package com.ibm.sbt.services.client.connections.follow;

import com.ibm.sbt.services.client.base.ConnectionsConstants;
import com.ibm.sbt.services.client.base.NamedUrlPart;
import com.ibm.sbt.services.client.base.URLBuilder;
import com.ibm.sbt.services.client.base.URLContainer;
import com.ibm.sbt.services.client.base.Version;
import com.ibm.sbt.services.client.base.VersionedUrl;

public enum FollowUrls implements URLContainer {
	FOLLOW_URL(new VersionedUrl(ConnectionsConstants.v4_0, "{serviceType}/follow/{authType}/atom/resources/{resourceId}"));

	private URLBuilder builder;
	
	private FollowUrls(VersionedUrl... urlVersions) {
		builder = new URLBuilder(urlVersions);
	}
	
	public String format(Version version, NamedUrlPart... args) {
		return builder.format(version, args);
	}

	public String getPattern(Version version){
		return builder.getPattern(version).getUrlPattern();
	}
}
