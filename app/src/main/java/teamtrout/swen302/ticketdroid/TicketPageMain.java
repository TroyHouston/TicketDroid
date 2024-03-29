package teamtrout.swen302.ticketdroid;

import android.app.ActionBar;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;


public class TicketPageMain extends FragmentActivity implements ActionBar.TabListener{

    AppSectionsPagerAdapter mAppSectionsPagerAdapter;
    ViewPager mViewPager;
    // Data set used by the adapter. This data will be displayed.

    static String[] title = new String[]{"Current","History"};

    currentTicketFragment ticketFrag = new currentTicketFragment(this);
    historyTicketFragment histoyFrag = new historyTicketFragment(this);

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Create the adapter that will return a fragment for each of the three primary sections
        // of the app.
        mAppSectionsPagerAdapter = new AppSectionsPagerAdapter(getSupportFragmentManager(),ticketFrag,histoyFrag);

        // Set up the action bar.
        final ActionBar actionBar = getActionBar();

        // Specify that the Home/Up button should not be enabled, since there is no hierarchical
        // currentParent.
        actionBar.setHomeButtonEnabled(false);

        // Specify that we will be displaying tabs in the action bar.
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);


        // Set up the ViewPager, attaching the adapter and setting up a listener for when the
        // user swipes between sections.
        mViewPager = (ViewPager) findViewById(R.id.pager);
        mViewPager.setAdapter(mAppSectionsPagerAdapter);
        mViewPager.setOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
            @Override
            public void onPageSelected(int position) {
                // When swiping between different app sections, select the corresponding tab.
                // We can also use ActionBar.Tab#select() to do this if we have a reference to the
                // Tab.
                actionBar.setSelectedNavigationItem(position);
            }
        });

        // For each of the sections in the app, add a tab to the action bar.
        for (int i = 0; i < mAppSectionsPagerAdapter.getCount(); i++) {
            // Create a tab with text corresponding to the page title defined by the adapter.
            // Also specify this Activity object, which implements the TabListener interface, as the
            // listener for when this tab is selected.
            actionBar.addTab(
                    actionBar.newTab()
                            .setText(mAppSectionsPagerAdapter.getPageTitle(i))
                            .setTabListener(this));
        }

    }

    public currentTicketFragment getCurrentTicket(){
        return ticketFrag;
    }

    public int getPage(){
        return mAppSectionsPagerAdapter.currentPage;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.ticket_list_page, menu);
        return true;
    }

    @Override
    public void onBackPressed() {
        return;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a currentParent activity in AndroidManifest.xml.
        int id = item.getItemId();

        switch(id) {
            case R.id.action_settings:
                return true;
            case R.id.action_add:
                new AddTicketDialog(ticketFrag).show(getFragmentManager(), "addTicketDialog");
                break;
        }
        return super.onOptionsItemSelected(item);
    }



    @Override
    public void onTabUnselected(ActionBar.Tab tab, FragmentTransaction fragmentTransaction) {
    }

    @Override
    public void onTabSelected(ActionBar.Tab tab, FragmentTransaction fragmentTransaction) {
        // When the given tab is selected, switch to the corresponding page in the ViewPager.
        mViewPager.setCurrentItem(tab.getPosition());
    }

    @Override
    public void onTabReselected(ActionBar.Tab tab, FragmentTransaction fragmentTransaction) {
    }

    /**
     * A {@link FragmentPagerAdapter} that returns a fragment corresponding to one of the primary
     * sections of the app.
     */
    public static class AppSectionsPagerAdapter extends FragmentPagerAdapter {

        currentTicketFragment currentFrag;
        historyTicketFragment histoyFrag;
        int currentPage = 0;

        public AppSectionsPagerAdapter(FragmentManager fm,currentTicketFragment currentFrag, historyTicketFragment histoyFrag) {
            super(fm);
            this.currentFrag = currentFrag;
            this.histoyFrag = histoyFrag;
        }

        @Override
        public Fragment getItem(int i) {
            switch (i) {
                case 0:
                    // The first section of the app is the most interesting -- it offers
                    // a launchpad into the other demonstrations in this example application.
                    currentPage = 0;
                    return currentFrag;

                case 1:
                    // The other sections of the app are dummy placeholders.
                    currentPage = 1;
                    return histoyFrag;
                default:
                    return null;
            }
        }

        @Override
        public int getCount() {
            return 2;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return title[position];
        }
    }


    public static class historyTicketFragment extends Fragment {

        TicketPageMain testma;

         private ArrayList<String> tickets = new ArrayList<String>();

         private ArrayList<TicketInfo> codes = new ArrayList<TicketInfo>();

        public historyTicketFragment(TicketPageMain testma){
            this.testma = testma;
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {

            View rootView = inflater.inflate(R.layout.history_ticket_page, container, false);


            recyclerView = (RecyclerView) rootView.findViewById(R.id.my_recycler_view_hist);
            // do not change the size of the RecyclerView
            recyclerView.setHasFixedSize(true);

            // use a linear layout manager
            RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(testma.getApplicationContext());
            recyclerView.setLayoutManager(mLayoutManager);

            for(int i = 0 ; i < LoginPage.db.currentAccount.ticketSize(); i ++){
                String info = Events.events.get(LoginPage.db.currentAccount.getTicket(i)).toStringBasic();

                String[] date2 = info.split("\n");
                String[] splitDate2 = date2[1].split("/");

                Calendar current = Calendar.getInstance();
                Calendar ticketCurrent = Calendar.getInstance();

                ticketCurrent.set(Integer.valueOf(splitDate2[2]),Calendar.OCTOBER,Integer.valueOf(splitDate2[0]));
                current.add(Calendar.DATE,3);

                if(current.after(ticketCurrent)){
                    addTicket(info,LoginPage.db.currentAccount.getTicket(i),true);
                }
            }

            return rootView;
        }

        RecyclerView recyclerView;

        public void addTicket(String ticketInfo, String code, boolean datab){
            tickets.add(ticketInfo);
            codes.add(new TicketInfo(ticketInfo,code));

            // Create the adapter
            RecyclerView.Adapter adapter = new TicketListAdapter(testma, codes, tickets, testma);
            recyclerView.setAdapter(adapter);
        }


        public void removeTicket(String ticketInfo, String code) {
            tickets.remove(ticketInfo);
            codes.remove(new TicketInfo(ticketInfo, code));
            // Create the adapter
            RecyclerView.Adapter adapter = new TicketListAdapter(testma, codes, tickets, testma);
            recyclerView.setAdapter(adapter);
        }
    }


    public static class currentTicketFragment extends Fragment {


        TicketPageMain testma;

        static ArrayList<String> tickets = new ArrayList<String>();
        static ArrayList<TicketInfo> codes = new ArrayList<TicketInfo>();

        public currentTicketFragment(TicketPageMain testma){
            this.testma = testma;
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {

            View rootView = inflater.inflate(R.layout.current_ticket_page, container, false);


            recyclerView = (RecyclerView) rootView.findViewById(R.id.my_recycler_view);
            // do not change the size of the RecyclerView
            recyclerView.setHasFixedSize(true);

            // use a linear layout manager
            RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(testma.getApplicationContext());
            recyclerView.setLayoutManager(mLayoutManager);
            for(int i = 0 ; i < LoginPage.db.currentAccount.ticketSize(); i ++){
                String info = Events.events.get(LoginPage.db.currentAccount.getTicket(i)).toStringBasic();

                String[] date2 = info.split("\n");
                String[] splitDate2 = date2[1].split("/");

                Calendar current = Calendar.getInstance();
                Calendar ticketCurrent = Calendar.getInstance();

                ticketCurrent.set(Integer.valueOf(splitDate2[2]),Calendar.OCTOBER,Integer.valueOf(splitDate2[0]));
                current.add(Calendar.DATE,3);

                if(current.before(ticketCurrent)){
                    addTicket(info,LoginPage.db.currentAccount.getTicket(i),true);
                }


            }

            Collections.sort(tickets, new Comparator<String>() {
                @Override
                public int compare(String s, String s2) {

                    String[] date1 = s.split("\n");
                    String[] date2 = s2.split("\n");

                    String[] splitDate1 = date1[1].split("/");
                    String[] splitDate2 = date2[1].split("/");

                    if(!splitDate1[2].equals(splitDate2[2]))
                        return splitDate1[2].compareTo(splitDate2[2]);

                    if(!splitDate1[1].equals(splitDate2[1]))
                        return splitDate1[1].compareTo(splitDate2[1]);

                    return splitDate1[0].compareTo(splitDate2[0]);

                }
            });

            Collections.sort(codes, new Comparator<TicketInfo>() {
                @Override
                public int compare(TicketInfo s, TicketInfo s2) {

                    String[] date1 = s.info.split("\n");
                    String[] date2 = s2.info.split("\n");

                    String[] splitDate1 = date1[1].split("/");
                    String[] splitDate2 = date2[1].split("/");

                    if(!splitDate1[2].equals(splitDate2[2]))
                        return splitDate1[2].compareTo(splitDate2[2]);

                    if(!splitDate1[1].equals(splitDate2[1]))
                        return splitDate1[1].compareTo(splitDate2[1]);

                    return splitDate1[0].compareTo(splitDate2[0]);

                }
            });

            return rootView;
        }

        RecyclerView recyclerView;

        public void addTicket(String ticketInfo, String code, boolean datab){
            tickets.add(ticketInfo);
            codes.add(new TicketInfo(ticketInfo,code));



            Collections.sort(tickets, new Comparator<String>() {
                @Override
                public int compare(String s, String s2) {

                   String[] date1 = s.split("\n");
                   String[] date2 = s2.split("\n");

                   String[] splitDate1 = date1[1].split("/");
                   String[] splitDate2 = date2[1].split("/");

                    if(!splitDate1[2].equals(splitDate2[2]))
                        return splitDate1[2].compareTo(splitDate2[2]);

                    if(!splitDate1[1].equals(splitDate2[1]))
                        return splitDate1[1].compareTo(splitDate2[1]);

                    return splitDate1[0].compareTo(splitDate2[0]);

                }
            });

            Collections.sort(codes, new Comparator<TicketInfo>() {
                @Override
                public int compare(TicketInfo s, TicketInfo s2) {

                    String[] date1 = s.info.split("\n");
                    String[] date2 = s2.info.split("\n");

                    String[] splitDate1 = date1[1].split("/");
                    String[] splitDate2 = date2[1].split("/");

                    if(!splitDate1[2].equals(splitDate2[2]))
                        return splitDate1[2].compareTo(splitDate2[2]);

                    if(!splitDate1[1].equals(splitDate2[1]))
                        return splitDate1[1].compareTo(splitDate2[1]);

                    return splitDate1[0].compareTo(splitDate2[0]);

                }
            });
            if(!datab){
                LoginPage.db.addTicket(code, testma.getApplicationContext());
            }
            // Create the adapter
            RecyclerView.Adapter adapter = new TicketListAdapter(testma, codes, tickets, testma);
            recyclerView.setAdapter(adapter);
        }

        public void removeTicket(String ticketInfo, String code) {
            tickets.remove(ticketInfo);
            codes.remove(new TicketInfo(ticketInfo, code));
            // Create the adapter
            RecyclerView.Adapter adapter = new TicketListAdapter(testma, codes, tickets, testma);
            recyclerView.setAdapter(adapter);
        }
    }

    public static class TicketInfo {

        public String info;
        public String code;

        public TicketInfo(String info, String code){
            this.info = info;
            this.code = code;
        }

        @Override
        public boolean equals(Object o) {

            if (o == null || getClass() != o.getClass()) return false;

            TicketInfo that = (TicketInfo) o;

            if (!code.equals(that.code)) return false;

            return true;
        }

        @Override
        public int hashCode() {
            return code.hashCode();
        }
    }


}
