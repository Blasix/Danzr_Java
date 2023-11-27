package com.blasix.danzr.commands.music;

import com.blasix.danzr.logic.VoiceLogic;
import com.blasix.danzr.commands.ICommand;
import com.blasix.danzr.lavaplayer.GuildMusicManager;
import com.blasix.danzr.lavaplayer.PlayerManager;
import com.blasix.danzr.lavaplayer.TrackScheduler;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;

import java.util.List;

public class Shuffle implements ICommand {
    @Override
    public String getName() {
        return "shuffle";
    }

    @Override
    public String getDescription() {
        return "Shuffle the queue";
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
        TrackScheduler trackScheduler = guildMusicManager.getTrackScheduler();
        trackScheduler.shuffleQueue();
        event.reply("Shuffled the queue").queue();
    }
}
