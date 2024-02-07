package commands.music;

import commands.ICommand;
import lavaplayer.*;
import logic.VoiceLogic;
import net.dv8tion.jda.api.EmbedBuilder;
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

        EmbedBuilder embedBuilder = new EmbedBuilder();
        embedBuilder.setColor(0x000082);

        GuildMusicManager guildMusicManager = PlayerManager.get().getGuildMusicManager(event.getGuild());
        if (guildMusicManager.getTrackScheduler().getPlayer().getPlayingTrack() == null) {
            embedBuilder.setColor(0x820000);
            embedBuilder.setTitle("There are no songs in the queue");
            event.replyEmbeds(embedBuilder.build()).queue();
            return;
        }
        TrackScheduler trackScheduler = guildMusicManager.getTrackScheduler();
        trackScheduler.shuffleQueue();
        embedBuilder.setTitle("Shuffled the queue");
        event.replyEmbeds(embedBuilder.build()).queue();
    }
}
