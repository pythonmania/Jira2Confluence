package com.daumcorp.ua.report;

import java.io.IOException;
import java.text.ParseException;
import java.util.Map;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPathExpressionException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.xml.sax.SAXException;

import com.daumcorp.ua.report.service.Obj2WikiText;
import com.daumcorp.ua.report.service.PostWiki;
import com.daumcorp.ua.report.service.QueryURLBuilder;
import com.daumcorp.ua.report.service.Xml2Obj;
import com.daumcorp.ua.report.util.PropertiesUtil;

@Service
public class Workflow {

	@Autowired
	QueryURLBuilder urlBuilder;

	@Autowired
	Xml2Obj xml2Obj;

	@Autowired
	Obj2WikiText obj2WikiText;

	@Autowired
	PostWiki postWiki;

	@Autowired
	PropertiesUtil properties;

	public void publish() throws IOException, XPathExpressionException,
			ParserConfigurationException, SAXException, ParseException {
		String jiraURL = urlBuilder.makeJiraURL("2011-03-26", "2011-04-06");

		Map<String, Object> obj = xml2Obj.parse(jiraURL);

		String wikitext = obj2WikiText.merge(obj);

		postWiki.publish(wikitext);
	}
}
