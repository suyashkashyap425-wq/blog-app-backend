package com.codewithdurgesh.blog.payloads;

public class CommentDto {

    private Integer id;
    private String content;

    public CommentDto() {
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
