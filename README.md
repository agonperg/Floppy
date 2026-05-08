# Floppy - Tu compañero universitario

Floppy es una aplicación móvil diseñada para facilitar la vida universitaria, permitiendo a los estudiantes gestionar comunidades, compartir apuntes, coordinar transporte y mucho más.

---

## 🛠️ Manual Técnico

Este apartado está dirigido a desarrolladores o personal técnico que desee compilar, instalar o revisar el código fuente del proyecto.

### 📋 Requisitos del Sistema

Para asegurar el correcto funcionamiento y compilación de la aplicación, se deben cumplir los siguientes requisitos:

*   **Sistema Operativo Android**: Compatible con dispositivos desde **Android 7.0 (Nougat, API 24)** hasta versiones superiores (probado hasta Android 14, API 34).
*   **Entorno de Desarrollo**: Android Studio Jellyfish (2023.3.1) o superior recomendado.
*   **Versión de Gradle**: 8.0 (Gradle Wrapper) / 8.1.4 (Android Gradle Plugin).
*   **Java Development Kit (JDK)**: Versión 17 o superior.
*   **Espacio en disco**: Se recomiendan al menos **500 MB** de espacio libre para la descarga de dependencias y el proceso de build.

### ⚙️ Instrucciones de Compilación e Instalación

Sigue estos pasos para poner en marcha el proyecto en un entorno local:

1.  **Clonar el repositorio**:
    ```bash
    git clone https://github.com/agonperg/Floppy.git
    ```
2.  **Abrir el proyecto**: Inicia Android Studio y selecciona `Open`, navegando hasta la carpeta raíz del proyecto.
3.  **Configuración de API Keys**:
    *   **Firebase**: El archivo de configuración `google-services.json` ya se encuentra incluido en la carpeta `/app`. Si deseas usar tu propia instancia, sustitúyelo por el tuyo.
    *   **Cloudinary**: Las credenciales para la gestión de archivos multimedia están integradas directamente en la clase `activity_general.java`. No es necesaria configuración adicional para pruebas.
4.  **Sincronización de dependencias**:
    *   Ve a `File` > `Sync Project with Gradle Files`.
    *   Espera a que Android Studio descargue todas las librerías necesarias (Firebase BoM, Cloudinary, Material Design, etc.).
5.  **Ejecución**:
    *   Conecta un dispositivo físico con la depuración USB activada o inicia un Emulador (AVD).
    *   Haz clic en el botón **Run** (icono de play verde) en la barra superior.

### 🔑 Credenciales de Prueba

Para facilitar la evaluación del tribunal y el acceso a todas las funcionalidades sin necesidad de registro previo, se han habilitado las siguientes credenciales de prueba:

*   **Email**: `usuario@prueba.com`
*   **Contraseña**: `123456`

> [!NOTE]
> Estas credenciales están conectadas a la base de datos de producción de Firebase. En caso de que no funcionen debido a cambios en el servidor, la aplicación permite el registro de nuevos usuarios de forma instantánea desde la pantalla principal.

---

## 🚀 Funcionalidades Principales

*   **Tablón de anuncios**: Noticias y actualizaciones de la comunidad.
*   **Gestión de Apuntes**: Subida y descarga de material académico mediante Cloudinary y Firestore.
*   **Transporte**: Búsqueda y publicación de viajes compartidos.
*   **Academias**: Localización y gestión de centros de apoyo docente.
*   **Comunidades**: Unirse y participar en grupos específicos por intereses.
