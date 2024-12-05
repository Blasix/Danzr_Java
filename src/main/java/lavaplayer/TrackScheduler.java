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

public class TrackScheduler extends AudioEventAdapter {
    private final AudioPlayer player;
    private List<SongInfo> queue = new ArrayList<>();
    private User nowPlayingUser = null;
    private boolean isLooping = false;

    public TrackScheduler(AudioPlayer player) {
        this.player = player;
    }

    @Override
    public void onTrackEnd(AudioPlayer player, AudioTrack track, AudioTrackEndReason endReason) {
        SongInfo q = queue.remove(0);
        player.startTrack(q.getTrack(), false);
        nowPlayingUser = q.getRequester();
        if (isLooping) {
            queue.add(q);
        }
    }
    public void queue(AudioTrack track, User user, boolean priority, boolean shuffle) {
        if (!player.startTrack(track, true)) {
            if (priority) queue.add(0, new SongInfo(track, user));
            if (shuffle) {
                int randomIndex = (int) (Math.random() * (queue.size() + 1));
                queue.add(randomIndex, new SongInfo(track, user));
            }
            else queue.add(new SongInfo(track, user));
        } else {
            nowPlayingUser = user;
        }
    }

    public AudioPlayer getPlayer() {
        return player;
    }

    public List<SongInfo> getQueue() {
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
        Collections.shuffle(queue);
    }
}
