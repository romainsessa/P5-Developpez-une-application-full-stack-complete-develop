package com.openclassrooms.mddapi.service.impl;

import java.util.List;

import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import com.openclassrooms.mddapi.dto.TopicDTO;
import com.openclassrooms.mddapi.entity.Topic;
import com.openclassrooms.mddapi.entity.User;
import com.openclassrooms.mddapi.exception.NotFoundException;
import com.openclassrooms.mddapi.mapper.TopicMapper;
import com.openclassrooms.mddapi.repository.TopicRepository;
import com.openclassrooms.mddapi.repository.UserRepository;
import com.openclassrooms.mddapi.service.ITopicService;

@Service
public class TopicService implements ITopicService {

	private TopicRepository topicRepository;
	private TopicMapper topicMapper;
	private UserRepository userRepository;

	public TopicService(TopicRepository topicRepository, TopicMapper topicMapper, UserRepository userRepository) {
		this.topicRepository = topicRepository;
		this.topicMapper = topicMapper;
		this.userRepository = userRepository;
	}

	public List<TopicDTO> getTopics() {
		List<Topic> topics = topicRepository.findAll();
		return topicMapper.toDto(topics);
	}

	public void subscribe(Long topicId, Authentication authentication) {
		String email = authentication.getName();
		System.out.println(email);
		User user = userRepository.findByEmail(email).orElseThrow(() -> new NotFoundException("Utilisateur"));
		Topic topic = topicRepository.findById(topicId).orElseThrow(() -> new NotFoundException("Thème"));

		boolean alreadySubscribed = topic.getUsers().stream().anyMatch(o -> o.getId().equals(user.getId()));
		if (!alreadySubscribed) {
			topic.getUsers().add(user);
			topicRepository.save(topic);
		}
	}

	public void unsubscribe(Long topicId, Authentication authentication) {
		String email = authentication.getName();

		User user = userRepository.findByEmail(email).orElseThrow(() -> new NotFoundException("Utilisateur"));
		Topic topic = topicRepository.findById(topicId).orElseThrow(() -> new NotFoundException("Thème"));

		boolean alreadySubscribed = topic.getUsers().stream().anyMatch(o -> o.getId().equals(user.getId()));

		if (alreadySubscribed) {
			topic.getUsers().remove(user);
			topicRepository.save(topic);
		}
	}

}
