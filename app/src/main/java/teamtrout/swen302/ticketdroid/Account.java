package teamtrout.swen302.ticketdroid;

import java.util.ArrayList;
import java.util.List;

public class Account {
    private String user;
    private String password;
    private List<String> tickets;

    public Account(String user, String password){
        this.user = user;
        this.password = password;
        this.tickets = new ArrayList<String>();
    }

    public String getPassword(){
        return password;
    }

    public void addTicket(String ticket){
        tickets.add(ticket);
    }

    public String getTicket(int i){
        return tickets.get(i);
    }

    public void deleteTicket(int i){
        tickets.remove(i);
    }

    public int ticketSize() {
        return tickets.size();
    }

    public String getUser(){
        return user;
    }
}
