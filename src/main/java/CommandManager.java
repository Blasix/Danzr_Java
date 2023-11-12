import commands.ICommand;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.events.session.ReadyEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

import java.util.*;

public class CommandManager extends ListenerAdapter {
    private List<ICommand> commands = new ArrayList<>();
    @Override
    public void onReady(ReadyEvent event) {
        // TODO for adding global commands:
        // replace guild with just event.getJDA()

        Guild guild = event.getJDA().getGuildById(767435347050758164L);

        for (ICommand command : commands) {
            guild.upsertCommand(command.getName(), command.getDescription()).addOptions(command.getOptions()).queue();
        }
    }

    @Override
    public void onSlashCommandInteraction(SlashCommandInteractionEvent event) {
        for (ICommand command : commands) {
            if (event.getName().equals(command.getName())) {
                command.execute(event);
                return;
            }
        }
    }

    public void addCommand(ICommand command) {
        commands.add(command);
    }
}
