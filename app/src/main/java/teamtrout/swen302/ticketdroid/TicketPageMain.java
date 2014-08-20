package teamtrout.swen302.ticketdroid;



import android.os.Bundle;
import android.app.Activity;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
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

}
