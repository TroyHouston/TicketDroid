package teamtrout.swen302.ticketdroid;

import android.graphics.drawable.Drawable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Events {

    public static HashMap<String, Event> events = new HashMap<String, Event>();

    public static void addEvents(){

            events.put("katy", new Event(R.drawable.katy,"22/12/2014","Katy Perry","8:30 PM","TSB Arena","5B"));
            events.put("john2014", new Event(R.drawable.katy,"28/12/2014","John Legend","8:30 PM","Westpac Stadium","26E"));
            events.put("ACDC", new Event(R.drawable.katy,"18/11/2014","ACDC","9:30 PM","WestPac","No Seating"));
            events.put("em2014", new Event(R.drawable.katy,"31/12/2014","Eminem","9:30 PM","RNV","No Seating"));
            events.put("john2014", new Event(R.drawable.katy,"28/12/2014","John Legend","8:30 PM","Westpac Stadium","26E"));
    }

    public static boolean validTicket(String ticketInput) {
        if (events.isEmpty()) {
            addEvents();
        }

        if (events.containsKey(ticketInput)){
            return true;
        }
        return false;
    }


    public static class Event{

        int image;
        String date;
        String name;
        String time;
        String location;
        String seat;

        public Event(int image,String date,String name, String time, String location, String seat){
            this.image = image;
            this.date = date;
            this.name = name;
            this.time = time;
            this.location = location;
            this.seat = seat;
        }

        public String toStringBasic(){
            return String.format("%s\n%s", name, date);
        }

        public String toStringDetailed(){
            return String.format("%s\t%s\n%s\t%s\nSeat:%s", name, date, location,time,seat);
        }

    }

}
