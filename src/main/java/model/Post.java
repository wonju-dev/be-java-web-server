package model;

import dto.PostCreateDTO;

public class Post {

    private Long id;
    private User user;
    private String title;
    private String content;

    public Post(User user, PostCreateDTO postCreateDTO) {
        this.user = user;
        this.title = postCreateDTO.getTitle();
        this.content = postCreateDTO.getContent();
    }

    public static Post of(User user, PostCreateDTO postCreateDTO) {
        return new Post(user, postCreateDTO);
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }
}