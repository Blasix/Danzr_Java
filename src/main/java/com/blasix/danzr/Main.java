package com.blasix.danzr;

import com.blasix.danzr.commands.music.*;
import com.blasix.danzr.logic.SelectSong;
import net.dv8tion.jda.api.*;

public class Main {
    public static void main(String[] args) {
        JDA jda = JDABuilder.createDefault(Secrets.getToken()).build();
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