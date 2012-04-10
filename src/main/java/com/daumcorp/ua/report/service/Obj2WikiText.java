package com.daumcorp.ua.report.service;

import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.daumcorp.ua.report.util.VelocityUtil;

@Service
public class Obj2WikiText {
	protected final Log logger = LogFactory.getLog(this.getClass());

	@Autowired
	VelocityUtil velocityUtil;

	public String merge(Map<String, Object> map) {
		logger.debug(map.get("projects"));
		return velocityUtil.merge("wiki_template.vm", map);
	}
}
