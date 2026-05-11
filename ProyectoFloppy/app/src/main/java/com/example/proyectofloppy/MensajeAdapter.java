package com.example.proyectofloppy;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;

public class MensajeAdapter extends RecyclerView.Adapter<MensajeAdapter.ViewHolder> {

    private List<Mensaje> listaMensajes;

    public MensajeAdapter(List<Mensaje> listaMensajes) {
        this.listaMensajes = listaMensajes;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_mensaje, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Mensaje mensaje = listaMensajes.get(position);
        holder.tvEmisor.setText(mensaje.getEmisorNombre());
        holder.tvContenido.setText(mensaje.getContenido());

        if (mensaje.getTimestamp() != null) {
            SimpleDateFormat sdf = new SimpleDateFormat("HH:mm", Locale.getDefault());
            holder.tvHora.setText(sdf.format(mensaje.getTimestamp().toDate()));
        } else {
            holder.tvHora.setText("");
        }
    }

    @Override
    public int getItemCount() {
        return listaMensajes.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvEmisor, tvContenido, tvHora;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvEmisor = itemView.findViewById(R.id.tvEmisor);
            tvContenido = itemView.findViewById(R.id.tvContenido);
            tvHora = itemView.findViewById(R.id.tvHora);
        }
    }
}
