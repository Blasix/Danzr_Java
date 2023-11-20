package lavaplayer;

import com.sedmelluq.discord.lavaplayer.player.AudioLoadResultHandler;
import com.sedmelluq.discord.lavaplayer.player.AudioPlayerManager;
import com.sedmelluq.discord.lavaplayer.player.DefaultAudioPlayerManager;
import com.sedmelluq.discord.lavaplayer.source.AudioSourceManagers;
import com.sedmelluq.discord.lavaplayer.tools.FriendlyException;
import com.sedmelluq.discord.lavaplayer.track.AudioPlaylist;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import logic.VoiceLogic;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;

import java.util.*;

public class PlayerManager {

    private static PlayerManager INSTANCE;
    private final Map<Long, GuildMusicManager> guildMusicManagers = new HashMap<>();
    private final AudioPlayerManager audioPlayerManager = new DefaultAudioPlayerManager();

    private PlayerManager() {
        AudioSourceManagers.registerRemoteSources(audioPlayerManager);
        AudioSourceManagers.registerLocalSource(audioPlayerManager);
    }

    public static PlayerManager get() {
        if (INSTANCE == null) {
            INSTANCE = new PlayerManager();
        }
        return INSTANCE;
    }

    public GuildMusicManager getGuildMusicManager(Guild guild) {
        return guildMusicManagers.computeIfAbsent(guild.getIdLong(), (guildId) -> {
            GuildMusicManager musicManager = new GuildMusicManager(audioPlayerManager);
            guild.getAudioManager().setSendingHandler(musicManager.getAudioForwarder());
            return musicManager;
        });
    }

    public void play(Guild guild, String trackURL, SlashCommandInteractionEvent event) {
        GuildMusicManager guildMusicManager = getGuildMusicManager(guild);
        audioPlayerManager.loadItemOrdered(guildMusicManager, trackURL, new AudioLoadResultHandler() {
            @Override
            public void trackLoaded(AudioTrack track) {
                guildMusicManager.getTrackScheduler().queue(track, event);
                event.replyEmbeds(VoiceLogic.createSongAddedEmbed(track.getInfo(), event)).queue();
            }

            @Override
            public void playlistLoaded(AudioPlaylist playlist) {
                if (playlist.isSearchResult()) {
                    // TODO: add menu for selecting track
                    guildMusicManager.getTrackScheduler().queue(playlist.getTracks().get(0), event);
                    event.replyEmbeds(VoiceLogic.createSongAddedEmbed(playlist.getTracks().get(0).getInfo(), event)).queue();
                    return;
                }
                for (AudioTrack track : playlist.getTracks()) {
                    guildMusicManager.getTrackScheduler().queue(track, event);
                }
                EmbedBuilder embedBuilder = new EmbedBuilder();
                embedBuilder.setTitle("Playlist added to queue");
                embedBuilder.setDescription("Added " + playlist.getTracks().size() + " songs to the queue");
                embedBuilder.setColor(0x008200);
                event.replyEmbeds(embedBuilder.build()).queue();
            }

            @Override
            public void noMatches() {
                EmbedBuilder embedBuilder = new EmbedBuilder();
                embedBuilder.setTitle("Nothing found with " + trackURL);
                embedBuilder.setColor(0x820000);
                event.replyEmbeds(embedBuilder.build()).queue();
            }

            @Override
            public void loadFailed(FriendlyException exception) {
                EmbedBuilder embedBuilder = new EmbedBuilder();
                embedBuilder.setTitle(exception.getMessage());
                embedBuilder.setColor(0x820000);
                event.replyEmbeds(embedBuilder.build()).queue();
            }
        });
    }
}
