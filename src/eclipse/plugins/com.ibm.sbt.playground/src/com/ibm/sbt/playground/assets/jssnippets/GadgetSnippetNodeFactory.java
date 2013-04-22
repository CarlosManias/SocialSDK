package com.ibm.sbt.playground.assets.jssnippets;

import com.ibm.sbt.playground.assets.AssetNode;
import com.ibm.sbt.playground.assets.CategoryNode;

public class GadgetSnippetNodeFactory extends JSSnippetNodeFactory {

	public static final String[]	EXTENSIONS	= new String[] { "doc.html", "html", "js", "css", "xml" };

	public GadgetSnippetNodeFactory() {
	}

	@Override
	public String[] getAssetExtensions() {
		return EXTENSIONS;
	}

	@Override
	public AssetNode createAssetNode(CategoryNode parent, String name) {
		return new GadgetSnippetAssetNode(parent, name);
	}
}
