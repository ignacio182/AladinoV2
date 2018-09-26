package com.product.aladino;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import com.bumptech.glide.Glide;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.ListenerRegistration;
import com.google.firebase.firestore.Query;
import com.product.aladino.adapter.ProductAdapter;
import com.product.aladino.model.Negocio;
import com.product.aladino.model.Product;

import javax.annotation.Nullable;

import butterknife.OnClick;

public class NegocioDetailActivity extends AppCompatActivity implements EventListener<DocumentSnapshot> {

    private static final String TAG = "NegocioDetail";
    public static final String KEY_NEGOCIO_ID = "key_negocio_id";

    private ImageView negocio_image;
    private ImageButton whatsapp_button;
    private RecyclerView product_recyclerView;
    private ProductAdapter productAdapter;
    private FirebaseFirestore mFirestore = FirebaseFirestore.getInstance();
    public DocumentReference negocioRef;
    private String negocioId;
    private Query query;
    private ListenerRegistration mRestaurantRegistration;
    private String whatsappNumber;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_negocio_detail);
        getSupportActionBar().hide();

        negocio_image = findViewById(R.id.negocio_view);
        whatsapp_button = findViewById(R.id.imageButton1);
        negocioId = getIntent().getExtras().getString(KEY_NEGOCIO_ID);
        negocioRef = mFirestore.collection("negocios").document(negocioId);

        whatsapp_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Intent.ACTION_VIEW,
                        Uri.parse(
                                "https://wa.me/" + whatsappNumber
                        )));
            }
        });

        setUpRecyclerView();

    }

    private void setUpRecyclerView(){

        // Initialize Firestore
        mFirestore = FirebaseFirestore.getInstance();

        // Get reference to the restaurant
        negocioRef = mFirestore.collection("negocios").document(negocioId);

        // Get ratings
        query = negocioRef
                .collection("products");

        FirestoreRecyclerOptions<Product> options = new FirestoreRecyclerOptions.Builder<Product>()
                .setQuery(query, Product.class)
                .build();

        productAdapter = new ProductAdapter(options);
        product_recyclerView = findViewById(R.id.product_recyclerView);
        product_recyclerView.setHasFixedSize(true);

        LinearLayoutManager mLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        product_recyclerView.setLayoutManager(mLayoutManager);
        DividerItemDecoration mDividerItemDecoration = new DividerItemDecoration(product_recyclerView.getContext(), mLayoutManager.getOrientation());
        product_recyclerView.addItemDecoration(mDividerItemDecoration);
        product_recyclerView.setAdapter(productAdapter);
    }

    @Override
    public void onEvent(@Nullable DocumentSnapshot snapshot, @Nullable FirebaseFirestoreException e) {
        if (e != null) {
            Log.w(TAG, "restaurant:onEvent", e);
            return;
        }
        onNegocioLoaded(snapshot.toObject(Negocio.class));
    }

    public void onNegocioLoaded(Negocio negocio){
        Glide.with(negocio_image.getContext())
                .load(negocio.getFoto())
                .into(negocio_image);

        whatsappNumber = negocio.getTelmovil();
    }

    @Override
    public void onStart() {
        super.onStart();
        productAdapter.startListening();
        mRestaurantRegistration = negocioRef.addSnapshotListener(this);
    }

    @Override
    public void onStop() {
        super.onStop();

        if (mRestaurantRegistration != null) {
            mRestaurantRegistration.remove();
            mRestaurantRegistration = null;
        }
        productAdapter.stopListening();
    }

    @OnClick(R.id.backButton)
    public void onBackArrowClicked(View view) {
        onBackPressed();
    }
}
