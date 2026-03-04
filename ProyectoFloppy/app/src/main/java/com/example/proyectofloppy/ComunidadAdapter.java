package com.example.proyectofloppy;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

public class ComunidadAdapter extends RecyclerView.Adapter<ComunidadAdapter.ViewHolder> {

    private List<Comunidad> listaComunidades;

    // 1. Definimos la interfaz para el clic
    public interface OnItemClickListener {
        void onItemClick(Comunidad comunidad);
    }

    private OnItemClickListener listener;

    // 2. Método para configurar el listener desde el Fragment
    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
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

        if(comunidad.getNombre() != null && !comunidad.getNombre().isEmpty()){
            holder.tvLetra.setText(comunidad.getNombre().substring(0, 1).toUpperCase());
        }

        // 3. Programamos el clic en toda la tarjeta (itemView)
        holder.itemView.setOnClickListener(v -> {
            if (listener != null) {
                listener.onItemClick(comunidad);
            }
        });
    }

    @Override
    public int getItemCount() {
        return listaComunidades.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvLetra, tvNombre, tvUbicacion;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvLetra = itemView.findViewById(R.id.tvLetraComunidad);
            tvNombre = itemView.findViewById(R.id.tvNombreComunidad);
            tvUbicacion = itemView.findViewById(R.id.tvUbicacionComunidad);
        }
    }

    public void filtrar(List<Comunidad> listaFiltrada) {
        this.listaComunidades = listaFiltrada;
        notifyDataSetChanged();
    }
}