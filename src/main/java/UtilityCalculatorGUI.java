import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.NumberFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

public class UtilityCalculatorGUI {
    private static final double WATER_PRICE = 82.0;
    private static final double ELECTRICITY_PRICE = 4.57;
    private static final double GAS_PRICE = 7.14;

    public static void main(String[] args) {
        // Создаем главное окно
        JFrame frame = new JFrame("Калькулятор коммунальных услуг");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(500, 400);
        frame.setLayout(new BorderLayout());

        // Панель для ввода данных
        JPanel inputPanel = new JPanel(new GridLayout(6, 2, 5, 5));
        inputPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Поля для ввода
        JTextField waterPrevField = new JTextField();
        JTextField electricityPrevField = new JTextField();
        JTextField gasPrevField = new JTextField();
        JTextField waterCurrentField = new JTextField();
        JTextField electricityCurrentField = new JTextField();
        JTextField gasCurrentField = new JTextField();

        // Добавляем компоненты
        inputPanel.add(new JLabel("Вода (пред. месяц):"));
        inputPanel.add(waterPrevField);
        inputPanel.add(new JLabel("Электричество (пред. месяц):"));
        inputPanel.add(electricityPrevField);
        inputPanel.add(new JLabel("Газ (пред. месяц):"));
        inputPanel.add(gasPrevField);
        inputPanel.add(new JLabel("Вода (тек. месяц):"));
        inputPanel.add(waterCurrentField);
        inputPanel.add(new JLabel("Электричество (тек. месяц):"));
        inputPanel.add(electricityCurrentField);
        inputPanel.add(new JLabel("Газ (тек. месяц):"));
        inputPanel.add(gasCurrentField);

        // Панель для результатов
        JTextArea resultArea = new JTextArea();
        resultArea.setEditable(false);
        resultArea.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Кнопка расчета
        JButton calculateButton = new JButton("Рассчитать");
        calculateButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    LocalDate currentDate = LocalDate.now();
                    String monthYear = currentDate.format(DateTimeFormatter.ofPattern("LLLL yyyy", new Locale("ru")));

                    // Получаем значения из полей ввода
                    double waterPrev = Double.parseDouble(waterPrevField.getText());
                    double electricityPrev = Double.parseDouble(electricityPrevField.getText());
                    double gasPrev = Double.parseDouble(gasPrevField.getText());
                    double waterCurrent = Double.parseDouble(waterCurrentField.getText());
                    double electricityCurrent = Double.parseDouble(electricityCurrentField.getText());
                    double gasCurrent = Double.parseDouble(gasCurrentField.getText());

                    // Расчет
                    double waterConsumed = waterCurrent - waterPrev;
                    double electricityConsumed = electricityCurrent - electricityPrev;
                    double gasConsumed = gasCurrent - gasPrev;

                    double waterCost = waterConsumed * WATER_PRICE;
                    double electricityCost = electricityConsumed * ELECTRICITY_PRICE;
                    double gasCost = gasConsumed * GAS_PRICE;
                    double totalCost = waterCost + electricityCost + gasCost;

                    // Форматирование чисел
                    NumberFormat numberFormat = NumberFormat.getInstance(new Locale("ru", "RU"));
                    numberFormat.setMinimumFractionDigits(2);
                    numberFormat.setMaximumFractionDigits(2);

                    String result = String.format(
                            "Коммунальные услуги за %s\n" +
                                    "-------------------------------\n" +
                                    "\uD83D\uDCA7 Вода: %.0f куб.м (%.0f → %.0f) * %s руб = %s руб\n" +
                                    "⚡ Электричество: %.0f кВт*ч (%.0f → %.0f) * %s руб = %s руб\n" +
                                    "\uD83D\uDD25 Газ: %.0f куб.м (%.0f → %.0f) * %s руб = %s руб\n" +
                                    "-------------------------------\n" +
                                    "Итого: %s руб",
                            monthYear, // "Май 2025"
                            waterConsumed, waterPrev, waterCurrent,
                            numberFormat.format(WATER_PRICE), numberFormat.format(waterCost),
                            electricityConsumed, electricityPrev, electricityCurrent,
                            numberFormat.format(ELECTRICITY_PRICE), numberFormat.format(electricityCost),
                            gasConsumed, gasPrev, gasCurrent,
                            numberFormat.format(GAS_PRICE), numberFormat.format(gasCost),
                            numberFormat.format(totalCost)
                    );

                    resultArea.setText(result);
                } catch (NumberFormatException ex) {
                    resultArea.setText("Ошибка: Пожалуйста, введите корректные числовые значения.");
                }
            }
        });

        // ... (после создания calculateButton)

        JButton sendTelegramButton = new JButton("Отправить в Telegram");
        sendTelegramButton.addActionListener(e -> {
            try {
                TelegramSender.sendToTelegram(resultArea.getText());
                JOptionPane.showMessageDialog(frame, "✅ Результаты отправлены в Telegram!");
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(frame, "❌ Ошибка: " + ex.getMessage(),
                        "Ошибка", JOptionPane.ERROR_MESSAGE);
            }
        });
// Создаем панель для кнопок
        JPanel buttonPanel = new JPanel();
        buttonPanel.add(calculateButton);
        buttonPanel.add(sendTelegramButton);

// Добавляем компоненты в окно
        frame.add(inputPanel, BorderLayout.NORTH);
        frame.add(new JScrollPane(resultArea), BorderLayout.CENTER);
        frame.add(buttonPanel, BorderLayout.SOUTH);  // Важно: добавляем buttonPanel, а не отдельную кнопку

        // Отображаем окно
        frame.setVisible(true);
    }
}