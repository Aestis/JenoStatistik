package Phreag.JenoStatistik2.Listener;

import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import Phreag.JenoStatistik2.JenoStatistik;
import Phreag.JenoStatistik2.StatType;

public class LoginLogoutListener implements Listener{
	
	@EventHandler(priority = EventPriority.MONITOR)
    public void onPlayerQuit(final PlayerQuitEvent e){
		//cannot be cancelled
		String name=e.getPlayer().getName();
	    JenoStatistik.instance.DBM.addStatCount(name, StatType.Zuletzt_Online, System.currentTimeMillis());   
	}
	
	@EventHandler(priority = EventPriority.MONITOR)
    public void onPlayerLogin(final PlayerJoinEvent e){
		//cannot be cancelled
		final String name=e.getPlayer().getName();
		Bukkit.getScheduler().runTaskAsynchronously(JenoStatistik.instance, new Runnable(){
			@Override
			public void run() {
				JenoStatistik.instance.DBM.checkAndCreateRecords(name);
				JenoStatistik.instance.DBM.addStatCount(name, StatType.Logins, (long)1);
			}
		});
	}
}
