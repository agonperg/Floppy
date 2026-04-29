package com.example.proyectofloppy;

import org.junit.Test;
import static org.junit.Assert.*;

public class ExampleUnitTest {

    @Test
    public void email_isValid_isCorrect() {
        String emailValido = "test@estudiante.com";
        String emailInvalido = "correo-sin-arroba.com";

        assertTrue("El email debería ser válido", emailValido.contains("@"));
        assertFalse("El email no debería ser válido", emailInvalido.contains("@"));
    }

    @Test
    public void password_length_isCorrect() {
        String passCorrecta = "123456";
        String passCorta = "123";

        assertTrue(passCorrecta.length() >= 6);
        assertFalse(passCorta.length() >= 6);
    }

    @Test
    public void registration_fields_notEmpty() {
        String nombre = "Floppy User";
        String apellido = "";
        assertTrue(nombre != null && !nombre.trim().isEmpty());
        assertFalse(apellido != null && !apellido.trim().isEmpty());
    }
}