package lavaplayer;

import dev.lavalink.youtube.YoutubeAudioSourceManager;
import logic.SelectSong;
import logic.VoiceLogic;
import com.sedmelluq.discord.lavaplayer.player.AudioLoadResultHandler;
import com.sedmelluq.discord.lavaplayer.player.AudioPlayerManager;
import com.sedmelluq.discord.lavaplayer.player.DefaultAudioPlayerManager;
import com.sedmelluq.discord.lavaplayer.source.AudioSourceManagers;
import com.sedmelluq.discord.lavaplayer.tools.FriendlyException;
import com.sedmelluq.discord.lavaplayer.track.AudioPlaylist;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;

import java.util.HashMap;
import java.util.Map;

public class PlayerManager {

    private static PlayerManager INSTANCE;
    private final Map<Long, GuildMusicManager> guildMusicManagers = new HashMap<>();
    private final AudioPlayerManager audioPlayerManager = new DefaultAudioPlayerManager();
    YoutubeAudioSourceManager ytSourceManager = new YoutubeAudioSourceManager();

    private PlayerManager() {
        ytSourceManager.useOauth2(null, false);

        audioPlayerManager.registerSourceManager(ytSourceManager);
        AudioSourceManagers.registerRemoteSources(audioPlayerManager,
                com.sedmelluq.discord.lavaplayer.source.youtube.YoutubeAudioSourceManager.class);
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

    public void play(Guild guild, String trackURL, SlashCommandInteractionEvent event, boolean priority, boolean shuffle) {
        GuildMusicManager guildMusicManager = getGuildMusicManager(guild);
        audioPlayerManager.loadItemOrdered(guildMusicManager, trackURL, new AudioLoadResultHandler() {
            @Override
            public void trackLoaded(AudioTrack track) {
                guildMusicManager.getTrackScheduler().queue(track, event.getUser(), priority, shuffle);
                event.replyEmbeds(VoiceLogic.createSongAddedEmbed(track.getInfo(), event.getUser(), event.getGuild())).queue();
            }

            @Override
            public void playlistLoaded(AudioPlaylist playlist) {
                if (playlist.isSearchResult()) {
                    SelectSong.guildMusicManager = guildMusicManager;
                    SelectSong.playlist = playlist;
                    SelectSong.priority = priority;
                    SelectSong.shuffle = shuffle;
                    SelectSong.displayMenu(event);
                    return;
                }
                for (AudioTrack track : playlist.getTracks()) {
                    guildMusicManager.getTrackScheduler().queue(track, event.getUser(), priority, shuffle);
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
