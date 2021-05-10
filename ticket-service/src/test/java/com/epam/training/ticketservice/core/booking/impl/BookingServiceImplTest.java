package com.epam.training.ticketservice.core.booking.impl;

import com.epam.training.ticketservice.core.booking.model.BookingDto;
import com.epam.training.ticketservice.core.booking.model.SeatDto;
import com.epam.training.ticketservice.core.booking.persistence.entity.Booking;
import com.epam.training.ticketservice.core.booking.persistence.entity.Seat;
import com.epam.training.ticketservice.core.booking.persistence.repository.BookingRepository;
import com.epam.training.ticketservice.core.mapper.*;
import com.epam.training.ticketservice.core.mapper.impl.*;
import com.epam.training.ticketservice.core.movie.model.MovieDto;
import com.epam.training.ticketservice.core.movie.persistence.entity.Movie;
import com.epam.training.ticketservice.core.pricecomponent.persistence.entity.BasePrice;
import com.epam.training.ticketservice.core.pricecomponent.persistence.entity.PriceComponent;
import com.epam.training.ticketservice.core.pricecomponent.persistence.repository.BasePriceRepository;
import com.epam.training.ticketservice.core.room.model.RoomDto;
import com.epam.training.ticketservice.core.room.persistence.entity.Room;
import com.epam.training.ticketservice.core.screening.persistence.entity.Screening;
import com.epam.training.ticketservice.core.screening.persistence.entity.ScreeningId;
import com.epam.training.ticketservice.core.screening.persistence.repository.ScreeningRepository;
import com.epam.training.ticketservice.core.user.persistence.entity.Role;
import com.epam.training.ticketservice.core.user.persistence.entity.User;
import com.epam.training.ticketservice.core.user.persistence.repository.UserRepository;
import com.epam.training.ticketservice.ui.utilities.StringToDate;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Date;
import java.util.List;
import java.util.Optional;

class BookingServiceImplTest {

    private BookingServiceImpl underTest;
    private ScreeningRepository screeningRepository;
    private BookingRepository bookingRepository;
    private UserRepository userRepository;
    private BookingEntityToDtoMapper entityToDtoMapper;
    private ScreeningEntityToDtoMapper screeningEntityToDtoMapper;
    private MovieEntityToDtoMapper movieEntityToDtoMapper;
    private RoomEntityToDtoMapper roomEntityToDtoMapper;
    private UserEntityToDtoMapper userEntityToDtoMapper;
    private SeatEntityToDtoMapper seatEntityToDtoMapper;
    private BasePriceRepository basePriceRepository;

    private static final String DATE_STRING = "2021-03-14 16:00";
    private static final StringToDate stringToDate = new StringToDate();
    private static final Date DATE = stringToDate.convert(DATE_STRING);


    private static final String ROOM_NAME = "A1";
    private static final Integer ROWS = 10;
    private static final Integer COLUMNS = 10;
    private static final RoomDto ROOM = new RoomDto.Builder()
            .withRoomName(ROOM_NAME)
            .withSeatRows(ROWS)
            .withSeatColumns(COLUMNS)
            .build();

    private static final String TITLE = "Sprited Away";
    private static final String GENRE = "animation";
    private static final Integer LENGTH = 125;
    private static final MovieDto MOVIE = new MovieDto.Builder()
            .withTitle(TITLE)
            .withGenre(GENRE)
            .withLength(LENGTH)
            .build();

    private static final String USERNAME = "user";
    private static final String PASSWORD = "user";
    private static final Role ROLE = Role.ROLE_USER;
    private static final Integer PRICE = 1500;

    private static final Room ROOM_ENTITY = new Room(null, "A1", 10, 10,null);
    private static final Movie MOVIE_ENTITY = new Movie(null, "Sprited Away", "animation", 125, null);
    private static final ScreeningId SCREENING_ID = new ScreeningId(MOVIE_ENTITY, ROOM_ENTITY, DATE);
    private static final Screening SCREENING_ENTITY = new Screening(SCREENING_ID, null);
    private static final User USER = new User(null,USERNAME,PASSWORD, ROLE);
    private static final List<Seat> SEATS = List.of(new Seat(5,5),new Seat(5,6));
    private static final Booking BOOKING_ENTITY = new Booking(null,USER,SCREENING_ENTITY,SEATS,SEATS.size()*PRICE);



