package com.ibm.sbt.playground.assets.jssnippets;

import java.util.Properties;

public class GadgetSnippet extends JSSnippet {
	private String	html;
	private String	docHtml;
	private String	js;
	private String	css;
	private String	xml;

	public GadgetSnippet() {
	}

	@Override
	public void init(Properties props) {
		super.init(props);
	}

	@Override
	public String getTheme() {
		return getProperty("theme");
	}

	@Override
	public void setTheme(String theme) {
		setProperty("theme", theme);
	}

	@Override
	public String getHtml() {
		return html;
	}

	@Override
	public void setHtml(String html) {
		this.html = html;
	}

	@Override
	public String getDocHtml() {
		return docHtml;
	}

	@Override
	public void setDocHtml(String docHtml) {
		this.docHtml = docHtml;
	}

	@Override
	public String getJs() {
		return js;
	}

	@Override
	public void setJs(String js) {
		this.js = js;
	}

	@Override
	public String getCss() {
		return css;
	}

	@Override
	public void setCss(String css) {
		this.css = css;
	}

	public String getXml() {
		return xml;
	}

	public void setXml(String xml) {
		this.xml = xml;
	}
}
