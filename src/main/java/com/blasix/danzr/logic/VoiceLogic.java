package com.blasix.danzr.logic;

import com.sedmelluq.discord.lavaplayer.track.AudioTrackInfo;
import com.blasix.danzr.lavaplayer.GuildMusicManager;
import com.blasix.danzr.lavaplayer.PlayerManager;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.*;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class VoiceLogic {
    public static boolean checkConnection(SlashCommandInteractionEvent event) {
        EmbedBuilder embedBuilder = new EmbedBuilder();
        embedBuilder.setColor(0x820000);

        Member member = event.getMember();
        GuildVoiceState memberVoiceState = member.getVoiceState();
        if (!memberVoiceState.inAudioChannel()) {
            embedBuilder.setTitle("You need to be in a voice channel to use this command");
            event.replyEmbeds(embedBuilder.build()).queue();
            return true;
        }

        Member self = event.getGuild().getSelfMember();
        GuildVoiceState selfVoiceState = self.getVoiceState();
        if (!selfVoiceState.inAudioChannel()) {
            embedBuilder.setTitle("I am not in an audio channel");
            event.replyEmbeds(embedBuilder.build()).queue();
            return true;
        } else {
            if(!memberVoiceState.getChannel().equals(selfVoiceState.getChannel())) {
                embedBuilder.setTitle("You need to be in the same voice channel as me to use this command");
                event.replyEmbeds(embedBuilder.build()).queue();
                return true;
            }
        }
        return false;
    }

    public static MessageEmbed createSongAddedEmbed(AudioTrackInfo info, User user, Guild guild) {
        GuildMusicManager guildMusicManager = PlayerManager.get().getGuildMusicManager(guild);
        List<SongInfo> queue = new ArrayList<>(guildMusicManager.getTrackScheduler().getQueue());

        EmbedBuilder embedBuilder = new EmbedBuilder();
        embedBuilder.setTitle(queue.isEmpty()? "Song started playing" : "Song added to queue");
        long hours = TimeUnit.MILLISECONDS.toHours(info.length);
        SimpleDateFormat sdf = hours > 0 ? new SimpleDateFormat("hh:mm:ss") : new SimpleDateFormat("mm:ss");
        String formattedLength = sdf.format(new Date(info.length));
        embedBuilder.setDescription("**Name:** `" + info.title + "`\n**Author:** `" + info.author + "`\n**Duration:** `" + formattedLength + "`");
        embedBuilder.setThumbnail("https://img.youtube.com/vi/" + info.identifier + "/0.jpg");
        embedBuilder.setFooter("Requested by " + user.getEffectiveName(), user.getAvatarUrl());
        embedBuilder.setColor(0x008200);
        return embedBuilder.build();
    }
}
