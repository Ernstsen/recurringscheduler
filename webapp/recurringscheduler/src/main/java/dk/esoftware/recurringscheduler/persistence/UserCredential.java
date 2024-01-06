package dk.esoftware.recurringscheduler.persistence;

import jakarta.persistence.*;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.util.UUID;

@Entity
public class UserCredential {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id", nullable = false)
    private UUID id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private UserEntity user;

    @Column(name = "credentialValue", length = 1024)
    @JdbcTypeCode(SqlTypes.NVARCHAR)
    private String value;

    @Lob
    @Column(name = "metadata")
    @JdbcTypeCode(SqlTypes.NVARCHAR)
    private String metadata;

    public UserCredential() {
    }

    public UserCredential(UserEntity user, String value, String metadata) {
        this.user = user;
        this.value = value;
        this.metadata = metadata;
    }

    public String getMetadata() {
        return metadata;
    }

    public void setMetadata(String metadata) {
        this.metadata = metadata;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
////    TODO: Type
//

    public UserEntity getUser() {
        return user;
    }

    public void setUser(UserEntity user) {
        this.user = user;
    }


}
