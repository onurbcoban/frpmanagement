package gui;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

// SQLite veritabanı işlemlerini yöneten yardımcı sınıf
public class Database {
    private static final String URL = "jdbc:sqlite:characters.db";

    // Uygulama başlarken tablo yoksa oluşturur
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
    
    public static void updateCharacter(Character c) {
        String sql = "UPDATE characters SET level = ?, experience = ?, gold = ? WHERE name = ?";

        try (Connection conn = DriverManager.getConnection(URL);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, c.getLevel());
            pstmt.setInt(2, c.getExperience());
            pstmt.setInt(3, c.getGold());
            pstmt.setString(4, c.getName());

            pstmt.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    // Karakter veritabanına eklenir
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

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Veritabanındaki tüm karakterleri liste olarak döner
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
                c.setGold(gold);
                list.add(c);

            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return list;
    }

    // Belirtilen isme sahip karakteri veritabanından siler
    public static void deleteCharacter(String name) {
        String sql = "DELETE FROM characters WHERE name = ?";
        try (Connection conn = DriverManager.getConnection(URL);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, name);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Karakterin XP, level ve gold değerlerini sıfırlar
    public static void resetCharacter(String name) {
        String sql = "UPDATE characters SET level = 1, experience = 0, gold = 100 WHERE name = ?";
        try (Connection conn = DriverManager.getConnection(URL);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, name);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}



		