package Phreag.JenoStatistik2;


import java.sql.ResultSet;

import org.apache.commons.lang.WordUtils;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import Phreag.JenoStatistik2.Listener.BlockPlaceBreakListener;
import Phreag.JenoStatistik2.Listener.BreedingFeedingListener;
import Phreag.JenoStatistik2.Listener.ChestShopListener;
import Phreag.JenoStatistik2.Listener.DeathDamageListener;
import Phreag.JenoStatistik2.Listener.LoginLogoutListener;
import Phreag.JenoStatistik2.Listener.MyPetListener;
import Phreag.JenoStatistik2.Listener.OnTimeAFKListener;
import Phreag.JenoStatistik2.Listener.ShopPremiumListener;
import Phreag.JenoStatistik2.Listener.VoteListener;

public class JenoStatistik extends JavaPlugin{

	public static JenoStatistik instance;
	public String Month;
	public DBManager DBM;
	@Override	
	public void onEnable(){
		Month="";
		instance=this;
		setupConfig();
		MySQL DB=new MySQL();
		if(!DB.connect()){
			getLogger().info("Fehler mit Mysql, JenoStatistik wird deaktiviert.");
			getServer().getPluginManager().disablePlugin(this);
		}
		DBM=new DBManager(DB);
		//importDataBase();
		
		getServer().getPluginManager().registerEvents(new VoteListener(), this);
		getServer().getPluginManager().registerEvents(new OnTimeAFKListener(), this);
		getServer().getPluginManager().registerEvents(new LoginLogoutListener(), this);
		getServer().getPluginManager().registerEvents(new DeathDamageListener(), this);
		getServer().getPluginManager().registerEvents(new BlockPlaceBreakListener(), this);
		getServer().getPluginManager().registerEvents(new BreedingFeedingListener(), this);
		getServer().getPluginManager().registerEvents(new ChestShopListener(), this);
		getServer().getPluginManager().registerEvents(new ShopPremiumListener(), this);
		getServer().getPluginManager().registerEvents(new MyPetListener(), this);

		
		//Commands
		this.getCommand("stat").setExecutor(new CommandStat());
		this.getCommand("topstat").setExecutor(new CommandTopStats());
		this.getCommand("js").setExecutor(new CommandJS());
		this.getCommand("jenostatistik").setExecutor(new CommandJS());
		this.getCommand("topmon").setExecutor(new CommandTopMonth());
	}
	
	public static boolean isResultSetEmpty(ResultSet rs){
		try{
			if (rs==null){
				return true;
			}
			int rowcount = 0;
			if (rs.last()) {
			  rowcount = rs.getRow();
			  rs.beforeFirst();
			}
			if (rowcount==0){
				return true;
			}
			return false;
		}catch(Exception e){
			return true;
		}
		
		
	}
//	private void importDataBase() {
//		FileConfiguration config = getConfig();
//		if (config.getBoolean("ImportDB")){
//			ResultSet rs=DBM.DB.getOldDB();
//			if(rs==null){
//				System.out.println("Keine Daten zum Importieren gefunden!");
//				return;
//			}
//			try{
//				System.out.println("Importiere alte DB...");
//				while (rs.next()){
//					if (rs.getInt("ontime")>180){
//						DBM.DB.importRecord(rs.getString("player"), rs.getString("server"), StatType.Spielzeit, rs.getInt("ontime"));
//						if(DBM.DB.doesRecordExist(rs.getString("player"), StatType.Erster_Login, rs.getString("server"),"2017 und davor")){
//							DBM.DB.setStat(rs.getString("player"), StatType.Erster_Login,rs.getLong("firstseen"), rs.getString("server"), "2017 und davor");
//						}else{
//							DBM.DB.importRecord(rs.getString("player"), rs.getString("server"), StatType.Erster_Login, rs.getLong("firstseen"));
//						}
//					}else{
//						continue;
//					}
//					if (rs.getInt("placed")>1){
//						DBM.DB.importRecord(rs.getString("player"), rs.getString("server"), StatType.Gesetzt, rs.getInt("placed"));
//					}
//					if (rs.getInt("broken")>1){
//						DBM.DB.importRecord(rs.getString("player"), rs.getString("server"), StatType.Abgebaut, rs.getInt("broken"));
//					}
//					if (rs.getInt("votes")>1){
//						DBM.DB.importRecord(rs.getString("player"), rs.getString("server"), StatType.Votes, rs.getInt("votes"));
//					}
//					
//					if (rs.getLong("lastseen")>10000){
//						if(!DBM.DB.doesRecordExist(rs.getString("player"), StatType.Zuletzt_Online, rs.getString("server"),"2017 und davor")){
//							DBM.DB.importRecord(rs.getString("player"), rs.getString("server"), StatType.Zuletzt_Online, rs.getLong("lastseen"));
//						}
//						
//					}
//				}
//				System.out.println("Import Abgeschlossen.");
//				config.set("ImportDB",false);
//				saveConfig();
//				
//			}catch(Exception e){
//				System.out.println("Fehler beim Importieren der alten Datenbank!");
//				return;
//			}
//			
//			
//		}
//		
//	}

	@Override
	public void onDisable(){
		DBM.Shutdown();
		getLogger().info("JenoStatistik Disabled");
    }
	
	public void showAvailableParams(CommandSender sender){
		sender.sendMessage("§cAktuelle Server sind: §6Vanilla, B-Team, Tekkit, Infinity");
		String types="";
		for (StatType t:StatType.values()){
			types=types+t.toString()+", ";
		}
		types=types.substring(0, types.length()-2);
		sender.sendMessage("§cAktuelle Kategorien sind: §6"+types);
	}
	public static StatType getStatType(String input){
		try{
			String words=input.replaceAll("_", " ");
			words=WordUtils.capitalizeFully(words);
			words=words.replaceAll(" ", "_");
			StatType type=StatType.valueOf(words);
			return type;
		}catch (Exception e){
			return null;
		}
	}
	
	
	
	private void setupConfig(){
        saveDefaultConfig();
        FileConfiguration config = getConfig();
        //Mysql Setup
        if(!config.isSet("Mysql.host")){config.set("Mysql.host","localhost");}
        if(!config.isSet("Mysql.database")){config.set("Mysql.database","jenostatistik");}
        if(!config.isSet("Mysql.port")){config.set("Mysql.port","3306");}
        if(!config.isSet("Mysql.username")){config.set("Mysql.username","user");}
        if(!config.isSet("Mysql.password")){config.set("Mysql.password","supersafepassword");}
        //Server info
        if(!config.isSet("Server")){config.set("Server","Server1");}
        if(!config.isSet("ImportDB")){config.set("ImportDB",false);}
        saveConfig();
    }

}
