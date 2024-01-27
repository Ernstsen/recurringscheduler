package dk.esoftware.recurringscheduler.crypto;

public interface HashingStrategy {

    HashingResult hash(String password);

    boolean verify(String password, HashingResult metadata);

    record HashingResult(String hash, String metadata) {
            public HashingResult(String hash, String metadata) {
                this.hash = hash;
                this.metadata = metadata;
            }

        }

}
