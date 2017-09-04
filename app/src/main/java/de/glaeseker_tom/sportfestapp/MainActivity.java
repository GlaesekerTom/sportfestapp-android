package de.glaeseker_tom.sportfestapp;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Handler;
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

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener,Table2Fragment.OnListFragmentInteractionListener, ScoreboardFragment.OnFragmentInteractionListener{

    private android.support.v4.app.FragmentManager fragmentManager;
    private String url = "http://192.168.20.30:80/sportfest/";
    private int permission;
    private boolean doubleBackToExitPressedOnce = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        permission = 2;
        //permission = getIntent().getExtras().getInt("permission");
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        fragmentManager = getSupportFragmentManager();

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
    }
    @Override
    protected  void onResume(){
        super.onResume();
        this.doubleBackToExitPressedOnce = false;
    }

    //----------------------Zurückbutton Event------------------------------------------------
    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            drawer.openDrawer(GravityCompat.START);
        }}
            /*if (fragmentManager.getBackStackEntryCount() > 1) {
                fragmentManager.popBackStack();
                // super.onBackPressed();
                // return;
            } else {*/
                /*if (doubleBackToExitPressedOnce) {
                    if(drawer.isDrawerOpen(GravityCompat.START)){
                        drawer.closeDrawer(GravityCompat.START);
                    }else{
                        drawer.openDrawer(GravityCompat.START);
                    }
                    //fragmentManager.popBackStack();
                    //super.onBackPressed();
                    return;
                }

                this.doubleBackToExitPressedOnce = true;
                Toast.makeText(this, "Press one more time to exit",
                        Toast.LENGTH_SHORT).show();

                new Handler().postDelayed(new Runnable() {

                    @Override
                    public void run() {

                        doubleBackToExitPressedOnce = false;
                    }
                }, 3000);

            }
        }
       // super.onBackPressed();
        /*if (doubleBackToExitPressedOnce) {
            moveTaskToBack(true);
            return;
        }

        this.doubleBackToExitPressedOnce = true;
        Toast.makeText(this, "Erneut drücken um, App zu verlassen.", Toast.LENGTH_SHORT).show();

        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                doubleBackToExitPressedOnce=false;
            }
        }, 2000);*/
    //Wenn Zurückbutton gedrückt
      /*  DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        System.out.println("KEKSE SIND TOLL UND LECKER");
        List<Fragment> fragmentlist = fragmentManager.getFragments();
        System.out.println("Fragmentslistsize: "+fragmentlist.size());
        //Wenn Drawer geöffnet, dann wird geschlossen, sonst normale Funktion des Zurückbuttons.
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        }if(getSupportFragmentManager().getFragments().size() > 0){
            removeAllExistingFragments();
            System.out.println("hier");
            fragmentManager.popBackStack();

       }else {
            System.out.println("super.onBackPressed()");
            super.onBackPressed();
        }*/
    //-------------------------------ActionBar-Menu-----------------------------------------
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        if (id == R.id.action_logout) {
            startActivity(new Intent(getApplicationContext(),LoginActivity.class));
            //return true;
        }

        return super.onOptionsItemSelected(item);
    }
    //-------------------------NavigationBar Item Handler------------------------------------------
    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        removeAllExistingFragments();
        Bundle bundle = new Bundle();

        if (id == R.id.nav_soccer) {
            Table2Fragment frag = new Table2Fragment();
            bundle.putString("tableType","soccer");
            bundle.putString("serverUrl",url);
            frag.setArguments(bundle);
            FragmentTransaction transaction = fragmentManager.beginTransaction();
            transaction.add(R.id.content_main,frag,"fragSoccer");
            transaction.commit();
        } else if (id == R.id.nav_volleyball) {
            Table2Fragment frag = new Table2Fragment();
            bundle.putString("tableType","volleyball");
            bundle.putString("serverUrl",url);
            frag.setArguments(bundle);
            FragmentTransaction transaction = fragmentManager.beginTransaction();
            transaction.add(R.id.content_main,frag,"fragVolleyball");
            transaction.commit();
        } else if (id == R.id.nav_badminton) {

            Table2Fragment frag = new Table2Fragment();
            bundle.putString("tableType","badminton");
            bundle.putString("serverUrl",url);
            frag.setArguments(bundle);
            FragmentTransaction transaction = fragmentManager.beginTransaction();
            transaction.add(R.id.content_main,frag,"fragBadminton");
            transaction.commit();

        }  else if (id == R.id.nav_basketball) {

            Table2Fragment frag = new Table2Fragment();
            bundle.putString("tableType","basketball");
            bundle.putString("serverUrl",url);
            frag.setArguments(bundle);
            FragmentTransaction transaction = fragmentManager.beginTransaction();
            transaction.add(R.id.content_main,frag,"fragBasketball");
            transaction.commit();
        }
        else if (id == R.id.nav_hockey) {
            Table2Fragment frag = new Table2Fragment();
            bundle.putString("tableType","hockey");
            bundle.putString("serverUrl",url);
            frag.setArguments(bundle);
            FragmentTransaction transaction = fragmentManager.beginTransaction();
            transaction.add(R.id.content_main, frag, "fragHockey");
            transaction.commit();

        }else if ( id == R.id.nav_placement1){
            PlacementFragment frag = new PlacementFragment();
            FragmentTransaction transaction = fragmentManager.beginTransaction();
            transaction.add(R.id.content_main,frag,"fragPlacement1");
            transaction.commit();

        }else if(id == R.id.nav_placement2){
            PlacementFragment frag = new PlacementFragment();
            FragmentTransaction transaction = fragmentManager.beginTransaction();
            transaction.add(R.id.content_main,frag,"fragPlacement2");
            transaction.commit();
        }
        else if (id== R.id.nav_total_placement){
            TotalPlacementFragment frag = new TotalPlacementFragment();
            frag.setServerURL(url);
            FragmentTransaction transaction = fragmentManager.beginTransaction();
            transaction.add(R.id.content_main,frag,"fragTotalPlacement");
            transaction.commit();
        }
        else if (id == R.id.nav_managetournament) {
            //Überprüfung der Berechtigungen für die Tuniererstellung vorhanden sind
            if(permission== 2) {
                ManageTournamentFragment frag = new ManageTournamentFragment();
                bundle.putString("serverUrl",url);
                frag.setArguments(bundle);
                FragmentTransaction transaction = fragmentManager.beginTransaction();
                transaction.add(R.id.content_main, frag, "fragBadminton");
                transaction.commit();
            }else{
                Toast.makeText(getApplicationContext(), "Du besitzt nicht ausreichende Berechtigungen!", Toast.LENGTH_SHORT).show();
                return false;
            }
        }
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
        System.out.println(transaction.toString());

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
        Table2Fragment frag = new Table2Fragment();
        Bundle bundle = new Bundle();
        bundle.putString("tableType",s);
        bundle.putString("serverUrl",url);
        frag.setArguments(bundle);
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.add(R.id.content_main, frag, "table" + s);
        transaction.commit();
    }


}
