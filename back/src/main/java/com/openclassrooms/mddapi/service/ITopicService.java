package com.openclassrooms.mddapi.service;

import java.util.List;

import org.springframework.security.core.Authentication;

import com.openclassrooms.mddapi.dto.TopicDTO;

public interface ITopicService {
	
	public List<TopicDTO> getTopics();

	public void subscribe(Long topicId, Authentication authentication);

	public void unsubscribe(Long topicId, Authentication authentication);
}
