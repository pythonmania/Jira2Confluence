package com.daumcorp.ua.report;

import java.io.FileNotFoundException;
import java.io.IOException;

import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;

public class Xml2Html {
	//	private static final String style = "sample/cities.xsl";
	//	private static final String source = "sample/cities.xml";
	//	private static final String style = "sample/stocks.xsl";
	//	private static final String source = "sample/stocks.xml";
	private static final String style = "sample/wiki.xsl";
	private static final String source = "sample/SearchRequest-SPDMNT.xml";

	public static void main(String[] args) throws TransformerException,
			TransformerConfigurationException, FileNotFoundException,
			IOException {
		TransformerFactory tFactory = TransformerFactory.newInstance(
				"net.sf.saxon.TransformerFactoryImpl", null);
		Transformer transformer = tFactory.newTransformer(new StreamSource(
				style));
		transformer.transform(new StreamSource(source), new StreamResult(
				System.out));
	}
}
