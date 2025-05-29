package model;

public final class CalculationResult {
    private final double waterConsumed;
    private final double electricityConsumed;
    private final double gasConsumed;
    private final double waterCost;
    private final double electricityCost;
    private final double gasCost;
    private final double totalCost;

    public CalculationResult(
            double waterConsumed,
            double electricityConsumed,
            double gasConsumed,
            double waterCost,
            double electricityCost,
            double gasCost,
            double totalCost
    ) {
        this.waterConsumed = waterConsumed;
        this.electricityConsumed = electricityConsumed;
        this.gasConsumed = gasConsumed;
        this.waterCost = waterCost;
        this.electricityCost = electricityCost;
        this.gasCost = gasCost;
        this.totalCost = totalCost;
    }

    // Геттеры
    public double waterConsumed() {
        return waterConsumed;
    }

    public double electricityConsumed() {
        return electricityConsumed;
    }

    public double gasConsumed() {
        return gasConsumed;
    }

    public double waterCost() {
        return waterCost;
    }

    public double electricityCost() {
        return electricityCost;
    }

    public double gasCost() {
        return gasCost;
    }

    public double totalCost() {
        return totalCost;
    }
}