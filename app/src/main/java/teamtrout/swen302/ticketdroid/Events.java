package teamtrout.swen302.ticketdroid;

import android.graphics.drawable.Drawable;

import java.util.ArrayList;
import java.util.List;

public class Events {

    private static List<Event> events = new ArrayList<Event>();

    public static void addEvents(){

            events.add(new Event(R.drawable.katy,"22/12/2014","Katy Perry","8:30 PM","TSB Arena","5B","katy2014c1"));
            events.add(new Event(R.drawable.katy,"28/12/2014","John Legend","8:30 PM","Westpac Stadium","26E","john2014"));
            events.add(new Event(R.drawable.katy,"18/11/2014","ACDC","9:30 PM","ACDC","No Seating","ACDC"));
            events.add(new Event(R.drawable.katy,"20/12/2014","Katy Perry","8:30 PM","TSB Arena","25F","katy2014c2"));
            events.add(new Event(R.drawable.katy,"31/12/2014","Eminem","9:30 PM","RNV","No Seating","em2014"));
    }

    public static boolean validTicket(String ticketInput) {
        if (events.isEmpty()) {
            addEvents();
        }

        for(int i = 0 ; i < events.size() ; i ++){
            if(events.get(i).ticketHash.equals(ticketInput))
                return true;
        }
        return false;
    }

    private static class Event{

        int image;
        String date;
        String name;
        String time;
        String location;
        String seat;
        String ticketHash;

        public Event(int image,String date,String name, String time, String location, String seat, String ticketHash){
            this.image = image;
            this.date = date;
            this.name = name;
            this.time = time;
            this.location = location;
            this.seat = seat;
            this.ticketHash = ticketHash;
        }

    }

}
