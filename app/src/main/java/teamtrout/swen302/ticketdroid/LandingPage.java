package teamtrout.swen302.ticketdroid;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;

import com.facebook.*;
import com.facebook.model.*;

import java.util.List;
import java.util.ArrayList;


public class LandingPage extends Activity {

    Database db;
    String email;
    String password;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
        //facebookLogout();
        setContentView(R.layout.activity_landing_page);
        Events.addEvents();
        db = new Database(this);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.landing_page, menu);
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
	
	public void goToLoginPage(View view) {
		Intent intent = new Intent(this, LoginPage.class);
        startActivity(intent);
	}
	
	public void goToRegisterPage(View view) {
		Intent intent = new Intent(this, RegisterPage.class);
        startActivity(intent);
	}

    public void loginWithFB(View view) {

        final List<String> perms = new ArrayList<String>();
        perms.add("email");
        // start Facebook Login
        Session.openActiveSession(this, true, perms, new Session.StatusCallback() {
            // callback when session changes state
            @Override
            public void call(Session session, SessionState state, Exception exception) {
                if (session.isOpened()) {
                    // make request to the /me API
                    Request.newMeRequest(session, new Request.GraphUserCallback() {

                        // callback after Graph API response with user object
                        @Override
                        public void onCompleted(GraphUser user, Response response) {
                            if (user != null) {
                                // Do the stuff
                                email = user.getProperty("email").toString();
                                password = user.getId();
                                if (!db.contains(email)) {
                                    // Register
                                    attemptRegisterWithFB();
                                }
                                // Login
                                attemptLoginWithFB();
                            }
                        }
                    }).executeAsync();
                }
            }
        });
    }

    public void facebookLogout(){
        Session session = Session.getActiveSession();
        if(session!=null)session.closeAndClearTokenInformation();
        SharedPreferences.Editor editor = getSharedPreferences("clear_cache", Context.MODE_PRIVATE).edit();
        editor.clear();
        editor.commit();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Session.getActiveSession().onActivityResult(this, requestCode, resultCode, data);
    }

    /*
     * This method
     * 1. Does a validation checks on the three fields
     * 2. Displays a message to the user about the outcome
     * 3. Adds to a db if valid, then redirect to login page
     */
    public void attemptRegisterWithFB() {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        String[] splitEmail = email.split("@");

        if (db.addAccount(this, email, password)) { //Successful registration
        } else { //Unexpected failure, database addition failed
            alertDialogBuilder.setMessage("Something went wrong")
                    .setCancelable(false)
                    .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialogInterface, int i) {
                            dialogInterface.cancel();
                        }
                    });
        }
        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }

    public void attemptLoginWithFB() {
        // This happens when the loginButton is pressed.

        Log.w("User",email);
        Log.w("Password", password.toString());

        Log.w("Boolean", Boolean.toString(db.validUser(email.toString(),password.toString())));
        Log.w("Boolean", Boolean.toString(db.contains(email.toString())));

        if (db.validUser(email.toString(), password.toString())){
            Log.w("Worked", "good");
            db.setUser(email.toString());
            LoginPage.db = this.db;
            //Go to ticketScreen if successful
            Intent intent = new Intent(this, TicketPageMain.class);

            startActivity(intent);
        }

        Log.w("Worked", "Not");
    }

}
