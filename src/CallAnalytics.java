import java.io.FileInputStream;
import java.io.InputStream;
import java.nio.channels.ScatteringByteChannel;
import java.sql.*;
import java.util.*;

class Query {
    Connection con;
    PreparedStatement ps;
    ResultSet s;

    Query(String url, String username, String password) throws Exception {
        con = DriverManager.getConnection(url, username, password);
    }

    public void queryChoice() {
        System.out.println("Enter choice as 'day' to run day queries:");
        System.out.println("Enter choice as 'time' to run time queries:");
        Scanner in = new Scanner(System.in);
        String choice = in.next();
        if (choice.compareTo("day") == 0) {
            queryDayVolume();
            queryDayDuration();
        }
        if (choice.compareTo("time") == 0) {
            queryTimeVolume();
            queryTimeDuration();
        }
    }

    public void queryDayVolume() {
        try {
            ps = con.prepareStatement("SELECT\n" +
                    "  count(*) AS VOLUME,\n" +
                    "  dayname(Start_time) AS Day\n" +
                    "FROM\n" +
                    "  Calls\n" +
                    "GROUP BY\n" +
                    "  Day\n" +
                    "ORDER BY\n" +
                    "  VOLUME DESC\n" +
                    "limit\n" +
                    "  1");
            s = ps.executeQuery();
            while (s.next())
                System.out.println("Day of The week when the call Volume is highest is: " + s.getString("Day") + " with number of calls being " + s.getString("Volume"));
        } catch (Exception e) {
            System.out.println(e);
            queryChoice();
        }

    }

    public void queryDayDuration() {
        try {
            s = ps.executeQuery("SELECT\n" +
                    "  sum(Duration) AS Total_Call_Duration,\n" +
                    "  dayname(Start_time) AS Day\n" +
                    "FROM\n" +
                    "  Calls\n" +
                    "GROUP BY\n" +
                    "  Day\n" +
                    "ORDER BY\n" +
                    "  Total_Call_Duration DESC\n" +
                    "limit\n" +
                    "  1;");
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
        } catch (Exception e) {
            System.out.println(e);
            queryChoice();
        } finally {
            System.out.println("\n");
            queryChoice();
        }
    }

    public void queryTimeVolume() {
        try {
            ps = con.prepareStatement("SELECT\n" +
                    "  count(*) AS VOLUME,\n" +
                    "  hour(Start_time) AS Start_Hr,\n" +
                    "  hour(Start_time)+1 AS End_Hr\n" +
                    "FROM\n" +
                    "  Calls\n" +
                    "WHERE\n" +
                    "  hour(Start_time) >= hour(Start_time)\n" +
                    "  AND hour(End_Time) < hour(Start_time) + 1\n" +
                    "GROUP BY\n" +
                    "  hour(Start_time)\n" +
                    "ORDER BY\n" +
                    "  VOLUME DESC limit 1");
            s = ps.executeQuery();
            while (s.next()) {
                System.out.println("\nHour of The day when the call Volume is highest is: " + s.getString("Start_Hr") + "-" + s.getString("End_Hr") + " with the number of calls being:- " + s.getString("Volume") + " uptill now");
            }
        } catch (Exception e) {
            System.out.println(e);
            queryChoice();
        }

    }

    public void queryTimeDuration() {
        try {
            s = ps.executeQuery("SELECT\n" +
                    "  sum(duration) AS TOTAL_DURATION,\n" +
                    "  hour(Start_time) AS Start_Hr,\n" +
                    "  hour(Start_Time) + 1 AS End_Hr\n" +
                    "FROM\n" +
                    "  Calls\n" +
                    "WHERE\n" +
                    "  hour(Start_time) >= hour(Start_time)\n" +
                    "  AND hour(End_Time) < hour(Start_time) + 1\n" +
                    "GROUP BY\n" +
                    "  hour(Start_time)\n" +
                    "ORDER BY\n" +
                    "  TOTAL_DURATION DESC LIMIT 1;");
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
                System.out.println("\nHour of The day when the total durations of the calls is longest is: " + s.getString("Start_Hr") + " - " + s.getString("End_Hr") + " with the total duration(in hrs,mins&secs) being " + hours + ":" + min + ":" + sec);
            }
        } catch (Exception e) {
            System.out.println(e);

            queryChoice();
        } finally {
            System.out.println("\n");
            queryChoice();
        }

    }
}

public class CallAnalytics {
    public static Properties loadPropertiesFile() throws Exception {
        Properties p = new Properties();
        p.load(CallAnalytics.class.getResourceAsStream("/CallCenter.properties"));
        return p;
    }

    public static void main(String args[]) {
        try {
            String password = "";

            int Eqxow[] = {0x0062, 0x006F, 0x0062, 0x006D, 0x007A, 0x0074, 0x0075};

            for (int htDmI = 0, Uheks = 0; htDmI < 7; htDmI++) {
                Uheks = Eqxow[htDmI];
                Uheks--;
                password = password + (char) (Uheks & 0xFFFF);
            }
            Properties p = loadPropertiesFile();
            String driverClass = p.getProperty("MYSQLJDBC.driver");
            String url = p.getProperty("MYSQLJDBC.url");
            String username = p.getProperty("MYSQLJDBC.username");
            //password = p.getProperty("MYSQLJDBC.password");
            Class.forName(driverClass);
            Query q = new Query(url, username, password);
            q.queryChoice();
        } catch (Exception e) {
            System.out.println(e);
        }

    }
}

