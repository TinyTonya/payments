package util;

import javax.swing.InputVerifier;
import javax.swing.JComponent;
import javax.swing.JOptionPane;
import javax.swing.JTextField;

public class IntegerInputVerifier extends InputVerifier {
    @Override
    public boolean verify(JComponent input) {
        JTextField textField = (JTextField) input;
        String text = textField.getText().trim();

        if (text.isEmpty()) {
            JOptionPane.showMessageDialog(
                    input,
                    "Поле не может быть пустым!",
                    "Ошибка ввода",
                    JOptionPane.ERROR_MESSAGE
            );
            return false;
        }

        try {
            Integer.parseInt(text);
            return true;
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(
                    input,
                    "Допустимы только целые числа!",
                    "Ошибка ввода",
                    JOptionPane.ERROR_MESSAGE
            );
            textField.setText(""); // Очистка поля
            return false;
        }
    }
}