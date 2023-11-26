package commands.music;

import commands.ICommandButtons;
import logic.SongInfo;
import logic.VoiceLogic;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import commands.ICommand;
import lavaplayer.GuildMusicManager;
import lavaplayer.PlayerManager;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;
import net.dv8tion.jda.api.interactions.components.ItemComponent;
import net.dv8tion.jda.api.interactions.components.buttons.Button;
import net.dv8tion.jda.api.utils.messages.MessageCreateBuilder;
import net.dv8tion.jda.api.utils.messages.MessageCreateData;
import net.dv8tion.jda.api.utils.messages.MessageEditBuilder;
import net.dv8tion.jda.api.utils.messages.MessageEditData;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

// TODO - Add pagination
// https://www.youtube.com/watch?v=IuTW7bGVK5o&list=PLMDWhd7MfizXOJXn905x8UqkWtMJ6tl-b&index=6

public class Queue implements ICommandButtons {
    private double sep = 8.0;
    @Override
    public String getName() {
        return "queue";
    }

    @Override
    public String getDescription() {
        return "Check the queue";
    }

    @Override
    public List<OptionData> getOptions() {
        return null;
    }

    @Override
    public void execute(SlashCommandInteractionEvent event) {
        if (VoiceLogic.checkConnection(event)) return;

        GuildMusicManager guildMusicManager = PlayerManager.get().getGuildMusicManager(event.getGuild());
        List<SongInfo> queue = new ArrayList<>(guildMusicManager.getTrackScheduler().getQueue());
        EmbedBuilder embedBuilder = new EmbedBuilder();

        int maxPage = (int) Math.ceil(queue.size() / sep);
        embedBuilder.setTitle("Queue");
        embedBuilder.setColor(0x000082);
        if (queue.isEmpty()) {
            embedBuilder.setDescription("The queue is empty");
            event.replyEmbeds(embedBuilder.build()).queue();
            return;
        }
        int i = 1;
        int sepcur;
        if (sep < queue.size()) sepcur = (int) sep;
        else sepcur = queue.size();
        for (int j = 0; j < sepcur; j++) {
            SongInfo songInfo = queue.get(j);
            long hours = TimeUnit.MILLISECONDS.toHours(songInfo.getTrack().getInfo().length);
            SimpleDateFormat sdf = hours > 0 ? new SimpleDateFormat("hh:mm:ss") : new SimpleDateFormat("mm:ss");
            String formattedLength = sdf.format(new Date(songInfo.getTrack().getInfo().length));

            embedBuilder.addField(i + ") " + songInfo.getTrack().getInfo().title, "(" + formattedLength + ") - Requested by: " + songInfo.getRequester().getAsMention(), false);
            i++;
        }

        embedBuilder.setFooter("Page 1 of " + maxPage);

        Button firstButton = Button.primary("first", "|<");
        Button previousButton = Button.primary("previous", "<");
        Button nextButton = Button.primary("next", ">");
        Button lastButton = Button.primary("last", ">|");

        MessageCreateData message = new MessageCreateBuilder().setEmbeds(embedBuilder.build()).setActionRow(
                firstButton.asDisabled(), previousButton.asDisabled(),
                (1 == maxPage) ? nextButton.asDisabled() : nextButton,
                (1 == maxPage) ? lastButton.asDisabled() : lastButton
        ).build();
        event.reply(message).queue();
    }

