package com.example.go4lunch.views.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.go4lunch.R;
import com.example.go4lunch.api.RepositoryFirebase;
import com.example.go4lunch.model.Worker;
import com.example.go4lunch.views.activities.RestaurantDetails;
import com.example.go4lunch.views.adapters.ListWorkersAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreSettings;
import com.google.firebase.firestore.Query;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class WorkmatesList extends Fragment implements ListWorkersAdapter.OnClickListenerItemList {

    @BindView(R.id.list_workmates_recyclerView)
    RecyclerView workersRecyclerView;
    private ListWorkersAdapter mAdapter;
    private List<Worker> workersList = new ArrayList<>();

    public WorkmatesList() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FirebaseFirestore fireStore = FirebaseFirestore.getInstance();
        FirebaseFirestoreSettings settings = new FirebaseFirestoreSettings.Builder()
                .build();
        fireStore.setFirestoreSettings(settings);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_list_workemates, container, false);
        ButterKnife.bind(this, view);
        workersRecyclerView.setHasFixedSize(true);
        workersRecyclerView.setLayoutManager(new LinearLayoutManager(view.getContext()));
        initList();
        return view;
    }


    private void initList() {

       Query query = RepositoryFirebase.getQueryWorkers(workersList);
        FirestoreRecyclerOptions<Worker> options = new FirestoreRecyclerOptions.Builder<Worker>()
                .setQuery(query, Worker.class)
                .build();
        mAdapter = new ListWorkersAdapter(options, this);
        workersRecyclerView.setAdapter(mAdapter);

    }

    @Override
    public void onStart() {
        super.onStart();
        mAdapter.startListening();
    }

    @Override
    public void onStop() {
        super.onStop();
        mAdapter.stopListening();
    }

    @Override
    public void onClickListener(int position) {
        if (workersList.get(position).getPlaceId().trim().equals("")) {
            Toast.makeText(getContext(), "No choice restaurant worker ", Toast.LENGTH_LONG).show();
        } else {
            Intent intent = new Intent(getContext(), RestaurantDetails.class);
            intent.putExtra("placeId", workersList.get(position).getPlaceId());
            intent.putExtra("restaurantName", workersList.get(position).getRestaurantChoose());
            startActivity(intent);
        }
    }
}
