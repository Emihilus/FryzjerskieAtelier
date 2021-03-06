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

public class LoginDialogFragment extends DialogFragment {

    SharedPreferences prefs;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        prefs = getActivity().getSharedPreferences("credentials", Context.MODE_PRIVATE);

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        // Get the layout inflater
        LayoutInflater inflater = requireActivity().getLayoutInflater();

        // Inflate and set the layout for the dialog
        // Pass null as the parent view because its going in the dialog layout
        View view =  inflater.inflate(R.layout.dialog_signin, null);
        builder.setView(view)
                // Add action buttons
                .setPositiveButton("Zaloguj", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        String login = ((EditText) view.findViewById(R.id.username)).getText().toString();
                        String pwd = ((EditText) view.findViewById(R.id.password)).getText().toString();
                        new AsyncTask<Void, Void, Void>() {

                            boolean result;

                            @Override
                            protected Void doInBackground(final Void... params) {

                                result = AtelierService.login(login, pwd);
                                return null;
                            }

                            @Override
                            protected void onPostExecute(Void unused) {
                                if(result) {
                                    Toast.makeText(getContext(),"Logowanie pomy??lne", Toast.LENGTH_LONG).show();
                                    prefs.edit().putString("login", login).commit();
                                    prefs.edit().putString("pwd", pwd).commit();
                                    ((MainActivity) getActivity()).navController.navigate(R.id.nav_home_events_list);
                                } else {
                                    Toast.makeText(getContext(),"Logowanie niepomy??lne, spr??buj ponownie", Toast.LENGTH_LONG).show();
                                    new LoginDialogFragment().show(getActivity().getSupportFragmentManager(),"TAG");
                                }
                                super.onPostExecute(unused);
                            }
                        }.execute();
                    }
                })
                .setNegativeButton("Rejestracja", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        LoginDialogFragment.this.getDialog().cancel();
                        new RegisterDialogFragment().show(getActivity().getSupportFragmentManager(),"TAG");
                    }
                });
        return builder.create();
    }
}