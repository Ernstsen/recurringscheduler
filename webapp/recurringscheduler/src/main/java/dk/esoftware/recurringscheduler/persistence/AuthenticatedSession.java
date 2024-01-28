package dk.esoftware.recurringscheduler.persistence;

import jakarta.persistence.*;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
public class AuthenticatedSession {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id", nullable = false)
    private UUID id;

    @Column(name = "creation_ts", nullable = false)
    @JdbcTypeCode(SqlTypes.TIMESTAMP)
    private LocalDateTime creationTS;

    @ManyToOne(optional = false)
    @JoinColumn(name = "user_entity_id", nullable = false)
    private UserEntity userEntity;

    protected AuthenticatedSession() {
    }

    public AuthenticatedSession(UserEntity userEntity) {
        this.userEntity = userEntity;
        this.creationTS = LocalDateTime.now();
    }

    public UserEntity getUserEntity() {
        return userEntity;
    }

    public void setUserEntity(UserEntity userEntity) {
        this.userEntity = userEntity;
    }

    public LocalDateTime getCreationTS() {
        return creationTS;
    }

    public void setCreationTS(LocalDateTime creationTS) {
        this.creationTS = creationTS;
    }

    public UUID getId() {
        return id;
    }

}
