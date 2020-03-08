package com.example.as_glumci_30.adapters;

import android.content.Context;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.RecyclerView;

import com.example.as_glumci_30.R;
import com.example.as_glumci_30.db.model.Glumac;


import java.util.ArrayList;
import java.util.List;

public class MyAdapter extends RecyclerView.Adapter<MyAdapter.MyViewHolder> {

    private Context context;
    ArrayList<Glumac> glumac;
    private OnItemClickListener listener;

    public MyAdapter(Context context, ArrayList<Glumac> glumac, OnItemClickListener listener) {
        this.glumac = glumac;
        this.listener = listener;
        this.context = context;
    }

    public interface OnItemClickListener {
        void OnItemClick(int position);
    }

    public Glumac get(int position) {
        return glumac.get( position );

    }

    public void clear() {
        glumac.clear();

    }

    public void addAll(List<Glumac> glumciList) {

        glumac.addAll( glumciList );

    }

    @NonNull
    @Override
    public MyAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from( parent.getContext() ).inflate( R.layout.single_item, parent, false );


        return new MyAdapter.MyViewHolder( view, listener );
    }


    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    public void onBindViewHolder(@NonNull MyAdapter.MyViewHolder holder, int position) {
        holder.tvIme.setText( glumac.get( position ).getmIme() );
        holder.tvPrezime.setText( glumac.get( position ).getmPrezime() );
        holder.rbMalaOcena.setRating( glumac.get( position ).getmRating() );


    }


    @Override
    public int getItemCount() {
        return glumac.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private TextView tvIme;
        private TextView tvPrezime;
        private RatingBar rbMalaOcena;
        private OnItemClickListener vhListener;


        public MyViewHolder(@NonNull View itemView, OnItemClickListener vhListener) {
            super( itemView );
            tvIme = itemView.findViewById( R.id.tvIme );
            tvPrezime = itemView.findViewById( R.id.tvPrezime );
            rbMalaOcena = itemView.findViewById( R.id.rbMaleOcene );
            this.vhListener = vhListener;
            itemView.setOnClickListener( this );
        }


        @Override
        public void onClick(View v) {
            vhListener.OnItemClick( getAdapterPosition() );
        }


    }
}
