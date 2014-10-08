package teamtrout.swen302.ticketdroid;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.facebook.Request;
import com.facebook.Response;
import com.facebook.Session;
import com.facebook.SessionState;
import com.facebook.UiLifecycleHelper;
import com.facebook.model.GraphUser;
import com.facebook.widget.LoginButton;

import java.util.ArrayList;
import java.util.List;


public class LandingPage extends Activity {

    Database db;
    String email;
    String password;
    boolean loggedIn = false;

    private Session.StatusCallback callback = new Session.StatusCallback() {
        @Override
        public void call(Session session, SessionState state, Exception exception) {
            onSessionStateChange(session, state, exception);
        }
    };
    private UiLifecycleHelper uiHelper;


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
        facebookLogout(this);
        setContentView(R.layout.activity_landing_page);
        Events.addEvents();
        uiHelper = new UiLifecycleHelper(this, callback);
        uiHelper.onCreate(savedInstanceState);
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
		// as you specify a currentParent activity in AndroidManifest.xml.
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

    public void loginWithFB() {
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

    public void facebookLogout(Context context){
        loggedIn = false;
        Session session = Session.getActiveSession();
        if (session != null) {
            session.closeAndClearTokenInformation();
        }
        else
        {
            Session session2 = Session.openActiveSession((Activity)context, false, null);
            if(session2 != null)
                session2.closeAndClearTokenInformation();
        }
        Session.setActiveSession(null);

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Session.getActiveSession().onActivityResult(this, requestCode, resultCode, data);
        uiHelper.onActivityResult(requestCode, resultCode, data);

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
            Log.d("Successful Login", "success");
            db.setUser(email.toString());
            LoginPage.db = this.db;
            //Go to ticketScreen if successful
            Intent intent = new Intent(this, TicketPageMain.class);

            startActivity(intent);
        }

        Log.w("Worked", "Not");
    }

    private void onSessionStateChange(Session session, SessionState state, Exception exception) {
        if (state.isOpened() && !loggedIn) {
            Log.d("TEST", "Logged in...");
            loginWithFB();
            loggedIn = true;

        } else if (state.isClosed()) {
            Log.d("TEST", "Logged out...");
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        uiHelper.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        uiHelper.onPause();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        uiHelper.onDestroy();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        uiHelper.onSaveInstanceState(outState);
    }

}
