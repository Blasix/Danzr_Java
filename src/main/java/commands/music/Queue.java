package commands.music;

import logic.SongInfo;
import logic.VoiceLogic;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import commands.ICommand;
import lavaplayer.GuildMusicManager;
import lavaplayer.PlayerManager;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

// TODO - Add pagination
// https://www.youtube.com/watch?v=IuTW7bGVK5o&list=PLMDWhd7MfizXOJXn905x8UqkWtMJ6tl-b&index=6

public class Queue implements ICommand {
    @Override
    public String getName() {
        return "queue";
    }

    @Override
    public String getDescription() {
        return "Check the queue";
    }

    @Override
    public List<OptionData> getOptions() {
        return null;
    }

    @Override
    public void execute(SlashCommandInteractionEvent event) {
        if (VoiceLogic.checkConnection(event)) return;

        GuildMusicManager guildMusicManager = PlayerManager.get().getGuildMusicManager(event.getGuild());
        List<SongInfo> queue = new ArrayList<>(guildMusicManager.getTrackScheduler().getQueue());
        EmbedBuilder embedBuilder = new EmbedBuilder();
        embedBuilder.setTitle("Queue");
        if (queue.isEmpty()) {
            embedBuilder.setDescription("The queue is empty");
            return;
        }
        int i = 1;
        for (SongInfo songInfo : queue) {
            long hours = TimeUnit.MILLISECONDS.toHours(songInfo.getTrack().getInfo().length);
            SimpleDateFormat sdf = hours > 0 ? new SimpleDateFormat("hh:mm:ss") : new SimpleDateFormat("mm:ss");
            String formattedLength = sdf.format(new Date(songInfo.getTrack().getInfo().length));

            embedBuilder.addField(i + ") " + songInfo.getTrack().getInfo().title, "(" + formattedLength + ") - Requested by: " + songInfo.getRequester().getAsMention(), false);
            i++;
        }
        event.replyEmbeds(embedBuilder.build()).queue();
    }
}
