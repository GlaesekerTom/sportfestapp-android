package de.glaeseker_tom.sportfestapp;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import java.util.List;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, PlacementFragment.OnFragmentInteractionListener,
        Table2Fragment.OnListFragmentInteractionListener, ScoreboardFragment.OnFragmentInteractionListener,
        PlacementFragmentContent.OnListFragmentInteractionListener2{

    private android.support.v4.app.FragmentManager fragmentManager;
    private String url = "http://192.168.20.30:80/sportfest/";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        fragmentManager = getSupportFragmentManager();
       /* fragmentManager.beginTransaction().add(R.id.content_main,new PlacementFragment(),"placement").commit();*/

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
    }

    //----------------------Zurückbutton Event------------------------------------------------
    @Override
    public void onBackPressed() {
        //Wenn Zurückbutton gedrückt
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        //Wenn Drawer geöffnet, dann wird geschlossen, sonst normale Funktion des Zurückbuttons.
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } if(getFragmentManager().getFragments().size() > 0){
            removeAllExistingFragments();
        }else {
            super.onBackPressed();
        }
    }
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

        //noinspection SimplifiableIfStatement
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

        if (id == R.id.nav_soccer) {
            PlacementFragment frag = new PlacementFragment();
           // frag.setTableType("soccer");
           // frag.setServerURL(url);
            FragmentTransaction transaction = fragmentManager.beginTransaction();
            transaction.add(R.id.content_main,frag,"tableSoccer");
            transaction.commit();
        } else if (id == R.id.nav_volleyball) {
            Table2Fragment frag = new Table2Fragment();
            frag.setServerURL(url);
            frag.setTableType("volleyball");
            FragmentTransaction transaction = fragmentManager.beginTransaction();
            transaction.add(R.id.content_main,frag,"tableVolleyball");
            transaction.commit();

        } else if (id == R.id.nav_badminton) {

            Table2Fragment frag = new Table2Fragment();
            frag.setTableType("badminton");
            frag.setServerURL(url);
            FragmentTransaction transaction = fragmentManager.beginTransaction();
            transaction.add(R.id.content_main,frag,"tableBadminton");
            transaction.commit();

        } else if (id == R.id.nav_hockey) {
            Table2Fragment frag = new Table2Fragment();
            frag.setTableType("hockey");
            frag.setServerURL(url);
            FragmentTransaction transaction = fragmentManager.beginTransaction();
            transaction.add(R.id.content_main, frag, "tableHockey");
            transaction.commit();

        }/*else if ( id == R.id.nav_placement){
            String[] table = {"Soccer","Volleyball","Badminton", "Hockey","Icehockey"};
          // PlacementFragment frag = new PlacementFragment(table);
            FragmentTransaction transaction = fragmentManager.beginTransaction();
           // transaction.add(R.id.content_main, frag, "tableHockey");
            transaction.commit();
        }*/
        else if (id == R.id.nav_managesports) {
            fragmentManager = getSupportFragmentManager();
            fragmentManager.beginTransaction().add(R.id.content_main,new PlacementFragment(),"placement").commit();
        }
        else if (id== R.id.nav_announcement){

        }
        //Nach dem eine Auswahl gemacht wurde wird NavigationBar wieder geschlossen
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    //Entfernt alle existierenden Fragments
    public void removeAllExistingFragments(){
        //Führt evtl. zu Problemen
        List<Fragment> fragmentlist = fragmentManager.getFragments();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        System.out.println("Fragment Namen::----------------------------------------");
        for (int i = 0; i < fragmentlist.size(); i++) {
            //System.out.println(fragmentlist.get(i).getTag());
            transaction.remove(fragmentlist.get(i));

        }
        transaction.commit();
    }

    @Override
    public void onListFragmentInteraction(String[] item) {
        removeAllExistingFragments();
        Bundle bundle = new Bundle();
        bundle.putStringArray("mm",item);
        bundle.putString("serverUrl",url);
        ScoreboardFragment frag = new ScoreboardFragment();
        frag.setArguments(bundle);
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.add(R.id.content_main, frag,"scoreboard");
        transaction.commit();
    }

    @Override
    public void onListFragmentInteraction(String s) {
        removeAllExistingFragments();
        Table2Fragment frag = new Table2Fragment();
        frag.setTableType(s);
        frag.setServerURL(url);
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.add(R.id.content_main, frag,"scoreboard");
        transaction.commit();
    }

    @Override
    public void onFragmentInteraction(Uri uri) {
    }
}
