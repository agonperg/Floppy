package com.example.proyectofloppy;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import java.util.List;

public class ComunidadAdapter extends RecyclerView.Adapter<ComunidadAdapter.ViewHolder> {

    private List<Comunidad> listaComunidades;
    private boolean esModoBusqueda = false; 

    public interface OnItemClickListener {
        void onItemClick(Comunidad comunidad);
    }

    public interface OnAccionClickListener {
        void onAccionClick(Comunidad comunidad);
    }

    private OnItemClickListener listener;
    private OnAccionClickListener accionListener;

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }

    public void setOnAccionClickListener(OnAccionClickListener accionListener) {
        this.accionListener = accionListener;
    }

    public void setEsModoBusqueda(boolean esModoBusqueda) {
        this.esModoBusqueda = esModoBusqueda;
    }

    public ComunidadAdapter(List<Comunidad> listaComunidades) {
        this.listaComunidades = listaComunidades;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_comunidad, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Comunidad comunidad = listaComunidades.get(position);

        holder.tvNombre.setText(comunidad.getNombre());
        holder.tvUbicacion.setText(comunidad.getUbicacion());
        
        // Mostrar último mensaje si existe
        if (comunidad.getUltimoMensaje() != null && !comunidad.getUltimoMensaje().isEmpty()) {
            holder.tvUltimo.setVisibility(View.VISIBLE);
            holder.tvUltimo.setText(comunidad.getUltimoMensaje());
        } else {
            holder.tvUltimo.setVisibility(View.GONE);
        }

        // Cargar imagen o letra
        if (comunidad.getImagenUrl() != null && !comunidad.getImagenUrl().isEmpty()) {
            holder.ivFoto.setVisibility(View.VISIBLE);
            holder.tvLetra.setVisibility(View.GONE);
            Glide.with(holder.itemView.getContext())
                    .load(comunidad.getImagenUrl())
                    .into(holder.ivFoto);
        } else {
            holder.ivFoto.setVisibility(View.GONE);
            holder.tvLetra.setVisibility(View.VISIBLE);
            if (comunidad.getNombre() != null && !comunidad.getNombre().isEmpty()) {
                holder.tvLetra.setText(comunidad.getNombre().substring(0, 1).toUpperCase());
            }
        }

        // Configurar icono del botón de acción
        if (esModoBusqueda) {
            holder.btnAccion.setImageResource(R.drawable.ic_add); 
            holder.btnAccion.setRotation(0); 
            holder.btnAccion.setColorFilter(android.graphics.Color.parseColor("#4CAF50")); 
        } else {
            holder.btnAccion.setImageResource(android.R.drawable.ic_menu_close_clear_cancel); 
            holder.btnAccion.setRotation(0); 
            holder.btnAccion.setColorFilter(android.graphics.Color.parseColor("#D32F2F")); 
        }

        holder.itemView.setOnClickListener(v -> {
            if (listener != null) {
                listener.onItemClick(comunidad);
            }
        });

        holder.btnAccion.setOnClickListener(v -> {
            if (accionListener != null) {
                accionListener.onAccionClick(comunidad);
            }
        });
    }

    @Override
    public int getItemCount() {
        return listaComunidades.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvLetra, tvNombre, tvUbicacion, tvUltimo;
        ImageView ivFoto;
        android.widget.ImageButton btnAccion;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvLetra = itemView.findViewById(R.id.tvLetraComunidad);
            tvNombre = itemView.findViewById(R.id.tvNombreComunidad);
            tvUbicacion = itemView.findViewById(R.id.tvUbicacionComunidad);
            tvUltimo = itemView.findViewById(R.id.tvUltimoMensaje);
            ivFoto = itemView.findViewById(R.id.ivFotoComunidad);
            btnAccion = itemView.findViewById(R.id.btnAbandonar); 
        }
    }

    public void filtrar(List<Comunidad> listaFiltrada) {
        this.listaComunidades = listaFiltrada;
        notifyDataSetChanged();
    }
}