package ru.tbank.practicum.repository;

import jakarta.transaction.Transactional;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.tbank.practicum.entity.DeviceState;

public interface DeviceStateRepository extends JpaRepository<DeviceState, Long> {
    @Transactional
    @Query("""
        select ds
        from DeviceState ds
        join fetch ds.device d
        order by ds.id
        """)
    List<DeviceState> findAllForApi();
}
