package commands.music;

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
        List<AudioTrack> queue = new ArrayList<>(guildMusicManager.getTrackScheduler().getQueue());
        EmbedBuilder embedBuilder = new EmbedBuilder();
        embedBuilder.setTitle("Queue");
        if (queue.isEmpty()) {
            embedBuilder.setDescription("The queue is empty");
            return;
        }
        int i = 1;
        for (AudioTrack track : queue) {
            long hours = TimeUnit.MILLISECONDS.toHours(track.getInfo().length);
            SimpleDateFormat sdf = hours > 0 ? new SimpleDateFormat("hh:mm:ss") : new SimpleDateFormat("mm:ss");
            String formattedLength = sdf.format(new Date(track.getInfo().length));

            embedBuilder.addField(i + ") " + track.getInfo().title, "(" + formattedLength + ") - Song by: " + track.getInfo().author, false);
            i++;
        }
        event.replyEmbeds(embedBuilder.build()).queue();
    }
}
