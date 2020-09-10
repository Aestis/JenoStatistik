package Phreag.JenoStatistik2;

import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;



public class DBManager {
	public MySQL DB;
	private ConcurrentHashMap<String, Long>BufferedValues;
	public DBManager(MySQL DB){
		this.DB=DB;
		UpdateMonth();
		BufferedValues=new ConcurrentHashMap<String, Long>();
		Bukkit.getScheduler().runTaskTimerAsynchronously(JenoStatistik.instance, new Runnable(){
			@Override
			public void run() {
				//System.out.println("Data Save Start");
				SaveNow();
			}
		},1200,1200);
	}
	public void Shutdown(){
		//ToDo: Save All
		SaveNow();
		DB.close();
	}
	public void addStatCount(String name, StatType type, Long value){
		if(name==null||name.length()==0||type.toString()==null||type.toString().length()==0)return;
		if (BufferedValues.containsKey(name+"~~~"+type.toString()) && type!= StatType.Zuletzt_Online){
			BufferedValues.put(name+"~~~"+type.toString(), BufferedValues.get(name+"~~~"+type.toString())+value);
		}else{
			BufferedValues.put(name+"~~~"+type.toString(), value);
		}
	}
	private void UpdateMonth(){
		Calendar cal = Calendar.getInstance();
		cal.setTime(new Date());
		int month = cal.get(Calendar.MONTH)+1;
		int year = cal.get(Calendar.YEAR);
		String newMonth=+year+"/"+String.format("%02d", month);
		if (!JenoStatistik.instance.Month.equals(newMonth)){
			JenoStatistik.instance.Month=newMonth;
			for (Player p:Bukkit.getOnlinePlayers()){
				checkAndCreateRecords(p.getName());
			}
		}
	}
	private void setLastOnline(){
		for (Player p:Bukkit.getOnlinePlayers()){
			addStatCount(p.getName(), StatType.Zuletzt_Online, System.currentTimeMillis());
		}
	}
	//Needs to be called async!
	private void SaveNow(){
		UpdateMonth();
		setLastOnline();
		Iterator<Entry<String, Long>> it=BufferedValues.entrySet().iterator();
		while (it.hasNext()){
			Entry<String, Long> Item=it.next();
			String Name=Item.getKey().split("~~~")[0];
			StatType Type= StatType.valueOf(Item.getKey().split("~~~")[1]);
			if (Type==StatType.Zuletzt_Online){
				if(DB.setStat(Name, Type, Item.getValue(), JenoStatistik.instance.getConfig().getString("Server"), JenoStatistik.instance.Month)){
					it.remove();
				}
			}else if (Type==StatType.Votes||Type==StatType.Shop_Einnahmen){
				if (!DB.doesRecordExist(Name, Type, JenoStatistik.instance.getConfig().getString("Server"),JenoStatistik.instance.Month)){
					DB.addRecord(Name, Type, 0,JenoStatistik.instance.getConfig().getString("Server"), JenoStatistik.instance.Month);
				}
				if(DB.addStatCount(Name, Type, Item.getValue(),JenoStatistik.instance.getConfig().getString("Server"), JenoStatistik.instance.Month)){
					it.remove();
				}
			}
			else{
				if(DB.addStatCount(Name, Type, Item.getValue(),JenoStatistik.instance.getConfig().getString("Server"), JenoStatistik.instance.Month)){
					it.remove();
				}
			}
		}
	}
	public void checkAndCreateRecords(String Name) {
		for (StatType type: StatType.values()){
			if (!DB.doesRecordExist(Name, type, JenoStatistik.instance.getConfig().getString("Server"),JenoStatistik.instance.Month)){
				if (type==StatType.Erster_Login|| type==StatType.Zuletzt_Online){
					DB.addRecord(Name, type, System.currentTimeMillis(),JenoStatistik.instance.getConfig().getString("Server"), JenoStatistik.instance.Month);
				}else{
					DB.addRecord(Name, type, 0,JenoStatistik.instance.getConfig().getString("Server"), JenoStatistik.instance.Month);
				}
			}
		}
	}
}
