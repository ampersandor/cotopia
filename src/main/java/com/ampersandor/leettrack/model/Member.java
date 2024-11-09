package com.ampersandor.leettrack.model;

import jakarta.persistence.*;

@Entity
public class Member {

    @Id @GeneratedValue(strategy=GenerationType.IDENTITY)
    private Long id;

    private String name;
    private String username;
    private int likes;

    public Member(){}

    public Member(Long id, String name, String username) {
        this.id = id;
        this.name = name;
        this.username = username;
    }

    public Member(String name, String username) {
        this.name = name;
        this.username = username;
    }

    public Long getId() {
        return id;
    }

    public int getLikes() {
        return likes;
    }

    public void setLikes(int likes) {
        this.likes = likes;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    @Override
    public String toString() {
        return "Member{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", username='" + username + '\'' +
                '}';
    }
}
