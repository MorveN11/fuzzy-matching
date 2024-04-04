package fuzzy.matching.util;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import fuzzy.matching.model.Book;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.lang.reflect.Type;
import java.util.List;

/**
 * A utility class for reading JSON files and parsing them into a list of objects.
 *
 * @param <T> the type of objects to parse from the JSON file
 */
public class JsonReader<T> {

    /**
     * Parses a JSON file into a list of objects.
     *
     * @param path  the path to the JSON file
     * @param clazz the class of the objects to parse
     * @return a list of objects parsed from the JSON file
     */
    public List<T> parseJsonToList(String path, Class<T> clazz) {
        List<T> items = null;
        try (Reader reader = new FileReader(path)) {
            Gson gson = new Gson();
            Type listType = TypeToken.getParameterized(List.class, clazz).getType();
            items = gson.fromJson(reader, listType);
            for (T item : items) {
                System.out.println(item);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return items;
    }
}
