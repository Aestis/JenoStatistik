package Phreag.JenoStatistik2.Listener;

import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import Phreag.JenoStatistik2.JenoStatistik;
import Phreag.JenoStatistik2.StatType;

public class DeathDamageListener implements Listener{
	
	@EventHandler(priority = EventPriority.MONITOR)
	//Spieler Stirbt
    public void onPlayerDeath(PlayerDeathEvent e){
		//cannot be cancelled
		JenoStatistik.instance.DBM.addStatCount(e.getEntity().getName(), StatType.Tode,(long)1); 
	}
	
	@EventHandler(priority = EventPriority.MONITOR)
	//Schaden
    public void onMobDeath(EntityDamageByEntityEvent e){
		if (e.isCancelled())return;
		if (e.getDamager().getType()==EntityType.PLAYER){
			Player damager=(Player)e.getDamager();
			if (e.getEntity().getType()==EntityType.PLAYER){
				Player damaged=(Player)e.getEntity();
				
				if(damaged.getHealth()-e.getFinalDamage()<=0){
					JenoStatistik.instance.DBM.addStatCount(damager.getName(), StatType.Spieler_Kills,(long)1);
					JenoStatistik.instance.DBM.addStatCount(damager.getName(), StatType.Spieler_Schaden,(long)(2*damaged.getHealth()));
				}else{
					JenoStatistik.instance.DBM.addStatCount(damager.getName(), StatType.Spieler_Schaden,(long)(2*e.getFinalDamage()));
				}
			}else{
				if (e.getEntity() instanceof LivingEntity){
					LivingEntity damaged=(LivingEntity) e.getEntity();
					if(damaged.getHealth()-e.getFinalDamage()<=0){
						JenoStatistik.instance.DBM.addStatCount(damager.getName(), StatType.Mob_Kills,(long)1);
					}
				}
			}
		}
	}
	
	@EventHandler(priority = EventPriority.MONITOR)
	public void onEntityDamage(EntityDamageEvent e){
		if (e.isCancelled())return;
		if (e.getEntityType()==EntityType.PLAYER){
			Player p = (Player)e.getEntity();
			JenoStatistik.instance.DBM.addStatCount(p.getName(), StatType.Erhaltener_Schaden,(long)(2*e.getFinalDamage()));
		}
	}
}
