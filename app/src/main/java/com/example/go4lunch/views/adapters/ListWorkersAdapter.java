package com.example.go4lunch.views.adapters;

import android.content.res.Resources;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.go4lunch.R;
import com.example.go4lunch.model.Worker;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ListWorkersAdapter extends FirestoreRecyclerAdapter<Worker,ListWorkersAdapter.WorkerViewHolder> {

    //FIELD
    private OnClickListenerItemList mWorkerClickListener;
    /**
     * Interface for the click on an item of Lists
     */
    public interface OnClickListenerItemList {
        void onClickListener(int position);
    }
    /**
     * Create a new RecyclerView adapter that listens to a FireStore Query.  See {@link
     * FirestoreRecyclerOptions} for configuration options.
     *
     * @param options
     */
    public ListWorkersAdapter(@NonNull FirestoreRecyclerOptions<Worker> options, OnClickListenerItemList listener ) {
        super(options);
        this.mWorkerClickListener = listener;
    }

    //ViewHolder
    public static class WorkerViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        @BindView(R.id.worker_name)
        TextView worker_name;
        @BindView(R.id.worker_image)
        ImageView worker_image;
        OnClickListenerItemList mWorkerClickListener;


        public WorkerViewHolder(@NonNull View itemView, final OnClickListenerItemList listener) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            this.mWorkerClickListener = listener;
            itemView.setOnClickListener( this);
        }

        @Override
        public void onClick(View view) {
            mWorkerClickListener.onClickListener(getAdapterPosition());
        }
    }

    @Override
    protected void onBindViewHolder(@NonNull WorkerViewHolder workerViewHolder, int position, @NonNull Worker worker) {
        Resources resource = workerViewHolder.itemView.getContext().getResources();
        String text;
       if (!worker.getRestaurantName().trim().equals("")) {
            text = worker.getNameWorker() + " " + resource.getString(R.string.is_eating_at)+ " " + worker.getRestaurantName() ;
       } else {
            text = worker.getNameWorker() + " " + resource.getString(R.string.hasn_t_decided);
        }
        workerViewHolder.worker_name.setText(text);

        Glide.with(workerViewHolder.worker_image.getContext())
                .load(worker.getImageWorker())
                .error(R.drawable.ic_group)
                .apply(RequestOptions.circleCropTransform())
                .into(workerViewHolder.worker_image);
    }

    @NonNull
    @Override
    public WorkerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.workers_item, parent, false);
        return new WorkerViewHolder(view, mWorkerClickListener);
    }

}
