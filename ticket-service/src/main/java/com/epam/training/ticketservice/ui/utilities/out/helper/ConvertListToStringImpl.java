package com.epam.training.ticketservice.ui.utilities.out.helper;

import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Component
public class ConvertListToStringImpl implements ConvertListToString {
    @Override
    public String listToString(List<?> list) {
        return list.stream()
                .map(Objects::toString)
                .collect(Collectors.joining(System.lineSeparator()));
    }
}
