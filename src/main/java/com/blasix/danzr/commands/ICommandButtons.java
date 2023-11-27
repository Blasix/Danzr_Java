package com.blasix.danzr.commands;

import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent;

public interface ICommandButtons extends ICommand {
    void executeButton(ButtonInteractionEvent event);
}
