package com.daumcorp.ua.report.service;

import org.springframework.stereotype.Service;

@Service
public class PostWiki {

	public void publish(String wikitext) {
		System.out.println(wikitext);	
	}

}
