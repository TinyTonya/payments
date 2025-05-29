package controller;

import api.MessageSender;
import api.TelegramSender;
import service.UtilityCalculatorService;
import view.UtilityCalculatorView;

public class UtilityCalculatorController {
    public static void main(String[] args) {
        UtilityCalculatorService service = new UtilityCalculatorService();
        MessageSender telegramSender = new TelegramSender();
        UtilityCalculatorView view = new UtilityCalculatorView(service, telegramSender);
    }
}