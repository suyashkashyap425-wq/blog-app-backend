package com.codewithdurgesh.blog.services;

import com.codewithdurgesh.blog.payloads.CommentDto;

public interface CommentService {

    // create new comment on a post
    CommentDto createComment(CommentDto commentDto, Integer postId);

    // delete comment by id
    void deleteComment(Integer commentId);
}
