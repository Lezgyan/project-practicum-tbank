package ru.tbank.practicum.repository;

import jakarta.transaction.Transactional;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.tbank.practicum.entity.Room;

public interface RoomRepository extends JpaRepository<Room, Long> {
    @Transactional
    @Query("""
            SELECT r FROM Room r
            JOIN FETCH r.devices d
            """)
    List<Room> getRooms();
}
