package logic;

import lavaplayer.GuildMusicManager;
import com.sedmelluq.discord.lavaplayer.track.AudioPlaylist;
import com.sedmelluq.discord.lavaplayer.track.AudioTrackInfo;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.components.ItemComponent;
import net.dv8tion.jda.api.interactions.components.buttons.Button;
import net.dv8tion.jda.api.utils.messages.MessageCreateBuilder;
import net.dv8tion.jda.api.utils.messages.MessageCreateData;
import net.dv8tion.jda.api.utils.messages.MessageEditBuilder;
import net.dv8tion.jda.api.utils.messages.MessageEditData;
import org.jetbrains.annotations.NotNull;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.TimeUnit;

public class SelectSong extends ListenerAdapter {

    public static AudioPlaylist playlist;
    public static GuildMusicManager guildMusicManager;
    public static boolean priority;
    public static boolean shuffle;

    public static void displayMenu(SlashCommandInteractionEvent event) {
        int AOB = Math.min(playlist.getTracks().size(), 5);
        ItemComponent[] buttons = new ItemComponent[AOB];
        EmbedBuilder embedBuilder = new EmbedBuilder();
        embedBuilder.setTitle("Select a song");
        embedBuilder.setColor(0x000082);

        for (int i = 0; i < AOB; i++) {
            AudioTrackInfo trackInfo = playlist.getTracks().get(i).getInfo();
            long hours = TimeUnit.MILLISECONDS.toHours(trackInfo.length);
            SimpleDateFormat sdf = hours > 0 ? new SimpleDateFormat("hh:mm:ss") : new SimpleDateFormat("mm:ss");
            String formattedLength = sdf.format(new Date(trackInfo.length));
            embedBuilder.addField((i+1)+") " + trackInfo.title, "(" + formattedLength + ") - Song by " + trackInfo.author, false);
            buttons[i] = Button.primary("select_song_" + i, (i+1)+"");
        }



        MessageCreateData message = new MessageCreateBuilder()
                .setEmbeds(embedBuilder.build())
                .addActionRow(buttons)
                .build();
        event.reply(message).queue();
    }

    @Override
    public void onButtonInteraction(@NotNull ButtonInteractionEvent event) {
        if (event.getComponentId().startsWith("select_song_")) {
            int index = Integer.parseInt(event.getComponentId().substring(12));
            guildMusicManager.getTrackScheduler().queue(playlist.getTracks().get(index), event.getUser(), priority, shuffle);
            MessageEditData message = new MessageEditBuilder()
                    .setEmbeds(VoiceLogic.createSongAddedEmbed(playlist.getTracks().get(index).getInfo(), event.getUser(), event.getGuild()))
                    .setComponents()
                    .build();
            event.editMessage(message).queue();
        }
    }
}
