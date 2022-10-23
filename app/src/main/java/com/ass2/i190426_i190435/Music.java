package com.ass2.i190426_i190435;

public class Music {
    String title, genre, description, link, image;

    public Music(String title, String genre, String description, String link, String image) {
        this.title = title;
        this.genre = genre;
        this.description = description;
        this.link = link;
        this.image = image;
    }



    public Music() {
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getGenre() {
        return genre;
    }

    public void setGenre(String genre) {
        this.genre = genre;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }
}
