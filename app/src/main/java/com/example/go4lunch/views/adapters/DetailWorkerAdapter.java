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

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class DetailWorkerAdapter extends RecyclerView.Adapter<DetailWorkerAdapter.DetailViewHolder> {

    //FIELD
    private List<Worker> mWorkers;

    //construtor
    public DetailWorkerAdapter(List<Worker> workers) {
        mWorkers = workers;
    }

    //ViewHolder
    class DetailViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.worker_image)
        ImageView mImageView;
        @BindView(R.id.worker_name)
        TextView mTextView;

        DetailViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    @NonNull
    @Override
    public DetailViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.workers_item, parent, false);
        return new DetailViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull DetailViewHolder holder, int position) {
        Worker workers = mWorkers.get(position);
        Resources resources = holder.itemView.getResources();

        String text = workers.getNameWorker() + resources.getString(R.string.is_joining);
        holder.mTextView.setText(text);

        Glide.with(holder.mImageView.getContext())
                .load(workers.getImageWorker())
                .error(R.drawable.ic_identity_black)
                .apply(RequestOptions.circleCropTransform())
                .into(holder.mImageView);
    }

    @Override
    public int getItemCount() {
        return (mWorkers != null) ? mWorkers.size() : 0;
    }
}
