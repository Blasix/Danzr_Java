package commands.music;

import logic.VoiceLogic;
import commands.ICommand;
import lavaplayer.GuildMusicManager;
import lavaplayer.PlayerManager;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;

import java.util.List;

public class Skip implements ICommand {
    @Override
    public String getName() {
        return "skip";
    }

    @Override
    public String getDescription() {
        return "Skips the current song";
    }

    @Override
    public List<OptionData> getOptions() {
        return null;
    }

    @Override
    public void execute(SlashCommandInteractionEvent event) {
        if (VoiceLogic.checkConnection(event)) return;


        GuildMusicManager guildMusicManager = PlayerManager.get().getGuildMusicManager(event.getGuild());
        if (guildMusicManager.getTrackScheduler().getPlayer().getPlayingTrack() == null) {
            event.reply("There is no song playing").queue();
            return;
        }
        guildMusicManager.getTrackScheduler().getPlayer().stopTrack();
        event.reply("Skipped the current song").queue();
    }
}
