package commands;

import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;

import java.util.List;

public class Sum implements ICommand{
    @Override
    public String getName() {
        return "sum";
    }

    @Override
    public String getDescription() {
        return "Adds two numbers together";
    }

    @Override
    public List<OptionData> getOptions() {
        return List.of(
                new OptionData(OptionType.INTEGER, "number_1", "The first number").setRequired(true),
                new OptionData(OptionType.INTEGER, "number_2", "The second number").setRequired(true)
        );
    }

    @Override
    public void execute(SlashCommandInteractionEvent event) {
        int num1 = event.getOption("number_1").getAsInt();
        int num2 = event.getOption("number_2").getAsInt();

        event.reply("The sum is " + (num1 + num2)).queue();
    }
}
