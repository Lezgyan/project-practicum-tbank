package ru.tbank.practicum.service;

import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.tbank.practicum.entity.Device;
import ru.tbank.practicum.entity.Room;
import ru.tbank.practicum.repository.RoomRepository;

@Slf4j
@Service
@RequiredArgsConstructor
public class AutomationService {

    private final RoomRepository roomRepository;

    private final List<DeviceService> deviceServiceList;

    public void process() {

        List<Room> rooms = roomRepository.getRooms();

        for (Room room : rooms) {
            List<Device> devices = room.getDevices();

            for (Device device : devices) {
                if (hasError(device)) {
                    continue;
                }

                for (DeviceService deviceService : deviceServiceList) {
                    deviceService.apply(device);
                }
            }
        }
    }

    private boolean hasError(Device device) {
        return device.getDeviceState().getHasError();
    }
}
