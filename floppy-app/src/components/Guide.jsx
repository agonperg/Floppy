const steps = [
  {
    n: 1,
    title: 'Descarga e Instala Floppy',
    desc: 'Descarga el APK directamente desde esta página (disponible para Android 7.0 o superior). Asegúrate de tener al menos 150MB de espacio disponible en tu dispositivo.',
  },
  {
    n: 2,
    title: 'Crea tu Cuenta',
    desc: 'Abre la app y selecciona si eres Estudiante o Docente. Completa el formulario con tu correo electrónico y contraseña. Verifica tu email con el código que recibirás.',
  },
  {
    n: 3,
    title: 'Completa tu Perfil',
    desc: 'Agrega una foto de perfil, una biografía y selecciona tus intereses académicos. Esto te ayudará a conectar con personas que comparten tus pasiones educativas.',
  },
  {
    n: 4,
    title: 'Explora Academias y Comunidades',
    desc: 'Navega por la sección de academias para encontrar especialidades que te interesen. Únete a comunidades relevantes tocando el botón "Unirse".',
  },
  {
    n: 5,
    title: 'Comparte y Colabora',
    desc: 'Sube materiales educativos en PDF, participa en debates, haz preguntas y ayuda a otros compañeros. ¡Juntos aprendemos mejor!',
  },
  {
    n: 6,
    title: 'Organiza Viajes',
    desc: 'Si necesitas transporte, busca viajes disponibles o crea uno nuevo en la sección "Viajes". Coordina con otros usuarios para ir juntos a eventos académicos.',
  },
]

export default function Guide() {
  return (
    <section
      id="guide"
      style={{
        padding: '5rem 2rem',
        background: 'linear-gradient(135deg, #f5f7fa 0%, #c3cfe2 100%)',
      }}
    >
      <div style={{ maxWidth: '1200px', margin: '0 auto' }}>
        <div
          style={{
            background: 'white',
            padding: '3rem',
            borderRadius: '16px',
            boxShadow: '0 8px 32px rgba(102,126,234,0.12)',
          }}
        >
          <h2
            style={{
              fontFamily: 'var(--font-display)',
              fontSize: '2rem',
              fontWeight: 900,
              marginBottom: '2.5rem',
              color: '#1a1a2e',
            }}
          >
            Guía Rápida de Uso
          </h2>

          {steps.map((step) => (
            <div
              key={step.n}
              style={{
                display: 'flex',
                gap: '1.75rem',
                marginBottom: '2rem',
                alignItems: 'flex-start',
              }}
            >
              <div
                style={{
                  background: 'linear-gradient(135deg, #667eea, #764ba2)',
                  color: 'white',
                  width: '50px',
                  height: '50px',
                  borderRadius: '50%',
                  display: 'flex',
                  alignItems: 'center',
                  justifyContent: 'center',
                  fontWeight: 900,
                  fontSize: '1.2rem',
                  flexShrink: 0,
                  fontFamily: 'var(--font-display)',
                  boxShadow: '0 4px 12px rgba(102,126,234,0.3)',
                }}
              >
                {step.n}
              </div>
              <div>
                <h3
                  style={{
                    fontSize: '1.1rem',
                    fontWeight: 700,
                    marginBottom: '0.4rem',
                    color: '#1a1a2e',
                    fontFamily: 'var(--font-display)',
                  }}
                >
                  {step.title}
                </h3>
                <p style={{ color: '#666', fontSize: '0.95rem', lineHeight: 1.65 }}>
                  {step.desc}
                </p>
              </div>
            </div>
          ))}
        </div>
      </div>
    </section>
  )
}