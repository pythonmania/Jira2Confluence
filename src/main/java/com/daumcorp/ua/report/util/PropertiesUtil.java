package com.daumcorp.ua.report.util;

import java.io.IOException;
import java.util.Properties;

import javax.annotation.PostConstruct;

import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;

@Service("properties")
public class PropertiesUtil {
	private Properties prop;

	@PostConstruct
	public void init() throws IOException {
		prop = new Properties();
		prop.load(new ClassPathResource("jira2confluence.properties")
				.getInputStream());
	}

	public String get(String key) {
		return prop.getProperty(key);
	}
}
