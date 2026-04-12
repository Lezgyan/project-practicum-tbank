package ru.tbank.practicum.service;

import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.tbank.practicum.entity.Device;
import ru.tbank.practicum.entity.DeviceState;
import ru.tbank.practicum.entity.Room;
import ru.tbank.practicum.repository.RoomRepository;

@ExtendWith(MockitoExtension.class)
class AutomationServiceTest {

    @Mock
    private RoomRepository roomRepository;

    @Mock
    private DeviceService deviceService1;

    @Mock
    private DeviceService deviceService2;

    @InjectMocks
    private AutomationService automationService;

    @BeforeEach
    public void createAutomationService() {
        automationService = new AutomationService(roomRepository, List.of(deviceService1, deviceService2));
    }

    @Test
    void process_deviceStateHasNoError_appliesAllDeviceServices() {
        Device device = new Device();
        DeviceState state = new DeviceState();
        state.setHasError(false);
        device.setDeviceState(state);

        Room room = new Room();
        room.setDevices(List.of(device));

        when(roomRepository.getRooms()).thenReturn(List.of(room));

        automationService.process();

        verify(deviceService1).apply(device);
        verify(deviceService2).apply(device);
    }

    @Test
    void process_deviceStateHasError_skipsAllDeviceServices() {
        Device device = new Device();
        DeviceState state = new DeviceState();
        state.setHasError(true);
        device.setDeviceState(state);

        Room room = new Room();
        room.setDevices(List.of(device));

        when(roomRepository.getRooms()).thenReturn(List.of(room));

        automationService = new AutomationService(roomRepository, List.of(deviceService1, deviceService2));

        automationService.process();

        verify(deviceService1, never()).apply(device);
        verify(deviceService2, never()).apply(device);
    }
}
