package com.mordvinovdsw.library.utils;

import java.nio.file.Files;
import java.nio.file.Paths;

public class DatabaseUtils {
    public static boolean doesDatabaseExist(String databasePath) {
        return Files.exists(Paths.get(databasePath));
    }
}
