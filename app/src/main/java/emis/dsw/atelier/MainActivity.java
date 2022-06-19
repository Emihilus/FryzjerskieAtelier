package emis.dsw.atelier;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.navigation.NavController;
import androidx.navigation.NavDestination;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.google.android.material.navigation.NavigationView;

import emis.dsw.atelier.databinding.ActivityMainBinding;
import emis.dsw.atelier.ui.LoginDialogFragment;
import emis.dsw.atelier.utils.AtelierService;

public class MainActivity extends AppCompatActivity {

    private AppBarConfiguration mAppBarConfiguration;
    private ActivityMainBinding binding;
    public NavController navController;
    SharedPreferences prefs;
    String login;
    String pwd;
    AppCompatActivity activity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        prefs = getSharedPreferences("credentials",Context.MODE_PRIVATE);
        login = prefs.getString("login", "");
        pwd = prefs.getString("pwd", "");
        AtelierService.cookie = prefs.getString("SESSION", "");

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setSupportActionBar(binding.appBarMain.toolbar);
        binding.appBarMain.fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                navController.navigate(R.id.nav_calendar);
                Toast.makeText(MainActivity.this ,"Wybierz date i godzine dla usługi", Toast.LENGTH_LONG).show();
            }
        });

        DrawerLayout drawer = binding.drawerLayout;
        NavigationView navigationView = binding.navView;
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_home_events_list, R.id.nav_services_list, R.id.nav_calendar)
                .setOpenableLayout(drawer)
                .build();
        navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main);


        new AsyncTask<Void, Void, Void>() {

            boolean result;

            @Override
            protected Void doInBackground(final Void... params) {

                result = AtelierService.checkLogin();
                return null;
            }

            @Override
            protected void onPostExecute(Void unused) {
                if(result) {
                    prefs.edit().putString("SESSION", AtelierService.cookie).commit();

                    NavigationUI.setupActionBarWithNavController(MainActivity.this, navController, mAppBarConfiguration);
                    NavigationUI.setupWithNavController(navigationView, navController);


                    navController.addOnDestinationChangedListener(new NavController.OnDestinationChangedListener() {
                        @Override
                        public void onDestinationChanged(@NonNull NavController controller, @NonNull NavDestination destination, @Nullable Bundle arguments) {
                            if (destination.getId() == R.id.nav_calendar) {
                                binding.appBarMain.fab.setVisibility(View.GONE);
                            } else {
                                binding.appBarMain.fab.setVisibility(View.VISIBLE);
                            }
                        }
                    });
                } else {
                    new LoginDialogFragment().show(MainActivity.this.getSupportFragmentManager(),"TAG");
                }

                super.onPostExecute(unused);
            }
        }.execute();


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.action_logout:
                new AsyncTask<Void, Void, Void>() {

                    boolean result;

                    @Override
                    protected Void doInBackground(final Void... params) {

                        result = AtelierService.logout();
                        return null;
                    }
                }.execute();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }
}