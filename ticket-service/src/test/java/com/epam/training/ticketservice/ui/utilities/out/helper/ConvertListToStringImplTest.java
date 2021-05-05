package com.epam.training.ticketservice.ui.utilities.out.helper;

import com.epam.training.ticketservice.core.movie.model.MovieDto;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.List;

class ConvertListToStringImplTest {
    @Test
    public void testShouldConvertAListToString() {
        //Given
        ConvertListToStringImpl convertListToString = new ConvertListToStringImpl();
        MovieDto movie1 = new MovieDto.Builder()
                .withTitle("Sprited Away")
                .withGenre("comedy")
                .withLength(400)
                .build();
        MovieDto movie2 = new MovieDto.Builder()
                .withTitle("Avangers")
                .withGenre("action")
                .withLength(180)
                .build();
        List<MovieDto> movies = List.of(movie1, movie2);
        String expected = movie1.toString() + System.lineSeparator() + movie2.toString();
        //When
        String actual = convertListToString.listToString(movies);
        //Then
        Assertions.assertEquals(expected, actual);
    }

}