    @BeforeEach
    public void init() {
        screeningRepository = Mockito.mock(ScreeningRepository.class);
        bookingRepository = Mockito.mock(BookingRepository.class);
        userRepository = Mockito.mock(UserRepository.class);
        basePriceRepository = Mockito.mock(BasePriceRepository.class);
        movieEntityToDtoMapper = new MovieEntityToDtoMapperImpl();
        roomEntityToDtoMapper = new RoomEntityToDtoMapperImpl();
        screeningEntityToDtoMapper =
                new ScreeningEntityToDtoMapperImpl(movieEntityToDtoMapper,roomEntityToDtoMapper);
        seatEntityToDtoMapper = new SeatEntityToDtoMapperImpl();
        userEntityToDtoMapper = new UserEntityToDtoMapperImpl();
        entityToDtoMapper= new BookingEntityToDtoMapperImpl(screeningEntityToDtoMapper,
                seatEntityToDtoMapper,userEntityToDtoMapper);

        underTest = new BookingServiceImpl(screeningRepository,
                bookingRepository,
                userRepository,
                entityToDtoMapper, basePriceRepository);

        Authentication authentication = Mockito.mock(UsernamePasswordAuthenticationToken.class);
        SecurityContext securityContext = Mockito.mock(SecurityContext.class);
        Mockito.when(authentication.getName()).thenReturn(USERNAME);
        Mockito.when(securityContext.getAuthentication()).thenReturn(authentication);
        SecurityContextHolder.setContext(securityContext);

    }

    @Test
    public void testGetBookingForUserShouldReturnAListOfBookingDto(){
        //Given
        Mockito.when(bookingRepository.getBookingByUser_Username(USERNAME))
                .thenReturn(List.of(BOOKING_ENTITY));

        List<BookingDto> expected = List.of(entityToDtoMapper.convertEntityToDto(BOOKING_ENTITY));
        //When
        List<BookingDto> actual = underTest.getBookingForUser(USERNAME);
        //Then
        Assertions.assertEquals(expected,actual);

        Mockito.verify(bookingRepository).getBookingByUser_Username(USERNAME);
        Mockito.verifyNoMoreInteractions(bookingRepository);
    }


    @Test
    public void createBookingShouldThrowIllegalArgumentExceptionIfScreeningNotExist(){
        //Given
        Mockito.when(screeningRepository.existsById_Movie_TitleAndId_Room_NameAndId_StartTime(TITLE,ROOM_NAME,DATE))
                .thenReturn(false);
        //When
        Assertions.assertThrows(IllegalArgumentException.class,
                ()->underTest.createBooking(TITLE,ROOM_NAME,DATE,seatEntityToDtoMapper.convertEntityToDto(SEATS)));
        //Then
        Mockito.verify(screeningRepository)
                .existsById_Movie_TitleAndId_Room_NameAndId_StartTime(TITLE,ROOM_NAME,DATE);
        Mockito.verifyNoMoreInteractions(screeningRepository);
    }

    @Test
    public void createBookingShouldThrowIllegalStateExceptionIfUserNotExist(){
        //Given
        Mockito.when(screeningRepository.existsById_Movie_TitleAndId_Room_NameAndId_StartTime(TITLE,ROOM_NAME,DATE))
                .thenReturn(true);

        //When
        Assertions.assertThrows(IllegalStateException.class,
                ()->underTest.createBooking(TITLE,ROOM_NAME,DATE,seatEntityToDtoMapper.convertEntityToDto(SEATS)));
        //Then
        Mockito.verify(screeningRepository)
                .existsById_Movie_TitleAndId_Room_NameAndId_StartTime(TITLE,ROOM_NAME,DATE);
        Mockito.verifyNoMoreInteractions(screeningRepository);
    }

