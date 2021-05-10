package com.epam.training.ticketservice.core.pricecomponent.impl;

import com.epam.training.ticketservice.core.booking.BookingService;
import com.epam.training.ticketservice.core.movie.MovieService;
import com.epam.training.ticketservice.core.movie.impl.MovieServiceImplTest;
import com.epam.training.ticketservice.core.pricecomponent.PriceComponentService;
import com.epam.training.ticketservice.core.pricecomponent.persistence.entity.PriceComponent;
import com.epam.training.ticketservice.core.pricecomponent.persistence.repository.BasePriceRepository;
import com.epam.training.ticketservice.core.pricecomponent.persistence.repository.PriceComponentRepository;
import com.epam.training.ticketservice.core.room.RoomService;
import com.epam.training.ticketservice.core.screening.ScreeningService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

class PriceComponentServiceImplTest {
    private  PriceComponentRepository priceComponentRepository;
    private  MovieService movieService;
    private  RoomService roomService;
    private  ScreeningService screeningService;
    private  BookingService bookingService;
    private  BasePriceRepository basePriceRepository;
    private PriceComponentService underTest;

    private static final String NAME = "asd";
    private static final Integer VALUE = 1500;
    private static final PriceComponent PRICE_COMPONENT = new PriceComponent(null,NAME,VALUE);

    @BeforeEach
    public void init() {
        priceComponentRepository = Mockito.mock(PriceComponentRepository.class);
        movieService = Mockito.mock(MovieService.class);
        roomService = Mockito.mock(RoomService.class);
        screeningService = Mockito.mock(ScreeningService.class);
        bookingService = Mockito.mock(BookingService.class);
        basePriceRepository = Mockito.mock(BasePriceRepository.class);
        underTest = new PriceComponentServiceImpl(priceComponentRepository,
                movieService,roomService,screeningService,bookingService,basePriceRepository);
    }

    @Test
    public void testCreatePriceComponentShouldThrowIllegalArgumentExceptionIfPriceComponentAlreadyExist(){
        //Given
        Mockito.when(priceComponentRepository.existsPriceComponentByName(NAME)).thenReturn(true);
        //When
        Assertions.assertThrows(IllegalArgumentException.class,()->underTest.createPriceComponent(NAME,VALUE));
        //Then
        Mockito.verify(priceComponentRepository).existsPriceComponentByName(NAME);
        Mockito.verifyNoMoreInteractions(priceComponentRepository);
    }

    @Test
    public void testCreatePriceComponentShouldCreatePriceComponentIfPriceComponentNotExist(){
        //Given
        Mockito.when(priceComponentRepository.existsPriceComponentByName(NAME)).thenReturn(false);
        Mockito.when(priceComponentRepository.save(PRICE_COMPONENT)).thenReturn(PRICE_COMPONENT);
        //When
        underTest.createPriceComponent(NAME,VALUE);
        //Then
        Mockito.verify(priceComponentRepository).save(PRICE_COMPONENT);
        Mockito.verify(priceComponentRepository).existsPriceComponentByName(NAME);
        Mockito.verifyNoMoreInteractions(priceComponentRepository);
    }

    @Test
    public void testAttachPriceComponentToMovieShouldThrowIllegalArgumentExceptionWhenPriceComponentNotExist(){
        //When
        Assertions.assertThrows(IllegalArgumentException.class,()->underTest.attachPriceComponentToMovie(NAME,"Spirited Away"));
        //Then
        Mockito.verify(priceComponentRepository).findPriceComponentByName(NAME);
        Mockito.verifyNoMoreInteractions(priceComponentRepository);
    }

    @Test
    public void testAttachPriceComponentToMovieShouldAttachPriceIfComponentExist(){
        //Given
        Mockito.when(priceComponentRepository.findPriceComponentByName(NAME)).thenReturn(Optional.of(PRICE_COMPONENT));
        //When
        underTest.attachPriceComponentToMovie(NAME,"Spirited Away");
        //Then
        Mockito.verify(movieService).updatePriceComponent(PRICE_COMPONENT,"Spirited Away");
        Mockito.verify(priceComponentRepository).findPriceComponentByName(NAME);
        Mockito.verifyNoMoreInteractions(movieService);
        Mockito.verifyNoMoreInteractions(priceComponentRepository);
    }

    @Test
    public void testAttachPriceComponentToRoomShouldThrowIllegalArgumentExceptionWhenPriceComponentNotExist(){
        //When
        Assertions.assertThrows(IllegalArgumentException.class,()->underTest.attachPriceComponentToRoom(NAME,"Pedersoli"));
        //Then
        Mockito.verify(priceComponentRepository).findPriceComponentByName(NAME);
        Mockito.verifyNoMoreInteractions(priceComponentRepository);
    }

    @Test
    public void testAttachPriceComponentToRoomShouldAttachPriceIfComponentExist(){
        //Given
        Mockito.when(priceComponentRepository.findPriceComponentByName(NAME)).thenReturn(Optional.of(PRICE_COMPONENT));
        //When
        underTest.attachPriceComponentToRoom(NAME,"Pedersoli");
        //Then
        Mockito.verify(roomService).updatePriceComponent(PRICE_COMPONENT,"Pedersoli");
        Mockito.verify(priceComponentRepository).findPriceComponentByName(NAME);
        Mockito.verifyNoMoreInteractions(roomService);
        Mockito.verifyNoMoreInteractions(priceComponentRepository);
    }

    @Test
    public void testAttachPriceComponentToScreeningShouldThrowIllegalArgumentExceptionWhenPriceComponentNotExist(){
        //When
        Assertions.assertThrows(IllegalArgumentException.class,()->underTest.attachPriceComponentToScreening(NAME,"Sprited Away","Pedersoli",new SimpleDateFormat("yyyy-MM-dd HH:mm")
                .parse("2021-03-14 16:00")));
        //Then
        Mockito.verify(priceComponentRepository).findPriceComponentByName(NAME);
        Mockito.verifyNoMoreInteractions(priceComponentRepository);
    }

    @Test
    public void testAttachPriceComponentToScreeningShouldAttachPriceIfComponentExist() throws ParseException {
        //Given
        Mockito.when(priceComponentRepository.findPriceComponentByName(NAME)).thenReturn(Optional.of(PRICE_COMPONENT));
        //When
        underTest.attachPriceComponentToScreening(NAME,"Sprited Away","Pedersoli",new SimpleDateFormat("yyyy-MM-dd HH:mm")
                .parse("2021-03-14 16:00"));
        //Then
        Mockito.verify(screeningService).updatePriceComponent(PRICE_COMPONENT,"Sprited Away","Pedersoli",new SimpleDateFormat("yyyy-MM-dd HH:mm")
                .parse("2021-03-14 16:00"));
        Mockito.verify(priceComponentRepository).findPriceComponentByName(NAME);
        Mockito.verifyNoMoreInteractions(screeningService);
        Mockito.verifyNoMoreInteractions(priceComponentRepository);
    }


}