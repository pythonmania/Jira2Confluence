package com.daumcorp.ua.report.service;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import javax.xml.namespace.QName;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import com.daumcorp.ua.report.bean.Issue_;
import com.daumcorp.ua.report.bean.Project;
import com.daumcorp.ua.report.bean.Projects;
import com.daumcorp.ua.report.util.PropertiesUtil;

@Service
public class Xml2Obj {

	private Document document;

	private XPath xpath;

	@Autowired
	PropertiesUtil properties;

	public Map<String, Object> parse(String url)
			throws ParserConfigurationException, SAXException, IOException,
			XPathExpressionException, ParseException {

		DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory
				.newInstance();
		DocumentBuilder documentBuilder = documentBuilderFactory
				.newDocumentBuilder();

		document = documentBuilder.parse(url + "&os_username="
				+ properties.get("username") + "&os_password="
				+ properties.get("password"));

		XPathFactory factory = XPathFactory.newInstance();
		xpath = factory.newXPath();

		Map<String, Object> ctx = new HashMap<String, Object>();
		Projects projects = new Projects();

		ctx.put("url", parseSingle("/rss/channel/link/text()"));

		NodeList nodelist = parseMulti("/rss/channel/item");
		for (int i = 1; i <= nodelist.getLength(); i++) {
			String title = parseSingle("/rss/channel/item[" + i
					+ "]/project/text()");
			title = title.replaceFirst("^.*(\\[.*\\])(.*)", "$2");

			Project project = projects.getProject(title);
			Issue_ issue = new Issue_();

			String itemTitle = parseSingle("/rss/channel/item[" + i
					+ "]/title/text()");
			itemTitle = itemTitle.replaceAll("\\[", "\\\\[");
			itemTitle = itemTitle.replaceAll("\\]", "\\\\]");
			issue.setTitle(itemTitle);

			issue.setLink(parseSingle("/rss/channel/item[" + i
					+ "]/link/text()"));

			issue.setAssignee(parseSingle("/rss/channel/item[" + i
					+ "]/assignee/text()"));

			issue.setStatus(parseSingle("/rss/channel/item[" + i
					+ "]/status/text()"));

			String created = parseSingle("/rss/channel/item[" + i
					+ "]/created/text()");
			issue.setCreated(convertDateFormat(created));

			project.addIssue(issue);
		}
		ctx.put("projects", projects.getProjects());
		return ctx;
	}

	private String convertDateFormat(String dateStr) throws ParseException {
		SimpleDateFormat sdfIn = new SimpleDateFormat(
				"EEE, dd MMM yyyy HH:mm:ss", Locale.US);
		SimpleDateFormat sdfOut = new SimpleDateFormat("yyyy.MM.dd HH:mm:ss",
				Locale.US);
		return sdfOut.format(sdfIn.parse(dateStr));
	}

	public Document init(String url) throws ParserConfigurationException,
			SAXException, IOException {
		DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory
				.newInstance();
		DocumentBuilder documentBuilder = documentBuilderFactory
				.newDocumentBuilder();
		Document document = documentBuilder.parse(url);

		return document;
	}

	private String parseSingle(String xpathQuery)
			throws XPathExpressionException {
		Node node = (Node) parse(xpathQuery, XPathConstants.NODE);
		return node.getNodeValue();
	}

	private NodeList parseMulti(String xpathQuery)
			throws XPathExpressionException {
		NodeList nodelist = (NodeList) parse(xpathQuery, XPathConstants.NODESET);
		return nodelist;
	}

	private Object parse(String xpathQuery, QName nodetype)
			throws XPathExpressionException {
		XPathExpression expr = xpath.compile(xpathQuery);
		Object result = expr.evaluate(document, nodetype);
		return result;
	}
}
