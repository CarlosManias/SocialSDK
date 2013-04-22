package com.ibm.sbt.playground.assets.jssnippets;

import java.util.Properties;

public class GadgetSnippet extends JSSnippet {
	private String	xml;

	public GadgetSnippet() {
	}

	@Override
	public void init(Properties props) {
		super.init(props);
	}

	public String getXml() {
		return xml;
	}

	public void setXml(String xml) {
		this.xml = xml;
	}
}
