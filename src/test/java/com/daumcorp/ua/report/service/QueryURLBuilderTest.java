package com.daumcorp.ua.report.service;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:appCtx.xml")
public class QueryURLBuilderTest {

	@Autowired
	QueryURLBuilder urlBuilder;

	@Test
	@Ignore
	public void testGetStartDate() {
		assertThat(urlBuilder.getStartDate(), is("2011-03-12"));
	}
}
