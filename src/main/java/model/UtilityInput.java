package model;

public final class UtilityInput {
    private final double waterPrev;
    private final double electricityPrev;
    private final double gasPrev;
    private final double waterCurrent;
    private final double electricityCurrent;
    private final double gasCurrent;
    private final boolean hasInternet; // Добавлено
    private final boolean hasTSN;

    public UtilityInput(
            double waterPrev,
            double electricityPrev,
            double gasPrev,
            double waterCurrent,
            double electricityCurrent,
            double gasCurrent,
            boolean hasInternet, // Добавлено
            boolean hasTSN
    ) {
        this.waterPrev = waterPrev;
        this.electricityPrev = electricityPrev;
        this.gasPrev = gasPrev;
        this.waterCurrent = waterCurrent;
        this.electricityCurrent = electricityCurrent;
        this.gasCurrent = gasCurrent;
        this.hasInternet = hasInternet; // Добавлено
        this.hasTSN = hasTSN;
    }

    // Геттеры
    public double waterPrev() {
        return waterPrev;
    }

    public double electricityPrev() {
        return electricityPrev;
    }

    public double gasPrev() {
        return gasPrev;
    }

    public double waterCurrent() {
        return waterCurrent;
    }

    public double electricityCurrent() {
        return electricityCurrent;
    }

    public double gasCurrent() {
        return gasCurrent;
    }
    public boolean hasInternet() {
        return hasInternet;
    }
    // Добавлено
    public boolean hasTSN() {
        return hasTSN;
    }
}