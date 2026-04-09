package ru.tbank.practicum.component;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import ru.tbank.practicum.dto.internal.DtoCreateDevice;
import ru.tbank.practicum.dto.internal.DtoCreateDeviceResponse;
import ru.tbank.practicum.dto.internal.DtoCreateRoom;
import ru.tbank.practicum.dto.internal.DtoCreateRoomResponse;
import ru.tbank.practicum.entity.Room;
import ru.tbank.practicum.enums.DeviceStatus;
import ru.tbank.practicum.enums.DeviceType;
import ru.tbank.practicum.repository.DeviceRepository;
import ru.tbank.practicum.repository.RoomRepository;
import ru.tbank.practicum.service.DeviceDbService;
import ru.tbank.practicum.service.RoomService;

@SpringBootTest
@ActiveProfiles("test")
class RoomServiceTestcontainersTest {

    @Autowired
    private RoomService roomService;

    @Autowired
    private DeviceDbService deviceDbService;

    @Autowired
    private RoomRepository roomRepository;

    @Autowired
    private DeviceRepository deviceRepository;

    @Test
    void givenExistingRoomAndExistingDevice_whenAddDevice_thenDeviceIsAddedToRoom() {

        DtoCreateRoomResponse dtoCreateRoomResponse =
                roomService.createRoom(new DtoCreateRoom("test-room", 12.0, 13.0, "Europe/Saratov"));

        DtoCreateDeviceResponse dtoCreateDeviceResponse = deviceDbService.createDevice(
                new DtoCreateDevice("test-device", DeviceType.BLINDS, DeviceStatus.ACTIVE));

        roomService.addDevice(dtoCreateRoomResponse.roomId(), dtoCreateDeviceResponse.deviceId());

        Room actualRoom = roomRepository.findById(dtoCreateRoomResponse.roomId()).orElseThrow();

        assertThat(actualRoom.getDevices())
                .anyMatch(savedDevice -> savedDevice.getId().equals(dtoCreateDeviceResponse.deviceId()));
    }
}
