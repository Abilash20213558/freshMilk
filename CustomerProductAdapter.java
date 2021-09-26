package com.example.mad_assignment;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.List;

public class CustomerProductAdapter extends RecyclerView.Adapter<CustomerProductAdapter.ProductViewHolder> {
    private Context mCtx;
    private List<AdminProductModel> productList;

    public CustomerProductAdapter(Context context, List<AdminProductModel> productLists) {
        mCtx = context;
        productList = productLists;
    }

    @Override
    public CustomerProductAdapter.ProductViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(mCtx).inflate(R.layout.customer_product_item, parent, false);
        return new CustomerProductAdapter.ProductViewHolder(v);
    }


    @Override
    public void onBindViewHolder(CustomerProductAdapter.ProductViewHolder holder, int position) {
        AdminProductModel products = productList.get(position);


        holder.text_product.setText(products.getProdName());
        holder.text_liter.setText(products.getLiter());
        holder.text_unitPrice.setText(products.getUnitPrice());
        holder.text_description.setText(products.getDescription());
        Picasso
                .get()
                .load(products.getImageURL())
                .placeholder(R.drawable.placceholder)
                .fit()
                .into(holder.imageView_Product);
    }

    @Override
    public int getItemCount() {
        return productList.size();
    }

    public class ProductViewHolder extends RecyclerView.ViewHolder {

        public CardView cardview;
        public TextView text_product, text_liter, text_unitPrice, text_description;
        public ImageView imageView_Product;
        public Button btnBuy;


        public ProductViewHolder(View itemView) {
            super(itemView);

            //set onClickListener for itemView
            itemView.findViewById(R.id.btn_buy).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    Intent intent = new Intent(view.getContext(), CustomerBuyProduct.class);
                    intent.putExtra("productName", productList.get(getBindingAdapterPosition()).getProdName());
                    intent.putExtra("unitPrice", productList.get(getBindingAdapterPosition()).getUnitPrice());
                    intent.putExtra("Liter", productList.get(getBindingAdapterPosition()).getLiter());
                    //intent.putExtra("Description", productList.get(getBindingAdapterPosition()).getDescription());
                    intent.putExtra("image_url", productList.get(getBindingAdapterPosition()).getImageURL());


                    view.getContext().startActivity(intent);
                }
            });
            cardview = itemView.findViewById(R.id.ProdCard);
            text_product = itemView.findViewById(R.id.cusProduct);
            text_liter = itemView.findViewById(R.id.cusLiter);
            text_unitPrice = itemView.findViewById(R.id.cusUnitPrice);
            text_description = itemView.findViewById(R.id.cusDescription);
            imageView_Product = itemView.findViewById(R.id.CusImage);
            btnBuy = itemView.findViewById(R.id.btn_buy);

        }


    }
}
