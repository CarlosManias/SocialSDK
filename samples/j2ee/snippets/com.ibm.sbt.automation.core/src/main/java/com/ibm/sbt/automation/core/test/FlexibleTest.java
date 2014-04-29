package com.ibm.sbt.automation.core.test;

import java.io.File;
import java.io.FileInputStream;
import java.util.HashMap;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import com.ibm.commons.runtime.util.ParameterProcessor;
import com.ibm.javascript.units.AbstractRhinoTest;
import com.ibm.sbt.automation.core.environment.TestEnvironment;
import com.ibm.sbt.automation.core.environment.TestEnvironmentFactory;
import com.ibm.sbt.automation.core.test.BaseTest.AuthType;
import com.ibm.sbt.automation.core.test.pageobjects.JavaScriptPreviewPage;
import com.ibm.sbt.automation.core.test.pageobjects.ResultPage;
import com.ibm.sbt.playground.assets.jssnippets.JSSnippet;
import com.ibm.sbt.security.authentication.AuthenticationException;
import com.ibm.sbt.services.client.SBTServiceException;

public class FlexibleTest {

	TestDelegate delegate = System.getProperty("testMode") == null ? new SeleniumDelegate()
			: new EmbeddedDelegate();

	@Before
	public void setupDelegate() {
		delegate.setupDelegate();
	}

	@After
	public void teardownDelegate() {
		delegate.teardownDelegate();
	}

	protected void setAuthType(AuthType autoDetect) {
		delegate.setAuthType(autoDetect);
	}

	protected JavaScriptPreviewPage executeSnippet(String snippetId) {
		// TODO Auto-generated method stub
		return delegate.executeSnippet(snippetId);
	}

	public TestEnvironment getEnvironment() {
		// TODO Auto-generated method stub
		return delegate.getEnvironment();
	}

	protected void addSnippetParam(String string, String communityUuid) {
		delegate.addSnippetParam(string, communityUuid);
	}
	
	protected void fail(String message, SBTServiceException cse) {
    	String failure = message;
    	
    	Throwable cause = cse.getCause();
    	if (cause != null) {
    		cause.printStackTrace();
    		failure += ", " + cause.getMessage();
    	} else {
    		cse.printStackTrace();
    		failure += ", " + cse.getMessage();
    	}
    	
    	Assert.fail(failure);
    }

}

class EmbeddedDelegate extends AbstractRhinoTest implements TestDelegate {
	
	public void loadProperties(){
		if (properties.isEmpty()) {
			try {
				properties.load(new FileInputStream(new File(
						"config/test.properties")));
			} catch (Exception e) {
			}
		}
	}

	
	private HashMap<String, String> params = new HashMap<String, String>();

	@Override
	public void setAuthType(AuthType autoDetect) {
		// unused
	}

	@Override
	public JavaScriptPreviewPage executeSnippet(String snippetName) {
		File f = new File(System.getProperty("user.dir")+"/../com.ibm.sbt.sample.web/src/main/webapp/samples/js");
		if (!f.exists()) throw new RuntimeException("File Not Found: " + f.getAbsolutePath());
		BaseFileLister.jsRootPath = f.getAbsolutePath();

		JSSnippet snippet = (JSSnippet) BaseFileLister
				.getJsSnippet(snippetName);
		
		if (snippet == null) {
			throw new IllegalArgumentException("Snippet "+snippetName+ " not found at " + BaseFileLister.jsRootPath);
		}
		
		if (snippet.getHtml()!= null) {
		String html = snippet.getHtml().replaceAll("'","\'").replaceAll("\n", "\\\\n").replaceAll("\r", "");
		
		String script = " "+
				"var __bdy = document.getElementsByTagName('body')[0]; \n" +
				"__bdy.innerHTML = '';\n" +
				"__bdy.innerHTML = '"+html+"';\n" +
				" if (!document.getElementById('json')){\n" +
				"var __testcontent = document.createElement('div'); __testcontent.id= 'json';\n" +
				"__bdy.appendChild(__testcontent);\n" +
				"};\n" ;

		super.executeScript(script, "injecting_html");
		
		}
		try {
			checkSnippet(snippet);
		} catch (Exception e) {
			e.printStackTrace();
			Assert.fail(e.getMessage());
		}

		return new JavaScriptPreviewPage(new ResultPage() {

			@Override
			public void setWebDriver(WebDriver webDriver) {
				// TODO Auto-generated method stub

			}

			@Override
			public WebElement getWebElement() {
				// TODO Auto-generated method stub
				return null;
			}

			@Override
			public WebDriver getWebDriver() {
				// TODO Auto-generated method stub
				return null;
			}

			@Override
			public String getText() {
				return dumpText();
			}

		}) {
			@Override
			protected String getElementTextContent(String divId) {
				return dumpText(divId);
			}
		};

	}

	private void checkSnippet(JSSnippet snippet) throws Exception {
		if (snippet != null) {
			String script = snippet.getJs();
			
			String parameterized  = ParameterProcessor.process(script,params);
			
			if (script != null) {
				executeScript(parameterized, "test");
			}
		}
	}
	
	@Override
	protected String getPrefixFolder() {
		File project = new File(System.getProperty("user.dir"),"../../../../sdk/");
		File sbt = new File(project, "com.ibm.sbt.web");
		sbt = new File(sbt, "src");
		sbt = new File(sbt, "main");
		sbt = new File(sbt, "webapp");
		sbt = new File(sbt, "js");
		sbt = new File(sbt, "sdk");
		return sbt.getAbsolutePath();
	}
	@Override
	protected String getFrameworkFolder() {
		File project = new File(System.getProperty("user.dir"),"../../../../sdk/");
		File sbt = new File(project, "com.ibm.sbt.core.test");

		return sbt.getAbsolutePath();
	}
	@Override
	protected String getTestFolder() {
		String currentDir = System.getProperty("user.dir");
		return currentDir;
	}

	public void execJsTest(String script) throws Exception {
		executeScript(script, "test");
		waitForAllPromisesToReturn();
	}

	@Override
	public void teardownDelegate() {
		super.tearDownJavaSide();
	}

	@Override
	public void setupDelegate() {
		try {
			super.setup();
		} catch (Exception e) {
			
			throw new RuntimeException(e);
		}
		//Assert.assertTrue(EndpointFactory.getEndpoint("connections") instanceof MockEndpoint);

	}

	@Override
	public TestEnvironment getEnvironment() {
		// TODO Auto-generated method stub
		return TestEnvironmentFactory.getEnvironment();
	}

	@Override
	public void addSnippetParam(String parameter, String value) {
		this.params.put(parameter, value);
	}
}

class SeleniumDelegate extends BaseApiTest implements TestDelegate {

	@Override
	public void teardownDelegate() {
		super.destroyContext();
	}

	@Override
	public void setupDelegate() {
		try {
			super.setupTest();
		} catch (AuthenticationException e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	public TestEnvironment getEnvironment() {
		return environment;
	}
	
}

interface TestDelegate {

	void setAuthType(AuthType autoDetect);

	void addSnippetParam(String parameter, String value);

	void teardownDelegate();

	void setupDelegate();

	JavaScriptPreviewPage executeSnippet(String snippetId);
	
	TestEnvironment getEnvironment();
}