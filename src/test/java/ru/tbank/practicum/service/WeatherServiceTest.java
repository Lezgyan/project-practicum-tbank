package ru.tbank.practicum.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import jakarta.persistence.EntityNotFoundException;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.tbank.practicum.dto.external.Coordinates;
import ru.tbank.practicum.dto.external.DtoCoordinateRequest;
import ru.tbank.practicum.dto.external.DtoWeatherResponse;
import ru.tbank.practicum.dto.external.MainWeatherInfo;
import ru.tbank.practicum.entity.Room;
import ru.tbank.practicum.entity.WeatherMeasurement;
import ru.tbank.practicum.mapper.MapperWeather;
import ru.tbank.practicum.repository.RoomRepository;

@ExtendWith(MockitoExtension.class)
class WeatherServiceTest {

    @Mock
    private WeatherClientService weatherClient;

    @Mock
    private RoomRepository roomRepository;

    @Mock
    private MapperWeather mapperWeather;

    @InjectMocks
    private WeatherService weatherService;

    @Test
    void processRooms_roomWithoutWeather_setsMappedWeatherAndSavesRoom() {
        Room room = new Room();
        room.setLat(55.75);
        room.setLon(37.61);

        DtoWeatherResponse response = createWeatherResponse();

        WeatherMeasurement newWeather = new WeatherMeasurement();

        when(roomRepository.findAll()).thenReturn(List.of(room));
        when(weatherClient.getWeatherByCoordinate(any(DtoCoordinateRequest.class)))
                .thenReturn(response);
        when(mapperWeather.mapToEntityWeather(response)).thenReturn(newWeather);

        weatherService.processRooms();

        assertThat(room.getWeather()).isSameAs(newWeather);
        verify(mapperWeather).mapToEntityWeather(response);
        verify(roomRepository).save(room);
    }

    @Test
    void processRooms_roomWithExistingWeather_updatesWeatherAndSavesRoom() {
        Room room = new Room();
        room.setLat(55.75);
        room.setLon(37.61);

        WeatherMeasurement existingWeather = new WeatherMeasurement();
        room.setWeather(existingWeather);

        DtoWeatherResponse response = createWeatherResponse();

        when(roomRepository.findAll()).thenReturn(List.of(room));
        when(weatherClient.getWeatherByCoordinate(any(DtoCoordinateRequest.class)))
                .thenReturn(response);

        weatherService.processRooms();

        verify(mapperWeather).updateEntityWeather(existingWeather, response);
        verify(roomRepository).save(room);
    }

    @Test
    void processRooms_weatherApiReturnsNull_throwsIllegalStateException() {
        Room room = new Room();
        room.setLat(55.75);
        room.setLon(37.61);

        when(roomRepository.findAll()).thenReturn(List.of(room));
        when(weatherClient.getWeatherByCoordinate(any(DtoCoordinateRequest.class)))
                .thenReturn(null);

        assertThatThrownBy(() -> weatherService.processRooms())
                .isInstanceOf(IllegalStateException.class)
                .hasMessage("Weather API returned empty response");
    }

    @Test
    void processRooms_roomCoordinatesExist_requestsWeatherByRoomLatitudeAndLongitude() {
        Long roomId = 1L;
        Room room = new Room();
        WeatherMeasurement weather = new WeatherMeasurement();
        room.setWeather(weather);

        when(roomRepository.findById(roomId)).thenReturn(Optional.of(room));

        WeatherMeasurement result = weatherService.getWeatherByRoomId(roomId);

        assertThat(result).isSameAs(weather);
    }

    @Test
    void getWeatherByRoomId_roomDoesNotExist_throwsEntityNotFoundException() {
        Long roomId = 1L;

        when(roomRepository.findById(roomId)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> weatherService.getWeatherByRoomId(roomId))
                .isInstanceOf(EntityNotFoundException.class)
                .hasMessage("Room not found: " + roomId);
    }

    @Test
    void getWeatherByRoomId_existingRoom_returnsRoomWeather() {
        Room room = new Room();
        room.setLat(10.0);
        room.setLon(20.0);

        DtoWeatherResponse response = createWeatherResponse();

        when(roomRepository.findAll()).thenReturn(List.of(room));
        when(weatherClient.getWeatherByCoordinate(any(DtoCoordinateRequest.class)))
                .thenReturn(response);
        when(mapperWeather.mapToEntityWeather(response)).thenReturn(new WeatherMeasurement());

        weatherService.processRooms();

        ArgumentCaptor<DtoCoordinateRequest> captor = ArgumentCaptor.forClass(DtoCoordinateRequest.class);
        verify(weatherClient, times(1)).getWeatherByCoordinate(captor.capture());

        assertEquals(10.0, captor.getValue().lat());
        assertEquals(20.0, captor.getValue().lon());
    }

    private DtoWeatherResponse createWeatherResponse() {
        return DtoWeatherResponse.builder()
                .coordinates(new Coordinates(12.0, 14.0))
                .main(createMainWeatherInfo())
                .build();
    }

    private MainWeatherInfo createMainWeatherInfo() {
        return MainWeatherInfo.builder().feelsLike(12.0).build();
    }
}
