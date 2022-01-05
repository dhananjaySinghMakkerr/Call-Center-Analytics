import java.io.FileInputStream;
import java.io.InputStream;
import java.sql.*;
import java.util.*;

public class CallAnalytics {
    public static Properties loadPropertiesFile() throws Exception {
        Properties p = new Properties();
        p.load(CallAnalytics.class.getResourceAsStream("/CallCenter.properties"));
        return p;
    }

    public static void main(String args[]) {
        try {
            Properties p = loadPropertiesFile();
            String driverClass = p.getProperty("MYSQLJDBC.driver");
            String url = p.getProperty("MYSQLJDBC.url");
            String username = p.getProperty("MYSQLJDBC.username");
            String pass = p.getProperty("MYSQLJDBC.password");
            Class.forName(driverClass);
            Connection con = DriverManager.getConnection(url, username, pass);
            PreparedStatement ps = con.prepareStatement("SELECT count(*) as Volume,Day FROM Calls group by Day order by Volume DESC limit 1");
            ResultSet s = ps.executeQuery();
            while (s.next())
                System.out.println("Day of The week when the call Volume is highest is: " + s.getString("Day") + " with number of calls being " + s.getString("Volume"));
            s = ps.executeQuery("SELECT sum(duration) as Total_Call_Duration,Day FROM Calls group by Day limit 1;");
            while (s.next()) {
                String min, sec;
                int hours, mins;
                int secs = Integer.parseInt(s.getString("Total_Call_Duration"));
                hours = secs / 3600;
                secs = secs % 3600;
                mins = secs / 60;
                secs = secs % 60;
                if (mins == 0) {
                    min = Integer.toString(mins);
                    min = min + '0';
                } else {
                    min = Integer.toString(mins);
                }
                if (secs == 0) {
                    sec = Integer.toString(secs);
                    sec = sec + '0';
                } else {
                    sec = Integer.toString(secs);
                }
                System.out.println("\nDay of The week when the total durations of the calls is longest is: " + s.getString("Day") + " with the total duration(in hrs,mins&secs) being " + hours + ":" + min + ":" + sec);
            }
            s = ps.executeQuery("SELECT count(*) as Volume,t.sStart as Start_Hr,t.sEnd as End_Hr FROM timeslots t,Calls c WHERE time(c.Start_time)>=t.sStart&&time(c.End_Time)<t.sEnd group by t.sStart Order By Volume DESC limit 1");
            while (s.next()) {
                System.out.println("\nHour of The day when the call Volume is highest is: " + s.getString("Start_Hr") + "-" + s.getString("End_Hr") + " with the number of calls being:- " + s.getString("Volume") + " uptill now");
            }
            s = ps.executeQuery("SELECT sum(duration) as Total_Duration,sStart,sEnd FROM Calls JOIN timeslots WHERE time(Start_time)>=sStart&&time(End_Time)<sEnd group by sStart Order By Total_Duration DESC limit 1");
            while (s.next()) {
                String sec, min;
                int hours, mins;
                int secs = Integer.parseInt(s.getString("Total_Duration"));
                hours = secs / 3600;
                secs = secs % 3600;
                mins = secs / 60;
                secs = secs % 60;
                if (mins == 0) {
                    min = Integer.toString(mins);
                    min = min + '0';
                } else {
                    min = Integer.toString(mins);
                }
                if (secs == 0) {
                    sec = Integer.toString(secs);
                    sec = sec + '0';
                } else {
                    sec = Integer.toString(secs);
                }
                System.out.println("\nHour of The day when the total durations of the calls is longest is: " + s.getString("sStart") + " - " + s.getString("sEnd") + " with the total duration(in hrs,mins&secs) being " + hours + ":" + min + ":" + sec);
            }
            con.close();

        } catch (Exception e) {
            System.out.println(e);
        }

    }

