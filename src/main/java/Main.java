import commands.music.*;
import logic.SelectSong;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.exceptions.InvalidTokenException;

import javax.swing.*;
import java.awt.*;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        String token = null;
        Scanner scanner = null;
        if (args.length > 0) {
            token = args[0];
        } else {
            if (!GraphicsEnvironment.isHeadless()) {
                while (token == null || token.isEmpty()) {
                    token = JOptionPane.showInputDialog("Please enter your bot token:");
                    if (token == null) {
                        System.out.println("Bot token is required to run the application.");
                        System.exit(0);
                    }
                }
            } else {
                scanner = new Scanner(System.in);
                while (token == null || token.isEmpty()) {
                    System.out.println("Please enter your bot token:");
                    token = scanner.nextLine();
                    if (token == null) {
                        System.out.println("Bot token is required to run the application.");
                        System.exit(0);
                    }
                }
            }
        }

        JDA jda = null;
        while (jda == null) {
            try {
                jda = JDABuilder.createDefault(token).build();
            } catch (InvalidTokenException e) {
                if (!GraphicsEnvironment.isHeadless()) {
                    token = JOptionPane.showInputDialog(e.getMessage() + "\n\nPlease try another bot token:");
                    if (token == null || token.isEmpty()) {
                        System.out.println("Bot token is required to run the application.");
                        System.exit(0);
                    }
                } else {
                    System.out.println(e.getMessage() + "\n\nPlease try another bot token:");
                    assert scanner != null;
                    token = scanner.nextLine();
                    if (token == null || token.isEmpty()) {
                        System.out.println("Bot token is required to run the application.");
                        System.exit(0);
                    }
                }
            }
        }

        if (scanner != null) {
            scanner.close();
        }

        CommandManager manager = new CommandManager();
        manager.addCommand(new Play());
        manager.addCommand(new Stop());
        manager.addCommand(new Skip());
        manager.addCommand(new Queue());
        manager.addCommand(new NowPlaying());
        manager.addCommand(new Shuffle());
        manager.addCommand(new Loop());
        manager.addCommand(new Pause());
        jda.addEventListener(manager);
        jda.addEventListener(new SelectSong());
        System.out.println("Bot is ready");
    }
}