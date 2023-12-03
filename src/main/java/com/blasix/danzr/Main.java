package com.blasix.danzr;

import com.blasix.danzr.commands.music.*;
import com.blasix.danzr.logic.SelectSong;
import net.dv8tion.jda.api.*;

import java.io.*;
import java.util.Map;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) throws IOException {
        String token;
        if (args.length > 0) {
            token = args[0];
        } else {
            try {
                Scanner scanner = new Scanner(new File("src/main/resources/config.txt"));
                token = scanner.nextLine().split("=")[1];
            } catch (FileNotFoundException e) {
                System.out.println("Please enter your bot token: ");
                Scanner scanner = new Scanner(System.in);
                token = scanner.nextLine();
                Writer writer = new FileWriter("src/main/resources/config.txt");
                writer.write("TOKEN=" + token);
                writer.close();
            }
        }

        JDA jda = JDABuilder.createDefault(token).build();
        CommandManager manager = new CommandManager();
        manager.addCommand(new Play());
        manager.addCommand(new Stop());
        manager.addCommand(new Skip());
        manager.addCommand(new Queue());
        manager.addCommand(new NowPlaying());
        manager.addCommand(new Shuffle());
        manager.addCommand(new Loop());
        jda.addEventListener(manager);
        jda.addEventListener(new SelectSong());
        System.out.println("Bot is ready");
    }
}