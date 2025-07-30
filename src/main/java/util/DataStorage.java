package util;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import model.CalculationResult;
import model.UtilityInput;

import java.io.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;

public class DataStorage {
    private static final String FILE_PATH = "data/utility_data.json";
    private static final Gson gson = new Gson();
    private static final DateTimeFormatter MONTH_FORMATTER = DateTimeFormatter.ofPattern("yyyy_MM");

    // Сохранить данные за текущий месяц
    public static void saveMonthlyData(UtilityInput input, CalculationResult result) throws IOException {
        Map<String, Map<String, Object>> allData = loadAllData();
        String monthKey = LocalDate.now().format(MONTH_FORMATTER); // Единый формат

        Map<String, Object> monthlyData = new HashMap<>();
        monthlyData.put("timestamp", System.currentTimeMillis());
        monthlyData.put("inputs", Map.of(
                "water", input.waterCurrent(),
                "electricity", input.electricityCurrent(),
                "gas", input.gasCurrent()
        ));

        allData.put(monthKey, monthlyData);

        new File("data").mkdirs();
        try (Writer writer = new FileWriter(FILE_PATH)) {
            gson.toJson(allData, writer);
        }
    }

    // Загрузить все данные из файла
    private static Map<String, Map<String, Object>> loadAllData() {
        try (Reader reader = new FileReader(FILE_PATH)) {
            return gson.fromJson(reader, new TypeToken<Map<String, Map<String, Object>>>(){}.getType());
        } catch (Exception e) {
            return new HashMap<>(); // Пустой Map при первом запуске
        }
    }

    // Получить данные за предыдущий месяц
    public static Map<String, Double> getPrevMonthInputs() {
        try {
            String prevMonthKey = LocalDate.now().minusMonths(1).format(MONTH_FORMATTER);
            Map<String, Map<String, Object>> allData = loadAllData();

            if (allData != null && allData.containsKey(prevMonthKey)) {
                return (Map<String, Double>) allData.get(prevMonthKey).get("inputs");
            }
        } catch (Exception e) {
            System.err.println("Ошибка загрузки данных: " + e.getMessage());
        }
        return Collections.emptyMap();
    }

    //Метод для загрузки всех результатов
    public static List<Map<String, Object>> getAllResults() {
        Map<String, Map<String, Object>> allData = loadAllData();
        return new ArrayList<>(allData.values());
    }
}