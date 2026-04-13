package ru.tbank.practicum.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import jakarta.persistence.EntityNotFoundException;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.tbank.practicum.dto.internal.DtoCreateRoom;
import ru.tbank.practicum.dto.internal.DtoCreateRoomResponse;
import ru.tbank.practicum.entity.Device;
import ru.tbank.practicum.entity.Room;
import ru.tbank.practicum.mapper.MapperRoom;
import ru.tbank.practicum.repository.DeviceRepository;
import ru.tbank.practicum.repository.RoomRepository;

@ExtendWith(MockitoExtension.class)
class RoomServiceTest {

    @Mock
    private RoomRepository roomRepository;

    @Mock
    private DeviceRepository deviceRepository;

    @Mock
    private MapperRoom mapperRoom;

    @InjectMocks
    private RoomService roomService;

    @Test
    public void createRoom_validRequest_returnsMappedCreateRoomResponse() {
        DtoCreateRoom request = mock(DtoCreateRoom.class);
        Room room = new Room();
        Room savedRoom = new Room();
        DtoCreateRoomResponse expectedResponse = mock(DtoCreateRoomResponse.class);

        when(mapperRoom.mapToEntityRoom(request)).thenReturn(room);
        when(roomRepository.save(room)).thenReturn(savedRoom);
        when(mapperRoom.mapToCreateRoomResponse(savedRoom)).thenReturn(expectedResponse);

        DtoCreateRoomResponse result = roomService.createRoom(request);

        assertThat(result).isSameAs(expectedResponse);
        verify(mapperRoom).mapToEntityRoom(request);
        verify(roomRepository).save(room);
        verify(mapperRoom).mapToCreateRoomResponse(savedRoom);
    }

    @Test
    public void addDevice_existingRoomAndExistingDevice_addsDeviceToRoom() {
        Long roomId = 1L;
        Long deviceId = 2L;

        Room room = new Room();
        Device device = new Device();

        when(roomRepository.findById(roomId)).thenReturn(Optional.of(room));
        when(deviceRepository.findById(deviceId)).thenReturn(Optional.of(device));

        roomService.addDevice(roomId, deviceId);

        assertThat(room.getDevices()).contains(device);
    }

    @Test
    public void addDevice_roomDoesNotExist_throwsEntityNotFoundException() {
        Long roomId = 1L;
        Long deviceId = 2L;

        when(roomRepository.findById(roomId)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> roomService.addDevice(roomId, deviceId))
                .isInstanceOf(EntityNotFoundException.class)
                .hasMessage("Room not found: " + roomId);
    }

    @Test
    public void addDevice_deviceDoesNotExist_throwsEntityNotFoundException() {
        Long roomId = 1L;
        Long deviceId = 2L;

        Room room = new Room();

        when(roomRepository.findById(roomId)).thenReturn(Optional.of(room));
        when(deviceRepository.findById(deviceId)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> roomService.addDevice(roomId, deviceId))
                .isInstanceOf(EntityNotFoundException.class)
                .hasMessage("Device not found: " + deviceId);
    }
}
