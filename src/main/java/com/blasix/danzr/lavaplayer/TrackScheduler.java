package com.blasix.danzr.lavaplayer;

import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;
import com.sedmelluq.discord.lavaplayer.player.event.AudioEventAdapter;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import com.sedmelluq.discord.lavaplayer.track.AudioTrackEndReason;
import com.blasix.danzr.logic.SongInfo;
import net.dv8tion.jda.api.entities.User;

import java.util.*;
import java.util.concurrent.*;

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
        if (isLooping) queue.offer(q);
        nowPlayingUser = q.getRequester();

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
