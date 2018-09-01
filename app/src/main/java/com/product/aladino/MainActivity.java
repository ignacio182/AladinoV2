package com.product.aladino;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.firebase.ui.firestore.SnapshotParser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

import butterknife.OnClick;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener,
                    NegocioAdapter.OnNegocioSelectedListener,
                    FilterFragment.FilterListener{

    private RecyclerView recyclerView;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private CollectionReference negociosRef = db.collection("negocios");
    private NegocioAdapter negocioAdapter;
    private UserLocation userLocation;
    private DrawerLayout drawerLayout;
    private FilterFragment filterFragment;
    private Filters mFilters;
    private Query query;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        mFilters = Filters.getDefault();

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        userLocation = new UserLocation(this);
        filterFragment = new FilterFragment();
        setUpRecyclerView();
    }

    private void setUpRecyclerView(){
        query = negociosRef;
        FirestoreRecyclerOptions<Negocio> options = new FirestoreRecyclerOptions.Builder<Negocio>()
                .setQuery(query, new SnapshotParser<Negocio>() {
                    @NonNull
                    @Override
                    public Negocio parseSnapshot(@NonNull DocumentSnapshot snapshot) {
                        Negocio negocio = snapshot.toObject(Negocio.class);
                        negocio.setId(snapshot.getId());
                        return negocio;
                    }
                })
                .build();


        negocioAdapter = new NegocioAdapter(options, this);
        recyclerView = findViewById(R.id.recycler_view);
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(mLayoutManager);
        DividerItemDecoration mDividerItemDecoration = new DividerItemDecoration(recyclerView.getContext(), mLayoutManager.getOrientation());
        recyclerView.addItemDecoration(mDividerItemDecoration);
        recyclerView.setAdapter(negocioAdapter);

    }

    @Override
    protected void onStart() {
        super.onStart();
        onFilter(this.mFilters);
        negocioAdapter.startListening();
    }

    @Override
    protected void onStop() {
        super.onStop();
        negocioAdapter.stopListening();
    }

    @Override
    public void onNegocioSelected(Negocio negocio) {
        Intent intent = new Intent(this, NegocioDetailActivity.class);
        intent.putExtra(NegocioDetailActivity.KEY_NEGOCIO_ID, negocio.getId());
        startActivity(intent);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    public void onFilterClicked() {
        filterFragment.show(getSupportFragmentManager(), FilterFragment.TAG);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_camera) {
            // Handle the camera action
        } else if (id == R.id.nav_gallery) {

        } else if (id == R.id.nav_slideshow) {

        } else if (id == R.id.nav_manage) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onFilter(Filters filters) {
        // Construct query basic query
        Query query_filter = negociosRef;

        // Category (equality filter)
        if (filters.hasCategory()) {
            query_filter = query.whereEqualTo("category", filters.getCategory());
        }

        query = query_filter;

        // Stop listening
        negocioAdapter.stopListening();

        // Clear existinkodig data
        negocioAdapter.getSnapshots().clear();
        negocioAdapter.notifyDataSetChanged();

        // Listen to new query
        query = query_filter;
        negocioAdapter.startListening();

        this.mFilters = filters;
    }
}
