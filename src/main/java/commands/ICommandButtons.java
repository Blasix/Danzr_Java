package commands;

import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent;

import java.util.*;

public interface ICommandButtons extends ICommand {
    void executeButton(ButtonInteractionEvent event);
}
