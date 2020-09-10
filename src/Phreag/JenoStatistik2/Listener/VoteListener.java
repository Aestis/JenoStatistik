package Phreag.JenoStatistik2.Listener;

import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;

import com.vexsoftware.votifier.model.VotifierEvent;

import Phreag.JenoStatistik2.JenoStatistik;
import Phreag.JenoStatistik2.StatType;

public class VoteListener implements Listener{
	
	@EventHandler(priority = EventPriority.MONITOR)
	public void onVotifierEvent(VotifierEvent e){
		//cannot be cancelled
		final String Name = e.getVote().getUsername();
		JenoStatistik.instance.DBM.addStatCount(Name, StatType.Votes, (long)1);
	}
}
