package gui;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class Database {
    private static final String URL = "jdbc:sqlite:characters.db";

    public static void initialize() {
        try (Connection conn = DriverManager.getConnection(URL);
             Statement stmt = conn.createStatement()) {

            String sql = "CREATE TABLE IF NOT EXISTS characters (" +
                    "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                    "name TEXT," +
                    "class TEXT," +
                    "level INTEGER," +
                    "experience INTEGER," +
                    "gold INTEGER)";
            stmt.executeUpdate(sql);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void saveCharacter(Character character, String classType) {
        String sql = "INSERT INTO characters (name, class, level, experience, gold) VALUES (?, ?, ?, ?, ?)";
        try (Connection conn = DriverManager.getConnection(URL);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, character.getName());
            pstmt.setString(2, classType);
            pstmt.setInt(3, character.getLevel());
            pstmt.setInt(4, character.getExperience());
            pstmt.setInt(5, character.getGold());
            pstmt.executeUpdate();

            System.out.println("Veritabanına kaydedildi: " + character.getName());

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static List<Character> getAllCharacters() {
        List<Character> list = new ArrayList<>();

        String sql = "SELECT * FROM characters";
        try (Connection conn = DriverManager.getConnection(URL);
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                String name = rs.getString("name");
                String classType = rs.getString("class");
                int level = rs.getInt("level");
                int xp = rs.getInt("experience");
                int gold = rs.getInt("gold");

                Character c = CharacterFactory.createCharacter(classType, name);
                c.setLevel(level);
                c.setExperience(xp);
                c.gainGold(gold - 100); // Başlangıç gold 100 kabul edildi

                list.add(c);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return list;
    }
    public static void deleteCharacter(String name) {
        String sql = "DELETE FROM characters WHERE name = ?";
        try (Connection conn = DriverManager.getConnection(URL);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, name);
            pstmt.executeUpdate();
            System.out.println("Silindi: " + name);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void resetCharacter(String name) {
        String sql = "UPDATE characters SET level = 1, experience = 0, gold = 100 WHERE name = ?";
        try (Connection conn = DriverManager.getConnection(URL);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, name);
            pstmt.executeUpdate();
            System.out.println("Sıfırlandı: " + name);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

}