    @Override
    public void executeButton(ButtonInteractionEvent event) {
        if (event.getComponentId().equals("first")) {
            editMessage(event, 1);
        } else if (event.getComponentId().equals("previous")) {
            editMessage(event, Integer.parseInt(event.getMessage().getEmbeds().get(0).getFooter().getText().split(" ")[1]) - 1);
        } else if (event.getComponentId().equals("next")) {
            editMessage(event, Integer.parseInt(event.getMessage().getEmbeds().get(0).getFooter().getText().split(" ")[1]) + 1);
        } else if (event.getComponentId().equals("last")) {
            editMessage(event, -1);
        }


//        GuildMusicManager guildMusicManager = PlayerManager.get().getGuildMusicManager(event.getGuild());
//        List<SongInfo> queue = new ArrayList<>(guildMusicManager.getTrackScheduler().getQueue());
//        int maxPage = (int) Math.ceil(queue.size() / 10.0);
//        if (page > maxPage) {
//            page = maxPage;
//        }
//        if (page < 1) {
//            page = 1;
//        }
//        EmbedBuilder embedBuilder = new EmbedBuilder();
//        embedBuilder.setTitle("Queue");
//        embedBuilder.setColor(0x000082);
//        if (queue.isEmpty()) {
//            embedBuilder.setDescription("The queue is empty");
//            event.replyEmbeds(embedBuilder.build()).queue();
//            return;
//        }
//        int i = 1;
//        for (SongInfo songInfo : queue) {
//            long hours = TimeUnit.MILLISECONDS.toHours(songInfo.getTrack().getInfo().length);
//            SimpleDateFormat sdf = hours > 0 ? new SimpleDateFormat("hh:mm:ss") : new SimpleDateFormat("mm:ss");
//            String formattedLength = sdf.format(new Date(songInfo.getTrack().getInfo().length));
//
//            embedBuilder.addField(i + ") " + songInfo.getTrack().getInfo().title, "(" + formattedLength + ") - Requested by: " + songInfo.getRequester().getAsMention(), false);
//            i++;
//        }
//        embedBuilder.setFooter("Page " + page + " of " + maxPage);
//        event.editMessageEmbeds(embedBuilder.build()).queue();
    }

    private void editMessage(ButtonInteractionEvent event, int page) {
        GuildMusicManager guildMusicManager = PlayerManager.get().getGuildMusicManager(event.getGuild());
        List<SongInfo> queue = new ArrayList<>(guildMusicManager.getTrackScheduler().getQueue());
        int maxPage = (int) Math.ceil(queue.size() / 10.0);

        if (page == -1) {
            page = maxPage;
        }

        if (page > maxPage) {
            page = maxPage;
        }

        if (page < 1) {
            page = 1;
        }

        EmbedBuilder embedBuilder = new EmbedBuilder();
        embedBuilder.setTitle("Queue");
        embedBuilder.setColor(0x000082);
        if (queue.isEmpty()) {
            embedBuilder.setDescription("The queue is empty");
            event.editMessageEmbeds(embedBuilder.build()).queue();
            return;
        }

        int start = (page - 1) * (int) sep;
        int end = start + (int) sep;
        if (end > queue.size()) {
            end = queue.size();
        }
        for (int j = start; j < end; j++) {
            SongInfo songInfo = queue.get(j);
            long hours = TimeUnit.MILLISECONDS.toHours(songInfo.getTrack().getInfo().length);
            SimpleDateFormat sdf = hours > 0 ? new SimpleDateFormat("hh:mm:ss") : new SimpleDateFormat("mm:ss");
            String formattedLength = sdf.format(new Date(songInfo.getTrack().getInfo().length));

            embedBuilder.addField(j+1 + ") " + songInfo.getTrack().getInfo().title, "(" + formattedLength + ") - Requested by: " + songInfo.getRequester().getAsMention(), false);
        }

        embedBuilder.setFooter("Page " + page + " of " + maxPage);

        Button firstButton = Button.primary("first", "|<");
        Button previousButton = Button.primary("previous", "<");
        Button nextButton = Button.primary("next", ">");
        Button lastButton = Button.primary("last", ">|");

        MessageEditData message = new MessageEditBuilder().setEmbeds(embedBuilder.build()).setActionRow(
                (page == 1) ? firstButton.asDisabled() : firstButton,
                (page == 1) ? previousButton.asDisabled() : previousButton,
                (page == maxPage) ? nextButton.asDisabled() : nextButton,
                (page == maxPage) ? lastButton.asDisabled() : lastButton
        ).build();

        event.editMessage(message).queue();
    }
}
