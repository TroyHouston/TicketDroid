package teamtrout.swen302.ticketdroid;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;

public class LoginPage extends Activity {

    static Database db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_page);

        db = new Database(this);
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

        Log.w("User", user.toString());
        Log.w("Password", password.toString());

        Log.w("Boolean", Boolean.toString(db.validUser(user.toString(),password.toString())));
        Log.w("Boolean", Boolean.toString(db.contains(user.toString())));

        if (db.validUser(user.toString(), password.toString())){
            Log.w("Worked", "good");
            db.setUser(user.toString());
            //Go to ticketScreen if successful
            Intent intent = new Intent(this, TicketPageMain.class);

            startActivity(intent);
        } else {
            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
            alertDialogBuilder.setTitle("Invalid Account");

            alertDialogBuilder.setMessage("Invalid username or password.")
                    .setCancelable(false)
                    .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialogInterface, int i) {
                            dialogInterface.cancel();
                        }
                    });

            AlertDialog alertDialog = alertDialogBuilder.create();
            alertDialog.show();
        }

        Log.w("Worked", "Not");
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
        Intent intent = new Intent(this, TicketPageMainOld.class);

        startActivity(intent);
    }
}
