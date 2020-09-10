package Phreag.JenoStatistik2.Listener;

import java.util.concurrent.ConcurrentHashMap;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerLoginEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import Phreag.JenoStatistik2.JenoStatistik;
import Phreag.JenoStatistik2.StatType;

public class OnTimeAFKListener implements Listener{
	private ConcurrentHashMap<String,Long> LastAction;
	private long LastSave;
	public OnTimeAFKListener(){
		LastAction=new ConcurrentHashMap<String,Long>();
		LastSave=System.currentTimeMillis();
		Bukkit.getScheduler().runTaskTimerAsynchronously(JenoStatistik.instance, new Runnable(){
			@Override
			public void run() {
				SaveTimes();
			}
		},200,200);
	}
	
	private synchronized void SaveTimes(){
		long seconds=(System.currentTimeMillis()-LastSave)/1000;
		LastSave=System.currentTimeMillis();
		for (Player p:Bukkit.getOnlinePlayers()){
			if(LastAction.containsKey(p.getName())){
				if (LastAction.get(p.getName())<(System.currentTimeMillis()-60000)){
					JenoStatistik.instance.DBM.addStatCount(p.getName(), StatType.AFK_Zeit, seconds);
				}
				JenoStatistik.instance.DBM.addStatCount(p.getName(), StatType.Spielzeit, seconds);
			}else{
				LastAction.put(p.getName(), System.currentTimeMillis());
			}
		}
	}
	
	
	@EventHandler(priority = EventPriority.MONITOR)
    public void onInteract(PlayerInteractEvent e){
		//Ignore cancelled
		LastAction.put(e.getPlayer().getName(), System.currentTimeMillis());
	}
	
	@EventHandler(priority = EventPriority.MONITOR)
    public void onLogin(PlayerLoginEvent e){
		LastAction.put(e.getPlayer().getName(), System.currentTimeMillis());
	}
	
	@EventHandler(priority = EventPriority.MONITOR)
    public void onLogout(PlayerQuitEvent e){
		if (e.getPlayer()==null) return;
		//remove
		SaveTimes();
		LastAction.remove(e.getPlayer().getName());
	}
	
	@EventHandler(priority = EventPriority.MONITOR)
    public void onCommand(PlayerCommandPreprocessEvent e){
		LastAction.put(e.getPlayer().getName(), System.currentTimeMillis());
	}
	@EventHandler(priority = EventPriority.MONITOR)
    public void onPlayerMove(PlayerMoveEvent e){
		if (e.isCancelled())return;
		if(e.getFrom().getPitch()!=e.getTo().getPitch()||e.getFrom().getYaw()!=e.getTo().getYaw()){
			LastAction.put(e.getPlayer().getName(), System.currentTimeMillis());
		}
	}
}
