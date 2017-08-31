package com.wsclient.model;

public class WebRequestElement {

	private String xpathElementName;
	private String tagName;
	private String elementName;
	private String elementValue;

	public String getXpathElementName() {
		return xpathElementName;
	}

	public void setXpathElementName(String xpathElementName) {
		this.xpathElementName = xpathElementName;
	}

	public String getElementName() {
		return elementName;
	}

	public void setElementName(String elementName) {
		this.elementName = elementName;
	}

	public String getElementValue() {
		return elementValue;
	}

	public void setElementValue(String elementValue) {
		this.elementValue = elementValue;
	}

	public String getTagName() {
		return tagName;
	}

	public void setTagName(String tagName) {
		this.tagName = tagName;
	}

}
