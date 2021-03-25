package com.example.go4lunch.views.adapters;

import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.RequestManager;
import com.bumptech.glide.request.RequestOptions;
import com.example.go4lunch.BuildConfig;
import com.example.go4lunch.R;
import com.example.go4lunch.model.Restaurant;
import com.example.go4lunch.utils.Utils;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.example.go4lunch.utils.Utils.starsAccordingToRating;


public class RestaurantItemViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
    @BindView(R.id.list_item_name_restaurant)
    TextView nameRestaurant;
    @BindView(R.id.list_item_restaurant_address)
    TextView addressRestaurant;
    @BindView(R.id.list_item_restaurant_image)
    ImageView imageRestaurant;
    @BindView(R.id.list_item_distance)
    TextView distanceRestaurant;
    @BindView(R.id.list_item_opening)
    TextView openingTime;
    @BindView(R.id.list_item_icon_rating)
    LinearLayout ratingWorker;
    @BindView(R.id.worker_number)
    TextView workerNumber;
    @BindView(R.id.star1)
    ImageView star1;
    @BindView(R.id.star2)
    ImageView star2;
    @BindView(R.id.star3)
    ImageView star3;

    private RestaurantListAdapter.onClickRestaurantItemListener mListener;

    RestaurantItemViewHolder(@NonNull View itemView, RestaurantListAdapter.onClickRestaurantItemListener listener) {
        super(itemView);
        this.mListener = listener;
        ButterKnife.bind(this, itemView);
        itemView.setOnClickListener(this);
    }

    /**
     * update item with restaurant information
     *
     * @param restaurantDetail restaurant
     * @param glide            library
     */
    void updateWithDetailRestaurant(Restaurant restaurantDetail, RequestManager glide) {

        String name;
        if (restaurantDetail.getNameRestaurant().length() > 20) {
            name = restaurantDetail.getNameRestaurant().substring(0, 20) + " ...";
        } else {
            name = restaurantDetail.getNameRestaurant();
        }

        this.nameRestaurant.setText(name);

        String restaurant;
        if (restaurantDetail.getAddress().length() > 35) {
            restaurant = restaurantDetail.getAddress().substring(0, 35) + " ...";
        } else {
            restaurant = restaurantDetail.getAddress();
        }

        this.addressRestaurant.setText(restaurant);


        if ((restaurantDetail.getHour())) {
            this.openingTime.setText(R.string.restaurant_open);
        } else {
            this.openingTime.setText(R.string.not_open_yet);
        }

        // Restaurants images
        if (restaurantDetail.getImage().isEmpty()) {
            imageRestaurant.setImageResource(R.drawable.ic_orange_bowl);
        } else {
            String path = "https://maps.googleapis.com/maps/api/place/photo?maxwidth=400&photoreference="
                    + restaurantDetail.getImage() +
                    "&key=" + BuildConfig.google_maps_key;

            glide.load(path)
                    .error(R.drawable.ic_restaurant_image)
                    .apply(RequestOptions.centerCropTransform())
                    .into(imageRestaurant);
        }

        //distance
        if (restaurantDetail.getDistanceCurrentWorker() > 0) {
            String dist = restaurantDetail.getDistanceCurrentWorker() + " m";
            distanceRestaurant.setText(dist);
        }

        //workers
        if (restaurantDetail.getWorkers() > 0) {
            ratingWorker.setVisibility(View.VISIBLE);
            String text = "(" + restaurantDetail.getWorkers() + ")";
            workerNumber.setText(text);
        } else {
            ratingWorker.setVisibility(View.INVISIBLE);
        }

        //Stars according to rating level
        int rating;
        if (restaurantDetail.getRating() > 0) {
            rating = starsAccordingToRating(restaurantDetail.getRating());
        } else {
            rating = 0;
        }
        Utils.starsView(rating, star1, star2, star3);
    }

    @Override
    public void onClick(View v) {
        mListener.onClickRestaurantItem(getAdapterPosition());
    }


}
