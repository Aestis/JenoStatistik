package Phreag.JenoStatistik2.Listener;

import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;

import Phreag.JenoStatistik2.JenoStatistik;
import Phreag.JenoStatistik2.StatType;

public class BlockPlaceBreakListener implements Listener{
	
	
	@EventHandler(priority = EventPriority.MONITOR)
	public void onBlockPlace(BlockPlaceEvent e){
		if (e.isCancelled())return;
		if (e.getPlayer()==null)return;
		JenoStatistik.instance.DBM.addStatCount(e.getPlayer().getName(), StatType.Gesetzt,(long)1);
	}
	
	@EventHandler(priority = EventPriority.MONITOR)
	public void onBlockBreak(BlockBreakEvent e){
		if (e.isCancelled())return;
		if (e.getPlayer()==null)return;
		JenoStatistik.instance.DBM.addStatCount(e.getPlayer().getName(), StatType.Abgebaut,(long)1);
	}

}
