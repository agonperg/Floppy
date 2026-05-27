package com.example.proyectofloppy;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

public class AcademiaAdapter extends RecyclerView.Adapter<AcademiaAdapter.AcademiaViewHolder> {

    private List<Academia> academiaList;
    private OnItemClickListener listener;

    public interface OnItemClickListener {
        void onItemClick(Academia academia);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }

    public AcademiaAdapter(List<Academia> academiaList) {
        this.academiaList = academiaList;
    }

    @NonNull
    @Override
    public AcademiaViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_academia, parent, false);
        return new AcademiaViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AcademiaViewHolder holder, int position) {
        Academia academia = academiaList.get(position);
        holder.tvNombre.setText(academia.getNombre());
        holder.tvDireccion.setText(academia.getDireccion());
        holder.tvDescripcion.setText(academia.getDescripcion());

        holder.itemView.setOnClickListener(v -> {
            if (listener != null) {
                listener.onItemClick(academia);
            }
        });
    }

    @Override
    public int getItemCount() {
        return academiaList.size();
    }

    public static class AcademiaViewHolder extends RecyclerView.ViewHolder {
        TextView tvNombre, tvDireccion, tvDescripcion;

        public AcademiaViewHolder(@NonNull View itemView) {
            super(itemView);
            tvNombre = itemView.findViewById(R.id.tv_item_nombre);
            tvDireccion = itemView.findViewById(R.id.tv_item_direccion);
            tvDescripcion = itemView.findViewById(R.id.tv_item_descripcion);
        }
    }
}
