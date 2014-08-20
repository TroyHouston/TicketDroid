package teamtrout.swen302.ticketdroid;



import android.os.Bundle;
import android.app.Activity;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;

import java.util.ArrayList;

public class TicketPageMain extends Activity {


	@Override
	protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.current_ticket_page);

        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.my_recycler_view);

        // improve performance if you know that changes in content
        // do not change the size of the RecyclerView
        recyclerView.setHasFixedSize(true);

        // use a linear layout manager
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(mLayoutManager);

        // Data set used by the adapter. This data will be displayed.
        ArrayList<String> myDataset = new ArrayList<String>();
        for (int i = 0; i < 2; i++) {
            myDataset.add("Katy Perry " + i);
        }

        // Create the adapter
        RecyclerView.Adapter adapter = new TicketListAdapter(TicketPageMain.this, myDataset,this);
        recyclerView.setAdapter(adapter);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.ticket_list_page, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        switch(id) {
            case R.id.action_settings:
                return true;
            case R.id.action_add:
                new AddTicketDialog(this).show(getFragmentManager(), "addTicketDialog");
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}
