package Phreag.JenoStatistik2.Listener;

import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;

import com.Acrobot.ChestShop.Events.ShopCreatedEvent;
import com.Acrobot.ChestShop.Events.TransactionEvent;
import com.Acrobot.ChestShop.Events.TransactionEvent.TransactionType;

import Phreag.JenoStatistik2.JenoStatistik;
import Phreag.JenoStatistik2.StatType;

public class ChestShopListener implements Listener{
	
	
	@EventHandler(priority = EventPriority.MONITOR)
	public void onShopUse(TransactionEvent e){
		if (e.getTransactionType()==TransactionType.SELL){
			JenoStatistik.instance.DBM.addStatCount(e.getOwner().getName(),StatType.Shop_Einnahmen, (long)e.getPrice());
		}
		
	}
	
	@EventHandler(priority = EventPriority.MONITOR)
	public void onShopCreate(ShopCreatedEvent e){
		JenoStatistik.instance.DBM.addStatCount(e.getPlayer().getName(),StatType.Shops_Erstellt, (long)1);
	}
}
