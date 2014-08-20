package teamtrout.swen302.ticketdroid;

import android.app.Activity;
import android.os.Bundle;
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

public class RegisterPage extends Activity {

    static Database db = new Database();
    EditText passwordField;
    EditText confirmField;
    EditText emailField;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_page);
        passwordField = (EditText) findViewById(R.id.password);
        confirmField = (EditText) findViewById(R.id.confirmPassword);
        emailField = (EditText) findViewById(R.id.emailAddress);

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


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.register_page, menu);
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

    public void attemptRegister(View view){
        if (!passwordField.getText().toString().equals(confirmField.getText().toString())){
            //passwords do not match
        }
        if(db.contains(emailField.getText().toString())) {
            //email address already has an account
        }
    }
}