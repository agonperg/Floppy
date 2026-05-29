const features = [
  {
    icon: '🎓',
    title: 'Academias Educativas',
    desc: 'Explora diferentes academias y especialidades para ampliar tu formación académica de la mano de profesionales.',
  },
  {
    icon: '👥',
    title: 'Comunidades Activas',
    desc: 'Únete a comunidades de estudiantes con intereses similares. Comparte conocimientos y aprende colaborativamente.',
  },
  {
    icon: '📚',
    title: 'Biblioteca Digital',
    desc: 'Accede a una amplia colección de archivos PDF, materiales de estudio y recursos compartidos por la comunidad.',
  },
  {
    icon: '💬',
    title: 'Foro de Discusión',
    desc: 'Participa en debates académicos, haz preguntas y obtén respuestas de compañeros y docentes expertos.',
  },
  {
    icon: '🚗',
    title: 'Viajes Compartidos',
    desc: 'Organiza y encuentra compañeros para viajes a eventos académicos, excursiones educativas y actividades grupales.',
  },
  {
    icon: '⚙️',
    title: 'Perfil Personalizado',
    desc: 'Crea tu perfil como estudiante o docente, gestiona tus intereses y controla tu privacidad con facilidad.',
  },
]

const delays = ['d1', 'd2', 'd3', 'd4', 'd5', 'd6']

function FeatureCard({ icon, title, desc, delay }) {
  return (
    <div className={`feature-card reveal ${delay}`}>
      <div className="feature-icon-wrap">{icon}</div>
      <h3 className="feature-title">{title}</h3>
      <p className="feature-desc">{desc}</p>
    </div>
  )
}

export default function Features() {
  return (
    <section id="features" className="features">
      <div className="features-inner">
        <div className="section-header reveal">
          <span className="section-tag">¿Qué ofrece Floppy?</span>
          <h2 className="section-title">Todo lo que necesitas para aprender</h2>
          <div className="section-title-line" />
          <p className="section-subtitle">
            Floppy reúne las herramientas educativas más importantes en una sola aplicación,
            diseñada para estudiantes y docentes.
          </p>
        </div>
        <div className="features-grid">
          {features.map((f, i) => (
            <FeatureCard key={f.title} {...f} delay={delays[i]} />
          ))}
        </div>
      </div>
    </section>
  )
}
