package com.example.proyectofloppy;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

public class ViajeAdapter extends RecyclerView.Adapter<ViajeAdapter.ViewHolder> {

    private List<Viaje> listaViajes;

    public interface OnItemClickListener {
        void onItemClick(Viaje viaje);
    }

    private OnItemClickListener listener;

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }

    public ViajeAdapter(List<Viaje> listaViajes) {
        this.listaViajes = listaViajes;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_viaje, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Viaje viaje = listaViajes.get(position);
        String nombre = viaje.getConductorNombre() != null ? viaje.getConductorNombre() : "Conductor";
        holder.tvNombre.setText(nombre);
        holder.tvPlazas.setText(viaje.getPlazas() + " plazas");
        holder.tvFecha.setText(viaje.getFecha() + ", " + viaje.getHora() + "h");
        holder.itemView.setOnClickListener(v -> {
            if (listener != null) listener.onItemClick(viaje);
        });
    }

    @Override
    public int getItemCount() {
        return listaViajes.size();
    }

    public void actualizar(List<Viaje> nuevaLista) {
        this.listaViajes = nuevaLista;
        notifyDataSetChanged();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvNombre, tvPlazas, tvFecha;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvNombre = itemView.findViewById(R.id.tvNombreConductorItem);
            tvPlazas = itemView.findViewById(R.id.tvPlazasItem);
            tvFecha = itemView.findViewById(R.id.tvFechaItem);
        }
    }
}