    @Test
    public void createBookingShouldReturnBookingToStringIfInputValid(){
        //Given
        Mockito.when(screeningRepository.existsById_Movie_TitleAndId_Room_NameAndId_StartTime(TITLE,ROOM_NAME,DATE))
                .thenReturn(true);
        Mockito.when(userRepository.findByUsername(USERNAME)).thenReturn(Optional.of(USER));
        Mockito.when(screeningRepository
                .getScreeningById_Movie_TitleAndId_Room_NameAndId_StartTime(TITLE,ROOM_NAME,DATE))
                .thenReturn(SCREENING_ENTITY);
        Mockito.when(screeningRepository.findById_Movie_TitleAndId_Room_NameAndId_StartTime(TITLE,ROOM_NAME,DATE))
                .thenReturn(Optional.of(SCREENING_ENTITY));
        Mockito.when(basePriceRepository.getBasePriceById(1)).thenReturn(new BasePrice(1,1500));
        Mockito.when(bookingRepository.getBookingByScreening(SCREENING_ENTITY)).thenReturn(List.of());
        Mockito.when(bookingRepository.save(BOOKING_ENTITY)).thenReturn(BOOKING_ENTITY);
        String expected = BOOKING_ENTITY.toString();
        //When
        String actual = underTest.createBooking(TITLE,ROOM_NAME,DATE,seatEntityToDtoMapper.convertEntityToDto(SEATS));
        //Then
        Assertions.assertEquals(expected,actual);

        Mockito.verify(screeningRepository)
                .existsById_Movie_TitleAndId_Room_NameAndId_StartTime(TITLE,ROOM_NAME,DATE);
        Mockito.verify(screeningRepository)
                .getScreeningById_Movie_TitleAndId_Room_NameAndId_StartTime(TITLE,ROOM_NAME,DATE);
        Mockito.verify(userRepository)
                .findByUsername(USERNAME);
        Mockito.verify(screeningRepository).findById_Movie_TitleAndId_Room_NameAndId_StartTime(TITLE,ROOM_NAME,DATE);
        Mockito.verify(basePriceRepository).getBasePriceById(1);
        Mockito.verify(bookingRepository).save(BOOKING_ENTITY);
        Mockito.verify(bookingRepository)
                .getBookingByScreening(SCREENING_ENTITY);
        Mockito
                .verifyNoMoreInteractions(screeningRepository);
        Mockito
                .verifyNoMoreInteractions(userRepository);
        Mockito
                .verifyNoMoreInteractions(bookingRepository);
        Mockito.verifyNoMoreInteractions(basePriceRepository);
    }

    @Test
    public void createBookingShouldThrowIllegalArgumentExceptionIfSeatNotExistInTheRoom(){
        //Given
        Mockito.when(screeningRepository.existsById_Movie_TitleAndId_Room_NameAndId_StartTime(TITLE,ROOM_NAME,DATE))
                .thenReturn(true);
        Mockito.when(userRepository.findByUsername(USERNAME)).thenReturn(Optional.of(USER));
        Mockito.when(screeningRepository
                .getScreeningById_Movie_TitleAndId_Room_NameAndId_StartTime(TITLE,ROOM_NAME,DATE))
                .thenReturn(SCREENING_ENTITY);
        Mockito.when(basePriceRepository.getBasePriceById(1)).thenReturn(new BasePrice(1,1500));
        Mockito.when(screeningRepository.findById_Movie_TitleAndId_Room_NameAndId_StartTime(TITLE,ROOM_NAME,DATE))
                .thenReturn(Optional.of(SCREENING_ENTITY));
        //When
        Assertions.assertThrows(IllegalArgumentException.class,()->underTest.createBooking(TITLE,ROOM_NAME,DATE,List.of(new SeatDto(11,12))));
        //Then
        Mockito.verify(screeningRepository).existsById_Movie_TitleAndId_Room_NameAndId_StartTime(TITLE,ROOM_NAME,DATE);
        Mockito.verify(screeningRepository).getScreeningById_Movie_TitleAndId_Room_NameAndId_StartTime(TITLE,ROOM_NAME,DATE);
        Mockito.verify(userRepository).findByUsername(USERNAME);
        Mockito.verify(screeningRepository).findById_Movie_TitleAndId_Room_NameAndId_StartTime(TITLE,ROOM_NAME,DATE);
        Mockito.verify(basePriceRepository).getBasePriceById(1);
        Mockito.verifyNoMoreInteractions(screeningRepository);
        Mockito.verifyNoMoreInteractions(userRepository);
        Mockito.verifyNoMoreInteractions(bookingRepository);
        Mockito.verifyNoMoreInteractions(basePriceRepository);

    }

