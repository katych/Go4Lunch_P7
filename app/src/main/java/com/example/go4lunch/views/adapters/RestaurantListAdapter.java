package com.example.go4lunch.views.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.RequestManager;
import com.example.go4lunch.R;
import com.example.go4lunch.model.Restaurant;

import java.util.ArrayList;


public class RestaurantListAdapter extends RecyclerView.Adapter<RestaurantItemViewHolder> {

    //interface to listen the click
    public interface onClickRestaurantItemListener {
        void onClickRestaurantItem(int position);
    }

    //FILEDS
    private ArrayList<Restaurant> mRestaurantList;
    private RequestManager glide;
    private onClickRestaurantItemListener mOnClickRestaurantItemListener;

    //constructor

    public RestaurantListAdapter(ArrayList<Restaurant> restaurantList, RequestManager glide, onClickRestaurantItemListener onClickRestaurantitemListener) {

        mRestaurantList = restaurantList;
        this.glide = glide;
        this.mOnClickRestaurantItemListener = onClickRestaurantitemListener;
    }

    @NonNull
    @Override
    public RestaurantItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_view_item, parent, false);
        return new RestaurantItemViewHolder(view, mOnClickRestaurantItemListener);
    }

    @Override
    public void onBindViewHolder(@NonNull RestaurantItemViewHolder holder, int position) {
        holder.updateWithDetailRestaurant(this.mRestaurantList.get(position), this.glide);
    }

    @Override
    public int getItemCount() {
        return (mRestaurantList != null) ? mRestaurantList.size() : 0;
    }

}
