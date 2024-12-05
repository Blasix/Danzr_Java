package commands.music;

import commands.ICommand;
import lavaplayer.PlayerManager;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.GuildVoiceState;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;

public class Play implements ICommand {
    @Override
    public String getName() {
        return "play";
    }

    @Override
    public String getDescription() {
        return "Plays a song";
    }

    @Override
    public List<OptionData> getOptions() {
        return List.of(
                new OptionData(OptionType.STRING, "url_or_query", "The url or query you want to play").setRequired(true),
                new OptionData(OptionType.BOOLEAN, "priority", "If the song should be played next"),
                new OptionData(OptionType.BOOLEAN, "shuffle", "If the songs should be inserted randomly")
        );
    }

    @Override
    public void execute(SlashCommandInteractionEvent event) {
        EmbedBuilder embedBuilder = new EmbedBuilder();
        embedBuilder.setColor(0x820000);

        Member member = event.getMember();
        GuildVoiceState memberVoiceState = member.getVoiceState();
        if (!memberVoiceState.inAudioChannel()) {
            embedBuilder.setTitle("You need to be in a voice channel to use this command");
            event.replyEmbeds(embedBuilder.build()).queue();
            return;
        }

        Member self = event.getGuild().getSelfMember();
        GuildVoiceState selfVoiceState = self.getVoiceState();
        if (!selfVoiceState.inAudioChannel()) {
            event.getGuild().getAudioManager().openAudioConnection(memberVoiceState.getChannel());
        } else {
            if(!memberVoiceState.getChannel().equals(selfVoiceState.getChannel())) {
                embedBuilder.setTitle("You need to be in the same voice channel as me to use this command");
                event.replyEmbeds(embedBuilder.build()).queue();
                return;
            }
        }

        String urlOrQuery = event.getOption("url_or_query").getAsString();

        boolean priority = false;
        try {
            priority = event.getOption("priority").getAsBoolean();
        } catch (NullPointerException ignored) {}

        boolean shuffle = false;
        try {
            shuffle = event.getOption("shuffle").getAsBoolean();
        } catch (NullPointerException ignored) {}

        try {
            new URI(urlOrQuery);
        } catch (URISyntaxException e) {
            urlOrQuery = "ytsearch:" + urlOrQuery;
        }

        PlayerManager playerManager = PlayerManager.get();
        playerManager.play(event.getGuild(), urlOrQuery, event, priority, shuffle);
    }
}
