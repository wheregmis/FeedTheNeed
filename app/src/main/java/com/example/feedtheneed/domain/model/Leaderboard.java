package com.example.feedtheneed.domain.model;

import java.io.Serializable;

public class Leaderboard implements Serializable {
    private String name;
    private String count;

    public Leaderboard(String name, String count) {
        this.name = name;
        this.count = count;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCount() {
        return count;
    }

    public void setCount(String count) {
        this.count = count;
    }

    @Override
    public String toString() {
        return "Leaderboard{" +
                "name='" + name + '\'' +
                ", count='" + count + '\'' +
                '}';
    }
}
