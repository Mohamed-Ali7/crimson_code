package com.crimson_code_blog_rest_apis.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.crimson_code_blog_rest_apis.services.TagService;

@RestController
@RequestMapping("/api/tags")
public class TagController {

	private TagService tagService;

	@Autowired
	public TagController(TagService tagService) {
		this.tagService = tagService;
	}
	
	
}
