package com.blasix.danzr.logic;

import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import net.dv8tion.jda.api.entities.User;

public class SongInfo {
    AudioTrack track;
    User requester;

    public SongInfo(AudioTrack track, User requester) {
        this.track = track;
        this.requester = requester;
    }

    public AudioTrack getTrack() {
        return track;
    }

    public User getRequester() {
        return requester;
    }
}
