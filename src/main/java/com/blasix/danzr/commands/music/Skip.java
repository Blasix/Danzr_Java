package com.blasix.danzr.commands.music;

import com.blasix.danzr.logic.VoiceLogic;
import com.blasix.danzr.commands.ICommand;
import com.blasix.danzr.lavaplayer.GuildMusicManager;
import com.blasix.danzr.lavaplayer.PlayerManager;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;

import java.util.List;

public class Skip implements ICommand {
    @Override
    public String getName() {
        return "skip";
    }

    @Override
    public String getDescription() {
        return "Skips the current song";
    }

    @Override
    public List<OptionData> getOptions() {
        return List.of(
                new OptionData(OptionType.INTEGER, "amount", "the amount of songs to skip (default = 1)")
        );
    }

    @Override
    public void execute(SlashCommandInteractionEvent event) {
        if (VoiceLogic.checkConnection(event)) return;

        int amount = 1;
        try {
            amount = event.getOption("amount").getAsInt();
        } catch (NullPointerException ignored) {}


        if (amount < 1) {
            amount = 1;
            return;
        }


        GuildMusicManager guildMusicManager = PlayerManager.get().getGuildMusicManager(event.getGuild());

        if (amount > guildMusicManager.getTrackScheduler().getQueue().size()) {
            event.reply("There are not enough songs in the queue").queue();
            return;
        }

        if (guildMusicManager.getTrackScheduler().getPlayer().getPlayingTrack() == null) {
            event.reply("There is no song playing").queue();
            return;
        }

        int i = amount;
        while (i  > 0) {
            guildMusicManager.getTrackScheduler().getPlayer().stopTrack();
            i--;
        }
        if (amount == 1) event.reply("Skipped the current song!").queue();
        else event.reply("Skipped " + amount + " songs!").queue();
    }
}
