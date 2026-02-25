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

    public ComunidadAdapter(List<Comunidad> listaComunidades) {
        this.listaComunidades = listaComunidades;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Aquí conectamos el adaptador con tu diseño de tarjeta individual (item_comunidad)
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_comunidad, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Comunidad comunidad = listaComunidades.get(position);

        // Asignamos el nombre y la ubicación
        holder.tvNombre.setText(comunidad.getNombre());
        holder.tvUbicacion.setText(comunidad.getUbicacion());

        // Sacamos la primera letra del nombre para ponerla en el circulito morado
        if(comunidad.getNombre() != null && !comunidad.getNombre().isEmpty()){
            holder.tvLetra.setText(comunidad.getNombre().substring(0, 1).toUpperCase());
        }
    }

    @Override
    public int getItemCount() {
        return listaComunidades.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvLetra, tvNombre, tvUbicacion;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            // Buscamos los IDs dentro de item_comunidad.xml
            tvLetra = itemView.findViewById(R.id.tvLetraComunidad);
            tvNombre = itemView.findViewById(R.id.tvNombreComunidad);
            tvUbicacion = itemView.findViewById(R.id.tvUbicacionComunidad);
        }
    }
}