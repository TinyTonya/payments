package model;

public final class CalculationResult {
    private final double waterConsumed;
    private final double electricityConsumed;
    private final double gasConsumed;
    private final double waterCost;
    private final double electricityCost;
    private final double gasCost;
    private final double totalCost;
    private final boolean hasInternet; // Добавлено
    private final boolean hasTSN;

    public CalculationResult(
            double waterConsumed,
            double electricityConsumed,
            double gasConsumed,
            double waterCost,
            double electricityCost,
            double gasCost,
            double totalCost,
            boolean hasInternet, // Добавлено
            boolean hasTSN
    ) {
        this.waterConsumed = waterConsumed;
        this.electricityConsumed = electricityConsumed;
        this.gasConsumed = gasConsumed;
        this.waterCost = waterCost;
        this.electricityCost = electricityCost;
        this.gasCost = gasCost;
        this.totalCost = totalCost;
        this.hasInternet = hasInternet; // Добавлено
        this.hasTSN = hasTSN;
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

    public boolean hasInternet() {
        return hasInternet;
    }
    // Добавлено
    public boolean hasTSN() {
        return hasTSN;
    }
}