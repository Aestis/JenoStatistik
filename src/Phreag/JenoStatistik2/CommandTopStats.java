package Phreag.JenoStatistik2;


import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DecimalFormat;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class CommandTopStats implements CommandExecutor {
	private String f(String raw){
		return raw.replaceAll("_", " ");
	}
	private String format(long amount) {
		DecimalFormat df = new DecimalFormat();      
		return df.format(amount); 
	}
	
		@Override
		public boolean onCommand(final CommandSender sender, Command cmd, String label, String[] args) {
			if (!sender.hasPermission("jenostatistik.stat")){
				sender.sendMessage("�cDu hast keine Berechtigung um die Statistik zu sehen!");
				return true;
			}
			
			if(args.length==0){
				showTop(sender, "GLOBAL");
				return true;
			}
			String Server=""; 
			if(args[0].equalsIgnoreCase("Vanilla")){
				Server="Vanilla";
			}else if (args[0].equalsIgnoreCase("Tekkit")){
				Server="Tekkit";
			}else if(args[0].equalsIgnoreCase("B-Team")){
				Server="B-Team";
			}else if(args[0].equalsIgnoreCase("Infinity")){	
					Server="Infinity";
			}
			if (Server.equals("")){
				try{
					StatType type=JenoStatistik.getStatType(args[0]);
					showTopKat(sender,"GLOBAL",type);
					return true;
				}catch(Exception e){
					sender.sendMessage("�cDer Parameter wurde nicht erkannt.");
					JenoStatistik.instance.showAvailableParams(sender);
					return true;
				}
			}else{
				if (args.length==2){
					try{
						StatType type=JenoStatistik.getStatType(args[1]);
						showTopKat(sender,Server,type);
						return true;
					}catch(Exception e){
						sender.sendMessage("�cDer Parameter wurde nicht erkannt.");
						JenoStatistik.instance.showAvailableParams(sender);
						return true;
					}
				}else{
					showTop(sender,Server);
					return true;
				}
			}
		}
		private void showTopKat(final CommandSender sender, final String Server, final StatType type){
			Bukkit.getScheduler().runTaskAsynchronously(JenoStatistik.instance, new Runnable(){
				public void run() {
					if(type==StatType.Erster_Login||type==StatType.Zuletzt_Online){
						sender.sendMessage("�cFehler: Zu dieser Kategroei kann man keine Rangliste anzeigen.");
						return;
					}
					//player, SUM(count) as count
					ResultSet rs;
					String title;
					if (Server.equals("GLOBAL")){
						rs=JenoStatistik.instance.DBM.DB.getTopKat(type);
						title="�2Zeige Top Spieler nach "+f(type.toString())+" auf JenoMiners.de";
					}else{
						rs=JenoStatistik.instance.DBM.DB.getTopKat(type, Server);
						title="�2Zeige Top Spieler nach "+f(type.toString())+" auf dem "+Server+"-Server von JenoMiners.de";
					}
					if (JenoStatistik.isResultSetEmpty(rs)){
						sender.sendMessage("�cZu deiner Anfrage wurden keine Statistiken gefunden.");
						sender.sendMessage("�cZur korrekten Verwendung der Befehle verwende �2/js");
						return;
					}
					try {
						sender.sendMessage(title);
						int Position = 1;
						while(rs.next()){
							if (Position<21){
								if (type==StatType.Erhaltener_Schaden||type==StatType.Spieler_Schaden){
									sender.sendMessage("�e"+rs.getString("player")+" �4-- �e"+format((rs.getLong("count")/2)));
								}
								else if (type==StatType.AFK_Zeit||type==StatType.Spielzeit){
									sender.sendMessage("�e"+rs.getString("player")+" �4-- �e"+timeString(rs.getLong("count")));
								}
								else{
									sender.sendMessage("�e"+rs.getString("player")+" �4-- �e"+format(rs.getLong("count")));
								}
							}else{
								break;
							}
							Position++;
						}
						rs.last();
						int rowcount = rs.getRow();
						rs.beforeFirst();
						Position = 1;
						while(rs.next()){
							if(rs.getString("player").equalsIgnoreCase(sender.getName())){
								sender.sendMessage("�6Du bist auf Position "+Position+" von "+rowcount+".");
								sender.sendMessage("�2Fuer eine Uebersicht der JenoStatistik-Befehle verwende �c/js");
								rs.close();
								return;
							}
							Position++;
						}
						sender.sendMessage("�cDu bist in der Liste nicht vertreten.");
						sender.sendMessage("�2Fuer eine Uebersicht der JenoStatistik-Befehle verwende �c/js");
						rs.close();
						
						
					} catch (SQLException e) {
						sender.sendMessage("�cOooops, da ist etwas schief gelaufen, notier dir mal die Uhrzeit und sag das Phreag ;).");
						e.printStackTrace();
					}
		        }
			});
		}
		private void showTop(final CommandSender sender, final String Server){
			Bukkit.getScheduler().runTaskAsynchronously(JenoStatistik.instance, new Runnable(){
				public void run() {
					//player, type, SUM(count) as count
					ResultSet rs;
					String title;
					if (Server.equals("GLOBAL")){
						rs=JenoStatistik.instance.DBM.DB.getTop();
						title="�2Zeige Top Spieler auf JenoMiners.de";
					}else{
						rs=JenoStatistik.instance.DBM.DB.getTop(Server);
						title="�2Zeige Top Spieler auf dem "+Server+"-Server von JenoMiners.de";
					}
					if (JenoStatistik.isResultSetEmpty(rs)){
						sender.sendMessage("�cZu deiner Anfrage wurden keine Statistiken gefunden.");
						sender.sendMessage("�cZur korrekten Verwendung der Befehle verwende �2/js");
						return;
					}
					try {
						sender.sendMessage(title);
						String type="";
						while(rs.next()){
							StatType t=StatType.valueOf(rs.getString("type"));
							if(t==StatType.Erster_Login||t==StatType.Zuletzt_Online)continue;
							if(!type.equals(t.toString())){
								type=t.toString();
								sender.sendMessage("�aTop Spieler nach "+f(t.toString()));
							}
							
							if (t==StatType.Erhaltener_Schaden||t==StatType.Spieler_Schaden){
								sender.sendMessage("�e"+rs.getString("player")+" �4-- �e"+format((rs.getLong("count")/2)));
							}
							else if (t==StatType.AFK_Zeit||t==StatType.Spielzeit){
								sender.sendMessage("�e"+rs.getString("player")+" �4-- �e"+timeString(rs.getLong("count")));
							}
							else{
								sender.sendMessage("�e"+rs.getString("player")+" �4-- �e"+format(rs.getLong("count")));
							}
						}
						rs.close();
						sender.sendMessage("�2Tipp: In den Ranglisten nach Kategorie siehst du auch deine Position");
						sender.sendMessage("�2Fuer eine Uebersicht der JenoStatistik-Befehle verwende �c/js");
					} catch (SQLException e) {
						sender.sendMessage("�cOooops, da ist etwas schief gelaufen, notier dir mal die Uhrzeit und sag das Phreag ;).");
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
