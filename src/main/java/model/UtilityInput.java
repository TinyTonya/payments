package model;

public final class UtilityInput {
    private final double waterPrev;
    private final double electricityPrev;
    private final double gasPrev;
    private final double waterCurrent;
    private final double electricityCurrent;
    private final double gasCurrent;

    public UtilityInput(
            double waterPrev,
            double electricityPrev,
            double gasPrev,
            double waterCurrent,
            double electricityCurrent,
            double gasCurrent
    ) {
        this.waterPrev = waterPrev;
        this.electricityPrev = electricityPrev;
        this.gasPrev = gasPrev;
        this.waterCurrent = waterCurrent;
        this.electricityCurrent = electricityCurrent;
        this.gasCurrent = gasCurrent;
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
}