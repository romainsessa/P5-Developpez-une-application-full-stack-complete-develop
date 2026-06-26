package com.openclassrooms.mddapi.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.openclassrooms.mddapi.dto.PostDTO;
import com.openclassrooms.mddapi.payload.request.CreateCommentRequest;
import com.openclassrooms.mddapi.payload.request.CreatePostRequest;
import com.openclassrooms.mddapi.service.IPostService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/post")
public class PostController {

	private final IPostService postService;

	public PostController(IPostService postService) {
		this.postService = postService;
	}

	@GetMapping
	public ResponseEntity<List<PostDTO>> getPostsFromTopic(Authentication authentication) {
		List<PostDTO> postDTO = postService.getPostsFromTopic(authentication);
		return ResponseEntity.ok(postDTO);
	}

	@GetMapping("/{id}")
	public ResponseEntity<PostDTO> getPostById(@PathVariable Long id) {
		PostDTO postDTO = postService.getPostById(id);
		return ResponseEntity.ok(postDTO);
	}

	@PostMapping
	public ResponseEntity<Long> createPost(@Valid @RequestBody CreatePostRequest postCreateDTO,
			Authentication authentication) {
		Long postId = postService.createPost(postCreateDTO, authentication);
		return ResponseEntity.ok(postId);
	}

	@PostMapping("/{postId}/comments")
	public ResponseEntity<Void> addComment(@PathVariable Long postId,
			@Valid @RequestBody CreateCommentRequest commentCreateDTO, Authentication authentication) {
		postService.addComment(postId, commentCreateDTO, authentication);
		return ResponseEntity.ok().build();
	}
}
