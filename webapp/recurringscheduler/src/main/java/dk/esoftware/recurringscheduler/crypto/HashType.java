package dk.esoftware.recurringscheduler.crypto;

public enum HashType {
    ARGON2(new Argon2()),
    ;

    HashingStrategy strategy;

    HashType(HashingStrategy strategy) {
        this.strategy = strategy;
    }

    public HashingStrategy getStrategy() {
        return strategy;
    }
}
