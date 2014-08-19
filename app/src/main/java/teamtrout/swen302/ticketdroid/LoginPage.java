package teamtrout.swen302.ticketdroid;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.util.JsonReader;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.charset.Charset;

public class LoginPage extends Activity {

    static Database db = new Database();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_page);

        try {
            FileInputStream stream = openFileInput("database");

            String jsonStr = null;
            try {
                FileChannel fc = stream.getChannel();
                MappedByteBuffer bb = fc.map(FileChannel.MapMode.READ_ONLY, 0, fc.size());

                jsonStr = Charset.defaultCharset().decode(bb).toString();
            } catch (IOException e) {
                e.printStackTrace();
            }
            finally {
                try {
                    stream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            if (jsonStr.length() != 0) {
                JSONObject jsonObj = new JSONObject(jsonStr);

                // Getting data JSON Array nodes
                JSONArray data  = jsonObj.getJSONArray("data");

                for (int i = 0; i < data.length(); i++) {
                    JSONObject c = data.getJSONObject(i);

                    String user = c.getString("username");
                    String password = c.getString("password");
                    db.addAccount(user, password);
                }

            }


        } catch (JSONException e){
            e.printStackTrace();
            throw new Error("Failed to load the database. Please contact the admin");
        } catch (FileNotFoundException e) {
            try {
                FileOutputStream fos = openFileOutput("database", MODE_PRIVATE);
            } catch (FileNotFoundException e1) {
                // TODO Auto-generated catch block
                e1.printStackTrace();
                throw new Error("Failed to create the database. Please contact the admin");
            }
        }

    }

    public Account readUser(JsonReader reader) throws IOException {
        String username = null;
        String password = null;

        reader.beginObject();
        while (reader.hasNext()) {
            String name = reader.nextName();
            if (name.equals("username")) {
                username = reader.nextString();
            } else if (name.equals("password")) {
                password = reader.nextString();
            } else {
                reader.skipValue();
            }
        }
        reader.endObject();
        return new Account(username, password);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.login_page, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void attemptLogin(View view) {
        // This happens when the loginButton is pressed.
        Editable user = ((EditText)findViewById(R.id.emailaddress)).getText();
        Editable password = ((EditText)findViewById(R.id.password)).getText();

        if (db.validUser(user.toString(), password.toString())){
            Log.w("something", "else");

            //Go to ticketScreen if successful
            Intent intent = new Intent(this, TicketPageMain.class);

            startActivity(intent);
        }

        Log.w("something", "other");
    }



    /**
     * A placeholder fragment containing a simple view.
     */
    public static class PlaceholderFragment extends Fragment {
        /**
         * The fragment argument representing the section number for this
         * fragment.
         */
        private static final String ARG_SECTION_NUMBER = "section_number";

        /**
         * Returns a new instance of this fragment for the given section number.
         */
        public static PlaceholderFragment newInstance(int sectionNumber) {
            PlaceholderFragment fragment = new PlaceholderFragment();
            Bundle args = new Bundle();
            args.putInt(ARG_SECTION_NUMBER, sectionNumber);
            fragment.setArguments(args);
            return fragment;
        }

        public PlaceholderFragment() {
        }


    }

    public void goToTicketPage(View view){
        Intent intent = new Intent(this, TicketPageMain.class);

        startActivity(intent);
    }
}
