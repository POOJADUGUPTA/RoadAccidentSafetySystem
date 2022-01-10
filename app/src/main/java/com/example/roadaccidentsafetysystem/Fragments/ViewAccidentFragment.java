package com.example.roadaccidentsafetysystem.Fragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.roadaccidentsafetysystem.Adapters.AdapterViewAccident;
import com.example.roadaccidentsafetysystem.LoginActivity;
import com.example.roadaccidentsafetysystem.Models.ModelViewAccident;
import com.example.roadaccidentsafetysystem.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ViewAccidentFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ViewAccidentFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public ViewAccidentFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ViewAccidentFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ViewAccidentFragment newInstance(String param1, String param2) {
        ViewAccidentFragment fragment = new ViewAccidentFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }


    FirebaseAuth firebaseAuth;
    String mUID = "uid";

    RecyclerView viewAccidentRv;


    private ArrayList<ModelViewAccident> viewAccidentList;
    AdapterViewAccident adapterViewAccident;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_view_accident, container, false);

        firebaseAuth = FirebaseAuth.getInstance();

        firebaseAuth = FirebaseAuth.getInstance();
        viewAccidentRv = view.findViewById(R.id.viewAccidentRv);
        viewAccidentList = new ArrayList<>();

        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        viewAccidentRv.setLayoutManager(layoutManager);

        FirebaseDatabase.getInstance().getReference("Accidents")
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        viewAccidentList.clear();
                        for (DataSnapshot ds : snapshot.getChildren()) {
                            ModelViewAccident modelViewAccident = ds.getValue(ModelViewAccident.class);
                            viewAccidentList.add(modelViewAccident);
                        }

                        adapterViewAccident = new AdapterViewAccident(getActivity(), viewAccidentList);
                        viewAccidentRv.setAdapter(adapterViewAccident);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

        checkUserStatus();

        return view;
    }

    private void checkUserStatus() {
        FirebaseUser user = firebaseAuth.getCurrentUser();
        if (user != null) {
            mUID = user.getUid();

        } else {
            startActivity(new Intent(getActivity(), LoginActivity.class));
            getActivity().finish();
        }
    }
}