package net.mastermaxi.mastermaxisvanillaplus.entity.custom;

public interface MuddyPigAccess {
    int mastermaxisvanillaplus$getMudVariant();
    void mastermaxisvanillaplus$setMudVariant(int variant);
    int mastermaxisvanillaplus$getHappiness();
    void mastermaxisvanillaplus$setHappiness(int happiness);
    void mastermaxisvanillaplus$addHappiness(int amount);
    boolean mastermaxisvanillaplus$isDigging();
    void mastermaxisvanillaplus$setDigging(boolean digging);
    boolean mastermaxisvanillaplus$isWallowing();
    void mastermaxisvanillaplus$setWallowing(boolean wallowing);
    void mastermaxisvanillaplus$tickPig();
}
