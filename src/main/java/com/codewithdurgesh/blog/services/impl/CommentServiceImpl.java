package com.codewithdurgesh.blog.services.impl;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.codewithdurgesh.blog.entities.Comment;
import com.codewithdurgesh.blog.entities.Post;
import com.codewithdurgesh.blog.exceptions.ResourceNotFoundException;
import com.codewithdurgesh.blog.payloads.CommentDto;
import com.codewithdurgesh.blog.repositories.CommentRepo;
import com.codewithdurgesh.blog.repositories.PostRepo;
import com.codewithdurgesh.blog.services.CommentService;

@Service
public class CommentServiceImpl implements CommentService {

    @Autowired
    private PostRepo postRepo;

    @Autowired
    private CommentRepo commentRepo;

    @Autowired
    private ModelMapper modelMapper;

    @Override
    public CommentDto createComment(CommentDto commentDto, Integer postId) {

        // 1. get post
        Post post = this.postRepo.findById(postId)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Post", "postId", postId));

        // 2. map dto -> entity
        Comment comment = this.modelMapper.map(commentDto, Comment.class);

        // 3. set post
        comment.setPost(post);

        // 4. save comment
        Comment savedComment = this.commentRepo.save(comment);

        // 5. map entity -> dto
        return this.modelMapper.map(savedComment, CommentDto.class);
    }

    @Override
    public void deleteComment(Integer commentId) {

        Comment comment = this.commentRepo.findById(commentId)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Comment", "commentId", commentId));

        this.commentRepo.delete(comment);
    }
}
