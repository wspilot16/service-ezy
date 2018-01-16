package com.wsclient.converter.test;

import java.io.File;
import java.io.IOException;

import org.apache.commons.io.FileUtils;
import org.json.JSONException;
import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.wsclient.util.XmlToJsonConverter;

import demo.Application;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = Application.class)
public class XmlJsonConverterTest {

	@Autowired XmlToJsonConverter converter;

	@Test @Ignore
	public void testConvertxmlToJson() throws IOException, JSONException {
		String xml = FileUtils.readFileToString(new File("src/test/data/sample.xml"));
		String json = converter.convert(xml).replaceAll("\\s", "");
	    System.out.println(json);
	    String expected = FileUtils.readFileToString(new File("src/test/data/sample.json")).replaceAll("\\s", "");
	    System.out.println(expected);
	    Assert.assertEquals(expected, json);
	}
}
