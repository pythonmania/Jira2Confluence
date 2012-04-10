package com.daumcorp.ua.report;

import java.io.IOException;
import java.text.ParseException;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPathExpressionException;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.xml.sax.SAXException;

public class ReportMain {
	public static void main(String[] args) throws IOException,
			ParserConfigurationException, SAXException,
			XPathExpressionException, ParseException {
		ApplicationContext ctx = new ClassPathXmlApplicationContext(
				"classpath:appCtx.xml");
		Workflow work = (Workflow) ctx.getBean("workflow");

		// publish todo list
		work.publish();
	}
}
