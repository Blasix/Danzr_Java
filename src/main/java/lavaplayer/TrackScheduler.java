package lavaplayer;

import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;
import com.sedmelluq.discord.lavaplayer.player.event.AudioEventAdapter;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import com.sedmelluq.discord.lavaplayer.track.AudioTrackEndReason;
import logic.SongInfo;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;

import java.util.*;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

public class TrackScheduler extends AudioEventAdapter {
    private final AudioPlayer player;
    private BlockingQueue<SongInfo> queue = new LinkedBlockingQueue<>();
    private User nowPlayingUser = null;
    private boolean isLooping = false;

    public TrackScheduler(AudioPlayer player) {
        this.player = player;
    }

    @Override
    public void onTrackEnd(AudioPlayer player, AudioTrack track, AudioTrackEndReason endReason) {
        if (isLooping) {
            player.startTrack(track.makeClone(), false);
        } else {
            SongInfo q = queue.poll();
            player.startTrack(q.getTrack(), false);
            nowPlayingUser = q.getRequester();
        }
    }
    public void queue(AudioTrack track, SlashCommandInteractionEvent event) {
        if (!player.startTrack(track, true)) {
            queue.offer(new SongInfo(track, event.getUser()));
        } else {
            nowPlayingUser = event.getUser();
        }
    }

    public AudioPlayer getPlayer() {
        return player;
    }

    public BlockingQueue<SongInfo> getQueue() {
        return queue;
    }

    public void toggleLooping() {
        isLooping = !isLooping;
    }

    public boolean isLooping() {
        return isLooping;
    }

    public User getNowPlayingUser() {
        return nowPlayingUser;
    }

    public void shuffleQueue() {
        List<SongInfo> queueCopy = new ArrayList<>(this.queue);
        Collections.shuffle(queueCopy);
        queue = new LinkedBlockingQueue<>(queueCopy);
    }
}
