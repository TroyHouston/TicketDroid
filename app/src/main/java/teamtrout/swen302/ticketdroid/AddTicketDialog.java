package teamtrout.swen302.ticketdroid;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.widget.Button;
import android.widget.EditText;

import static teamtrout.swen302.ticketdroid.TicketPageMain.currentTicketFragment;

/**
 * Created by Troy on 20/08/2014.
 * Git error.
 */
public class AddTicketDialog extends DialogFragment {

    currentTicketFragment currentParent;


    AddTicketDialog(currentTicketFragment currentParent) {
        this.currentParent = currentParent;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // Use the Builder class for convenient dialog construction
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        // Import an XML design
        LayoutInflater inflater = getActivity().getLayoutInflater();
        builder.setView(inflater.inflate(R.layout.dialog_add_ticket, null));
        String you = "<font color='red'> Add Ticket </font>";

        //Html.fromHtml(you);
        builder.setTitle(R.string.dialog_title)
                .setPositiveButton("Add", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        EditText codeField = (EditText) getDialog().findViewById(R.id.code);
                        String enteredCode = codeField.getText().toString();

                        //If QR code is valid add ticket (cant add same ticket)
                        if (Events.validTicket(enteredCode)
                                && !currentTicketFragment.codes.contains(enteredCode)) {
                            String ticketInfo = (Events.events.get(enteredCode)).toStringBasic();
                            currentParent.addTicket(ticketInfo, enteredCode, false);
                        } else {
                            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getActivity());
                            alertDialogBuilder.setTitle("Error");

                            alertDialogBuilder.setMessage("The code is invalid.")
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

                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // Close the dialog.
                    }
                });

        AlertDialog temp = builder.create();
        temp.show();
        Button add = temp.getButton(DialogInterface.BUTTON_POSITIVE);
        add.setTextColor(Color.parseColor("#3F51B5"));


        return temp;
    }
}
