package view;

import javax.swing.*;
import java.awt.*;
import java.text.NumberFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

import model.CalculationResult;
import model.UtilityInput;
import service.UtilityCalculatorService;
import exception.InvalidInputException;
import api.MessageSender;


public class UtilityCalculatorView {
    private final UtilityCalculatorService calculatorService;
    private final MessageSender messageSender;
    private JTextArea resultArea;
    private JTextField waterPrevField;
    private JTextField electricityPrevField;
    private JTextField gasPrevField;
    private JTextField waterCurrentField;
    private JTextField electricityCurrentField;
    private JTextField gasCurrentField;

    public UtilityCalculatorView(UtilityCalculatorService calculatorService, MessageSender messageSender) {
        this.calculatorService = calculatorService;
        this.messageSender = messageSender;
        initializeUI();
    }

    private void initializeUI() {
        JFrame frame = new JFrame("Калькулятор коммунальных услуг");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(500, 400);
        frame.setLayout(new BorderLayout());

        JPanel inputPanel = createInputPanel();
        resultArea = new JTextArea();
        resultArea.setEditable(false);
        resultArea.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JButton calculateButton = new JButton("Рассчитать");
        calculateButton.addActionListener(e -> onCalculateButtonClick());

        JButton sendTelegramButton = new JButton("Отправить в Telegram");
        sendTelegramButton.addActionListener(e -> sendToTelegram());

        JPanel buttonPanel = new JPanel();
        buttonPanel.add(calculateButton);
        buttonPanel.add(sendTelegramButton);

        frame.add(inputPanel, BorderLayout.NORTH);
        frame.add(new JScrollPane(resultArea), BorderLayout.CENTER);
        frame.add(buttonPanel, BorderLayout.SOUTH);

        frame.setVisible(true);
    }

    private void sendToTelegram() {
        try {
            String message = resultArea.getText();
            if (message == null || message.isEmpty()) {
                JOptionPane.showMessageDialog(null, "Нет данных для отправки", "Ошибка", JOptionPane.WARNING_MESSAGE);
                return;
            }

            messageSender.send(message);
            JOptionPane.showMessageDialog(null, "Результаты отправлены в Telegram!", "Успех", JOptionPane.INFORMATION_MESSAGE);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(null, "Ошибка отправки: " + ex.getMessage(), "Ошибка", JOptionPane.ERROR_MESSAGE);
        }
    }

    private JPanel createInputPanel() {
        JPanel panel = new JPanel(new GridLayout(6, 2, 5, 5));
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        waterPrevField = new JTextField();
        electricityPrevField = new JTextField();
        gasPrevField = new JTextField();
        waterCurrentField = new JTextField();
        electricityCurrentField = new JTextField();
        gasCurrentField = new JTextField();

        panel.add(new JLabel("Вода (пред. месяц):"));
        panel.add(waterPrevField);
        panel.add(new JLabel("Электричество (пред. месяц):"));
        panel.add(electricityPrevField);
        panel.add(new JLabel("Газ (пред. месяц):"));
        panel.add(gasPrevField);
        panel.add(new JLabel("Вода (тек. месяц):"));
        panel.add(waterCurrentField);
        panel.add(new JLabel("Электричество (тек. месяц):"));
        panel.add(electricityCurrentField);
        panel.add(new JLabel("Газ (тек. месяц):"));
        panel.add(gasCurrentField);

        return panel;
    }

    private void onCalculateButtonClick() {
        try {
            UtilityInput input = readInputFields();
            CalculationResult result = calculatorService.calculate(input);
            displayResult(result);
        } catch (InvalidInputException | NumberFormatException e) {
            resultArea.setText("Ошибка: " + e.getMessage());
        }
    }

    private UtilityInput readInputFields() throws NumberFormatException {
        double waterPrev = Double.parseDouble(waterPrevField.getText());
        double electricityPrev = Double.parseDouble(electricityPrevField.getText());
        double gasPrev = Double.parseDouble(gasPrevField.getText());
        double waterCurrent = Double.parseDouble(waterCurrentField.getText());
        double electricityCurrent = Double.parseDouble(electricityCurrentField.getText());
        double gasCurrent = Double.parseDouble(gasCurrentField.getText());

        return new UtilityInput(
                waterPrev, electricityPrev, gasPrev,
                waterCurrent, electricityCurrent, gasCurrent
        );
    }

    private void displayResult(CalculationResult result) {
        LocalDate currentDate = LocalDate.now();
        String monthYear = currentDate.format(DateTimeFormatter.ofPattern("LLLL yyyy", new Locale("ru")));

        NumberFormat numberFormat = NumberFormat.getInstance(new Locale("ru", "RU"));
        numberFormat.setMinimumFractionDigits(2);
        numberFormat.setMaximumFractionDigits(2);

        String formattedResult = String.format(
                "Коммунальные услуги за %s\n" +
                        "         \n" +
                        "Вода: %.0f куб.м (%.0f → %.0f) * %s руб = %s руб\n" +
                        "Электричество: %.0f кВт*ч (%.0f → %.0f) * %s руб = %s руб\n" +
                        "Газ: %.0f куб.м (%.0f → %.0f) * %s руб = %s руб\n" +
                        "-------------------------------\n" +
                        "Итого: %s руб",
                monthYear,
                result.waterConsumed(), Double.parseDouble(waterPrevField.getText()), Double.parseDouble(waterCurrentField.getText()),
                numberFormat.format(82.0), numberFormat.format(result.waterCost()),
                result.electricityConsumed(), Double.parseDouble(electricityPrevField.getText()), Double.parseDouble(electricityCurrentField.getText()),
                numberFormat.format(4.57), numberFormat.format(result.electricityCost()),
                result.gasConsumed(), Double.parseDouble(gasPrevField.getText()), Double.parseDouble(gasCurrentField.getText()),
                numberFormat.format(7.14), numberFormat.format(result.gasCost()),
                numberFormat.format(result.totalCost())
        );

        resultArea.setText(formattedResult);
    }
}