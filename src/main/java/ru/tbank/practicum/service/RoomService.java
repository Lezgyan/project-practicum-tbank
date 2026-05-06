package ru.tbank.practicum.service;

import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.tbank.practicum.dto.internal.DtoCreateRoom;
import ru.tbank.practicum.dto.internal.DtoCreateRoomResponse;
import ru.tbank.practicum.dto.internal.RoomResponseDto;
import ru.tbank.practicum.entity.Device;
import ru.tbank.practicum.entity.Room;
import ru.tbank.practicum.mapper.MapperRoom;
import ru.tbank.practicum.repository.DeviceRepository;
import ru.tbank.practicum.repository.RoomRepository;

@Service
@RequiredArgsConstructor
public class RoomService {
    private final RoomRepository roomRepository;

    private final DeviceRepository deviceRepository;

    private final MapperRoom mapperRoom;

    @Transactional
    public DtoCreateRoomResponse createRoom(DtoCreateRoom createRoom) {
        Room room = mapperRoom.mapToEntityRoom(createRoom);
        Room newRoom = roomRepository.save(room);
        return mapperRoom.mapToCreateRoomResponse(newRoom);
    }

    @Transactional
    public void addDevice(Long roomId, Long deviceId) {
        Room room = roomRepository
                .findById(roomId)
                .orElseThrow(() -> new EntityNotFoundException("Room not found: " + roomId));
        Device device = deviceRepository
                .findById(deviceId)
                .orElseThrow(() -> new EntityNotFoundException("Device not found: " + deviceId));

        room.addDevice(device);
    }

    public List<RoomResponseDto> getRooms() {
        List<Room> roomList = roomRepository.findAll();
        return mapperRoom.toDtoList(roomList);
    }
}
