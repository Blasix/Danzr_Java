import commands.ICommand;
import commands.ICommandButtons;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent;
import net.dv8tion.jda.api.events.session.ReadyEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class CommandManager extends ListenerAdapter {
    private final List<ICommand> commands = new ArrayList<>();
    @Override
    public void onReady(ReadyEvent event) {
        for (ICommand command : commands) {
            event.getJDA().upsertCommand(command.getName(), command.getDescription()).addOptions(command.getOptions() == null ? new ArrayList<>() : command.getOptions()).queue();
        }
    }

    @Override
    public void onButtonInteraction(@NotNull ButtonInteractionEvent event) {
        for (ICommand command : commands) {
            if (command instanceof ICommandButtons commandButtons) {
                commandButtons.executeButton(event);
                return;
            }
        }
    }

    @Override
    public void onSlashCommandInteraction(@NotNull SlashCommandInteractionEvent event) {
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
