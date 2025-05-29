import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.*;
import java.util.HashMap;
import java.util.Map;

public class DataStorage {
    private static final String FILE_PATH = "data/utility_data.json";
    private static final Gson gson = new Gson();

    public static void saveValues(double water, double electricity, double gas) throws IOException {
        Map<String, Double> data = new HashMap<>();
        data.put("water", water);
        data.put("electricity", electricity);
        data.put("gas", gas);

        new File("data").mkdirs();
        try (Writer writer = new FileWriter(FILE_PATH)) {
            gson.toJson(data, writer);
        }
    }

    public static Map<String, Double> loadValues() {
    try (Reader reader = new FileReader(FILE_PATH)) {
        return gson.fromJson(reader, new TypeToken<Map<String, Double>>(){}.getType());
    } catch (Exception e) {
        return Map.of(
                "water", 0.0,
                "electricity", 0.0,
                "gas", 0.0
        );
    }
}
}