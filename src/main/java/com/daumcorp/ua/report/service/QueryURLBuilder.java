package com.daumcorp.ua.report.service;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.daumcorp.ua.report.util.PropertiesUtil;
import com.daumcorp.ua.report.util.VelocityUtil;

@Service
public class QueryURLBuilder {
	protected final Log logger = LogFactory.getLog(this.getClass());

	SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

	@Autowired
	PropertiesUtil properties;

	@Autowired
	VelocityUtil velocityUtil;

	public String makeJiraURL() throws UnsupportedEncodingException {
		return makeJiraURL(getStartDate(), getEndDate());
	}

	public String makeJiraURL(String startDate, String endDate)
			throws UnsupportedEncodingException {
		Map<String, Object> ctx = new HashMap<String, Object>();
		ctx.put("projects", properties.get("projects"));
		ctx.put("start_date", startDate);
		ctx.put("end_date", endDate);
		ctx.put("member", properties.get("member"));
		ctx.put("order", properties.get("order"));

		String jiraQuery = velocityUtil.merge("jira_query.vm", ctx);
		logger.info("jira Query : " + jiraQuery);

		String jiraURL = properties.get("baseUrl")
				+ URLEncoder.encode(jiraQuery, "UTF-8") + "&tempMax=1000";
		logger.info("jira URL : " + jiraURL);

		return jiraURL;
	}

	public String getStartDate() {
		Calendar cal = Calendar.getInstance();
		cal.set(Calendar.WEEK_OF_YEAR, cal.get(Calendar.WEEK_OF_YEAR) - 1);
		cal.set(Calendar.DAY_OF_WEEK, Calendar.SATURDAY);
		return sdf.format(cal.getTime());
	}

	public String getEndDate() {
		Calendar cal = Calendar.getInstance();
		cal.set(Calendar.DAY_OF_WEEK, Calendar.SATURDAY);
		return sdf.format(cal.getTime());
	}
}
