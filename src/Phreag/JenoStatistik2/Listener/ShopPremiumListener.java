package Phreag.JenoStatistik2.Listener;

import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;

import com.snowgears.shop.ShopType;
import com.snowgears.shop.event.PlayerExchangeShopEvent;
import com.snowgears.shop.event.PlayerInitializeShopEvent;

import Phreag.JenoStatistik2.JenoStatistik;
import Phreag.JenoStatistik2.StatType;

public class ShopPremiumListener implements Listener{
	
	
	@EventHandler(priority = EventPriority.MONITOR)
	public void onShopUse(PlayerExchangeShopEvent e){
		if(e.isCancelled())return;
		if (e.getType()==ShopType.SELL){
			JenoStatistik.instance.DBM.addStatCount(e.getShop().getOwnerName(),StatType.Shop_Einnahmen, (long)e.getShop().getPrice());
		}
	}
	
	@EventHandler(priority = EventPriority.MONITOR)
	public void onShopCreate(PlayerInitializeShopEvent e){
		JenoStatistik.instance.DBM.addStatCount(e.getPlayer().getName(),StatType.Shops_Erstellt, (long)1);
	}
}
