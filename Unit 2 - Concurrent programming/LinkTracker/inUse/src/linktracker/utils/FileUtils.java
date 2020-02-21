package linktracker.utils;

import linktracker.model.WebPage;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;

/**
 * Class to handle files
 */
public class FileUtils {

    /**
     * Method to read a file and get the webs
     * @param file (path of a file)
     * @return List<WebPage> (webs list)
     */
    public static List<WebPage> loadPages(Path file) {

        List<WebPage> list = new ArrayList<WebPage>();
        try {
            BufferedReader reader = Files.newBufferedReader(file, StandardCharsets.UTF_8);
            String line;
            while((line = reader.readLine()) != null) {
                String[] parts = line.split(";");
                String name = parts[0];
                String url = parts[1];
                WebPage web = new WebPage(name,url);
                list.add(web);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return list;
    }
}
