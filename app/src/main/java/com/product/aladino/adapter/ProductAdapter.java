package com.product.aladino.adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.RequestOptions;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.product.aladino.model.Product;
import com.product.aladino.R;

import butterknife.BindView;
import butterknife.ButterKnife;


public class ProductAdapter extends FirestoreRecyclerAdapter<Product, ProductAdapter.ProductHolder> {


    public ProductAdapter(@NonNull FirestoreRecyclerOptions<Product> options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull ProductHolder holder, int position, @NonNull final Product model) {

        Glide.with(holder.product_photo.getContext())
                .load(model.getFoto())
                .apply(new RequestOptions().transforms(new CenterCrop(), new RoundedCorners(20)))
                .into(holder.product_photo);

        holder.product_name.setText(model.getNombre());
        holder.price.setText("$" + String.format("%.2f",model.getPrecio()));
        holder.description.setText(model.getDescripcion());
    }

    @NonNull
    @Override
    public ProductHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_product, parent, false);
        return new ProductHolder(v);
    }

    class ProductHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.product_name)
        TextView product_name;

        @BindView(R.id.product_photo)
        ImageView product_photo;

        @BindView(R.id.price)
        TextView price;

        @BindView(R.id.description)
        TextView description;

        public ProductHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);
        }
    }
}
