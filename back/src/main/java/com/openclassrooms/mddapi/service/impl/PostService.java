package com.openclassrooms.mddapi.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.security.core.Authentication;

import com.openclassrooms.mddapi.dto.PostDTO;
import com.openclassrooms.mddapi.entity.Comment;
import com.openclassrooms.mddapi.entity.Post;
import com.openclassrooms.mddapi.entity.Topic;
import com.openclassrooms.mddapi.entity.User;
import com.openclassrooms.mddapi.exception.NotFoundException;
import com.openclassrooms.mddapi.mapper.PostMapper;
import com.openclassrooms.mddapi.payload.request.CreateCommentRequest;
import com.openclassrooms.mddapi.payload.request.CreatePostRequest;
import com.openclassrooms.mddapi.repository.CommentRepository;
import com.openclassrooms.mddapi.repository.PostRepository;
import com.openclassrooms.mddapi.repository.TopicRepository;
import com.openclassrooms.mddapi.repository.UserRepository;
import com.openclassrooms.mddapi.service.IPostService;

public class PostService implements IPostService {

	private final CommentRepository commentRepository;
	private final PostRepository postRepository;
	private final TopicRepository topicRepository;
	private final UserRepository userRepository;
	private final PostMapper postMapper;

	public PostService(CommentRepository commentRepository, PostRepository postRepository,
			TopicRepository topicRepository, UserRepository userRepository, PostMapper postMapper) {
		this.commentRepository = commentRepository;
		this.postRepository = postRepository;
		this.topicRepository = topicRepository;
		this.userRepository = userRepository;
		this.postMapper = postMapper;
	}

	public Long createPost(CreatePostRequest postCreateDTO, Authentication authentication) {
		String email = authentication.getName();
		Long topicId = postCreateDTO.getTopicId();

		User user = userRepository.findByEmail(email).orElseThrow(() -> new NotFoundException("Utilisateur"));

		Topic topic = topicRepository.findById(topicId).orElseThrow(() -> new NotFoundException("Article"));

		Post post = new Post();
		post.setTitle(postCreateDTO.getTitle());
		post.setContent(postCreateDTO.getContent());
		post.setAuthor(user);
		post.setTopic(topic);

		Post savedPost = this.postRepository.save(post);
		return savedPost.getId();
	}

	public List<PostDTO> getAllPosts() {
		List<Post> posts = postRepository.findAll();
		return postMapper.toDto(posts);
	}

	public PostDTO getPostById(Long id) {
		Post post = postRepository.findById(id).orElseThrow(() -> new NotFoundException("Article"));
		return postMapper.toDto(post);
	}

	public List<PostDTO> getPostsFromTopic(Authentication authentication) {
		String email = authentication.getName();

		User user = userRepository.findByEmail(email).orElseThrow(() -> new NotFoundException("Utilisateur"));

		List<Topic> subscribedTopics = user.getTopics();
		if (subscribedTopics.isEmpty()) {
			return new ArrayList<>();
		}
		List<Post> posts = postRepository.findByTopicIn(subscribedTopics);
		return postMapper.toDto(posts);
	}

	public void addComment(Long postId, CreateCommentRequest commentCreateDTO, Authentication authentication) {
		String email = authentication.getName();
		User user = userRepository.findByEmail(email).orElseThrow(() -> new NotFoundException("Utilisateur"));

		Post post = postRepository.findById(postId).orElseThrow(() -> new NotFoundException("Article"));

		Comment comment = new Comment();
		comment.setContent(commentCreateDTO.content());
		comment.setAuthor(user);
		comment.setPost(post);
		commentRepository.save(comment);
	}

}
