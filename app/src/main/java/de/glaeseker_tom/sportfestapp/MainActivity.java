package de.glaeseker_tom.sportfestapp;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;
import java.util.List;
import de.glaeseker_tom.sportfestapp.fragments.ManageTournamentFragment;
import de.glaeseker_tom.sportfestapp.fragments.PlacementFragment;
import de.glaeseker_tom.sportfestapp.fragments.ScoreboardFragment;
import de.glaeseker_tom.sportfestapp.fragments.MatchListFragment;
import de.glaeseker_tom.sportfestapp.fragments.TotalPlacementFragment;

/*
* Die MainActivity dient als Container für alle Fragmente.
* Der
 */
public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener,MatchListFragment.OnListFragmentInteractionListener, ScoreboardFragment.OnFragmentInteractionListener{

    private android.support.v4.app.FragmentManager fragmentManager;
    private String url = "http://192.168.20.30:80/sportfest/";
    private int permission;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //Erhält Benutzerberechtigung vom Server in AccountHandler und wird beim Start der Activity übergeben.
        permission = getIntent().getExtras().getInt("permission");

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        //Holt den FragmentManager, welcher die FragmentTransactions durchführt.
        fragmentManager = getSupportFragmentManager();

        //Erstellt Navigation Drawer
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
    }

    //----------------------Zurückbutton Event------------------------------------------------------
    //Wird ausgeführt wenn der Zurückknopf gedrückt wurde. In meinem Fall öffnet bzw. schließt er nur den Navigation Drawer.
    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            drawer.openDrawer(GravityCompat.START);
        }}

    //-------------------------------ActionBar-Menu-------------------------------------------------
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Erstellt das Menü, oben rechts in der Actionbar.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Behandelt Clicks in der Actionbar.
        int id = item.getItemId();
        //Überprüft, ob der Menüpunkt "Ausloggen" gedrückt wurde.
        if (id == R.id.action_logout) {
            startActivity(new Intent(getApplicationContext(),LoginActivity.class));
        }
        return super.onOptionsItemSelected(item);
    }
    //-------------------------Navigation Drawer Item Handler---------------------------------------
    //Legt fest, was bei welcher Auswahl im NavigationDrawer geschieht.
    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Behandelt Navigationsauswahl
        int id = item.getItemId();
        removeAllExistingFragments();
        Bundle bundle = new Bundle();
        bundle.putString("serverUrl",url);
        FragmentTransaction transaction = fragmentManager.beginTransaction();

        if (id == R.id.nav_soccer) {
            MatchListFragment frag = new MatchListFragment();
            bundle.putString("tableType","soccer");
            frag.setArguments(bundle);
            transaction.add(R.id.content_main,frag,"fragSoccer");
        } else if (id == R.id.nav_volleyball) {
            MatchListFragment frag = new MatchListFragment();
            bundle.putString("tableType","volleyball");
            frag.setArguments(bundle);
            transaction.add(R.id.content_main,frag,"fragVolleyball");
        } else if (id == R.id.nav_badminton) {
            MatchListFragment frag = new MatchListFragment();
            bundle.putString("tableType","badminton");
            frag.setArguments(bundle);
            transaction.add(R.id.content_main,frag,"fragBadminton");
        }  else if (id == R.id.nav_basketball) {
            MatchListFragment frag = new MatchListFragment();
            bundle.putString("tableType","basketball");
            frag.setArguments(bundle);
            transaction.add(R.id.content_main,frag,"fragBasketball");
        } else if (id == R.id.nav_hockey) {
            MatchListFragment frag = new MatchListFragment();
            bundle.putString("tableType","hockey");
            frag.setArguments(bundle);
            transaction.add(R.id.content_main, frag, "fragHockey");
        } else if ( id == R.id.nav_placement1){
            PlacementFragment frag = new PlacementFragment();
            transaction.add(R.id.content_main,frag,"fragPlacement1");

        } else if(id == R.id.nav_placement2){
            PlacementFragment frag = new PlacementFragment();
            transaction.add(R.id.content_main,frag,"fragPlacement2");
        } else if (id== R.id.nav_total_placement){
            TotalPlacementFragment frag = new TotalPlacementFragment();
            frag.setServerURL(url);
            transaction.add(R.id.content_main,frag,"fragTotalPlacement");
        } else if (id == R.id.nav_managetournament) {
            //überprüft, ob die Berechtigungen für die Tuniererstellung vorhanden sind
            if(permission== 2) {
                ManageTournamentFragment frag = new ManageTournamentFragment();
                frag.setArguments(bundle);
                transaction.add(R.id.content_main, frag, "fragBadminton");
            }else{
                Toast.makeText(getApplicationContext(), "Du besitzt nicht ausreichende Berechtigungen!", Toast.LENGTH_SHORT).show();
                return false;
            }
        }
        transaction.commit();

        //Nach dem eine Auswahl gemacht wurde wird NavigationBar wieder geschlossen
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    //Entfernt alle existierenden Fragments
    public void removeAllExistingFragments(){
        List<Fragment> fragmentlist = fragmentManager.getFragments();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        for (int i = 0; i < fragmentlist.size(); i++) {
            transaction.remove(fragmentlist.get(i));
            transaction.addToBackStack(null);
        }
        transaction.commit();

    }

    @Override
    public void onListFragmentInteraction(final String[] item) {
        if(item[5].equals("-")) {
            startScoreboard(item);
        }else{
            if(permission > 1){
                AlertDialog.Builder builder= new AlertDialog.Builder(this);
                builder.setTitle("Ergebnis steht fest. Trotzdem bearbeiten?");
                builder.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                        startScoreboard(item);
                    }
                });
                builder.setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.cancel();
                    }
                });
                builder.show();
            }else{
                Toast.makeText(this, "Ergebnis steht bereits fest!", Toast.LENGTH_SHORT).show();
            }
        }
    }
    public void startScoreboard(String[] item){
        removeAllExistingFragments();
        Bundle bundle = new Bundle();
        bundle.putStringArray("mm", item);
        bundle.putString("serverUrl", url);
        ScoreboardFragment frag = new ScoreboardFragment();
        frag.setArguments(bundle);
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.add(R.id.content_main, frag, "scoreboard");
        transaction.commit();
    }

    @Override
    public void onListFragmentInteraction(String s) {
        removeAllExistingFragments();
        MatchListFragment frag = new MatchListFragment();
        Bundle bundle = new Bundle();
        bundle.putString("tableType",s);
        bundle.putString("serverUrl",url);
        frag.setArguments(bundle);
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.add(R.id.content_main, frag, "table" + s);
        transaction.commit();
    }
}
