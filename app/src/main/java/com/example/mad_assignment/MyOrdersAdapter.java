package com.example.mad_assignment;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.List;

public class MyOrdersAdapter extends RecyclerView.Adapter<MyOrdersAdapter.ProductViewHolder> {
    private Context mCtx;
    private List<myOrderModel> productList;

    public MyOrdersAdapter(Context context, List<myOrderModel> productLists) {
        mCtx = context;
        productList = productLists;
    }

    @Override
    public MyOrdersAdapter.ProductViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(mCtx).inflate(R.layout.my_orders_item,parent,false);
        return new MyOrdersAdapter.ProductViewHolder(v);
    }

    @Override
    public void onBindViewHolder(MyOrdersAdapter.ProductViewHolder holder, int position) {
        myOrderModel products = productList.get(position);


        holder.text_product.setText(products.getProdName());
        holder.text_totalAmount.setText(products.getTotalAmount());
        holder.text_unitPrice.setText(products.getUnitPrice());
        holder.text_quantity.setText(products.getQuantity());
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

    public  class ProductViewHolder extends RecyclerView.ViewHolder{

        public CardView cardview;
        public TextView text_product,text_totalAmount,text_unitPrice,text_quantity;
        public ImageView imageView_Product;

        public ProductViewHolder(View itemView) {
            super(itemView);

            //set onClickListener for itemView
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(view.getContext(),MyOrdresUpdateDelete.class);
                    intent.putExtra("productName", productList.get(getBindingAdapterPosition()).getProdName());
                    intent.putExtra("unitPrice", productList.get(getBindingAdapterPosition()).getUnitPrice());
                    intent.putExtra("totalAmount", productList.get(getBindingAdapterPosition()).getTotalAmount());
                    intent.putExtra("quantity", productList.get(getBindingAdapterPosition()).getQuantity());
                    intent.putExtra("image_url", productList.get(getBindingAdapterPosition()).getImageURL());


                    view.getContext().startActivity(intent);
                }
            });
            cardview = itemView.findViewById(R.id.myOrderCard);
            text_product = itemView.findViewById(R.id.productName);
            text_totalAmount = itemView.findViewById(R.id.totalAmount);
            text_unitPrice = itemView.findViewById(R.id.unitPrice);
            text_quantity = itemView.findViewById(R.id.quantity);
            imageView_Product = itemView.findViewById(R.id.image);

        }

    }

}
