package bocanegra.mauro.yabank.domainlayer.adapters;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

import bocanegra.mauro.yabank.R;
import bocanegra.mauro.yabank.presentationlayer.activities.RecargaActivity;

public class CompaniesAdapter extends RecyclerView.Adapter<CompaniesAdapter.ViewHolder> {

    private ArrayList<String> mDataset;
    Context context;

    public CompaniesAdapter(ArrayList<String> dataSet, Context c){
        mDataset = dataSet;
        context = c;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_company, parent, false);
        return new CompaniesAdapter.ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        viewHolder.fullCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, RecargaActivity.class);
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount(){ return 5;}

    // -------------------------------------------------- //
    // ---------------- VIEWHOLDER CLASS ---------------- //
    //--------------------------------------------------- //

    public class ViewHolder extends RecyclerView.ViewHolder{

        private View fullCard;

        public ViewHolder(View v){
            super(v);
            fullCard = v.findViewById(R.id.item_container);
        }
    }
}
