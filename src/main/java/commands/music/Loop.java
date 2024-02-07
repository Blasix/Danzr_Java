package commands.music;

import commands.ICommand;
import lavaplayer.GuildMusicManager;
import lavaplayer.PlayerManager;
import logic.VoiceLogic;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;

import java.util.List;

public class Loop implements ICommand {
    @Override
    public String getName() {
        return "loop";
    }

    @Override
    public String getDescription() {
        return "Toggle looping of the queue";
    }

    @Override
    public List<OptionData> getOptions() {
        return null;
    }

    @Override
    public void execute(SlashCommandInteractionEvent event) {
        if (VoiceLogic.checkConnection(event)) return;
        EmbedBuilder embedBuilder = new EmbedBuilder();
        embedBuilder.setColor(0x000082);

        GuildMusicManager guildMusicManager = PlayerManager.get().getGuildMusicManager(event.getGuild());
        boolean looping = guildMusicManager.getTrackScheduler().isLooping();
        guildMusicManager.getTrackScheduler().toggleLooping();
        if (looping) embedBuilder.setTitle("Stopped looping the queue");
        else embedBuilder.setTitle("Looping the queue");
        event.replyEmbeds(embedBuilder.build()).queue();
    }
}
