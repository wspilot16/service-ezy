package com.wsclient.service;

public class WebserviceUtil {

	public static String removeNameSpace(String nodeName) {
		if(nodeName.contains(":")) {
			String[] tagName = nodeName.split(":");
			return tagName[1];
		} else {
			return nodeName;
		}
	}
}
