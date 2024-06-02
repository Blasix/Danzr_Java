package commands.other;

import commands.ICommand;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;

public class Help implements ICommand {
    @Override
    public String getName() {
        return "help";
    }

    @Override
    public String getDescription() {
        return "Shows the list of commands";
    }

    @Override
    public void execute(SlashCommandInteractionEvent event) {
        EmbedBuilder embed = new EmbedBuilder();
        embed.setColor(0x000082);

        embed.setTitle("Commands");
        embed.addField("Music",
                "`/play <song> <priority>` - Plays the requested song\n" +
                "`/nowplaying` - Shows the currently playing song\n" +
                "`/queue` - Shows the queue\n" +
                "`/loop` - Toggle looping of the queue\n" +
                "`/shuffle` - Shuffles the queue\n" +
                "`/pause` - Pauses/resumes the player\n" +
                "`/skip <count>` - Skips a certain amount of songs\n" +
                "`/stop` - Stops the player and clears the queue\n"
        , false);
        embed.addField("Other",
                "`/info` - Shows the bot information\n" +
                "`/help` - Shows this list of commands\n",
                false);

        event.replyEmbeds(embed.build()).queue();
    }
}
