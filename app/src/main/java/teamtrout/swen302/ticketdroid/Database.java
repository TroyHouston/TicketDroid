package teamtrout.swen302.ticketdroid;

import android.content.Context;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Scanner;

public class Database {

    //Username -> Account
    private HashMap<String,Account> database;
    public Account currentAccount;

    public Database(Context context){
        database = new HashMap<String,Account>();
        load(context);
    }

    public void removeTicket(int ticket, Context context) {
        currentAccount.deleteTicket(ticket);

        try {
            writeToTicketFile(context);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public boolean loadHistory(Context context){
        File dbFile = new File(context.getFilesDir(),"history");

        if(!dbFile.exists()) {
            try{
                writeToTicketFile(context);
            } catch (Exception IOException){}
            return true;
        }
        try {
            FileInputStream fr = context.openFileInput("history");
            Scanner f = new Scanner(fr);

            while(f.hasNext()) {
                String next = f.next();
                if (next.equals("user")) {
                    f.next();
                    String user = f.next();
                    Account acc = database.get(user);
                    next = f.next();
                    while(next != null && !next.equals("user") ){
                        acc.addTicket(next);
                        if(f.hasNext())
                            next = f.next();
                        else
                            next = null;
                    }
                }
            }
            return true;

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean writeToHistory(Context context){
        File dbFile = new File(context.getFilesDir(), "history");

        if(!dbFile.exists()) {
            try{
                writeToHistoryFile(context);
            } catch (Exception IOException) {}
            return true;
        }
        try {
            FileInputStream fr = context.openFileInput("history");
            Scanner f = new Scanner(fr);

            while(f.hasNext()) {
                String next = f.next();
                if (next.equals("user")) {
                    f.next();
                    String user = f.next();
                    Account acc = database.get(user);
                    next = f.next();
                    while(next != null && !next.equals("user") ){
                        acc.addTicket(next);
                        if(f.hasNext())
                            next = f.next();
                        else
                            next = null;
                    }
                }
            }
            return true;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        return false;
    }

    public boolean writeToHistoryFile(Context context) throws IOException{
        File dbFile = new File(context.getFilesDir(),"history");

        if(!dbFile.exists()) {
            dbFile.createNewFile();
        }
        FileOutputStream  fw = context.openFileOutput("history",Context.MODE_PRIVATE);
        StringBuilder sb = new StringBuilder();
        Boolean first = true;
        for(Map.Entry<String,Account> entry:database.entrySet()){
            Account account = database.get(entry.getKey());
            if(!first) sb.append("\n,");
            else first = false;
            sb.append("user");
            sb.append("\nusername "+entry.getKey());
            for(int j = 0 ; j < account.ticketSize(); j ++){
                sb.append("\n" + account.getTicket(j));
            }
            sb.append("\n");
        }
        fw.write(sb.toString().getBytes());
        fw.close();
        return true;
    }

    public boolean load(Context context){
        File dbFile = new File(context.getFilesDir(),"database");
        //dbFile.delete();
        //File dbFile2 = new File(context.getFilesDir(),"ticket");
        //dbFile2.delete();
        if(!dbFile.exists()) {
            try{
                writeToAccountFile(context);
            } catch (Exception IOException){}
            return true;
        }
        try {
            FileInputStream fr = context.openFileInput("database");
            Scanner f = new Scanner(fr);

            while(f.hasNext()) {
                String next = f.next();
                if (next.equals("user")) {
                    f.next();
                    String user = f.next();
                    f.next();
                    String password = f.next();
                    loadAccount(context, user, password);
                }
            }
            loadTickets(context);
            return true;
                } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * loads account from the local storage database
     * @param user
     * @param password
     * @return True if adds successfully, else false.
     */
    public boolean loadAccount(Context context, String user, String password) {
        if(database.containsKey(user)){

            return false;
        }
        //Check max length?
        //No special characters in pw?

        database.put(user.toLowerCase(Locale.ENGLISH), new Account(user.toLowerCase(Locale.ENGLISH),password));

        return true;
    }

    public boolean loadTickets(Context context){
        File dbFile = new File(context.getFilesDir(),"ticket");

        if(!dbFile.exists()) {
            try{
                writeToTicketFile(context);
            } catch (Exception IOException){}
            return true;
        }
        try {
            FileInputStream fr = context.openFileInput("ticket");
            Scanner f = new Scanner(fr);

            while(f.hasNext()) {
                String next = f.next();
                if (next.equals("user")) {
                    f.next();
                    String user = f.next();
                    Account acc = database.get(user);
                    next = f.next();
                    while(next != null && !next.equals("user") ){
                        acc.addTicket(next);
                        if(f.hasNext())
                            next = f.next();
                        else
                            next = null;
                    }
                }
            }
            return true;

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * Adds account to the local storage database
     * @param user
     * @param password
     * @return True if adds successfully, else false.
     */
    public boolean addAccount(Context context, String user, String password) {
        if(database.containsKey(user)){
            return false;
        }
        //Check max length?
        //No special characters in pw?
        database.put(user.toLowerCase(Locale.ENGLISH), new Account(user.toLowerCase(Locale.ENGLISH),password));
        try{
            writeToAccountFile(context);
        }
        catch (Exception IOException){
            return false;
        }
        return true;
    }

    private boolean writeToAccountFile(Context context) throws IOException{
        File dbFile = new File(context.getFilesDir(),"database");
        if(!dbFile.exists()) {
            dbFile.createNewFile();
        }
        FileOutputStream  fw = context.openFileOutput("database",Context.MODE_PRIVATE);
       StringBuilder sb = new StringBuilder();
        Boolean first = true;
        for(Map.Entry<String,Account> entry:database.entrySet()){
            if(!first) sb.append("\n");
            else first = false;
            sb.append("user");
            sb.append("\nusername "+entry.getKey());
            sb.append("\npassword "+entry.getValue().getPassword());
            sb.append("\n");
        }
        fw.write(sb.toString().getBytes());
        fw.close();
        return true;
    }

    private boolean writeToTicketFile(Context context) throws IOException {
        File dbFile = new File(context.getFilesDir(),"ticket");

        dbFile = new File(context.getFilesDir(),"ticket");
        if(!dbFile.exists()) {
            dbFile.createNewFile();
        }
        FileOutputStream  fw = context.openFileOutput("ticket",Context.MODE_PRIVATE);
        StringBuilder sb = new StringBuilder();
        Boolean first = true;
        for(Map.Entry<String,Account> entry:database.entrySet()){
            Account account = database.get(entry.getKey());
            if(!first) sb.append("\n,");
            else first = false;
            sb.append("user");
            sb.append("\nusername "+entry.getKey());
            for(int j = 0 ; j < account.ticketSize(); j ++){
                sb.append("\n" + account.getTicket(j));
            }
            sb.append("\n");
        }
        fw.write(sb.toString().getBytes());
        fw.close();
        return true;
    }

    public boolean addTicket(String ticket, Context context){

        database.get(currentAccount.getUser()).addTicket(ticket);

            try {
                writeToTicketFile(context);
            } catch (IOException e) {
                e.printStackTrace();
                return false;
            }

            return true;
    }

    /**
     * Checks if the users details that are passed to it exist/valid
     * @param user
     * @param password
     * @return true if exist/valid, else false
     */
    public boolean validUser(String user, String password) {
        return database.containsKey(user.toLowerCase(Locale.ENGLISH))
                && database.get(user.toLowerCase(Locale.ENGLISH)).getPassword().equals(password);
    }

    /**
     * Checks to see if the user is already in the database
     * @param user
     * @return true if exist, else false
     */
    public boolean contains(String user) {
        if (database.containsKey(user)){
            return true;
        }

        return false;
    }


    public void setUser(String s) {
        currentAccount = database.get(s);
    }
}