package ru.tbank.practicum.service;

import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.tbank.practicum.dto.external.DtoCoordinateRequest;
import ru.tbank.practicum.dto.external.DtoWeatherResponse;
import ru.tbank.practicum.entity.Room;
import ru.tbank.practicum.entity.WeatherMeasurement;
import ru.tbank.practicum.mapper.MapperWeather;
import ru.tbank.practicum.repository.RoomRepository;

@Service
@RequiredArgsConstructor
public class WeatherService {

    private final WeatherClientService weatherClient;
    private final RoomRepository roomRepository;
    private final MapperWeather mapperWeather;

    public void processRooms() {
        List<Room> roomList = roomRepository.findAll();

        for (Room room : roomList) {

            DtoWeatherResponse dtoWeatherResponse = getWeather(room);

            if (room.getWeather() == null) {
                room.setWeather(mapperWeather.mapToEntityWeather(dtoWeatherResponse));
            } else {
                mapperWeather.updateEntityWeather(room.getWeather(), dtoWeatherResponse);
            }

            roomRepository.save(room);
        }
    }

    private DtoWeatherResponse getWeather(Room room) {

        DtoCoordinateRequest request = new DtoCoordinateRequest(room.getLat(), room.getLon());

        DtoWeatherResponse response = weatherClient.getWeatherByCoordinate(request);

        if (response == null || response.main() == null) {
            throw new IllegalStateException("Weather API returned empty response");
        }

        return response;
    }

    public WeatherMeasurement getWeatherByRoomId(Long id) {
        Optional<Room> room = roomRepository.findById(id);
        return room.map(Room::getWeather).orElse(null);
    }
}
