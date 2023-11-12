package commands;

import lavaplayer.PlayerManager;
import net.dv8tion.jda.api.entities.GuildVoiceState;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.events.Event;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;

import java.util.List;

public class Play implements ICommand{
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
                new OptionData(OptionType.STRING, "url_or_query", "The url or query you want to play").setRequired(true)
        );
    }

    @Override
    public void execute(SlashCommandInteractionEvent event) {
        Member member = event.getMember();
        GuildVoiceState memberVoiceState = member.getVoiceState();
        if (!memberVoiceState.inAudioChannel()) {
            event.reply("You need to be in a voice channel to use this command").queue();
            return;
        }

        Member self = event.getGuild().getSelfMember();
        GuildVoiceState selfVoiceState = self.getVoiceState();
        if (!selfVoiceState.inAudioChannel()) {
            event.getGuild().getAudioManager().openAudioConnection(memberVoiceState.getChannel());
        } else {
            if(!memberVoiceState.getChannel().equals(selfVoiceState.getChannel())) {
                event.reply("You need to be in the same voice channel as me to use this command").queue();
                return;
            }
        }

        String urlOrQuery = event.getOption("url_or_query").getAsString();
        PlayerManager playerManager = PlayerManager.get();
        playerManager.play(event.getGuild(), urlOrQuery);
        event.reply("Playing").queue();
    }
}
