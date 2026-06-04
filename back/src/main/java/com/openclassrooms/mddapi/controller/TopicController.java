package com.openclassrooms.mddapi.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.openclassrooms.mddapi.dto.TopicDTO;
import com.openclassrooms.mddapi.service.ITopicService;

@RestController
@RequestMapping("/api/topic")
public class TopicController {

	private ITopicService topicService;

	public TopicController(ITopicService topicService) {
		this.topicService = topicService;
	}

	@GetMapping
	public ResponseEntity<List<TopicDTO>> getAll() {
		return ResponseEntity.ok(topicService.getTopics());
	}

	@PostMapping("/{topicId}/subscribe")
	public ResponseEntity<Void> subscribe(@PathVariable Long topicId, Authentication authentication) {
		topicService.subscribe(topicId, authentication);
		return ResponseEntity.ok().build();
	}

	@DeleteMapping("/{topicId}/unsubscribe")
	public ResponseEntity<Void> unsubscribe(@PathVariable Long topicId, Authentication authentication) {
		topicService.unsubscribe(topicId, authentication);
		return ResponseEntity.ok().build();
	}

}
