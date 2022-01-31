package com.epam.training.ticketservice.core.writer;

public interface OutputStringWriter<T> {
    String writeOutAsString(T object);
}
