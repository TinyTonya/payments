package view;

import javax.swing.*;
import java.awt.*;
import java.text.NumberFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Locale;
import java.util.Map;

import model.CalculationResult;
import model.UtilityInput;
import service.UtilityCalculatorService;
import exception.InvalidInputException;
import api.MessageSender;
import util.DataStorage;
import util.IntegerInputVerifier;


public class UtilityCalculatorView {
    private final UtilityCalculatorService calculatorService;
    private final MessageSender messageSender;
    private final IntegerInputVerifier inputVerifier = new IntegerInputVerifier();
    private JTextArea resultArea;
    private JTextField waterPrevField;
    private JTextField electricityPrevField;
    private JTextField gasPrevField;
    private JTextField waterCurrentField;
    private JTextField electricityCurrentField;
    private JTextField gasCurrentField;
    private JCheckBox internetCheckBox;
    private JCheckBox tsnCheckBox;

    public UtilityCalculatorView(UtilityCalculatorService calculatorService, MessageSender messageSender) {
        this.calculatorService = calculatorService;
        this.messageSender = messageSender;
        initializeUI();
        loadPrevMonthData();
    }

    private void loadPrevMonthData() {
        try {
            Map<String, Double> prevData = DataStorage.getPrevMonthInputs();
            if (!prevData.isEmpty()) {
                waterPrevField.setText(String.valueOf(prevData.getOrDefault("water", 0.0).intValue()));
                electricityPrevField.setText(String.valueOf(prevData.getOrDefault("electricity", 0.0).intValue()));
                gasPrevField.setText(String.valueOf(prevData.getOrDefault("gas", 0.0).intValue()));
            }
        } catch (Exception e) {
            System.err.println("Ошибка загрузки данных: " + e.getMessage());
            // Поля останутся пустыми
        }
    }

    private void initializeUI() {
        JFrame frame = new JFrame("Калькулятор коммунальных услуг");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(600, 500);
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
        JPanel panel = new JPanel(new GridLayout(8, 2, 5, 5));
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        waterPrevField = new JTextField();
        waterPrevField.setInputVerifier(inputVerifier); // Добавлено
        electricityPrevField = new JTextField();
        electricityPrevField.setInputVerifier(inputVerifier); // Добавлено
        gasPrevField = new JTextField();
        gasPrevField.setInputVerifier(inputVerifier); // Добавлено
        waterCurrentField = new JTextField();
        waterCurrentField.setInputVerifier(inputVerifier); // Добавлено
        electricityCurrentField = new JTextField();
        electricityCurrentField.setInputVerifier(inputVerifier); // Добавлено
        gasCurrentField = new JTextField();
        gasCurrentField.setInputVerifier(inputVerifier);

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

        // Добавлено: Чекбоксы для Интернета и ТСН
        internetCheckBox = new JCheckBox("\uD83C\uDF10 Интернет (700 руб/мес)");
        internetCheckBox.setSelected(true);  // Включено по умолчанию

        tsnCheckBox = new JCheckBox("\uD83C\uDFE2 ТСН (1230 руб/мес)");
        tsnCheckBox.setSelected(true);

        panel.add(new JLabel("Доп. услуги:"));
        panel.add(new JLabel("")); // Пустая ячейка для выравнивания
        panel.add(internetCheckBox);
        panel.add(tsnCheckBox);

        return panel;
    }

    private void onCalculateButtonClick() {
        try {
            UtilityInput input = readInputFields();
            CalculationResult result = calculatorService.calculate(input);
            DataStorage.saveMonthlyData(input, result); // Сохраняем данные
            displayResult(result);
        } catch (Exception e) {
            resultArea.setText("Ошибка: " + e.getMessage());
        }
    }

    private UtilityInput readInputFields() throws NumberFormatException {
        int waterPrev = Integer.parseInt(waterPrevField.getText());
        int electricityPrev = Integer.parseInt(electricityPrevField.getText());
        int gasPrev = Integer.parseInt(gasPrevField.getText());
        int waterCurrent = Integer.parseInt(waterCurrentField.getText());
        int electricityCurrent = Integer.parseInt(electricityCurrentField.getText());
        int gasCurrent = Integer.parseInt(gasCurrentField.getText());

        return new UtilityInput(
                waterPrev, electricityPrev, gasPrev,
                waterCurrent, electricityCurrent, gasCurrent,
                internetCheckBox.isSelected(), // Добавлено
                tsnCheckBox.isSelected()
        );
    }

    private void displayResult(CalculationResult result) {
        LocalDate currentDate = LocalDate.now();
        String monthYear = currentDate.format(DateTimeFormatter.ofPattern("LLLL yyyy", new Locale("ru")));

        NumberFormat numberFormat = NumberFormat.getInstance(new Locale("ru", "RU"));
        numberFormat.setMinimumFractionDigits(2);
        numberFormat.setMaximumFractionDigits(2);

        // Emoji для услуг
        String waterEmoji = "\uD83D\uDCA7";       // Капля
        String electricityEmoji = "\u26A1";       // Молния
        String gasEmoji = "\uD83D\uDD25";         // Огонь
        String internetEmoji = "\uD83C\uDF10";    // Глобус
        String tsnEmoji = "\uD83C\uDFE2";         // Офисное здание

        StringBuilder formattedResult = new StringBuilder();
        formattedResult.append(String.format(
                "Потребление за %s\n\n" +
                        "%s Вода: %.0f м3 (%.0f → %.0f)*%s р = %s р\n" +
                        "%s Электричество: %.0f кВт (%.0f → %.0f)*%s р = %s р\n" +
                        "%s Газ: %.0f м3 (%.0f → %.0f)*%s р = %s р\n",
                monthYear,
                waterEmoji,
                result.waterConsumed(), Double.parseDouble(waterPrevField.getText()), Double.parseDouble(waterCurrentField.getText()),
                numberFormat.format(82.0), numberFormat.format(result.waterCost()),
                electricityEmoji,
                result.electricityConsumed(), Double.parseDouble(electricityPrevField.getText()), Double.parseDouble(electricityCurrentField.getText()),
                numberFormat.format(4.57), numberFormat.format(result.electricityCost()),
                gasEmoji,
                result.gasConsumed(), Double.parseDouble(gasPrevField.getText()), Double.parseDouble(gasCurrentField.getText()),
                numberFormat.format(7.14), numberFormat.format(result.gasCost())
        ));

        // Добавлено: Информация о доп. услугах
        if (result.hasInternet()) {
            formattedResult.append(String.format("%s Интернет: %s р\n", internetEmoji, numberFormat.format(700)));
        }
        if (result.hasTSN()) {
            formattedResult.append(String.format("%s ТСН: %s р\n", tsnEmoji, numberFormat.format(1230)));
        }

        // Добавлено: Итого и Всего к оплате
        formattedResult.append(String.format(
                "-------------------------------\n" +
                        "\uD83D\uDCB0 Итого по счетчикам: %s р\n" +
                        "\uD83D\uDCB8 Всего к оплате: %s р",
                numberFormat.format(result.waterCost() + result.electricityCost() + result.gasCost()),
                numberFormat.format(result.totalCost())
        ));

        resultArea.setText(formattedResult.toString());
    }
}