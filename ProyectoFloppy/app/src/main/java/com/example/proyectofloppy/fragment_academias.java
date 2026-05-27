package com.example.proyectofloppy;

import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.card.MaterialCardView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

import java.util.ArrayList;
import java.util.List;

public class fragment_academias extends Fragment {

    private RecyclerView rvAcademias;
    private AcademiaAdapter adapter;
    private List<Academia> academiaList = new ArrayList<>();
    private List<Academia> listaOriginal = new ArrayList<>();
    private com.google.android.material.floatingactionbutton.FloatingActionButton fabAdd;
    private FirebaseFirestore db;
    private FirebaseAuth mAuth;

    private String categoriaSeleccionada = "Todas";
    private String textoBusqueda = "";

    // Category chips
    private MaterialCardView chipTodas, chipIdiomas, chipCiencias, chipLetras, chipApoyo;
    private TextView tvTodas, tvIdiomas, tvCiencias, tvLetras, tvApoyo;

    public fragment_academias() {}

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_academias, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        db = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();

        rvAcademias = view.findViewById(R.id.rv_academias);
        fabAdd = view.findViewById(R.id.fab_add_academia);
        ImageView btnBack = view.findViewById(R.id.btn_back_academias);
        EditText etBuscar = view.findViewById(R.id.et_buscar_academia);

        // Category Chips
        chipTodas = view.findViewById(R.id.chip_todas);
        chipIdiomas = view.findViewById(R.id.chip_idiomas);
        chipCiencias = view.findViewById(R.id.chip_ciencias);
        chipLetras = view.findViewById(R.id.chip_letras);
        chipApoyo = view.findViewById(R.id.chip_apoyo);

        tvTodas = view.findViewById(R.id.tv_chip_todas);
        tvIdiomas = view.findViewById(R.id.tv_chip_idiomas);
        tvCiencias = view.findViewById(R.id.tv_chip_ciencias);
        tvLetras = view.findViewById(R.id.tv_chip_letras);
        tvApoyo = view.findViewById(R.id.tv_chip_apoyo);

        // Hide FAB by default, then show based on role
        fabAdd.setVisibility(View.GONE);

        adapter = new AcademiaAdapter(academiaList);
        rvAcademias.setLayoutManager(new LinearLayoutManager(getContext()));
        rvAcademias.setAdapter(adapter);

        btnBack.setOnClickListener(v -> getParentFragmentManager().popBackStack());

        fabAdd.setOnClickListener(v -> {
            getParentFragmentManager().beginTransaction()
                    .replace(R.id.fragment_container, new fragment_crear_academia())
                    .addToBackStack(null)
                    .commit();
        });

        // Set item click listener
        adapter.setOnItemClickListener(academia -> {
            fragment_detalle_academia fragment = new fragment_detalle_academia();
            Bundle bundle = new Bundle();
            bundle.putString("id", academia.getId());
            bundle.putString("nombre", academia.getNombre());
            bundle.putString("direccion", academia.getDireccion());
            bundle.putString("descripcion", academia.getDescripcion());
            bundle.putString("creadorId", academia.getCreadorId());
            fragment.setArguments(bundle);

            getParentFragmentManager().beginTransaction()
                    .replace(R.id.fragment_container, fragment)
                    .addToBackStack(null)
                    .commit();
        });

        // Setup search filter
        if (etBuscar != null) {
            etBuscar.addTextChangedListener(new TextWatcher() {
                @Override public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
                @Override public void onTextChanged(CharSequence s, int start, int before, int count) {}
                @Override public void afterTextChanged(Editable s) {
                    textoBusqueda = s.toString();
                    aplicarFiltros();
                }
            });
        }

        // Setup Category Listeners
        setupCategoryChips();

        // Fetch User Role
        comprobarRolUsuario();

