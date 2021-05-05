package com.epam.training.ticketservice.ui.utilities;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;


class StringToDateTest {

    @Test
    public void testShouldConvertStringToDateWhenInputIsValid() throws ParseException {
        //Given
        StringToDate stringToDate = new StringToDate();
        String date = "2021-03-14 16:00";
        Date expected = new SimpleDateFormat("yyyy-MM-dd HH:mm")
                .parse(date);
        //When
        Date actual = stringToDate.convert(date);
        //Then
        Assertions.assertEquals(expected, actual);
    }

    @Test
    public void testShouldThrowParseExceptionWhenInputIsInValid() {
        //Given
        StringToDate stringToDate = new StringToDate();
        String date = "2021-03-14";
        //When
        Assertions.assertThrows(ParseException.class,
                () -> stringToDate.convert(date));

    }

}