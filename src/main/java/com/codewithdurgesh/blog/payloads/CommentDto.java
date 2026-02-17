package com.codewithdurgesh.blog.payloads;

public class CommentDto {

    private Integer id;
    private String content;

    // ===== NoArgsConstructor =====
    public CommentDto() {
        super();
    }

    // ===== Getters & Setters =====

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
