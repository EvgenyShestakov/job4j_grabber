package ru.job4j.grabber;

import java.time.LocalDateTime;

public class Post {
    private int id;
    private String name;
    private String text;
    private String link;
    private LocalDateTime created;

    public Post(String name, String text, String link, LocalDateTime created) {
        this.name = name;
        this.text = text;
        this.link = link;
        this.created = created;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public LocalDateTime getCreated() {
        return created;
    }

    public void setCreated(LocalDateTime created) {
        this.created = created;
    }

    @Override
    public String toString() {
        return "Post{"
                + "id=" + id
                + ", name='" + name + '\''
                + ", text='" + text + '\''
                + ", link='" + link + '\''
                + ", created=" + created
                + '}';
    }
}
