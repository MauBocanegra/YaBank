package bocanegra.mauro.yabank.presentationlayer.fragments;

import android.app.Activity;
import android.content.Context;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

import bocanegra.mauro.yabank.R;
import bocanegra.mauro.yabank.domainlayer.WS_DB;
import bocanegra.mauro.yabank.domainlayer.adapters.CompaniesAdapter;
import bocanegra.mauro.yabank.domainlayer.db.AppDatabase;
import bocanegra.mauro.yabank.domainlayer.db.CompanyDAO;
import bocanegra.mauro.yabank.domainlayer.db.CompanyPOJO_DB;


public class RecargasFragment extends Fragment implements WS_DB.OnDBAllRequested {

    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private LinearLayoutManager mLayoutManager;
    private ArrayList<CompanyPOJO_DB> recargas;

    AppDatabase database;
    CompanyDAO companyDAO;

    private static final String TAG="RecargasFragment";


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

        WS_DB.instanciateDB(getActivity(), this);

        return view;
    }

    @Override
    public void dbAnswered(ArrayList<CompanyPOJO_DB> arrayList) {
        Log.d(TAG, "num_of_companies = "+arrayList.size());

        recargas = arrayList;
        mAdapter = new CompaniesAdapter(recargas, getActivity());
        mRecyclerView.setAdapter(mAdapter);
        mLayoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(mLayoutManager);
    }
}
