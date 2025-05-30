package service;

import model.UtilityInput;
import model.CalculationResult;
import exception.InvalidInputException;

public class UtilityCalculatorService {
    private static final double WATER_PRICE = 82.0;
    private static final double ELECTRICITY_PRICE = 4.57;
    private static final double GAS_PRICE = 7.14;
    private static final int INTERNET_PRICE = 700;    // Добавлено
    private static final int TSN_PRICE = 1230;

    public CalculationResult calculate(UtilityInput input) throws InvalidInputException {
        validateInput(input);

        double waterConsumed = input.waterCurrent() - input.waterPrev();
        double electricityConsumed = input.electricityCurrent() - input.electricityPrev();
        double gasConsumed = input.gasCurrent() - input.gasPrev();

        double waterCost = waterConsumed * WATER_PRICE;
        double electricityCost = electricityConsumed * ELECTRICITY_PRICE;
        double gasCost = gasConsumed * GAS_PRICE;
        double totalCost = waterCost + electricityCost + gasCost;

        // Добавлено: Учет Интернета и ТСН
        if (input.hasInternet()) {
            totalCost += INTERNET_PRICE;
        }
        if (input.hasTSN()) {
            totalCost += TSN_PRICE;
        }

        return new CalculationResult(
                waterConsumed, electricityConsumed, gasConsumed,
                waterCost, electricityCost, gasCost,
                totalCost,
                input.hasInternet(), input.hasTSN() // Добавлено
        );
    }

    private void validateInput(UtilityInput input) throws InvalidInputException {
        if (input.waterCurrent() < input.waterPrev()) {
            throw new InvalidInputException("Текущее значение воды не может быть меньше предыдущего");
        }
        if (input.electricityCurrent() < input.electricityPrev()) {
            throw new InvalidInputException("Текущее значение электричества не может быть меньше предыдущего");
        }
        if (input.gasCurrent() < input.gasPrev()) {
            throw new InvalidInputException("Текущее значение газа не может быть меньше предыдущего");
        }
    }
}