    @Test
    public void createBookingShouldThrowIllegalArgumentExceptionIfSeatNotExistInTheRoomWhenSeatColumnLessThanZero(){
        //Given
        Mockito.when(screeningRepository.existsById_Movie_TitleAndId_Room_NameAndId_StartTime(TITLE,ROOM_NAME,DATE))
                .thenReturn(true);
        Mockito.when(userRepository.findByUsername(USERNAME)).thenReturn(Optional.of(USER));
        Mockito.when(screeningRepository
                .getScreeningById_Movie_TitleAndId_Room_NameAndId_StartTime(TITLE,ROOM_NAME,DATE))
                .thenReturn(SCREENING_ENTITY);
        Mockito.when(basePriceRepository.getBasePriceById(1)).thenReturn(new BasePrice(1,1500));
        Mockito.when(screeningRepository.findById_Movie_TitleAndId_Room_NameAndId_StartTime(TITLE,ROOM_NAME,DATE))
                .thenReturn(Optional.of(SCREENING_ENTITY));
        //When
        Assertions.assertThrows(IllegalArgumentException.class,()->underTest.createBooking(TITLE,ROOM_NAME,DATE,List.of(new SeatDto(11,-2))));
        //Then
        Mockito.verify(screeningRepository).existsById_Movie_TitleAndId_Room_NameAndId_StartTime(TITLE,ROOM_NAME,DATE);
        Mockito.verify(screeningRepository).getScreeningById_Movie_TitleAndId_Room_NameAndId_StartTime(TITLE,ROOM_NAME,DATE);
        Mockito.verify(screeningRepository).findById_Movie_TitleAndId_Room_NameAndId_StartTime(TITLE,ROOM_NAME,DATE);
        Mockito.verify(userRepository).findByUsername(USERNAME);
        Mockito.verify(basePriceRepository).getBasePriceById(1);
        Mockito.verifyNoMoreInteractions(screeningRepository);
        Mockito.verifyNoMoreInteractions(userRepository);
        Mockito.verifyNoMoreInteractions(bookingRepository);
        Mockito.verifyNoMoreInteractions(basePriceRepository);

    }

    @Test
    public void createBookingShouldThrowIllegalArgumentExceptionIfSeatNotExistInTheRoomWhenSeatRowLessThanZero(){
        //Given
        Mockito.when(screeningRepository.existsById_Movie_TitleAndId_Room_NameAndId_StartTime(TITLE,ROOM_NAME,DATE))
                .thenReturn(true);
        Mockito.when(userRepository.findByUsername(USERNAME)).thenReturn(Optional.of(USER));
        Mockito.when(screeningRepository
                .getScreeningById_Movie_TitleAndId_Room_NameAndId_StartTime(TITLE,ROOM_NAME,DATE))
                .thenReturn(SCREENING_ENTITY);
        Mockito.when(basePriceRepository.getBasePriceById(1)).thenReturn(new BasePrice(1,1500));
        Mockito.when(screeningRepository.findById_Movie_TitleAndId_Room_NameAndId_StartTime(TITLE,ROOM_NAME,DATE))
                .thenReturn(Optional.of(SCREENING_ENTITY));
        //When
        Assertions.assertThrows(IllegalArgumentException.class,()->underTest.createBooking(TITLE,ROOM_NAME,DATE,List.of(new SeatDto(-1,11))));
        //Then
        Mockito.verify(screeningRepository).existsById_Movie_TitleAndId_Room_NameAndId_StartTime(TITLE,ROOM_NAME,DATE);
        Mockito.verify(screeningRepository).getScreeningById_Movie_TitleAndId_Room_NameAndId_StartTime(TITLE,ROOM_NAME,DATE);
        Mockito.verify(userRepository).findByUsername(USERNAME);
        Mockito.verify(basePriceRepository).getBasePriceById(1);
        Mockito.verify(screeningRepository).findById_Movie_TitleAndId_Room_NameAndId_StartTime(TITLE,ROOM_NAME,DATE);
        Mockito.verifyNoMoreInteractions(screeningRepository);
        Mockito.verifyNoMoreInteractions(userRepository);
        Mockito.verifyNoMoreInteractions(bookingRepository);
        Mockito.verifyNoMoreInteractions(basePriceRepository);

    }

