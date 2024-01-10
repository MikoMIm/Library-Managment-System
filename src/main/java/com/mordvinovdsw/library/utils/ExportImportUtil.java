package com.mordvinovdsw.library.utils;

import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.channels.FileChannel;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.sql.*;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public class ExportImportUtil {

    private static final String EXPECTED_STRUCTURE =
            "CREATE TABLE Authors (Author_Id INTEGER PRIMARY KEY AUTOINCREMENT, Author_Name TEXT NOT NULL);" +
                    "CREATE TABLE Book_Authors (Book_Id INTEGER, Author_Id INTEGER, PRIMARY KEY (Book_Id, Author_Id), FOREIGN KEY (Book_Id) REFERENCES Book_List (Book_Id), FOREIGN KEY (Author_Id) REFERENCES Authors (Author_Id));" +
                    "CREATE TABLE Book_Genres (Book_Id INTEGER, Genre_Id INTEGER, PRIMARY KEY (Book_Id, Genre_Id), FOREIGN KEY (Book_Id) REFERENCES Book_List (Book_Id), FOREIGN KEY (Genre_Id) REFERENCES Genres (Genre_Id));" +
                    "CREATE TABLE Book_Issue (Issue_Id INTEGER PRIMARY KEY AUTOINCREMENT, Book_Id INTEGER NOT NULL, Member_Id INTEGER NOT NULL, Issue_Date DATE NOT NULL, Return_Date DATE, Status TEXT NOT NULL, FOREIGN KEY (Book_Id) REFERENCES Book_List (Book_Id), FOREIGN KEY (Member_Id) REFERENCES Members (Member_Id));" +
                    "CREATE TABLE Book_List (Book_Id INTEGER PRIMARY KEY AUTOINCREMENT, Book_Title TEXT, Book_Numbers INTEGER, ISBN_10 TEXT, ISBN_13 TEXT);" +
                    "CREATE TABLE Genres (Genre_Id INTEGER PRIMARY KEY AUTOINCREMENT, Genre_Name TEXT NOT NULL);" +
                    "CREATE TABLE Members (Member_ID INTEGER PRIMARY KEY AUTOINCREMENT, Member_Name TEXT, Phone_Number TEXT, Email_adress TEXT, Register_Date TEXT, Date_Expair TEXT, Member_Status TEXT);" +
                    "CREATE TABLE users (Id INTEGER PRIMARY KEY AUTOINCREMENT, login TEXT NOT NULL UNIQUE, password TEXT NOT NULL);";

    public void exportDatabase(String sourceFilePath) {
        if (checkDatabaseStructure(sourceFilePath)) {
            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("Save Database File");
            fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("DB Files", "*.db"));
            File selectedFile = fileChooser.showSaveDialog(new Stage());

            if (selectedFile != null) {
                copyFile(sourceFilePath, selectedFile.getAbsolutePath());
                DialogUtil.showDialog("Success", "Database exported successfully to " + selectedFile.getAbsolutePath());
            }
        } else {
            DialogUtil.showError("The database structure does not match the expected structure.");
        }
    }

    private boolean checkDatabaseStructure(String sourceFilePath) {
        try (Connection conn = DriverManager.getConnection("jdbc:sqlite:" + sourceFilePath);
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT name FROM sqlite_master WHERE type='table' AND name NOT LIKE 'sqlite_%'")) {

            Set<String> expectedTables = new HashSet<>(Arrays.asList("Authors", "Book_Authors", "Book_Genres", "Book_Issue", "Book_List", "Genres", "Members", "users"));
            Set<String> actualTables = new HashSet<>();

            while (rs.next()) {
                actualTables.add(rs.getString("name"));
            }

            return actualTables.equals(expectedTables);

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    private void copyFile(String sourceFilePath, String destinationFilePath) {
        File sourceFile = new File(sourceFilePath);
        File destinationFile = new File(destinationFilePath);

        try (FileChannel sourceChannel = new FileInputStream(sourceFile).getChannel();
             FileChannel destChannel = new FileOutputStream(destinationFile).getChannel()) {
            destChannel.transferFrom(sourceChannel, 0, sourceChannel.size());
            System.out.println("Database exported successfully to " + destinationFilePath);
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Error exporting database");
        }
    }

    public void importDatabase(String destinationFilePath) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Open Database File");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("DB Files", "*.db"));
        File selectedFile = fileChooser.showOpenDialog(new Stage());

        if (selectedFile != null) {
            if (checkDatabaseStructure(selectedFile.getAbsolutePath())) {
                replaceDatabaseFile(selectedFile, new File(destinationFilePath));
                DialogUtil.showDialog("Restart Required", "The application will now restart to apply changes.");
                LoginScreenUtil.openLoginScreen();
            } else {
                System.out.println("The database structure of the file to be imported does not match the expected structure.");
            }
        }
    }

    private void replaceDatabaseFile(File sourceFile, File destinationFile) {
        try {
            if (destinationFile.exists()) {
                String backupFileName = destinationFile.getAbsolutePath() + ".bak";
                File backupFile = new File(backupFileName);
                Files.copy(destinationFile.toPath(), backupFile.toPath(), StandardCopyOption.REPLACE_EXISTING);
                DialogUtil.showDialog("Backup Created", "A backup of the current database has been created: " + backupFileName);
            }

            Files.copy(sourceFile.toPath(), destinationFile.toPath(), StandardCopyOption.REPLACE_EXISTING);
            DialogUtil.showDialog("Success", "Database has been imported and renamed to 'library.db' successfully.");

        } catch (IOException e) {
            DialogUtil.showError("Error importing database: " + e.getMessage());
        }
    }

    public void createNewDatabase(String filePath) {
        try {
            Path backupFolderPath = Paths.get("backup");
            if (!Files.exists(backupFolderPath)) {
                Files.createDirectory(backupFolderPath);
            }
            Path sourceFilePath = Paths.get(filePath);
            if (Files.exists(sourceFilePath)) {
                Path backupFilePath = backupFolderPath.resolve(sourceFilePath.getFileName());
                Files.copy(sourceFilePath, backupFilePath, StandardCopyOption.REPLACE_EXISTING);
            }
            Files.deleteIfExists(sourceFilePath);
            try (Connection conn = DriverManager.getConnection("jdbc:sqlite:" + filePath)) {
                if (conn != null) {
                    try (Statement stmt = conn.createStatement()) {
                        String[] tableCreationStatements = EXPECTED_STRUCTURE.split(";");
                        for (String sql : tableCreationStatements) {
                            if (!sql.trim().isEmpty()) {
                                stmt.execute(sql);
                            }
                        }
                        DialogUtil.showDialog("Success", "New database created successfully at " + filePath);
                    }
                }
            }
        } catch (IOException | SQLException e) {
            DialogUtil.showError("Error creating new database: " + e.getMessage());
            e.printStackTrace();
        }
    }
}

