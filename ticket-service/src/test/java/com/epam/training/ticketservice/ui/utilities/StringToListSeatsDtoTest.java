package com.epam.training.ticketservice.ui.utilities;

import com.epam.training.ticketservice.core.booking.model.SeatDto;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class StringToListSeatsDtoTest {
    private StringToListSeatsDto underTest = new StringToListSeatsDto();

    @Test
    public void testConvertShouldReturnAListOfSeatDto(){
        //Given
        List<SeatDto> expected = List.of(new SeatDto(5,5),new SeatDto(5,6));
        //When
        List<SeatDto> actual = underTest.convert("5,5 5,6");
        //Then
        Assertions.assertEquals(expected,actual);
    }

}