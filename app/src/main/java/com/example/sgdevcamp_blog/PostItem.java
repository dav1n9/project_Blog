package com.example.sgdevcamp_blog;

public class PostItem {
    private int id;             // 게시글 고유 id
    private String title;       // 제목
    private String content;     // 게시글
    private String writeDate;   // 작성 날짜

    public PostItem() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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

    public String getWriteDate() {
        return writeDate;
    }

    public void setWriteDate(String writeDate) {
        this.writeDate = writeDate;
    }
}
