package teamtrout.swen302.ticketdroid;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;

/*
 * This is the page where new users are recorded to the database
 */
public class RegisterPage extends Activity {

    Database db;
    EditText passwordField;
    EditText confirmField;
    EditText emailField;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_page);

        passwordField = (EditText) findViewById(R.id.password);
        confirmField = (EditText) findViewById(R.id.confirmPassword);
        emailField = (EditText) findViewById(R.id.emailAddress);

        db = new Database(this);
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
        return (id == R.id.action_settings) || super.onOptionsItemSelected(item);
    }

    /*
     * This method
     * 1. Does a validation checks on the three fields
     * 2. Displays a message to the user about the outcome
     * 3. Adds to a db if valid, then redirect to login page
     */
    public void attemptRegister(View view) {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        String enteredPwd = passwordField.getText().toString();
        String enteredConfirm = confirmField.getText().toString();
        String enteredEmail = emailField.getText().toString();
        String[] splitEmail = enteredEmail.split("@");

        //Error Checks
        if (!enteredPwd.equals(enteredConfirm)) { //passwords do not match
            alertDialogBuilder.setTitle("Error");
            alertDialogBuilder.setMessage("Passwords do not match")
                    .setCancelable(false)
                    .setPositiveButton("Ok",new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialogInterface, int i) {
                            dialogInterface.cancel();
                        }
                    });
        } else if (enteredPwd.length() < 4) { //password too short
            alertDialogBuilder.setTitle("Error");
            alertDialogBuilder.setMessage("Password is not long enough")
                    .setCancelable(false)
                    .setPositiveButton("Ok",new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialogInterface, int i) {
                            dialogInterface.cancel();
                        }
                    });
        } else if (db.contains(enteredEmail)) { //already contains this email
            alertDialogBuilder.setTitle("Error");

            alertDialogBuilder.setMessage("This email already exists")
                    .setCancelable(false)
                    .setPositiveButton("Ok",new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialogInterface, int i) {
                            dialogInterface.cancel();
                        }
                    });
        } else if (splitEmail.length != 2 || splitEmail[0].length() < 1 || splitEmail[1].length() < 1 ) { // invalid email
                alertDialogBuilder.setMessage("Invalid Email Address")
                        .setCancelable(false)
                        .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialogInterface, int i) {
                                dialogInterface.cancel();
                            }
                        });
        } else if (db.addAccount(this, enteredEmail, enteredPwd)) { //Successful registration
            final Class afterPage = LoginPage.class;
            final RegisterPage me = this;
            alertDialogBuilder.setMessage("Account added")
                    .setCancelable(false)
                    .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialogInterface, int i) {
                            dialogInterface.cancel();
                            Intent intent = new Intent(me, afterPage);
                            startActivity(intent);
                        }
                    });
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
}
