package com.blasix.danzr.commands.music;

import com.blasix.danzr.commands.ICommand;
import com.blasix.danzr.lavaplayer.GuildMusicManager;
import com.blasix.danzr.lavaplayer.PlayerManager;
import com.blasix.danzr.logic.VoiceLogic;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;

import java.util.List;

public class Pause implements ICommand {
    @Override
    public String getName() {
        return "pause";
    }

    @Override
    public String getDescription() {
        return "Toggles if the music is paused or not";
    }

    @Override
    public List<OptionData> getOptions() {
        return null;
    }

    @Override
    public void execute(SlashCommandInteractionEvent event) {
        if (VoiceLogic.checkConnection(event)) return;

        GuildMusicManager guildMusicManager = PlayerManager.get().getGuildMusicManager(event.getGuild());
        boolean isPaused = guildMusicManager.getTrackScheduler().getPlayer().isPaused();
        guildMusicManager.getTrackScheduler().getPlayer().setPaused(!isPaused);
        if (!isPaused)
            event.reply("The music is paused!").queue();
        else
            event.reply("The music is resumed!").queue();

    }
}
