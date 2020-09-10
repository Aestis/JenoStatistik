package Phreag.JenoStatistik2;


import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class CommandStat implements CommandExecutor {
	private String format(long amount) {
		DecimalFormat df = new DecimalFormat();      
		return df.format(amount); 
	}
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if (!sender.hasPermission("jenostatistik.stat")){
			sender.sendMessage("§cDu hast keine Berechtigung um die Statistik zu sehen!");
			return true;
		}
		if (args.length==0){
			getStats(sender, sender.getName(), "GLOBAL");
			//JenoStatistik.instance.showCommandUsage(sender);
			return true;
		}
		if (args.length==1){
			getStats(sender, args[0], "GLOBAL");
			//JenoStatistik.instance.showCommandUsage(sender);
			return true; 
		}
		
		String Server=""; 
		if(args[1].equalsIgnoreCase("Vanilla")){
			Server="Vanilla";
		}else if (args[1].equalsIgnoreCase("Tekkit")){
			Server="Tekkit";
		}else if(args[1].equalsIgnoreCase("B-Team")){
			Server="B-Team";
		}else if(args[1].equalsIgnoreCase("Infinity")){	
			Server="Infinity";
		}
		if(Server.equals("")){
			try{
				StatType type=JenoStatistik.getStatType(args[1]);
				getStatDetails(sender, args[0],"GLOBAL", type);
				return true;
			}catch(Exception e){
				sender.sendMessage("§cDer Parameter wurde nicht erkannt.");
				JenoStatistik.instance.showAvailableParams(sender);
				return true;
			}
		}else{
			if(args.length==3){
				try{
					StatType type=JenoStatistik.getStatType(args[2]);
					getStatDetails(sender, args[0],Server, type);
					return true;
				}catch(Exception e){
					sender.sendMessage("§cDer Parameter wurde nicht erkannt.");
					JenoStatistik.instance.showAvailableParams(sender);
					return true;
				}
			}else{
				getStats(sender, args[0], Server);
				return true;
			}
		}
	}
	
	private String f(String raw){
		return raw.replaceAll("_", " ");
	}
	
	private void getStatDetails(final CommandSender sender, final String target, final String Server, final StatType type){
		Bukkit.getScheduler().runTaskAsynchronously(JenoStatistik.instance, new Runnable(){
			public void run() {
				ResultSet rs;
				ResultSet sum;
				String title;
				if (Server.equals("GLOBAL")){
					rs=JenoStatistik.instance.DBM.DB.getPlayerStatDetails(target, type, 12);
					sum=JenoStatistik.instance.DBM.DB.getPlayerStatDetailSum(target, type);
					title="§2Zeige Statistikdetails ("+f(type.toString())+") von "+ target+" auf JenoMiners.de";
				}else{
					rs=JenoStatistik.instance.DBM.DB.getPlayerStatDetailsServer(target, Server, type, 12);
					sum=JenoStatistik.instance.DBM.DB.getPlayerStatDetailSum(target, type, Server);
					title="§2Zeige Statistikdetails ("+f(type.toString())+") von "+ target+" auf dem "+Server+"-Server von JenoMiners.de";
				}
				try {
					if (JenoStatistik.isResultSetEmpty(rs)){
						sender.sendMessage("§cZu diesem Spieler wurden in dieser Kategorie keine Statistiken gefunden.");
						sender.sendMessage("§cDer Name muss exakt geschrieben sein.");
						return;
					}
					sender.sendMessage(title);
					while(rs.next()){
						if (type==StatType.Erhaltener_Schaden||type==StatType.Spieler_Schaden){
							sender.sendMessage("§a"+rs.getString("month")+" §4-- §e"+format((rs.getLong("count")/2)));
						}
						else if (type==StatType.AFK_Zeit||type==StatType.Spielzeit){
							sender.sendMessage("§a"+rs.getString("month")+" §4-- §e"+(timeString(rs.getLong("count"))));
						}
						else if (type==StatType.Erster_Login){
							if (rs.getLong("count")>1453140470253L){
								sender.sendMessage("§a Erster Login §4-- §e"+ new SimpleDateFormat("dd.MM.yyyy HH:mm:ss").format(new Date(rs.getLong("count"))));
							}else{
								sender.sendMessage("§a Erster Login §4-- §e vor dem 18.01.2016");
							}
						}
						else if (type==StatType.Zuletzt_Online){
							if (rs.getLong("count")>0L){
								sender.sendMessage("§aZuletzt Online §4-- §e"+ new SimpleDateFormat("dd.MM.yyyy HH:mm:ss").format(new Date(rs.getLong("count"))));
							}else{
								sender.sendMessage("§aZuletzt Online §4-- §e vor dem 18.01.2016");
							}
						}
						else{
							sender.sendMessage("§a"+rs.getString("month")+" §4-- §e"+format(rs.getLong("count")));
						}
					}
					rs.close();
					if (sum.next()){
						if (type==StatType.Erhaltener_Schaden||type==StatType.Spieler_Schaden){
							sender.sendMessage("§a§l Gesamtsumme: §e"+format((sum.getLong("count")/2)));
						}
						else if (type==StatType.Erster_Login||type==StatType.Zuletzt_Online){}
						else if (type==StatType.AFK_Zeit||type==StatType.Spielzeit){
							sender.sendMessage("§a§l Gesamtsumme: §e"+timeString(sum.getLong("count")));
						}
						else{
							sender.sendMessage("§a§l Gesamtsumme: §e"+format(sum.getLong("count")));
						}
					}
					sum.close();
					sender.sendMessage("§2Fuer eine Uebersicht der JenoStatistik-Befehle verwende §c/js");
					
				} catch (SQLException e) {
					sender.sendMessage("§cOooops, da ist etwas schief gelaufen, notier dir mal die Uhrzeit und sag das Phreag ;).");
					e.printStackTrace();
				}
			}
		});
	}
	private void getStats(final CommandSender sender, final String target, final String Server){
		Bukkit.getScheduler().runTaskAsynchronously(JenoStatistik.instance, new Runnable(){
			public void run() {
				ResultSet rs;
				String title;
				if (Server.equals("GLOBAL")){
					rs=JenoStatistik.instance.DBM.DB.getPlayerStatSum(target);
					title="§2Zeige Statistiken von "+ target+" auf JenoMiners.de";
				}else{
					rs=JenoStatistik.instance.DBM.DB.getPlayerStatSum(target, Server);
					title="§2Zeige Statistiken von "+ target+" auf dem "+Server+"-Server von JenoMiners.de";
				}
	        	
				try {
					if (JenoStatistik.isResultSetEmpty(rs)){
						sender.sendMessage("§cZu diesem Spieler wurden in dieser Kategorie keine Statistiken gefunden.");
						sender.sendMessage("§cDer Name muss exakt geschrieben sein.");
						return;
					}
					sender.sendMessage(title);
					while(rs.next()){
						StatType type=StatType.valueOf(rs.getString("type"));
						if (type==StatType.Erhaltener_Schaden||type==StatType.Spieler_Schaden){
							sender.sendMessage("§e"+f(type.toString())+" §4-- §e"+format((rs.getLong("count")/2)));
						}
						else if (type==StatType.AFK_Zeit||type==StatType.Spielzeit){
							sender.sendMessage("§e"+f(type.toString())+" §4-- §e"+timeString(rs.getLong("count")));
						}
						else if (type==StatType.Erster_Login){
							if (rs.getLong("count")>1453140470253L){
								sender.sendMessage("§e"+f(type.toString())+" §4-- §e"+ new SimpleDateFormat("dd.MM.yyyy HH:mm:ss").format(new Date(rs.getLong("count"))));
							}else{
								sender.sendMessage("§e"+f(type.toString())+" §4-- §e vor dem 18.01.2016");
							}
						}
						else if (type==StatType.Zuletzt_Online){
							if (rs.getLong("count")>0L){
								sender.sendMessage("§e"+f(type.toString())+" §4-- §e"+ new SimpleDateFormat("dd.MM.yyyy HH:mm:ss").format(new Date(rs.getLong("count"))));
							}else{
								sender.sendMessage("§e"+f(type.toString())+" §4-- §e vor dem 18.01.2016");
							}
						}
						else{
							sender.sendMessage("§e"+f(type.toString())+" §4-- §e"+format(rs.getLong("count")));
						}
					}
					rs.close();
					sender.sendMessage("§2Fuer eine Uebersicht der JenoStatistik-Befehle verwende §c/js");
				} catch (SQLException e) {
					sender.sendMessage("§cOooops, da ist etwas schief gelaufen, notier dir mal die Uhrzeit und sag das Phreag ;).");
					e.printStackTrace();
				}
	        }
	    });
	}
	private String timeString (long seconds){
		long minutes=0;
		long hours=0;
		if (seconds>60){
			minutes=seconds/60;
			seconds=seconds-(minutes*60);
		}
		if (minutes>60){
			hours=minutes/60;
			minutes=minutes-(hours*60);
		}
		return(hours+"H "+minutes+"M "+seconds+"s");
	}
}