    @Test
    public void createBookingShouldThrowIllegalArgumentExceptionIfSeatAlreadyTakenInTheRoom(){
        //Given
        Mockito.when(screeningRepository.existsById_Movie_TitleAndId_Room_NameAndId_StartTime(TITLE,ROOM_NAME,DATE))
                .thenReturn(true);
        Mockito.when(userRepository.findByUsername(USERNAME)).thenReturn(Optional.of(USER));
        Mockito.when(screeningRepository
                .getScreeningById_Movie_TitleAndId_Room_NameAndId_StartTime(TITLE,ROOM_NAME,DATE))
                .thenReturn(SCREENING_ENTITY);
        Mockito.when(bookingRepository.getBookingByScreening(SCREENING_ENTITY)).thenReturn(List.of(BOOKING_ENTITY));
        Mockito.when(basePriceRepository.getBasePriceById(1)).thenReturn(new BasePrice(1,1500));
        Mockito.when(screeningRepository.findById_Movie_TitleAndId_Room_NameAndId_StartTime(TITLE,ROOM_NAME,DATE))
                .thenReturn(Optional.of(SCREENING_ENTITY));
        //When
        Assertions.assertThrows(IllegalArgumentException.class,()->underTest.createBooking(TITLE,ROOM_NAME,DATE,seatEntityToDtoMapper.convertEntityToDto(SEATS)));
        //Then
        Mockito.verify(screeningRepository).existsById_Movie_TitleAndId_Room_NameAndId_StartTime(TITLE,ROOM_NAME,DATE);
        Mockito.verify(screeningRepository).getScreeningById_Movie_TitleAndId_Room_NameAndId_StartTime(TITLE,ROOM_NAME,DATE);
        Mockito.verify(userRepository).findByUsername(USERNAME);
        Mockito.verify(bookingRepository).getBookingByScreening(SCREENING_ENTITY);
        Mockito.verify(screeningRepository).findById_Movie_TitleAndId_Room_NameAndId_StartTime(TITLE,ROOM_NAME,DATE);
        Mockito.verify(basePriceRepository).getBasePriceById(1);
        Mockito.verifyNoMoreInteractions(bookingRepository);
        Mockito.verifyNoMoreInteractions(screeningRepository);
        Mockito.verifyNoMoreInteractions(userRepository);
        Mockito.verifyNoMoreInteractions(basePriceRepository);

    }

    @Test
    public void calculatePriceShouldCountTheGivenPriceComponentsIfExist(){
        //Given
        PriceComponent priceComponent = new PriceComponent(null,"asd",1500);
        Movie movie = new Movie(null,TITLE,GENRE,LENGTH,priceComponent);
        Room room = new Room(null,ROOM_NAME,ROWS,COLUMNS,priceComponent);
        ScreeningId screeningId = new ScreeningId(movie,room,DATE);
        Screening screening = new Screening(screeningId,priceComponent);
        Mockito.when(basePriceRepository.getBasePriceById(1)).thenReturn(new BasePrice(1,1500));
        Mockito.when(screeningRepository.findById_Movie_TitleAndId_Room_NameAndId_StartTime(TITLE,ROOM_NAME,DATE))
                .thenReturn(Optional.of(screening));
        Integer expected = 6000;
        //When
        Integer actual = underTest.calculatePrice(TITLE,ROOM_NAME,DATE,List.of(new SeatDto(5,5)));

        //Then
        Assertions.assertEquals(expected,actual);
        Mockito.verify(basePriceRepository).getBasePriceById(1);
        Mockito.verify(screeningRepository).findById_Movie_TitleAndId_Room_NameAndId_StartTime(TITLE,ROOM_NAME,DATE);
        Mockito.verifyNoMoreInteractions(basePriceRepository);
        Mockito.verifyNoMoreInteractions(screeningRepository);

    }
}