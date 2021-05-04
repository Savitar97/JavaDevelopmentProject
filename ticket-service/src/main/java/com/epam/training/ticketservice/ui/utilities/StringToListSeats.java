package com.epam.training.ticketservice.ui.utilities;

import com.epam.training.ticketservice.core.booking.model.SeatDto;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class StringToListSeats implements Converter<String, List<SeatDto>> {
    @Override
    public List<SeatDto> convert(String s) {
        return Arrays.stream(s.split(" ")).map(element -> {
            List<String> seats = List.of(element.split(","));
            return SeatDto.builder()
                    .row(Integer.parseInt(seats.get(0)))
                    .column(Integer.parseInt(seats.get(1)))
                    .build();
        }).collect(Collectors.toList());
    }
}
