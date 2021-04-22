package com.epam.training.ticketservice.core.movie.model;

import java.util.Objects;

public class MovieDto {
    final private String title;
    final private String genre;
    final private Integer length;

    public String getTitle() {
        return title;
    }

    public String getGenre() {
        return genre;
    }

    public Integer getLength() {
        return length;
    }

    public MovieDto(String title, String genre, Integer length) {
        this.title = title;
        this.genre = genre;
        this.length = length;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MovieDto movieDto = (MovieDto) o;
        return Objects.equals(title, movieDto.title) && Objects.equals(genre, movieDto.genre) && Objects.equals(length, movieDto.length);
    }

    @Override
    public int hashCode() {
        return Objects.hash(title, genre, length);
    }

    @Override
    public String toString() {
        return "MovieDto{" +
                "title='" + title + '\'' +
                ", genre='" + genre + '\'' +
                ", length=" + length +
                '}';
    }

    public static class Builder {
        private String title;
        private String genre;
        private Integer length;

        public Builder withTitle(String title){
            this.title=title;
            return this;
        }
        public Builder withGenre(String genre){
            this.genre=genre;
            return this;
        }
        public Builder withLength(Integer length){
            this.length=length;
            return this;
        }

        public MovieDto build() {
            return new MovieDto( this.title,this.genre,this.length);
        }


    }

}
