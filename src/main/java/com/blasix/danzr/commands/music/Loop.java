package com.blasix.danzr.commands.music;

import com.blasix.danzr.logic.VoiceLogic;
import com.blasix.danzr.commands.ICommand;
import com.blasix.danzr.lavaplayer.GuildMusicManager;
import com.blasix.danzr.lavaplayer.PlayerManager;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;

import java.util.List;

public class Loop implements ICommand {
    @Override
    public String getName() {
        return "loop";
    }

    @Override
    public String getDescription() {
        return "Toggle looping of the queue";
    }

    @Override
    public List<OptionData> getOptions() {
        return null;
    }

    @Override
    public void execute(SlashCommandInteractionEvent event) {
        if (VoiceLogic.checkConnection(event)) return;

        GuildMusicManager guildMusicManager = PlayerManager.get().getGuildMusicManager(event.getGuild());
        if (guildMusicManager.getTrackScheduler().getPlayer().getPlayingTrack() == null) {
            event.reply("There are no songs in the queue").queue();
            return;
        }
        guildMusicManager.getTrackScheduler().toggleLooping();
        event.reply("Looping is now: " + guildMusicManager.getTrackScheduler().isLooping()).queue();
    }
}
