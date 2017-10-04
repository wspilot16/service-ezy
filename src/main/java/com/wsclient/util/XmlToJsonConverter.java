package com.wsclient.util;

import org.json.JSONObject;
import org.json.XML;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

@Component
public class XmlToJsonConverter {
	@Autowired ObjectMapper mapper;

	public String convert(String xml) throws JsonProcessingException {
		JSONObject jsonObject = XML.toJSONObject(xml);
		mapper.enable(SerializationFeature.INDENT_OUTPUT);
		String json = mapper.writer().withDefaultPrettyPrinter().writeValueAsString(jsonObject.toString());
		json = json.replaceAll("\\\\\"", "\"");
		json = json.substring(1, json.length()-1);
		return json;
	}
}
