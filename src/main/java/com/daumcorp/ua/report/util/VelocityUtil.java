package com.daumcorp.ua.report.util;

import java.io.StringWriter;
import java.util.HashMap;
import java.util.Map;

import javax.annotation.PostConstruct;

import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.VelocityEngine;
import org.springframework.stereotype.Service;

@Service
public class VelocityUtil {

	private VelocityEngine ve;

	@PostConstruct
	public void init() {
		ve = new VelocityEngine();
		ve.setProperty("file.resource.loader.path", "src/main/resources");
		ve.setProperty("input.encoding", "UTF-8");
		ve.setProperty("output.encoding", "UTF-8");
		ve.init();
	}

	public String merge(String templateName) {
		return merge(templateName, new VelocityContext());
	}

	public String merge(String templateName, Map<String, Object> ctx) {
		VelocityContext context = new VelocityContext();

		if (ctx == null)
			ctx = new HashMap<String, Object>();

		for (String key : ctx.keySet()) {
			context.put(key, ctx.get(key));
		}

		return merge(templateName, context);
	}

	public String merge(String templateName, VelocityContext context) {
		Template t = ve.getTemplate(templateName);
		StringWriter w = new StringWriter();
		t.merge(context, w);
		return w.toString();
	}
}
