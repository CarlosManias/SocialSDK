package com.ibm.sbt.services.client.connections.communities;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import java.util.HashMap;

import lib.MockableTest;
import lib.TestEndpoint;
import org.junit.Ignore;
import org.junit.Test;
import org.w3c.dom.Node;
import com.ibm.commons.xml.DOMUtil;
import com.ibm.commons.xml.NamespaceContext;
import com.ibm.commons.xml.XMLException;
import com.ibm.commons.xml.xpath.XPathExpression;
import com.ibm.sbt.services.client.ClientServicesException;
import com.ibm.sbt.services.util.extractor.field.XMLFieldExtractor;
import com.ibm.sbt.services.client.connections.communities.Community;
import com.ibm.sbt.services.client.connections.communities.CommunityList;
import com.ibm.sbt.services.client.connections.communities.CommunityService;
import com.ibm.sbt.services.client.Response;
import com.ibm.sbt.services.client.ClientService.Args;

/**
 * Tests for the java connections Communities API a test class provides its own tests extending the test
 * endpoint abstract class
 * 
 * @author Lorenzo Boccaccia
 * @date Nov 22, 2012
 */
public class CommunitiesTest extends MockableTest {

	protected static final String	TEST_COMMUNITY_DESCRIPTION	= "Test Community Description";
	protected static final String	NEW_COMMUNITY				= "New Community";

	@Test
	public void testCreate() throws Exception {
		CommunityService svc = new CommunityService();
		Community c = new Community(null);
		c.setTitle(NEW_COMMUNITY);
		c.setContent(TEST_COMMUNITY_DESCRIPTION);
		svc.createCommunity(c);
		svc.createCommunity(c);
	}

}
