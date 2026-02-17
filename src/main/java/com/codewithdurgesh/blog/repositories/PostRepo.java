package com.codewithdurgesh.blog.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.codewithdurgesh.blog.entities.Category;
import com.codewithdurgesh.blog.entities.Post;
import com.codewithdurgesh.blog.entities.User;

public interface PostRepo extends JpaRepository<Post, Integer> {

    // get all posts by user
    List<Post> findByUser(User user);

    // get all posts by category
    List<Post> findByCategory(Category category);

    // search posts by title (Durgesh style)
    List<Post> findByTitleContaining(String title);
}
