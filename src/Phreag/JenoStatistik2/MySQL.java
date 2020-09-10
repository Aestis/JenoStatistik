package Phreag.JenoStatistik2;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class MySQL {
	private Connection con;
	
	public ResultSet getTopMonth(String Month){
		PreparedStatement stmt = null;
		try {
			String sql="";
			for (StatType type: StatType.values()){
				if(type==StatType.Erster_Login||type==StatType.Zuletzt_Online){
					
				}else{
					if (sql.equals("")){
						sql="(SELECT player, type, SUM(count) as count FROM stats2 WHERE type = ? AND month = ? GROUP by player, type, month ORDER BY count DESC LIMIT 3)";
					}else{
						sql=sql+" UNION ALL (SELECT player, type, SUM(count) as count FROM stats2 WHERE type = ? AND month = ? GROUP by player, type, month ORDER BY count DESC LIMIT 3)";
					}
				}
			}
			stmt = con.prepareStatement(sql);
			int i = 1;
			for (StatType type: StatType.values()){
				if(type==StatType.Erster_Login||type==StatType.Zuletzt_Online){	
				}else{
					stmt.setString(i, type.toString());
					stmt.setString(i+1, Month);
					i=i+2;
				}
			}
		    ResultSet rs=stmt.executeQuery();
		    //stmt.close();
		    return rs;    
		} catch (SQLException e) {
			JenoStatistik.instance.getLogger().info("Fehler beim Abfragen der Top Spieler");
			e.printStackTrace();
			return null;
		}
	}
	public ResultSet getTopMonth(String Server, String Month){
		PreparedStatement stmt = null;
		try {
			String sql="";
			for (StatType type: StatType.values()){
				if(type==StatType.Erster_Login||type==StatType.Zuletzt_Online){
					
				}else{
					if (sql.equals("")){
						sql="(SELECT player, type, SUM(count) as count FROM stats2 WHERE type = ? AND server = ? AND month = ? GROUP by player, type, server, month ORDER BY count DESC LIMIT 3)";
					}else{
						sql=sql+" UNION ALL (SELECT player, type, SUM(count) as count FROM stats2 WHERE type = ? AND server = ? AND month = ? GROUP by player, type, server, month ORDER BY count DESC LIMIT 3)";
					}
				}
			}
			stmt = con.prepareStatement(sql);
			int i = 1;
			for (StatType type: StatType.values()){
				if(type==StatType.Erster_Login||type==StatType.Zuletzt_Online){	
				}else{
					stmt.setString(i, type.toString());
					stmt.setString(i+1, Server);
					stmt.setString(i+2, Month);
					i=i+3;
				}
			}
		    ResultSet rs=stmt.executeQuery();
		    //stmt.close();
		    return rs; 
		} catch (SQLException e) {
			JenoStatistik.instance.getLogger().info("Fehler beim Abfragen der Top Spieler auf dem Server "+Server);
			e.printStackTrace();
			return null;
		}
	}
	public ResultSet getTopKatMonth(StatType type, String Month){
		PreparedStatement stmt = null;
		try {
			stmt = con.prepareStatement("SELECT player, type, SUM(count) as count FROM stats2 WHERE type = ? AND month = ? GROUP BY type, player, month ORDER BY type ASC, count DESC");
			stmt.setString(1, type.toString());
			stmt.setString(2, Month);
		    ResultSet rs=stmt.executeQuery();
		    //stmt.close();
		    return rs;     
		      
		} catch (SQLException e) {
			JenoStatistik.instance.getLogger().info("Fehler beim Abfragen der Top Spieler in der Kategorie "+type.toString());
			e.printStackTrace();
			return null;
		}
	}
	public ResultSet getTopKatMonth(StatType type, String Server, String Month){
		PreparedStatement stmt = null;
		try {
			stmt = con.prepareStatement("SELECT player, type, SUM(count) as count FROM stats2 WHERE server = ? AND type = ? AND month = ? GROUP BY type, player, server, month ORDER BY type ASC, count DESC");
			stmt.setString(1, Server);
			stmt.setString(2, type.toString());
			stmt.setString(3, Month);
		    ResultSet rs=stmt.executeQuery();
		    //stmt.close();
		    return rs;         
		} catch (SQLException e) {
			JenoStatistik.instance.getLogger().info("Fehler beim Abfragen der Top Spieler in der Kategorie "+type.toString()+" auf dem Server "+Server);
			e.printStackTrace();
			return null;
		}
	}
	
	//Top 
	public ResultSet getTop(){
		PreparedStatement stmt = null;
		try {
			String sql="";
			for (StatType type: StatType.values()){
				if(type==StatType.Erster_Login||type==StatType.Zuletzt_Online){
					
				}else{
					if (sql.equals("")){
						sql="(SELECT player, type, SUM(count) as count FROM stats2 WHERE type = ? GROUP by player, type ORDER BY count DESC LIMIT 3)";
					}else{
						sql=sql+" UNION ALL (SELECT player, type, SUM(count) as count FROM stats2 WHERE type = ? GROUP by player, type ORDER BY count DESC LIMIT 3)";
					}
				}
			}
			stmt = con.prepareStatement(sql);
			int i = 1;
			for (StatType type: StatType.values()){
				if(type==StatType.Erster_Login||type==StatType.Zuletzt_Online){	
				}else{
					stmt.setString(i, type.toString());
					i++;
				}
			}
		    ResultSet rs=stmt.executeQuery();
		    //stmt.close();
		    return rs;    
		} catch (SQLException e) {
			JenoStatistik.instance.getLogger().info("Fehler beim Abfragen der Top Spieler");
			e.printStackTrace();
			return null;
		}
	}
	public ResultSet getTop(String Server){
		PreparedStatement stmt = null;
		try {
			String sql="";
			for (StatType type: StatType.values()){
				if(type==StatType.Erster_Login||type==StatType.Zuletzt_Online){
					
				}else{
					if (sql.equals("")){
						sql="(SELECT player, type, SUM(count) as count FROM stats2 WHERE type = ? AND server = ? GROUP by player, type, server ORDER BY count DESC LIMIT 3)";
					}else{
						sql=sql+" UNION ALL (SELECT player, type, SUM(count) as count FROM stats2 WHERE type = ? AND server = ? GROUP by player, type, server ORDER BY count DESC LIMIT 3)";
					}
				}
			}
			stmt = con.prepareStatement(sql);
			int i = 1;
			for (StatType type: StatType.values()){
				if(type==StatType.Erster_Login||type==StatType.Zuletzt_Online){	
				}else{
					stmt.setString(i, type.toString());
					stmt.setString(i+1, Server);
					i=i+2;
				}
			}
		    ResultSet rs=stmt.executeQuery();
		    //stmt.close();
		    return rs; 
		} catch (SQLException e) {
			JenoStatistik.instance.getLogger().info("Fehler beim Abfragen der Top Spieler auf dem Server "+Server);
			e.printStackTrace();
			return null;
		}
	}
	public ResultSet getTopKat(StatType type){
		PreparedStatement stmt = null;
		try {
			stmt = con.prepareStatement("SELECT player, type, SUM(count) as count FROM stats2 WHERE type = ? GROUP BY type, player ORDER BY type ASC, count DESC");
			stmt.setString(1, type.toString());
		    ResultSet rs=stmt.executeQuery();
		    //stmt.close();
		    return rs;     
		      
		} catch (SQLException e) {
			JenoStatistik.instance.getLogger().info("Fehler beim Abfragen der Top Spieler in der Kategorie "+type.toString());
			e.printStackTrace();
			return null;
		}
	}
	public ResultSet getTopKat(StatType type, String Server){
		PreparedStatement stmt = null;
		try {
			stmt = con.prepareStatement("SELECT player, type, SUM(count) as count FROM stats2 WHERE server = ? AND type = ? GROUP BY type, player, server ORDER BY type ASC, count DESC");
			stmt.setString(1, Server);
			stmt.setString(2, type.toString());
		    ResultSet rs=stmt.executeQuery();
		    //stmt.close();
		    return rs;         
		} catch (SQLException e) {
			JenoStatistik.instance.getLogger().info("Fehler beim Abfragen der Top Spieler in der Kategorie "+type.toString()+" auf dem Server "+Server);
			e.printStackTrace();
			return null;
		}
	}
	
	
	
	//Details je Server und Type je server
	public ResultSet getPlayerStatDetails(String name, StatType type,  int numberOfMonths){
		PreparedStatement stmt = null;
		try {
			  
		      stmt = con.prepareStatement("SELECT month, SUM(count) as count FROM stats2 WHERE player = ? AND type = ? GROUP BY type, month, player ORDER BY month DESC LIMIT ?");
		      stmt.setString(1, name);
		      stmt.setString(2, type.toString());
		      stmt.setInt(3, numberOfMonths);
		      ResultSet rs=stmt.executeQuery();
		      //stmt.close();
		      return rs;
		} catch (SQLException e) {
			JenoStatistik.instance.getLogger().info("Fehler beim Abfragen des Eintrages fuer "+ name);
			e.printStackTrace();
			return null;
		}
	}
	public ResultSet getPlayerStatDetailsServer(String name, String Server, StatType type,  int numberOfMonths){
		PreparedStatement stmt = null;
		try {
			  
		      stmt = con.prepareStatement("SELECT month, count FROM stats2 WHERE player = ? AND type = ? AND server = ? ORDER BY month DESC LIMIT ?");
		      stmt.setString(1, name);
		      stmt.setString(2, type.toString());
		      stmt.setString(3, Server);
		      stmt.setInt(4, numberOfMonths);
		      ResultSet rs=stmt.executeQuery();
		      //stmt.close();
		      return rs;
		} catch (SQLException e) {
			JenoStatistik.instance.getLogger().info("Fehler beim Abfragen des Eintrages fuer "+ name);
			e.printStackTrace();
			return null;
		}
	}
	//Summe für den Spieler und Typ auf Spez. Server - sollte 1 Element sein
	public ResultSet getPlayerStatDetailSum(String name, StatType type, String Server){
		PreparedStatement stmt = null;
		try {
		      stmt = con.prepareStatement("SELECT SUM(count) as count FROM stats2 WHERE player = ? AND type = ? AND server = ? GROUP BY type, player ORDER BY type ASC");
		      stmt.setString(1, name);
		      stmt.setString(2, type.toString());
		      stmt.setString(3, Server);
		      ResultSet rs=stmt.executeQuery();
		      //stmt.close();
		      return rs;
		} catch (SQLException e) {
			JenoStatistik.instance.getLogger().info("Fehler beim Abfragen des Eintrages fuer "+ name);
			e.printStackTrace();
			return null;
		}
	}
	//Summe für den Spieler und Typ auf allen Servern - sollte 1 Element sein
		public ResultSet getPlayerStatDetailSum(String name, StatType type){
			PreparedStatement stmt = null;
			try {
			      stmt = con.prepareStatement("SELECT SUM(count) as count FROM stats2 WHERE player = ? AND type = ? GROUP BY type, player ORDER BY type ASC");
			      stmt.setString(1, name);
			      stmt.setString(2, type.toString());
			      ResultSet rs=stmt.executeQuery();
			      //stmt.close();
			      return rs;
			} catch (SQLException e) {
				JenoStatistik.instance.getLogger().info("Fehler beim Abfragen des Eintrages fuer "+ name);
				e.printStackTrace();
				return null;
			}
		}
	//Alles je Server und Spieler - Liste
	public ResultSet getPlayerStatSum(String name, String server){
		PreparedStatement stmt = null;
		try {
			String sql="(SELECT type, SUM(count) as count FROM stats2 WHERE player = ? AND server = ? AND type <> ? AND type <> ? GROUP BY type, player, server ORDER BY type ASC)";
		    sql=sql+"UNION (SELECT type, count FROM stats2 WHERE player = ? AND server = ? AND type = ? ORDER BY type DESC LIMIT 1)" ;
		    sql=sql+"UNION (SELECT type, count FROM stats2 WHERE player = ? AND server = ? AND type = ? ORDER BY type DESC LIMIT 1)" ;
			stmt = con.prepareStatement(sql);
		      stmt.setString(1, name);
		      stmt.setString(2, server);
		      stmt.setString(3, StatType.Erster_Login.toString());
		      stmt.setString(4, StatType.Zuletzt_Online.toString());
		      stmt.setString(5, name);
		      stmt.setString(6, server);
		      stmt.setString(7, StatType.Erster_Login.toString());
		      stmt.setString(8, name);
		      stmt.setString(9, server);
		      stmt.setString(10, StatType.Zuletzt_Online.toString());
		      ResultSet rs=stmt.executeQuery();
		      //stmt.close();
		      return rs;
		} catch (SQLException e) {
			JenoStatistik.instance.getLogger().info("Fehler beim Abfragen des Eintrages fuer "+ name);
			e.printStackTrace();
			return null;
		}
	}
	//Alles für den Spieler
	public ResultSet getPlayerStatSum(String name){
		PreparedStatement stmt = null;
		try {
			String sql="(SELECT type, SUM(count) as count FROM stats2 WHERE player = ? AND type <> ? AND type <> ? GROUP BY type, player ORDER BY type ASC)";
		    sql=sql+"UNION (SELECT type, count FROM stats2 WHERE player = ? AND type = ? ORDER BY count ASC LIMIT 1)" ;
		    sql=sql+"UNION (SELECT type, count FROM stats2 WHERE player = ? AND type = ? ORDER BY count DESC LIMIT 1)" ;
			stmt = con.prepareStatement(sql);
		      stmt.setString(1, name);
		      stmt.setString(2, StatType.Erster_Login.toString());
		      stmt.setString(3, StatType.Zuletzt_Online.toString());
		      stmt.setString(4, name);
		      stmt.setString(5, StatType.Erster_Login.toString());
		      stmt.setString(6, name);
		      stmt.setString(7, StatType.Zuletzt_Online.toString());
		      ResultSet rs=stmt.executeQuery();
		      //stmt.close();
		      return rs;
		} catch (SQLException e) {
			JenoStatistik.instance.getLogger().info("Fehler beim Abfragen des Eintrages fuer "+ name);
			e.printStackTrace();
			return null;
		}
	}
	
	public boolean addStatCount(String name, StatType type, long value, String Server, String month){
		//System.out.println("AddstatCount "+name+" "+type.toString()+" "+value);
		PreparedStatement stmt = null;
		try {
			  stmt = con.prepareStatement("UPDATE stats2 SET count = count + ? WHERE server = ? AND player = ? and month = ? and type = ?");
		      stmt.setLong(1, value);
		      stmt.setString(2, Server);
		      stmt.setString(3, name);
		      stmt.setString(4, month);
		      stmt.setString(5, type.toString());
		      stmt.executeUpdate();
		      stmt.close();
		} catch (SQLException e) {
			JenoStatistik.instance.getLogger().info("Fehler beim Updaten des Eintrages fuer "+ name+" ["+type+"="+value+"]");
			e.printStackTrace();
			return false;
		}
		return true;
	}
	public boolean setStat(String name, StatType type, long value, String Server, String month){
		PreparedStatement stmt = null;
		try {
			  if(type==StatType.Zuletzt_Online||type==StatType.Erster_Login){
				  month="ANY";
			  }
			  stmt = con.prepareStatement("UPDATE stats2 SET count = ? WHERE server = ? AND player = ? and month = ? and type = ?");
		      stmt.setLong(1, value);
		      stmt.setString(2, Server);
		      stmt.setString(3, name);
		      stmt.setString(4, month);
		      stmt.setString(5, type.toString());
		      stmt.executeUpdate();
		      stmt.close();
		} catch (SQLException e) {
			JenoStatistik.instance.getLogger().info("Fehler beim Setzen des Eintrages fuer "+ name+" ["+type+"="+value+"]");
			e.printStackTrace();
			return false;
		}
		return true;
	}
	
	
	public boolean addRecord(String name, StatType type, long value,String Server, String month){
		PreparedStatement stmt = null;
		try {
			  if(type==StatType.Zuletzt_Online||type==StatType.Erster_Login){
				  month="ANY";
			  }
		      stmt = con.prepareStatement("INSERT INTO stats2(player, server, month, type, count) VALUES (?,?,?,?,?)");
		      stmt.setString(1, name);
		      stmt.setString(2, Server);
		      stmt.setString(3, month);
		      stmt.setString(4, type.toString());
		      stmt.setLong(5, value);
		      stmt.executeUpdate();
		      stmt.close();
		} catch (SQLException e) {
			JenoStatistik.instance.getLogger().info("Fehler beim Erstellen des Eintrages fuer "+ name+" ["+type+"="+value+"]");
			e.printStackTrace();
			return false;
		}
		return true;
	}
	public boolean importRecord(String name, String Server, StatType type, long value){
		PreparedStatement stmt = null;
		try {
			  String month="2017 und davor";
			  if(type==StatType.Zuletzt_Online||type==StatType.Erster_Login){
				  month="ANY";
			  }
		      stmt = con.prepareStatement("INSERT INTO stats2(player, server, month, type, count) VALUES (?,?,?,?,?)");
		      stmt.setString(1, name);
		      stmt.setString(2, Server);
		      stmt.setString(3, month);
		      stmt.setString(4, type.toString());
		      stmt.setLong(5, value);
		      stmt.executeUpdate();
		      stmt.close();
		} catch (SQLException e) {
			JenoStatistik.instance.getLogger().info("Fehler beim Erstellen des Eintrages fuer "+ name+" ["+type+"="+value+"]");
			e.printStackTrace();
			return false;
		}
		return true;
	}
	public ResultSet getOldDB(){
		PreparedStatement stmt = null;
		try {
		      stmt = con.prepareStatement("SELECT * from stats");
		      ResultSet rs=stmt.executeQuery();
		      return rs;
		} catch (SQLException e) {
			JenoStatistik.instance.getLogger().info("Fehler beim Abfragen der alten Datenbank");
			e.printStackTrace();
			return null;
		}
	}
	public boolean doesRecordExist(String name, StatType type, String Server, String month){
		PreparedStatement stmt = null;
		try {
			  if(type==StatType.Zuletzt_Online||type==StatType.Erster_Login){
				  month="ANY";
			  }
			  stmt = con.prepareStatement("SELECT * FROM stats2 WHERE player = ? AND server = ? and month = ? and type = ?");
		      stmt.setString(1, name);
		      stmt.setString(2, Server);
		      stmt.setString(3, month);
		      stmt.setString(4, type.toString());
		      ResultSet rs= stmt.executeQuery();
		      if (rs.next()){
		    	  rs.close();
		    	  stmt.close();
		    	  return true;
		      }
		      rs.close();
		      stmt.close();
		      return false;
		} catch (SQLException e) {
			JenoStatistik.instance.getLogger().info("Fehler beim Checken des Eintrages fuer "+ name+" ["+type+"]");
			e.printStackTrace();
			return false;
		}
	
	}

	
	
	private boolean createTables() {
    	try {
    		con.createStatement().executeUpdate("CREATE TABLE IF NOT EXISTS stats2 (id INT NOT NULL AUTO_INCREMENT PRIMARY KEY, player VARCHAR(64), server VARCHAR(64), month VARCHAR(64) ,type VARCHAR(64), count BIGINT(64))");
    		return true;
		} catch (SQLException e) {
			JenoStatistik.instance.getLogger().info("Fehler beim Erstellen der Tabellen!");
			e.printStackTrace();
			return false;
		}
    }
	public boolean connect() {
        try {
            Class.forName("com.mysql.jdbc.Driver");
        } catch(ClassNotFoundException e) {
        	JenoStatistik.instance.getLogger().info("Konnte den MySQL-Driver nicht finden!");
            return false;
        }
        try {
            con = DriverManager.getConnection("jdbc:mysql://" + JenoStatistik.instance.getConfig().getString("Mysql.host") + ":" + JenoStatistik.instance.getConfig().getString("Mysql.port") + "/" + JenoStatistik.instance.getConfig().getString("Mysql.database") + "?user=" + JenoStatistik.instance.getConfig().getString("Mysql.username") + "&password=" + JenoStatistik.instance.getConfig().getString("Mysql.password") + "&autoReconnect=true&useSSL=false");
            if(!(con.isClosed())) {
            	JenoStatistik.instance.getLogger().info("Erfolgreich mit MySQL verbunden!");
            }
        } catch(SQLException e) {
        	JenoStatistik.instance.getLogger().info("Konnte nicht mit MySQL verbinden! Error: " + e.getMessage());
            return false;
        }
        
    return createTables();
    }
	public void close() {
	    try {
	        if(con != null && (!(con.isClosed()))) {
	            con.close();
	            if(con.isClosed()) {
	            	JenoStatistik.instance.getLogger().info("MySQL-Verbindung geschlossen.");
	            }
	        }
	    } catch(SQLException e) {
	    	JenoStatistik.instance.getLogger().info("Fehler beim Schliessen der MySQL-Verbindung.");
	    }
	}
}
