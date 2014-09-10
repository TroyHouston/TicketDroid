package teamtrout.swen302.ticketdroid;

import android.content.Intent;
import android.view.GestureDetector;
import android.view.MotionEvent;

/**
 * Created by Ryan on 20/08/2014.
 */
public class GestureControl extends GestureDetector.SimpleOnGestureListener {

    private TicketPageMain callingActivity;

    public GestureControl(TicketPageMain callingActivity){
        this.callingActivity = callingActivity;
    }

    @Override
    public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX,
                           float velocityY) {
        String swipe = "";
        float sensitvity = 50;

        if((e1.getX() - e2.getX()) > sensitvity){
            swipe += "Swipe Left\n";
            Intent i = new Intent(callingActivity,HistoryTicketPage.class);
            callingActivity.startActivity(i);
        }else if((e2.getX() - e1.getX()) > sensitvity){
            swipe += "Swipe Right\n";
        }else{
            swipe += "\n";
        }

        if((e1.getY() - e2.getY()) > sensitvity){
            swipe += "Swipe Up\n";
        }else if((e2.getY() - e1.getY()) > sensitvity){
            swipe += "Swipe Down\n";
        }else{
            swipe += "\n";
        }

        return super.onFling(e1, e2, velocityX, velocityY);
    }
}


