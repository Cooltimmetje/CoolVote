package me.Cooltimmetje.CoolVote.Commands;

import me.Cooltimmetje.CoolVote.Database.MysqlManager;
import me.Cooltimmetje.CoolVote.Utilities.ChatUtils;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.HashMap;

/**
 * This class has been created on 10/29/2015 at 17:16 by Cooltimmetje.
 */
public class VoteCommand implements CommandExecutor {

    private HashMap<String,String> confirm = new HashMap<>();

    public boolean onCommand(CommandSender sender, Command cmd, String arg, String[] args) {
        if (cmd.getLabel().equalsIgnoreCase("vote")) {
            if (sender instanceof Player) {
                Player p = (Player) sender;
                if(args.length >= 1){
                    if(!confirm.containsKey(p.getName()) || !confirm.get(p.getName()).equals(args[0])){
                        ChatUtils.sendMsgTag(p, "Vote", "Are you sure you want to vote for &c" + args[0] + " &a? &lPlease confirm by running the command again! YOU CAN'T CHANGE AFTER YOU HAVE CONFIRMED YOUR VOTE!");
                        confirm.put(p.getName(), args[0]);
                    } else {
                        ChatUtils.sendMsgTag(p, "Vote", "Your vote for &c" + args[0] + " &ahas been confirmed, please wait while we register it.");
                        MysqlManager.registerVote(p, args[0]);
                    }
                } else {
                    ChatUtils.sendMsgTag(p, "Vote", ChatUtils.error + "Please specify who you are voting for!");
                }
            }
        }
        return false;
    }

}
