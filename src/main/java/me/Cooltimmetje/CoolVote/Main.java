package me.Cooltimmetje.CoolVote;

import me.Cooltimmetje.CoolVote.Commands.VoteCommand;
import me.Cooltimmetje.CoolVote.Database.MysqlManager;
import me.Cooltimmetje.CoolVote.Utilities.MiscUtils;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandExecutor;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

/**
 * This class has been created on 10/28/2015 at 17:15 by Cooltimmetje.
 */
public class Main extends JavaPlugin {

    private long startTime;
    private static Plugin plugin;

    public void onEnable(){
        startTime = System.currentTimeMillis(); //Registers the plugin startTime to measure loading time afterwards...
        getLogger().info("Enabling plugin... Please wait.");
        sendDebug("&9Debug> &aStarting plugin load... &oPlease wait.");

        plugin = this; //Registering the plugin variable to allow other classes to access it

        getLogger().info("Starting pre-setup...."); //For everything that will cause issues if it gets done after registering stuff
        this.saveDefaultConfig();
        MysqlManager.setupHikari();

        getLogger().info("Registering Commands..."); //Can you guess what this does? Yes! It registers the commands.
        registerCommand("vote", new VoteCommand());
        //format: registerCommand("cmd", new ExecutorClass);

        getLogger().info("Plugin ready! (Loadtime: " + getLoad() + "ms)");
        sendDebug("&9Debug> &aPlugin load finished! &c(" + getLoad() + "ms) &3&oYou can take a look in the console for more load information.");
    }

    public void onDisable() {
        getLogger().info("Disabling plugin... Please wait.");



        plugin = null; //To prevent memory leaks
    }


    //Used to resister commands.
    private void registerCommand(String cmd, CommandExecutor executor){
        getCommand(cmd).setExecutor(executor);
        getLogger().info("Registerd command \"" + cmd + "\" with executor\"" + executor.toString() + "\"");
    }

    //Used to show the load time.
    private long getLoad(){
        return System.currentTimeMillis() - startTime;
    }

    //Used to send debug messages.
    public static void sendDebug(String msg){
        for(Player p : Bukkit.getOnlinePlayers()) {
            if(p.isOp()) {
                p.sendMessage(MiscUtils.color(msg));
            }
        }
    }

    //Returns the plugin instance.
    public static Plugin getPlugin() {
        return plugin;
    }

}
