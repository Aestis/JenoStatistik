package Phreag.JenoStatistik2.Listener;

import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;

import Phreag.JenoStatistik2.JenoStatistik;
import Phreag.JenoStatistik2.StatType;
import de.Keyle.MyPet.api.event.MyPetCreateEvent;
import de.Keyle.MyPet.api.event.MyPetCreateEvent.Source;

public class MyPetListener implements Listener{
	
	@EventHandler(priority = EventPriority.MONITOR)
    public void onPetCreate(MyPetCreateEvent e){
		if(e.getSource()==Source.Leash){
			JenoStatistik.instance.DBM.addStatCount(e.getOwner().getName(),StatType.Pets_Gefangen, (long)1);
		}
	}
}
