package gui;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class Database {
    private static final String URL = "jdbc:sqlite:characters.db";

    public static void initialize() {
        try (Connection conn = DriverManager.getConnection(URL);
             Statement stmt = conn.createStatement()) {

            // Karakter tablosu
            String characterSql = "CREATE TABLE IF NOT EXISTS characters (" +
                    "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                    "name TEXT," +
                    "class TEXT," +
                    "level INTEGER," +
                    "experience INTEGER," +
                    "gold INTEGER)";
            stmt.executeUpdate(characterSql);

            // Item tablosu
            String itemSql = "CREATE TABLE IF NOT EXISTS items (" +
                    "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                    "name TEXT UNIQUE," +
                    "value INTEGER)";
            stmt.executeUpdate(itemSql);

            // Karakter-Item ilişki tablosu
            String inventorySql = "CREATE TABLE IF NOT EXISTS character_items (" +
                    "character_name TEXT," +
                    "item_name TEXT)";
            stmt.executeUpdate(inventorySql);

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

                // Envanteri yükle
                c.getInventory().addAll(getInventoryForCharacter(name));
                list.add(c);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    public static void deleteCharacter(String name) {
        try (Connection conn = DriverManager.getConnection(URL)) {
            try (PreparedStatement pstmt1 = conn.prepareStatement("DELETE FROM characters WHERE name = ?")) {
                pstmt1.setString(1, name);
                pstmt1.executeUpdate();
            }
            try (PreparedStatement pstmt2 = conn.prepareStatement("DELETE FROM character_items WHERE character_name = ?")) {
                pstmt2.setString(1, name);
                pstmt2.executeUpdate();
            }
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
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // ITEM VERİ TABANI FONKSİYONLARI

    public static void saveItem(Item item) {
        String sql = "INSERT OR IGNORE INTO items (name, value) VALUES (?, ?)";
        try (Connection conn = DriverManager.getConnection(URL);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, item.getName());
            pstmt.setInt(2, item.getValue());
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static List<Item> getAllItems() {
        List<Item> items = new ArrayList<>();
        String sql = "SELECT * FROM items";
        try (Connection conn = DriverManager.getConnection(URL);
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                items.add(new Item(rs.getString("name"), rs.getInt("value")));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return items;
    }

    public static void assignItemToCharacter(String characterName, String itemName) {
        String sql = "INSERT INTO character_items (character_name, item_name) VALUES (?, ?)";
        try (Connection conn = DriverManager.getConnection(URL);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, characterName);
            pstmt.setString(2, itemName);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static List<Item> getInventoryForCharacter(String characterName) {
        List<Item> list = new ArrayList<>();
        String sql = "SELECT i.name, i.value FROM items i " +
                "JOIN character_items ci ON i.name = ci.item_name " +
                "WHERE ci.character_name = ?";
        try (Connection conn = DriverManager.getConnection(URL);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, characterName);
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                list.add(new Item(rs.getString("name"), rs.getInt("value")));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    public static void removeItemFromCharacter(String characterName, String itemName) {
        String sql = "DELETE FROM character_items WHERE character_name = ? AND item_name = ?";
        try (Connection conn = DriverManager.getConnection(URL);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, characterName);
            pstmt.setString(2, itemName);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
