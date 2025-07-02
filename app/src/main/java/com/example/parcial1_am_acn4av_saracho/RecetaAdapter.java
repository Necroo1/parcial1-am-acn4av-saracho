package com.example.parcial1_am_acn4av_saracho;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.List;

public class RecetaAdapter extends RecyclerView.Adapter<RecetaAdapter.RecetaViewHolder> {
    private List<Receta> recetaList;
    private Context context;
    private OnRecetaClickListener listener;

    public interface OnRecetaClickListener {
        void onRecetaClick(Receta receta);
    }

    public RecetaAdapter(Context context, List<Receta> recetaList, OnRecetaClickListener listener) {
        this.context = context;
        this.recetaList = recetaList;
        this.listener = listener;
    }

    @Override
    public RecetaViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_receta_image, parent, false);
        return new RecetaViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecetaViewHolder holder, int position) {
        Receta receta = recetaList.get(position);
        holder.txtNombre.setText(receta.getNombre());
        holder.txtPais.setText(receta.getPais());

        Glide.with(context)
                .load(receta.getImagen_url())
                .into(holder.imgReceta);

        holder.cardView.setOnClickListener(v -> listener.onRecetaClick(receta));
    }

    @Override
    public int getItemCount() {
        return recetaList.size();
    }

    public void updateList(List<Receta> list) {
        this.recetaList = list;
        notifyDataSetChanged();
    }

    static class RecetaViewHolder extends RecyclerView.ViewHolder {
        ImageView imgReceta;
        TextView txtNombre, txtPais;
        CardView cardView;

        RecetaViewHolder(View itemView) {
            super(itemView);
            imgReceta = itemView.findViewById(R.id.imgReceta);
            txtNombre = itemView.findViewById(R.id.txtNombre);
            txtPais = itemView.findViewById(R.id.txtPais);
            cardView = (CardView) itemView;
        }
    }
}
