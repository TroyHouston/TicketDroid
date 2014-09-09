package teamtrout.swen302.ticketdroid;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;

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
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void attemptRegister(View view) {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);

        if (!passwordField.getText().toString().equals(confirmField.getText().toString())) { //passwords do not match
            alertDialogBuilder.setTitle("Error");

            alertDialogBuilder.setMessage("Passwords do not match")
                    .setCancelable(false)
                    .setPositiveButton("Ok",new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialogInterface, int i) {
                            dialogInterface.cancel();
                        }
                    });

            AlertDialog alertDialog = alertDialogBuilder.create();
            alertDialog.show();
            return;
        }

        if (passwordField.getText().toString().length() < 4) { //password too short
            alertDialogBuilder.setTitle("Error");

            alertDialogBuilder.setMessage("Password is not long enough")
                    .setCancelable(false)
                    .setPositiveButton("Ok",new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialogInterface, int i) {
                            dialogInterface.cancel();
                        }
                    });

            AlertDialog alertDialog = alertDialogBuilder.create();
            alertDialog.show();
            return;
        }

        if (db.contains(emailField.getText().toString())) { //already contains this email
            alertDialogBuilder.setTitle("Error");

            alertDialogBuilder.setMessage("This email already exists")
                    .setCancelable(false)
                    .setPositiveButton("Ok",new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialogInterface, int i) {
                            dialogInterface.cancel();
                        }
                    });

            AlertDialog alertDialog = alertDialogBuilder.create();
            alertDialog.show();
            return;
        }

        String[] split = emailField.getText().toString().split("@");

        if(split[0].length() < 1 || split[1].length() < 1 || split.length == 2) { // valid email
                alertDialogBuilder.setMessage("Invalid Email Address")
                        .setCancelable(false)
                        .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialogInterface, int i) {
                                dialogInterface.cancel();
                            }
                        });

                AlertDialog alertDialog = alertDialogBuilder.create();
                alertDialog.show();

        }

        if(db.addAccount(this,emailField.getText().toString(), passwordField.getText().toString())) { //valid
            alertDialogBuilder.setMessage("Account added")
                    .setCancelable(false)
                    .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialogInterface, int i) {
                            dialogInterface.cancel();
                        }
                    });

            AlertDialog alertDialog = alertDialogBuilder.create();
            alertDialog.show();


        } else { //general error message
            alertDialogBuilder.setMessage("Something went wrong")
                    .setCancelable(false)
                    .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialogInterface, int i) {
                            dialogInterface.cancel();
                        }
                    });

            AlertDialog alertDialog = alertDialogBuilder.create();
            alertDialog.show();
        }
    }
}
