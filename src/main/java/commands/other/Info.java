package commands.other;

import commands.ICommand;
import logic.Version;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;

public class Info implements ICommand {
    @Override
    public String getName() {
        return "info";
    }

    @Override
    public String getDescription() {
        return "Shows the bot information";
    }

    @Override
    public void execute(SlashCommandInteractionEvent event) {
        EmbedBuilder embed = new EmbedBuilder();
        embed.setColor(0x000082);

        embed.setTitle("Bot Information - v" + Version.VERSION);
        if (Version.isNewerVersion()) {
            embed.addField("New Version Available", "Please update the bot to the latest version", false);
        }
        embed.setThumbnail("https://blasix.com/assets/logo/danzr.png");
        embed.setDescription("This is a discord music bot. If you just want to use it please use `/help`. If you have a server yourself and want to run this bot please go to [GitHub](https://github.com/Blasix/Danzr_Java) and follow the instructions.");

        embed.setFooter("Created by Blasix ❤️", "https://blasix.com/assets/logo/logo.png");
        event.replyEmbeds(embed.build()).queue();
    }
}
