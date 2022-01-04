import java.sql.*;
import java.util.*;

public class CallAnalytics {
    public static void main(String args[])  {
        try {
            Class.forName("com.mysql.jdbc.Driver");
            Connection con = DriverManager.getConnection(
                    "jdbc:mysql://callrecord.cjqbe0i8s3kd.us-east-1.rds.amazonaws.com:3306/CallRecord?characterEncoding=utf8&useSSL=false&useUnicode=true", "admin", "callrecords");
            PreparedStatement ps=con.prepareStatement("SELECT count(*) as Volume,Day FROM Calls group by Day order by Volume DESC limit 1");
            ResultSet s=ps.executeQuery();
            while(s.next())
                System.out.println("Day of The week when the call Volume is highest is: "+s.getString("Day")+" with number of calls being "+s.getString("Volume"));
            s=ps.executeQuery("SELECT sum(duration) as Total_Call_Duration,Day FROM Calls group by Day limit 1;");
            while(s.next()) {
                int hours,mins;
                int secs=Integer.parseInt(s.getString("Total_Call_Duration"));
                hours = secs / 3600;
                secs=secs%3600;
                mins=secs/60;
                secs=secs%60;
                System.out.println("\nDay of The week when the total durations of the calls is longest is: " + s.getString("Day") + " with the total duration(in hrs,mins&secs) being "+hours+":"+mins+":"+secs);
            }
            s=ps.executeQuery("SELECT count(*) as Volume,t.sStart as Start_Hr,t.sEnd as End_Hr FROM timeslots t,Calls c WHERE time(c.Start_time)>=t.sStart&&time(c.End_Time)<t.sEnd group by t.sStart Order By Volume DESC limit 1");
            while(s.next())
            {
                System.out.println("\nHour of The day when the call Volume is highest is: "+s.getString("Start_Hr")+"-"+s.getString("End_Hr")+" with the number of calls being:- "+s.getString("Volume")+" uptill now");
            }
            s=ps.executeQuery("SELECT sum(duration) as Total_Duration,sStart,sEnd FROM Calls JOIN timeslots WHERE time(Start_time)>=sStart&&time(End_Time)<sEnd group by sStart Order By Total_Duration DESC limit 1");
            while(s.next()) {
                int hours,mins;
                int secs=Integer.parseInt(s.getString("Total_Duration"));
                hours = secs / 3600;
                secs=secs%3600;
                mins=secs/60;
                secs=secs%60;

                System.out.println("\nHour of The day when the total durations of the calls is longest is: " + s.getString("sStart") + " - " +s.getString("sEnd")+" with the total duration(in hrs,mins&secs) being "+hours+":"+mins+":"+secs);
            }
            con.close();

        } catch (Exception e) {
            System.out.println(e);
        }

    }
}