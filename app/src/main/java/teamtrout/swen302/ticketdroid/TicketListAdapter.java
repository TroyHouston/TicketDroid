package teamtrout.swen302.ticketdroid;


import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;


public class TicketListAdapter extends RecyclerView.Adapter<TicketListAdapter.ViewHolder> implements View.OnClickListener,
        View.OnLongClickListener{

    private ArrayList<String> tickets;
    private ArrayList<String> codes;
    private static Context sContext;
    private TicketPageMain callingActivity;

    // Adapter's Constructor
    public TicketListAdapter(Context context,ArrayList<String> codes, ArrayList<String> tickets, TicketPageMain callingActivity) {
        this.tickets = tickets;
        this.codes = codes;
        sContext = context;
        this.callingActivity = callingActivity;
    }

    // Create new views. This is invoked by the layout manager.
    @Override
    public TicketListAdapter.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                         int viewType) {

        // Create a new view by inflating the row item xml.
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_row, parent, false);

        v.setLongClickable(true);
        v.setClickable(true);

        // Set the view to the ViewHolder
        ViewHolder holder = new ViewHolder(v);
        holder.mCardView.setOnClickListener(TicketListAdapter.this);
        holder.mCardView.setOnLongClickListener(TicketListAdapter.this);

        holder.mCardView.setTag(holder);

       /* GestureDetector.SimpleOnGestureListener simpleOnGestureListener
                = new GestureControl(callingActivity);

        final GestureDetector gestureDetector
                = new GestureDetector(simpleOnGestureListener);

        holder.mCardView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                return !gestureDetector.onTouchEvent(motionEvent);
            }
        });*/
        return holder;
    }

    // Replace the contents of a view. This is invoked by the layout manager.
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

        //holder.mNumberRowTextView.setText(String.valueOf(position) + ". ");
        // Get element from your dataset at this position and set the text for the specified element
        holder.mNameTextView.setText(tickets.get(position));

        //holder.getPosition()
        //Set image of ticket

        ImageView img= (ImageView) holder.mCardView.findViewById(R.id.ticketImage);
        Bitmap bitmapImage = BitmapFactory.decodeResource(img.getResources(),(Events.events.get(codes.get(holder.getPosition()))).getImage());
        Bitmap scaledImage = Bitmap.createScaledBitmap(bitmapImage, 150,150,true);
        img.setImageBitmap(scaledImage);

        // Set the color to red if row is even, or to green if row is odd.
        //if (position % 2 == 0) {
           // holder.mNumberRowTextView.setTextColor(Color.RED);
        //} else {
          //  holder.mNumberRowTextView.setTextColor(Color.GREEN);
        //}
    }

    /* Return the size of your data set (invoked by the layout manager) */
    @Override
    public int getItemCount() {
        return tickets.size();
    }

    // Implement OnClick listener. The clicked item text is displayed in a Toast message.
    @Override
    public void onClick(View view) {
        ViewHolder holder = (ViewHolder) view.getTag();
        int j = view.getId();
        if (view.getId() == holder.mCardView.getId()) {
            Intent i = new Intent(callingActivity,QrPage.class);
            //give detailed ticket info to QR class
            Bundle bundle = new Bundle();
            bundle.putString("code", codes.get(holder.getPosition()));
            i.putExtras(bundle);
            callingActivity.startActivity(i);
        }
    }

    // Implement OnLongClick listener. Long Clicked items is removed from list.
    @Override
    public boolean onLongClick(View view) {
        final ViewHolder holder = (ViewHolder) view.getTag();

        if (tickets.get(holder.getPosition()) != null) {
            final String[] event = tickets.get(holder.getPosition()).split("\\r?\\n");

            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(sContext);

            alertDialogBuilder.setMessage("Do you want to delete " + event[0] + " " + event[1])
                    .setCancelable(false)
                    .setNegativeButton("No", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialogInterface, int i) {
                            dialogInterface.cancel();
                        }
                    })
                    .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialogInterface, int i) {
                            LoginPage.db.removeTicket(holder.getPosition(), sContext);

                            tickets.remove(holder.getPosition());
                            notifyDataSetChanged();

                            dialogInterface.cancel();
                        }
                    });

            AlertDialog alertDialog = alertDialogBuilder.create();
            alertDialog.show();

        }
        return false;
    }

    // Create the ViewHolder class to keep references to your views
    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView mNameTextView;
        public CardView mCardView;

        /**
         * Constructor
         * @param v The container view which holds the elements from the row item xml
         */
        public ViewHolder(View v) {
            super(v);
            mNameTextView = (TextView) v.findViewById(R.id.textView);
            mCardView = (CardView) v.findViewById(R.id.card_view);
        }
    }
}