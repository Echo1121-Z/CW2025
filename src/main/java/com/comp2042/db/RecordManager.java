package com.comp2042.db;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class RecordManager {

    private static final String CHARSET = "UTF-8";

    // get current directory path
    private static String getWritableBasePath() {
        return "./data/";
    }

    /**
     * write a number which type is long to file
     *
     * @param fileName name of the file to write
     * @param value long-type of content
     */
    public static boolean saveLongToFile(String fileName, long value) {
        String basePath = getWritableBasePath();
        //System.out.printf("basePath: %s\n", basePath);
        String filePath = basePath + fileName;
        Path path = Paths.get(filePath);
        Path parent = path.getParent();
        try {
            if (parent != null && !Files.exists(parent)) {
                Files.createDirectories(parent);
            }

            // use try-with-resources for closed correctly
            try (OutputStream os = Files.newOutputStream(path, StandardOpenOption.CREATE,
                    StandardOpenOption.TRUNCATE_EXISTING);
                 OutputStreamWriter dos = new OutputStreamWriter(os, CHARSET);
                 BufferedWriter writer = new BufferedWriter(dos)) {
                writer.write(String.valueOf(value));
                // current time
                LocalDateTime now = LocalDateTime.now();
                // time formatter
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd:HH-mm-ss");
                String formattedTime = now.format(formatter);
                //System.out.printf("write %s, time: %s\n", value, formattedTime);
                return true;
            }
        } catch (Exception e) {
            System.out.printf("write file: %s failed, error: %s\n", filePath, e.getMessage());
        }

        // try-with-resources dos.close() and os.close() would called automatically
        return false;
    }

    /**
     * read a long score from file
     *
     * @param fileName file name
     * @return the score which type is long
     */
    public static long readLongFromFile(String fileName) {
        String basePath = getWritableBasePath();
        System.out.printf("basePath: %s\n", basePath);
        // ensure directory of parent exists
        String filePath = basePath + fileName;
        Path path = Paths.get(filePath);
        if (!Files.exists(path)) {
            System.err.printf("file: %s not exist.\n", filePath);
            return 0;
        }
        // use try-with-resources
        try (InputStream is = Files.newInputStream(path);
             InputStreamReader dis = new InputStreamReader(is, CHARSET);
             BufferedReader reader = new BufferedReader(dis)) {
            StringBuilder content = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                content.append(line).append(System.lineSeparator());
            }

            if (!content.isEmpty()) {
                content.setLength(content.length() - System.lineSeparator().length());
            }

            return Long.parseLong(content.toString());
        } catch (Exception e) {
            System.out.printf("read score from: %s error: %s\n", filePath, e.getMessage());
        }
        return 0;
    }
}
