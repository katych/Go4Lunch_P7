package com.example.go4lunch.views.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.go4lunch.BuildConfig;
import com.example.go4lunch.R;
import com.example.go4lunch.model.Restaurant;
import com.example.go4lunch.utils.Utils;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.example.go4lunch.utils.Utils.starsAccordingToRating;


public class FavoritesRestaurantListAdapter extends FirestoreRecyclerAdapter<Restaurant,
        FavoritesRestaurantListAdapter.FavoriteItemViewHolder> {


    //interface to set the click
    public interface favoritesClickListener {
        void onClickItemRestaurant(int position);
    }

    //FIELD
    private favoritesClickListener mFavoritesClickListener;

    //ViewHolder
    public class FavoriteItemViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        @BindView(R.id.list_item_name_restaurant)
        TextView nameRestaurant;
        @BindView(R.id.list_item_restaurant_address)
        TextView addressRestaurant;
        @BindView(R.id.list_item_restaurant_image)
        ImageView imageRestaurant;
        @BindView(R.id.star1)
        ImageView star1;
        @BindView(R.id.star2)
        ImageView star2;
        @BindView(R.id.star3)
        ImageView star3;

        favoritesClickListener mFavoritesClickListener;

        FavoriteItemViewHolder(@NonNull View itemView, favoritesClickListener listener) {
            super(itemView);
            this.mFavoritesClickListener = listener;
            ButterKnife.bind(this, itemView);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            mFavoritesClickListener.onClickItemRestaurant(getAdapterPosition());
        }
    }

    /**
     * Create a new RecyclerView adapter that listens to a Firestore Query.  See {@link
     * FirestoreRecyclerOptions} for configuration options.
     *
     * @param options configuration
     */
    public FavoritesRestaurantListAdapter(@NonNull FirestoreRecyclerOptions<Restaurant> options, favoritesClickListener listener) {
        super(options);
        this.mFavoritesClickListener = listener;
    }

    @NonNull
    @Override
    public FavoriteItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_restaurant_favoris, parent, false);
        return new FavoriteItemViewHolder(view, mFavoritesClickListener);
    }


    @Override
    protected void onBindViewHolder(@NonNull FavoriteItemViewHolder holder, int i, @NonNull Restaurant favoriteRestaurant) {

        holder.nameRestaurant.setText(favoriteRestaurant.getNameRestaurant());
        holder.addressRestaurant.setText(favoriteRestaurant.getAddress());

        // Restaurants images
        if (favoriteRestaurant.getImage().isEmpty()) {
            holder.imageRestaurant.setImageResource(R.drawable.ic_bowl);
        } else {
            String path = "https://maps.googleapis.com/maps/api/place/photo?maxwidth=400&photoreference="
                    + favoriteRestaurant.getImage() +
                    "&key=" + BuildConfig.google_maps_key;

            Glide.with(holder.imageRestaurant.getContext())
                    .load(path)
                    .error(R.drawable.ic_orange_bowl)
                    .apply(RequestOptions.centerCropTransform())
                    .into(holder.imageRestaurant);
        }

        //Stars according to rating level
        int rating;
        if (favoriteRestaurant.getRating() > 0) {
            rating = starsAccordingToRating(favoriteRestaurant.getRating());
        } else {
            rating = 0;
        }

        Utils.starsView(rating, holder.star1, holder.star2, holder.star3);
    }

}
