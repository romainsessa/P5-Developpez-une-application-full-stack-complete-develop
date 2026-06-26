package com.openclassrooms.mddapi.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.openclassrooms.mddapi.entity.Post;
import com.openclassrooms.mddapi.entity.Topic;

@Repository
public interface PostRepository extends JpaRepository<Post, Long>{
    List<Post> findByTopicIn(List<Topic> topics);
}
