package com.parag.fruits.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import com.parag.fruits.R;
import com.parag.fruits.model.Fruit;
import com.squareup.picasso.Picasso;
import java.util.ArrayList;
import butterknife.BindView;
import butterknife.ButterKnife;

public class FruitAdapter extends RecyclerView.Adapter<FruitAdapter.MyViewHolder> {

    private Context context;
    private ArrayList<Fruit> fruitArrayList;
    private LayoutInflater layoutInflater;
    public FruitAdapter(Context context, ArrayList<Fruit> fruitArrayList)
    {
        this.context = context;
        this.fruitArrayList = fruitArrayList;
        layoutInflater = LayoutInflater.from(context);
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = layoutInflater.inflate(R.layout.fruit_item,parent,false);
        return  new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        Fruit fruit = fruitArrayList.get(position);
        Picasso.with(context).load(fruit.getUrl()).into(holder.imageView);
        holder.txtFruitName.setText(fruit.getName());
        holder.txtFruitDescription.setText(fruit.getDescription());
    }

    @Override
    public int getItemCount() {
        return fruitArrayList.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder
    {
        @BindView(R.id.imageview_fruit_image) ImageView imageView;
        @BindView((R.id.txt_fruit_name)) TextView txtFruitName;
        @BindView((R.id.txt_fruit_description)) TextView txtFruitDescription;
        public MyViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }
}
