package controller;

import service.UtilityCalculatorService;
import view.UtilityCalculatorView;

public class UtilityCalculatorController {
    public static void main(String[] args) {
        UtilityCalculatorService service = new UtilityCalculatorService();
        UtilityCalculatorView view = new UtilityCalculatorView(service);
    }
}