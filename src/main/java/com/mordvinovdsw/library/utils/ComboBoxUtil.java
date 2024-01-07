package com.mordvinovdsw.library.utils;

import com.mordvinovdsw.library.Database.DBConnection;
import com.mordvinovdsw.library.models.Book;
import com.mordvinovdsw.library.models.IdentifiableItem;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ListCell;
import javafx.scene.paint.Color;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

public class ComboBoxUtil {
    public static void fillBookSearchOptions(ComboBox<String> comboBox) {
        comboBox.setItems(FXCollections.observableArrayList(
                "Book ID", "Book Name", "Book Genre", "Book ISBN"
        ));
    }

    public static void fillMemberSearchOptions(ComboBox<String> comboBox) {
        comboBox.setItems(FXCollections.observableArrayList(
                "Member ID", "Member Name", "Member Phone Number", "Member Email",
                "Registration Date", "Date Expire", "Status"
        ));
    }

    public static void fillIssueSearchOptions(ComboBox<String> comboBox) {
        comboBox.setItems(FXCollections.observableArrayList(
                "Issue ID", "Book ID", "Member ID", "Issue Date", "Return Date", "Status"
        ));
    }

    public static void fillMemberComboBox(ComboBox<IdentifiableItem> comboBox) {
        ObservableList<IdentifiableItem> members = FXCollections.observableArrayList();
        String sql = "SELECT Member_ID, Member_Name FROM Members";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {

            while (rs.next()) {
                int memberId = rs.getInt("Member_ID");
                String memberName = rs.getString("Member_Name");
                members.add(new IdentifiableItem(memberId, memberName));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        comboBox.setItems(members);
    }

    public static void fillBookComboBox(ComboBox<IdentifiableItem> comboBox) {
        ObservableList<IdentifiableItem> items = FXCollections.observableArrayList();
        String sql = "SELECT Book_Id, Book_Title, Book_Numbers FROM Book_List";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {

            while (rs.next()) {
                int bookId = rs.getInt("Book_Id");
                String bookTitle = rs.getString("Book_Title");
                items.add(new IdentifiableItem(bookId, bookTitle));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        comboBox.setItems(items);
    }
}

