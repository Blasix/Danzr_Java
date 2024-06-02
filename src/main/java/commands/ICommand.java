package commands;

import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.DefaultMemberPermissions;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;

import java.util.ArrayList;
import java.util.List;

public interface ICommand {
    String getName();
    String getDescription();
    default List<OptionData> getOptions() {
        return new ArrayList<>();
    }
    default DefaultMemberPermissions getPrivileges() {
        return DefaultMemberPermissions.ENABLED;
    }
    void execute(SlashCommandInteractionEvent event);
}
