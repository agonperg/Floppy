const steps = [
  {
    n: 1,
    title: 'Descarga e Instala Floppy',
    desc: 'Descarga el APK desde esta página (Android 7.0 o superior). Necesitarás al menos 150MB de espacio disponible y habilitar la instalación de fuentes desconocidas.',
  },
  {
    n: 2,
    title: 'Crea tu Cuenta',
    desc: 'Abre la app y elige si eres Estudiante o Docente. Completa el formulario con correo y contraseña, luego verifica tu email con el enlace que recibirás.',
  },
  {
    n: 3,
    title: 'Completa tu Perfil',
    desc: 'Añade foto de perfil, una biografía y selecciona tus intereses académicos para conectar con personas que compartan tus pasiones.',
  },
  {
    n: 4,
    title: 'Explora Academias y Comunidades',
    desc: 'Navega por academias especializadas y únete a comunidades tocando el botón "Unirse". Cada comunidad tiene su propio espacio de debate y recursos.',
  },
  {
    n: 5,
    title: 'Comparte y Colabora',
    desc: 'Sube materiales en PDF, participa en foros de discusión, haz preguntas y ayuda a otros. ¡Juntos aprendemos mejor!',
  },
  {
    n: 6,
    title: 'Organiza Viajes Compartidos',
    desc: 'Busca o crea viajes para ir a eventos académicos junto a otros usuarios. Coordina fácilmente desde la sección "Viajes".',
  },
]

const delays = ['d1', 'd2', 'd3', 'd4', 'd5', 'd6']

export default function Guide() {
  return (
    <section id="guide" className="guide">
      <div className="guide-inner">
        <div className="section-header reveal">
          <span className="section-tag">¿Cómo empezar?</span>
          <h2 className="section-title">Guía Rápida de Uso</h2>
          <div className="section-title-line" />
          <p className="section-subtitle">
            Empieza a usar Floppy en minutos siguiendo estos sencillos pasos.
          </p>
        </div>

        <div className={`guide-card reveal`}>
          <div className="guide-steps">
            {steps.map((step, i) => (
              <div key={step.n} className={`guide-step reveal ${delays[i]}`}>
                <div className="step-number-wrap">
                  <div className="step-number">{step.n}</div>
                  <div className="step-line" />
                </div>
                <div className="step-content">
                  <h3 className="step-title">{step.title}</h3>
                  <p className="step-desc">{step.desc}</p>
                </div>
              </div>
            ))}
          </div>
        </div>
      </div>
    </section>
  )
}
