package com.example.proyectofloppy;

import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.PopupMenu;

public class fragment_ajustesCuenta extends Fragment {

    private EditText etNombre, etCarrera;
    private Button btnGuardar;
    private ImageView ivSettings;

    public fragment_ajustesCuenta() {}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_ajustes_cuenta, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        etNombre = view.findViewById(R.id.etNombreUsuario);
        etCarrera = view.findViewById(R.id.etCarreraFacultad);
        btnGuardar = view.findViewById(R.id.btnGuardarPerfil);
        ivSettings = view.findViewById(R.id.ivSettings);

        if (btnGuardar != null) {
            btnGuardar.setOnClickListener(v -> volverABienvenida());
        }

        if (ivSettings != null) {
            ivSettings.setOnClickListener(this::mostrarMenuDesplegable);
        }
    }

    private void mostrarMenuDesplegable(View v) {
        PopupMenu popup = new PopupMenu(getContext(), v);
        popup.getMenu().add(0, 1, 0, "Notificaciones");
        popup.getMenu().add(0, 2, 1, "Tema (Oscuro/Claro)");

        popup.setOnMenuItemClickListener(item -> {
            if (item.getItemId() == 1) {
                abrirAjustesNotificaciones();
                return true;
            } else if (item.getItemId() == 2) {
                alternarModoOscuro();
                return true;
            }
            return false;
        });
        popup.show();
    }

    private void abrirAjustesNotificaciones() {
        Intent intent = new Intent();
        intent.setAction("android.settings.APP_NOTIFICATION_SETTINGS");
        intent.putExtra("android.provider.extra.APP_PACKAGE", getContext().getPackageName());
        startActivity(intent);
    }

    private void alternarModoOscuro() {
        int modoActual = AppCompatDelegate.getDefaultNightMode();
        if (modoActual == AppCompatDelegate.MODE_NIGHT_YES) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        } else {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
        }
    }

    private void volverABienvenida() {
        if (getParentFragmentManager() != null) {
            getParentFragmentManager().popBackStack();
        }
    }
}