package bocanegra.mauro.yabank.presentationlayer.fragments;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

import bocanegra.mauro.yabank.R;
import bocanegra.mauro.yabank.domainlayer.adapters.CompaniesAdapter;


public class RecargasFragment extends Fragment {

    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private LinearLayoutManager mLayoutManager;
    private ArrayList<String> recargas;


    public RecargasFragment() {
        // Required empty public constructor
    }

    public static RecargasFragment newInstance() {
        return new RecargasFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_recargas, container, false);

        mRecyclerView = (RecyclerView) view.findViewById(R.id.companies_recyclerview);
        mRecyclerView.setHasFixedSize(true);

        recargas = new ArrayList<>();
        mAdapter = new CompaniesAdapter(recargas, getActivity());
        mRecyclerView.setAdapter(mAdapter);
        mLayoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(mLayoutManager);

        return view;
    }
}
