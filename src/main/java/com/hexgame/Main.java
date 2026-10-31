package com.hexgame;
import javax.swing.*; // Imports Java's standard GUI toolkit components
import java.awt.event.KeyEvent;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.HashSet;
import java.util.Properties;
import java.util.Set;

public class Main {

    public static void main(String[] args) { // The main application entry loop
        Properties config = new Properties(); // Optional settings file next to the game
        try (FileInputStream in = new FileInputStream("config.properties")) {
            config.load(in);
        } catch (IOException e) {
            // File missing: fall back to the defaults below
        }

        HexGrid.setViewSize(
            parseInt(config.getProperty("screen.width"), 980),
            parseInt(config.getProperty("screen.height"), 750)
        );
        HexGrid.setCameraKeys(
            parseKeys(config.getProperty("camera.up", "W UP")),
            parseKeys(config.getProperty("camera.down", "S DOWN")),
            parseKeys(config.getProperty("camera.left", "A LEFT")),
            parseKeys(config.getProperty("camera.right", "D RIGHT"))
        );

        // Always create Swing UI on the Event Dispatch Thread to prevent graphical or threading glitches
        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame("4X Strategy – Hex Map"); // Creates the main OS-level window frame with a custom title
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // Program fully terminates when clicking the 'X' button
            frame.setResizable(false); // Disables resizing to keep your specific layout dimensions intact

            MainMenu menu = new MainMenu(frame); // Creates the new start menu
            frame.add(menu); // Attaches the menu layout inside the window frame

            frame.pack(); // Sizes the frame window automatically so the Menu fits with no extra border space
            frame.setLocationRelativeTo(null); // Centers the completed game window perfectly in the middle of the user's monitor
            frame.setVisible(true); // Commands the OS to display the window graphically on screen
        });
    }

    private static int parseInt(String value, int fallback) {
        try {
            return value == null ? fallback : Integer.parseInt(value.trim());
        } catch (NumberFormatException e) {
            System.err.println("Invalid number in config: " + value);
            return fallback;
        }
    }

    // Turns "W UP" into the matching KeyEvent.VK_* codes
    private static Set<Integer> parseKeys(String value) {
        Set<Integer> result = new HashSet<>();
        for (String name : value.trim().split("\\s+")) {
            try {
                result.add(KeyEvent.class.getField("VK_" + name.toUpperCase()).getInt(null));
            } catch (Exception e) {
                System.err.println("Unknown key name in config: " + name);
            }
        }
        return result;
    }
}
