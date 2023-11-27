package com.blasix.danzr;

import com.blasix.danzr.commands.ICommand;
import com.blasix.danzr.commands.ICommandButtons;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent;
import net.dv8tion.jda.api.events.session.ReadyEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;

import java.util.*;

public class CommandManager extends ListenerAdapter {
    private final List<ICommand> commands = new ArrayList<>();
    @Override
    public void onReady(ReadyEvent event) {
        // TODO for adding global com.blasix.danzr.commands:
        // replace guild with just event.getJDA()

        Guild guild = event.getJDA().getGuildById(767435347050758164L);

        for (ICommand command : commands) {
            guild.upsertCommand(command.getName(), command.getDescription()).addOptions(command.getOptions() == null ? new ArrayList<>() : command.getOptions()).queue();
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