        // Fetch Academies
        cargarAcademias();
    }

    private void setupCategoryChips() {
        chipTodas.setOnClickListener(v -> seleccionarCategoria("Todas"));
        chipIdiomas.setOnClickListener(v -> seleccionarCategoria("Idiomas"));
        chipCiencias.setOnClickListener(v -> seleccionarCategoria("Ciencias"));
        chipLetras.setOnClickListener(v -> seleccionarCategoria("Letras"));
        chipApoyo.setOnClickListener(v -> seleccionarCategoria("Apoyo"));
    }

    private void seleccionarCategoria(String categoria) {
        categoriaSeleccionada = categoria;
        
        // Reset colors
        deseleccionarChip(chipTodas, tvTodas);
        deseleccionarChip(chipIdiomas, tvIdiomas);
        deseleccionarChip(chipCiencias, tvCiencias);
        deseleccionarChip(chipLetras, tvLetras);
        deseleccionarChip(chipApoyo, tvApoyo);

        // Set active colors
        if (categoria.equals("Todas")) seleccionarChip(chipTodas, tvTodas);
        else if (categoria.equals("Idiomas")) seleccionarChip(chipIdiomas, tvIdiomas);
        else if (categoria.equals("Ciencias")) seleccionarChip(chipCiencias, tvCiencias);
        else if (categoria.equals("Letras")) seleccionarChip(chipLetras, tvLetras);
        else if (categoria.equals("Apoyo")) seleccionarChip(chipApoyo, tvApoyo);

        aplicarFiltros();
    }

    private void seleccionarChip(MaterialCardView card, TextView text) {
        card.setCardBackgroundColor(Color.parseColor("#0C3E69"));
        text.setTextColor(Color.parseColor("#FFFFFF"));
        text.setTypeface(null, android.graphics.Typeface.BOLD);
    }

    private void deseleccionarChip(MaterialCardView card, TextView text) {
        card.setCardBackgroundColor(Color.parseColor("#F0EBF7"));
        text.setTextColor(Color.parseColor("#0C3E69"));
        text.setTypeface(null, android.graphics.Typeface.NORMAL);
    }

    private void comprobarRolUsuario() {
        if (mAuth.getCurrentUser() == null) return;
        String uid = mAuth.getCurrentUser().getUid();

        db.collection("users").document(uid).get()
                .addOnSuccessListener(documentSnapshot -> {
                    if (documentSnapshot.exists()) {
                        String rol = documentSnapshot.getString("rol");
                        if ("docente".equalsIgnoreCase(rol)) {
                            fabAdd.setVisibility(View.VISIBLE);
                        } else {
                            fabAdd.setVisibility(View.GONE);
                        }
                    }
                })
                .addOnFailureListener(e -> {
                    Log.e("fragment_academias", "Error al obtener rol", e);
                });
    }

    private void cargarAcademias() {
        db.collection("academias")
                .get()
                .addOnSuccessListener(querySnapshot -> {
                    listaOriginal.clear();
                    for (DocumentSnapshot doc : querySnapshot.getDocuments()) {
                        Academia ac = doc.toObject(Academia.class);
                        if (ac != null) {
                            ac.setId(doc.getId());
                            if ("Academia Estrella".equalsIgnoreCase(ac.getNombre()) || "prueba".equalsIgnoreCase(ac.getDescripcion())) {
                                db.collection("academias").document(doc.getId()).delete();
                                continue;
                            }
                            listaOriginal.add(ac);
                        }
                    }
                    
                    // Si hay pocas academias en la base de datos, las fusionamos con las 20 reales
                    if (listaOriginal.size() < 15) {
                        crearAcademiasSemilla();
                    } else {
                        aplicarFiltros();
                    }
                })
                .addOnFailureListener(e -> {
                    Log.e("fragment_academias", "Error al cargar academias", e);
                    // Como fallback por si Firestore no tiene internet, mostramos semilla de todos modos
                    crearAcademiasSemilla();
                });
    }

    private void crearAcademiasSemilla() {
        List<Academia> semillas = new ArrayList<>();

        // --- IDIOMAS (5) ---
        semillas.add(new Academia("1", "Academia X-Idiomas", "Aprende inglés, francés y alemán con profesores nativos. Preparación de títulos oficiales B1/B2/C1 de Cambridge y DELF.", "Av. de Madrid, 45, Jaén", "system"));
        semillas.add(new Academia("2", "Jaén English Centre", "Centro preparador premium de exámenes de Cambridge (First, Advanced). Clases conversacionales, simulacros de examen mensuales y grupos reducidos para universitarios.", "Calle Virgen de la Capilla, 12, Jaén", "system"));
        semillas.add(new Academia("3", "Alliance Française Jaén", "Cursos oficiales de francés para todos los niveles. Preparación intensiva de exámenes DELF/DALF con profesores nativos y materiales oficiales acreditados.", "Calle San Clemente, 8, Jaén", "system"));
        semillas.add(new Academia("4", "Schule Deutsch Jaén", "Aprende alemán de forma práctica y comunicativa. Cursos intensivos para Erasmus e ingenieros que buscan empleo en Alemania. Niveles A1-C1.", "Paseo de la Estación, 32, Jaén", "system"));
        semillas.add(new Academia("5", "Language Hub Academy", "Tu centro multicultural en Jaén. Cursos de italiano, portugués y chino comercial. Intercambios lingüísticos gratuitos los viernes para alumnos matriculados.", "Calle Navas de Tolosa, 19, Jaén", "system"));

        // --- CIENCIAS (5) ---
        semillas.add(new Academia("6", "Academia Ateneo", "Clases de apoyo universitario para Ingeniería, Matemáticas y Física. Preparación intensiva de exámenes de la UJA con exámenes de años anteriores.", "Calle Linares, 17, Jaén", "system"));
        semillas.add(new Academia("7", "Centro de Estudios Pitágoras", "Apoyo integral en Ciencias para Secundaria, Bachillerato y primeros cursos de Universidad. Expertos en Álgebra, Cálculo, Química y Estadística.", "Calle Millán de Priego, 22, Jaén", "system"));
        semillas.add(new Academia("8", "Newton Aula de Ciencias", "Especialistas en la preparación de selectividad en matemáticas (sociales y científicas), física y química. Grupos de máximo 6 alumnos para atención personalizada.", "Calle Baeza, 4, Jaén", "system"));
        semillas.add(new Academia("9", "Sigma Matemáticas & Física", "Clases particulares de nivel universitario. Cálculo infinitesimal, física cuántica, estadística descriptiva e inferencial para titulaciones técnicas.", "Calle Doctor Eduardo García-Triviño, 15, Jaén", "system"));
        semillas.add(new Academia("10", "Química Avanzada Jaén", "Refuerzo específico para estudiantes de los grados de Química, Biología y Ciencias Ambientales. Laboratorio propio para prácticas complementarias.", "Calle Álamos, 25, Jaén", "system"));

        // --- LETRAS (5) ---
        semillas.add(new Academia("11", "Centro de Estudios Letras", "Clases particulares de Derecho, Historia, Lengua y Literatura para selectividad y grados universitarios.", "Calle Virgen de la Cabeza, 8, Jaén", "system"));
        semillas.add(new Academia("12", "Academia Minerva", "Preparación de asignaturas de humanidades, literatura universal y filosofía. Técnicas de estudio avanzadas, redacción académica y comprensión de textos.", "Calle Hurtado, 14, Jaén", "system"));
        semillas.add(new Academia("13", "Clases de Apoyo Derecho UJA", "Refuerzo especializado para el Grado en Derecho. Derecho Civil, Constitucional, Penal y Procesal explicado por profesionales del sector.", "Calle Muñoz Garnica, 6, Jaén", "system"));
        semillas.add(new Academia("14", "Aula de Humanidades Séneca", "Apoyo a estudiantes de Historia, Historia del Arte y Filologías. Talleres de latín, griego clásico y comentario de texto para oposiciones y universidad.", "Calle Roldán y Marín, 3, Jaén", "system"));
        semillas.add(new Academia("15", "Redacción y Literatura Jaén", "Taller de escritura creativa, comentarios de texto críticos y clases de Lengua Castellana para estudiantes de secundaria, bachillerato e idiomas.", "Calle Maestra, 10, Jaén", "system"));

        // --- APOYO UJA (5) ---
        semillas.add(new Academia("16", "Apoyo Universitario UJA Jaén", "Academia especializada exclusivamente en asignaturas difíciles del campus de Las Lagunillas. Tutores especializados en las materias de la UJA.", "Calle Ben Saprut, 3, Jaén", "system"));
        semillas.add(new Academia("17", "Academia Las Lagunillas", "Frente al campus universitario de Jaén. Apoyo en Economía, ADE, Finanzas y Contabilidad para todos los cursos de los grados de la UJA.", "Av. de Andalucía, 78, Jaén", "system"));
        semillas.add(new Academia("18", "Informática UJA Academy", "Clases de programación en Java, C++, Python y bases de datos SQL para los grados de Ingeniería Informática de la UJA. Prácticas y proyectos guiados.", "Calle Senda de los Huertos, 14, Jaén", "system"));
        semillas.add(new Academia("19", "Enfermería Jaén Refuerzo", "Clases de apoyo específicas para el Grado en Enfermería de la UJA. Anatomía, Fisiología, Farmacología y tutoría en simulación clínica.", "Calle Bernabé Soriano, 18, Jaén", "system"));
        semillas.add(new Academia("20", "Academia Magna UJA", "Preparación integral de asignaturas filtro (las más suspendidas) de la Universidad de Jaén. Contamos con un 95% de aprobados en la convocatoria de julio.", "Calle Doctor García Triviño, 8, Jaén", "system"));

        for (Academia sem : semillas) {
            boolean existe = false;
            for (Academia original : listaOriginal) {
                if (original.getId().equals(sem.getId()) || original.getNombre().equalsIgnoreCase(sem.getNombre())) {
                    existe = true;
                    break;
                }
            }
            if (!existe) {
                listaOriginal.add(sem);
                db.collection("academias").document(sem.getId()).set(sem);
            }
        }

        aplicarFiltros();
    }

    private void aplicarFiltros() {
        academiaList.clear();
        for (Academia ac : listaOriginal) {
            // Filtro por Texto (búsqueda)
            boolean coincideTexto = true;
            if (!textoBusqueda.isEmpty()) {
                String searchLower = textoBusqueda.toLowerCase();
                coincideTexto = (ac.getNombre() != null && ac.getNombre().toLowerCase().contains(searchLower))
                        || (ac.getDireccion() != null && ac.getDireccion().toLowerCase().contains(searchLower))
                        || (ac.getDescripcion() != null && ac.getDescripcion().toLowerCase().contains(searchLower));
            }

            // Filtro por Categoría
            boolean coincideCategoria = true;
            if (!categoriaSeleccionada.equals("Todas")) {
                String desc = (ac.getDescripcion() != null ? ac.getDescripcion().toLowerCase() : "");
                String nom = (ac.getNombre() != null ? ac.getNombre().toLowerCase() : "");
                
                if (categoriaSeleccionada.equals("Idiomas")) {
                    coincideCategoria = desc.contains("inglés") || desc.contains("idioma") || desc.contains("francés") || desc.contains("nativo") || nom.contains("idioma");
                } else if (categoriaSeleccionada.equals("Ciencias")) {
                    coincideCategoria = desc.contains("física") || desc.contains("matemáticas") || desc.contains("ciencias") || desc.contains("química") || desc.contains("ingeniería");
                } else if (categoriaSeleccionada.equals("Letras")) {
                    coincideCategoria = desc.contains("derecho") || desc.contains("historia") || desc.contains("lengua") || desc.contains("letras");
                } else if (categoriaSeleccionada.equals("Apoyo")) {
                    coincideCategoria = desc.contains("uja") || desc.contains("apoyo") || nom.contains("uja") || nom.contains("apoyo");
                }
            }

            if (coincideTexto && coincideCategoria) {
                academiaList.add(ac);
            }
        }
        adapter.notifyDataSetChanged();
    }
}