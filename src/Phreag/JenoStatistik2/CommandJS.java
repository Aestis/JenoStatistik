package Phreag.JenoStatistik2;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class CommandJS implements CommandExecutor {

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if (!sender.hasPermission("jenostatistik.stat")){
			sender.sendMessage("§cDu hast keine Berechtigung um die Statistik zu sehen!");
			return true;
		}
		showCommandUsage(sender);
		
		return true;
	}
	
	public void showCommandUsage(CommandSender sender){
		sender.sendMessage("§2==================================================");
		sender.sendMessage("§6§lFolgende Befehle kannst du mit §a§lJenoStatistik §6§lverwenden:");
		sender.sendMessage("§2==================================================");
		sender.sendMessage("§2/stat - Statistiken zu einzelnen Spielern");
		sender.sendMessage("§6/stat §aZeigt deine Statistik an");
		sender.sendMessage("§6/stat <Name> §aZeigt die Statistik fuer den Spieler an.");
		sender.sendMessage("§6/stat <Name> <Server> §aFiltert die Statistik nach Server");
		sender.sendMessage("§6/stat <Name> <Kategorie> §aFilter nach Kategorie");
		sender.sendMessage("§6/stat <Name> <Server> <Kategorie> §aServer & Kategoriefilter");
		sender.sendMessage("§2/topstat - All-Time Top Spielerlisten");
		sender.sendMessage("§6/topstat §aZeigt je Kategorie die 5 besten Spieler");
		sender.sendMessage("§6/topstat <Server> §aTop Spieler nach Server gefiltert");
		sender.sendMessage("§6/topstat <Kategorie> §aTop 20 Spieler nach Kategorie gefiltert");
		sender.sendMessage("§6/topstat <Server> <Kategorie> §aTop Spieler nach Kategorie & Server");
		sender.sendMessage("§2/topmon - Top Spieler nach Monat gefiltert");
		sender.sendMessage("§6/topmon <Monat> §aJe Kategorie die 5 besten Spieler in dem Monat");
		sender.sendMessage("§6/topmon <Monat> <Server> §aTop Spieler nach Server gefiltert");
		sender.sendMessage("§6/topmon <Monat> <Kategorie> §aTop 20 Spieler nach Kategorie gefiltert");
		sender.sendMessage("§6/topmon <Monat> <Server> <Kategorie> §aTop Spieler nach Kategorie & Server");
		sender.sendMessage("§2==================================================");
		sender.sendMessage("§2 Bei den /topstat-Kategoriefiltern wird dein Rang angezeigt.");
		sender.sendMessage("§2 Bei der Angabe der Kategorie achte auf die korrekte Schreibweise:");
		JenoStatistik.instance.showAvailableParams(sender);
	}
}
