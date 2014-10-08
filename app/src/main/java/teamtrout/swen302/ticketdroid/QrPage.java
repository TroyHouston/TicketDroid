package teamtrout.swen302.ticketdroid;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;

import java.util.Locale;


public class QrPage extends Activity {

    String code;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qr);
        CardView cv = (CardView) findViewById(R.id.card_view);

        //Get detailed ticket Info
        Bundle bundle = getIntent().getExtras();
        code = bundle.getString("code");
        qrifyString(code);
        Log.d("working:  "," " + code);
        //Set detailed ticket Info
        setTicketInfo(code);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.qr_menu_page, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a currentParent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void qrifyString(String s) {
        QRCodeWriter writer = new QRCodeWriter();
        try {
            BitMatrix bitMatrix = writer.encode(s, BarcodeFormat.QR_CODE, 512, 512);
            int width = bitMatrix.getWidth();
            int height = bitMatrix.getHeight();
            Bitmap bmp = Bitmap.createBitmap(width, height, Bitmap.Config.RGB_565);
            for (int x = 0; x < width; x++) {
                for (int y = 0; y < height; y++) {
                    bmp.setPixel(x, y, bitMatrix.get(x, y) ? Color.BLACK : Color.WHITE);
                }
            }
            ((ImageView) findViewById(R.id.imageView1)).setImageBitmap(bmp);

        } catch (WriterException e) {
            e.printStackTrace();
        }
    }

    public void findOnMap(View view) {

        String address = (Events.events.get(code)).getEventLocation();
        String uri = String.format(Locale.ENGLISH, "geo:0,0?q=%s", address);
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(uri));
        view.getContext().startActivity(intent);
    }

    public void setTicketInfo(String code){
        TextView ticketInfo = (TextView) findViewById(R.id.ticketInfo);
        ticketInfo.setText((Events.events.get(code)).toStringDetailed());
        //Set Image
        ImageView img= (ImageView) findViewById(R.id.ticketImage);
        img.setImageResource((Events.events.get(code)).getImage());
    }
}
