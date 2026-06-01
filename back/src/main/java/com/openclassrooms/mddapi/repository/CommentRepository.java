package com.openclassrooms.mddapi.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.openclassrooms.mddapi.entity.Comment;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Long> {

}
