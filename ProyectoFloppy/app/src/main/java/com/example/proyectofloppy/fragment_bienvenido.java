package com.example.proyectofloppy;

import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

public class fragment_bienvenido extends Fragment {

    public fragment_bienvenido() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_bienvenido, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        ImageView fotoPerfil = view.findViewById(R.id.imageUserInfo);

        if (fotoPerfil != null) {
            fotoPerfil.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // Acción para ir al fragment de Ajustes de Cuenta
                    irAAjustes();
                }
            });
        }
    }

    private void irAAjustes() {
        fragment_ajustesCuenta fragmentAjustes = new fragment_ajustesCuenta();

        FragmentTransaction transaction = getParentFragmentManager().beginTransaction();
        transaction.replace(R.id.fragment_container, fragmentAjustes);
        transaction.addToBackStack(null); // Esto permite volver atrás con el botón del móvil
        transaction.commit();
    }
}