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
    private boolean isDocente = false;
    private OnDeleteClickListener deleteClickListener;

    public interface OnDeleteClickListener {
        void onDeleteClick(Academia academia);
    }

    public AcademiaAdapter(List<Academia> academiaList) {
        this.academiaList = academiaList;
    }

    public void setDocente(boolean docente) {
        this.isDocente = docente;
        notifyDataSetChanged();
    }

    public void setOnDeleteClickListener(OnDeleteClickListener listener) {
        this.deleteClickListener = listener;
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

        if (isDocente) {
            holder.btnDelete.setVisibility(View.VISIBLE);
            holder.btnDelete.setOnClickListener(v -> {
                if (deleteClickListener != null) {
                    deleteClickListener.onDeleteClick(academia);
                }
            });
        } else {
            holder.btnDelete.setVisibility(View.GONE);
        }
    }

    @Override
    public int getItemCount() {
        return academiaList.size();
    }

    public static class AcademiaViewHolder extends RecyclerView.ViewHolder {
        TextView tvNombre, tvDireccion, tvDescripcion;
        android.widget.ImageView btnDelete;

        public AcademiaViewHolder(@NonNull View itemView) {
            super(itemView);
            tvNombre = itemView.findViewById(R.id.tv_item_nombre);
            tvDireccion = itemView.findViewById(R.id.tv_item_direccion);
            tvDescripcion = itemView.findViewById(R.id.tv_item_descripcion);
            btnDelete = itemView.findViewById(R.id.btn_delete_academia);
        }
    }
}
