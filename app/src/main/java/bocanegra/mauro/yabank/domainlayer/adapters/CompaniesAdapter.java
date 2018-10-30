package bocanegra.mauro.yabank.domainlayer.adapters;

import android.content.Context;
import android.content.Intent;
import android.media.Image;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import java.util.ArrayList;

import bocanegra.mauro.yabank.R;
import bocanegra.mauro.yabank.domainlayer.db.CompanyPOJO_DB;
import bocanegra.mauro.yabank.presentationlayer.activities.RecargaActivity;

public class CompaniesAdapter extends RecyclerView.Adapter<CompaniesAdapter.ViewHolder> {

    private ArrayList<CompanyPOJO_DB> mDataset;
    private Context context;

    public CompaniesAdapter(ArrayList<CompanyPOJO_DB> dataSet, Context c){
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

        switch(mDataset.get(i).getImg_id()){
            case CompanyPOJO_DB.key_img1:{
                viewHolder.img1.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.ic_claro));
                viewHolder.img2.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.ic_claro));
                viewHolder.img3.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.ic_claro));
                break;
            }
            case CompanyPOJO_DB.key_img2:{
                viewHolder.img1.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.ic_tuenti));
                viewHolder.img2.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.ic_tuenti));
                viewHolder.img3.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.ic_tuenti));
                break;
            }
            case CompanyPOJO_DB.key_img3:{
                viewHolder.img1.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.ic_entel));
                viewHolder.img2.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.ic_entel));
                viewHolder.img3.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.ic_entel));
                break;
            }
        }
    }

    @Override
    public int getItemCount(){ return mDataset.size();}

    // -------------------------------------------------- //
    // ---------------- VIEWHOLDER CLASS ---------------- //
    //--------------------------------------------------- //

    public class ViewHolder extends RecyclerView.ViewHolder{

        private View fullCard;
        private ImageView img1;
        private ImageView img2;
        private ImageView img3;


        public ViewHolder(View v){
            super(v);
            fullCard = v.findViewById(R.id.item_container);
            img1 = v.findViewById(R.id.item_company_img1);
            img2 = v.findViewById(R.id.item_company_img2);
            img3 = v.findViewById(R.id.item_company_img3);
        }
    }
}
