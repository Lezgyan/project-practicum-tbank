package ru.tbank.practicum.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import java.time.OffsetDateTime;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.annotations.UpdateTimestamp;
import org.hibernate.type.SqlTypes;
import ru.tbank.practicum.entity.statePayload.DeviceStatePayload;

@Entity
@Table(name = "device_state")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class DeviceState {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(name = "device_state_payload", columnDefinition = "jsonb", nullable = false)
    private DeviceStatePayload deviceStatePayload;

    @Column(name = "has_error", nullable = false)
    private Boolean hasError = false;

    @Column(name = "error_message")
    private String errorMessage;

    @UpdateTimestamp
    @Column(name = "updated_at", nullable = false)
    private OffsetDateTime updatedAt;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "device_id")
    private Device device;
}
