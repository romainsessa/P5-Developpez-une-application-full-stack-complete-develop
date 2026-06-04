package com.openclassrooms.mddapi.payload.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class CreatePostRequest {
	@NotNull
	private Long topicId;

	@NotBlank
	private String title;

	@NotBlank
	private String content;

	public CreatePostRequest() {
		// TODO Auto-generated constructor stub
	}

	public CreatePostRequest(@NotNull Long topicId, @NotBlank String title, @NotBlank String content) {
		super();
		this.topicId = topicId;
		this.title = title;
		this.content = content;
	}

	public Long getTopicId() {
		return topicId;
	}

	public void setTopicId(Long topicId) {
		this.topicId = topicId;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

}
