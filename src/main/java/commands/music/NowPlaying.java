package commands.music;

import commands.ICommand;
import lavaplayer.GuildMusicManager;
import lavaplayer.PlayerManager;
import logic.VoiceLogic;
import com.sedmelluq.discord.lavaplayer.track.AudioTrackInfo;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.TimeUnit;

public class NowPlaying implements ICommand {
    @Override
    public String getName() {
        return "nowplaying";
    }

    @Override
    public String getDescription() {
        return "Check what song is currently playing";
    }

    @Override
    public void execute(SlashCommandInteractionEvent event) {
        if (VoiceLogic.checkConnection(event)) return;

        GuildMusicManager guildMusicManager = PlayerManager.get().getGuildMusicManager(event.getGuild());
        if (guildMusicManager.getTrackScheduler().getPlayer().getPlayingTrack() == null) {
            event.reply("There is no song playing").queue();
            return;
        }
        AudioTrackInfo trackInfo = guildMusicManager.getTrackScheduler().getPlayer().getPlayingTrack().getInfo();
        User nowPlayingUser = guildMusicManager.getTrackScheduler().getNowPlayingUser();
        EmbedBuilder embedBuilder = new EmbedBuilder();
        embedBuilder.setTitle("Now Playing");

        long hours = TimeUnit.MILLISECONDS.toHours(trackInfo.length);
        SimpleDateFormat sdf = hours > 0 ? new SimpleDateFormat("hh:mm:ss") : new SimpleDateFormat("mm:ss");
        String formattedLength = sdf.format(new Date(trackInfo.length));

        embedBuilder.setDescription("**Name:** `" + trackInfo.title + "`\n**Author:** `" + trackInfo.author + "`\n**Duration:** `" + formattedLength + "`");
        embedBuilder.setThumbnail("https://img.youtube.com/vi/" + trackInfo.identifier + "/0.jpg");
        embedBuilder.setFooter("Requested by " + nowPlayingUser.getEffectiveName(), nowPlayingUser.getAvatarUrl());
        embedBuilder.setColor(0x000082);
        event.replyEmbeds(embedBuilder.build()).queue();
    }
}
