package com.ibm.sbt.playground.assets.jssnippets;

import java.io.IOException;
import com.ibm.sbt.playground.assets.Asset;
import com.ibm.sbt.playground.assets.CategoryNode;
import com.ibm.sbt.playground.vfs.VFSFile;

public class GadgetSnippetAssetNode extends JSSnippetAssetNode {

	public GadgetSnippetAssetNode(CategoryNode parent, String name) {
		super(parent, name);
	}

	public GadgetSnippetAssetNode(CategoryNode parent, String name, String category, String unid,
			String jspUrl) {
		super(parent, name, category, unid, jspUrl);
	}

	@Override
	public GadgetSnippet load(VFSFile root) throws IOException {
		return (GadgetSnippet) super.load(root);
	}

	@Override
	public Asset createAsset(VFSFile root) throws IOException {
		VFSFile parent = getParentFile(root);
		String html = loadResource(parent, "html");
		String docHtml = loadResource(parent, "doc.html");
		String js = loadResource(parent, "js");
		String css = loadResource(parent, "css");
		String xml = loadResource(parent, "xml");
		GadgetSnippet s = new GadgetSnippet();
		s.setHtml(html);
		s.setDocHtml(docHtml);
		s.setJs(js);
		s.setCss(css);
		s.setXml(xml);
		return s;
	}
}
