package lavaplayer;

import logic.SongInfo;
import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;
import com.sedmelluq.discord.lavaplayer.player.event.AudioEventAdapter;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import com.sedmelluq.discord.lavaplayer.track.AudioTrackEndReason;
import net.dv8tion.jda.api.entities.User;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.BlockingDeque;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingDeque;

public class TrackScheduler extends AudioEventAdapter {
    private final AudioPlayer player;
    private BlockingDeque<SongInfo> queue = new LinkedBlockingDeque<>();
    private User nowPlayingUser = null;
    private boolean isLooping = false;

    public TrackScheduler(AudioPlayer player) {
        this.player = player;
    }

    @Override
    public void onTrackEnd(AudioPlayer player, AudioTrack track, AudioTrackEndReason endReason) {
        SongInfo q = queue.poll();
        player.startTrack(q.getTrack(), false);
        nowPlayingUser = q.getRequester();
        if (isLooping) {
            queue.offer(q);
        }
    }
    public void queue(AudioTrack track, User user, boolean priority) {
        if (!player.startTrack(track, true)) {
            if (priority) queue.offerFirst(new SongInfo(track, user));
            else queue.offer(new SongInfo(track, user));
        } else {
            nowPlayingUser = user;
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
        queue = new LinkedBlockingDeque<>(queueCopy);
    }
}
