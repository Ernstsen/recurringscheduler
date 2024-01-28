package dk.esoftware.recurringscheduler.crypto;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.bouncycastle.crypto.generators.Argon2BytesGenerator;
import org.bouncycastle.crypto.params.Argon2Parameters;

import java.security.SecureRandom;
import java.util.Arrays;
import java.util.Base64;

public class Argon2 implements HashingStrategy {
    private static final ObjectMapper mapper = new ObjectMapper();
    private static final SecureRandom RANDOM = new SecureRandom();
    private static final int DEFAULT_ITERATIONS = 10;
    private static final int DEFAULT_MEMORY = 65536;
    private static final int DEFAULT_PARALLELISM = 1;
    private static final int DEFAULT_SALT_LENGTH = 16;
    private static final int DEFAULT_HASH_LENGTH = 32;
    private static final int DEFAULT_TYPE = Argon2Parameters.ARGON2_id;
    private static final int DEFAULT_VERSION_TYPE = Argon2Parameters.ARGON2_VERSION_13;

    private Metadata getDefaultMetadata() {
        final byte[] salt = new byte[DEFAULT_SALT_LENGTH];
        RANDOM.nextBytes(salt);

        return new Metadata(
                DEFAULT_TYPE, DEFAULT_VERSION_TYPE, DEFAULT_ITERATIONS,
                DEFAULT_MEMORY, DEFAULT_PARALLELISM, DEFAULT_SALT_LENGTH,
                DEFAULT_HASH_LENGTH,
                salt
        );
    }

    private Argon2BytesGenerator generatorFromMetadata(Metadata metadata) {
        final Argon2Parameters parameters = new Argon2Parameters.Builder(metadata.type())
                .withVersion(metadata.typeVersion())
                .withMemoryAsKB(metadata.memory())
                .withParallelism(metadata.parallelism())
                .withSalt(metadata.salt())
                .build();

        final Argon2BytesGenerator generator = new Argon2BytesGenerator();
        generator.init(parameters);
        return generator;
    }

    private byte[] hashWithMetadata(String password, Metadata metadata) {
        final Argon2BytesGenerator generator = generatorFromMetadata(metadata);
        final byte[] hash = new byte[metadata.hashLength()];
        generator.generateBytes(password.getBytes(), hash, 0, hash.length);
        return hash;
    }

    @Override
    public HashingResult hash(String password) {
        final Metadata metadata = getDefaultMetadata();

        final byte[] hash = hashWithMetadata(password, metadata);

        try {
            return new HashingResult(
                    Base64.getEncoder().encodeToString(hash),
                    mapper.writeValueAsString(metadata)
            );
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Failed to serialize metadata for hash", e);
        }
    }

    @Override
    public boolean verify(String password, HashingResult hashingResult) {

        Metadata metadata;
        try {
            metadata = mapper.readValue(hashingResult.metadata(), Metadata.class);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Failed to deserialized Argon2 metadata", e);
        }

        final byte[] newHash = hashWithMetadata(password, metadata);
        final byte[] existingHash = Base64.getDecoder().decode(hashingResult.hash());

        return Arrays.equals(newHash, existingHash);
    }


    record Metadata(
            int type, int typeVersion, int iterations,
            int memory, int parallelism, int saltLength,
            int hashLength, byte[] salt) {
    }
}
