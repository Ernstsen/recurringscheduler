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

    @Column(name = "algorithm")
    @JdbcTypeCode(SqlTypes.NVARCHAR)
    private String algorithm;

    @Lob
    @Column(name = "metadata")
    @JdbcTypeCode(SqlTypes.NVARCHAR)
    private String metadata;

    @Enumerated
    @Column(name = "credential_type", nullable = false)
    @JdbcTypeCode(SqlTypes.INTEGER)
    private CredentialType credentialType;

    public CredentialType getCredentialType() {
        return credentialType;
    }

    public void setCredentialType(CredentialType credentialType) {
        this.credentialType = credentialType;
    }

    public UserCredential() {
    }

    public UserCredential(UserEntity user, CredentialType credentialType, String algorithm, String value, String metadata) {
        this.user = user;
        this.credentialType = credentialType;
        this.value = value;
        this.algorithm = algorithm;
        this.metadata = metadata;
    }

    public UUID getId() {
        return id;
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

    public String getAlgorithm() {
        return algorithm;
    }

    public void setAlgorithm(String algorithm) {
        this.algorithm = algorithm;
    }

    public UserEntity getUser() {
        return user;
    }

    public void setUser(UserEntity user) {
        this.user = user;
    }


    public enum CredentialType {
        PASSWORD,
        COLLECT_TOKEN
    }
}
