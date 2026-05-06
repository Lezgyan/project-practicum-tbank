package ru.tbank.practicum.controller;

import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.tbank.practicum.dto.internal.DtoCreateRoom;
import ru.tbank.practicum.dto.internal.DtoCreateRoomResponse;
import ru.tbank.practicum.dto.internal.RoomResponseDto;
import ru.tbank.practicum.service.RoomService;

@RestController
@RequestMapping("/room")
@RequiredArgsConstructor
public class RoomController {

    private final RoomService roomService;

    @PostMapping("/create")
    public ResponseEntity<DtoCreateRoomResponse> createRoom(@RequestBody @Valid DtoCreateRoom createRoom) {
        DtoCreateRoomResponse dtoCreateRoomResponse = roomService.createRoom(createRoom);
        return ResponseEntity.status(HttpStatus.CREATED).body(dtoCreateRoomResponse);
    }

    @PutMapping("/{roomId}/addDevice/{deviceId}")
    public ResponseEntity<?> addDevice(@PathVariable Long roomId, @PathVariable Long deviceId) {
        roomService.addDevice(roomId, deviceId);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/")
    public ResponseEntity<List<RoomResponseDto>> getRooms() {
        return ResponseEntity.ok().body(roomService.getRooms());
    }
}
