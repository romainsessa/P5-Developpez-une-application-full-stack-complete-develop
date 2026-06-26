package com.openclassrooms.mddapi.service;

import java.util.List;

import org.springframework.security.core.Authentication;

import com.openclassrooms.mddapi.dto.PostDTO;
import com.openclassrooms.mddapi.payload.request.CreateCommentRequest;
import com.openclassrooms.mddapi.payload.request.CreatePostRequest;

public interface IPostService {
	
	public Long createPost(CreatePostRequest postCreateDTO, Authentication authentication);

	public List<PostDTO> getAllPosts();

	public PostDTO getPostById(Long id);

	public List<PostDTO> getPostsFromTopic(Authentication authentication);

	public void addComment(Long postId, CreateCommentRequest commentCreateDTO, Authentication authentication);
}
