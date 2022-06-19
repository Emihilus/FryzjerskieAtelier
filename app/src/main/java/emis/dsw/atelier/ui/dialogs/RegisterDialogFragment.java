package emis.dsw.atelier.ui.dialogs;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;

import emis.dsw.atelier.MainActivity;
import emis.dsw.atelier.R;
import emis.dsw.atelier.utils.AtelierService;

public class RegisterDialogFragment extends DialogFragment {

    SharedPreferences prefs;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        prefs = getActivity().getSharedPreferences("credentials", Context.MODE_PRIVATE);

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        // Get the layout inflater
        LayoutInflater inflater = requireActivity().getLayoutInflater();

        // Inflate and set the layout for the dialog
        // Pass null as the parent view because its going in the dialog layout
        View view =  inflater.inflate(R.layout.dialog_register, null);
        builder.setView(view)
                // Add action buttons
                .setPositiveButton("Rejestruj", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        String login = ((EditText) view.findViewById(R.id.username)).getText().toString();
                        String pwd = ((EditText) view.findViewById(R.id.password)).getText().toString();
                        String name = ((EditText) view.findViewById(R.id.firstname)).getText().toString();
                        String surname = ((EditText) view.findViewById(R.id.surname)).getText().toString();
                        String phone = ((EditText) view.findViewById(R.id.phone)).getText().toString();

                        new AsyncTask<Void, Void, Void>() {

                            boolean result;

                            @Override
                            protected Void doInBackground(final Void... params) {

                                result = AtelierService.register(login, pwd, name, surname, phone);
                                return null;
                            }

                            @Override
                            protected void onPostExecute(Void unused) {
                                if(result) {
                                    Toast.makeText(getContext(),"Rejestracja pomyślna", Toast.LENGTH_LONG).show();
                                    prefs.edit().putString("login", login).commit();
                                    prefs.edit().putString("pwd", pwd).commit();
                                    ((MainActivity) getActivity()).navController.navigate(R.id.nav_home_events_list);
                                } else {
                                    Toast.makeText(getContext(),"Rejestracja niepomyślna, spróbuj ponownie", Toast.LENGTH_LONG).show();
                                    new RegisterDialogFragment().show(getActivity().getSupportFragmentManager(),"TAG");
                                }
                                super.onPostExecute(unused);
                            }
                        }.execute();
                    }
                })
                .setNegativeButton("Anuluj", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        RegisterDialogFragment.this.getDialog().cancel();
                        new LoginDialogFragment().show(getActivity().getSupportFragmentManager(),"TAG");
                    }
                });
        return builder.create();
    }
}