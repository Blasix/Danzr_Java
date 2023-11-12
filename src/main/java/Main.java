import commands.*;
import net.dv8tion.jda.api.*;

public class Main {
    public static void main(String[] args) {
        JDA jda = JDABuilder.createDefault(Secrets.getToken()).build();
        CommandManager manager = new CommandManager();
        manager.addCommand(new Sum());
        manager.addCommand(new Play());
        jda.addEventListener(manager);
    }
}