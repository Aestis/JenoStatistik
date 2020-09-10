package Phreag.JenoStatistik2.Listener;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.event.entity.CreatureSpawnEvent.SpawnReason;
import org.bukkit.event.entity.EntityTameEvent;

import Phreag.JenoStatistik2.JenoStatistik;
import Phreag.JenoStatistik2.StatType;

public class BreedingFeedingListener implements Listener{

	@EventHandler(priority = EventPriority.MONITOR)
	public void onEntitySpawn(CreatureSpawnEvent e){
		if(e.isCancelled())return;
		if(e.getSpawnReason()!=SpawnReason.BREEDING)return;
		Player p = getBreeder(e.getLocation());
		if(p!=null){
			JenoStatistik.instance.DBM.addStatCount(p.getName(), StatType.Tiere_Gezuechtet,(long)1);
		}
		
	}
	
	@EventHandler(priority = EventPriority.MONITOR)
	public void onEntityTame(EntityTameEvent e){
		if(e.isCancelled())return;
		JenoStatistik.instance.DBM.addStatCount(e.getOwner().getName(), StatType.Tiere_Gezaehmt,(long)1);
	}
	
	private Player getBreeder(Location loc){
		double min = Double.MAX_VALUE;
		Player candidate = null;
		for (Player p:Bukkit.getOnlinePlayers()){
			if(p.getWorld().equals(loc.getWorld())){
				double dist=getDistance(loc, p.getLocation());
				if (dist<min)candidate=p;
				min=dist;
			}
		}
		
		if(min<10.0){
			return candidate;
		}
		return null;
	}
	
	private double getDistance(Location loc1, Location loc2){
		return Math.sqrt((loc1.getX()-loc2.getX())*(loc1.getX()-loc2.getX())+((loc1.getY()-loc2.getY())*(loc1.getY()-loc2.getY())+((loc1.getZ()-loc2.getZ())*(loc1.getZ()-loc2.getZ())))); 
	}
}
