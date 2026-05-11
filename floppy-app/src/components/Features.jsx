import { useState } from 'react'

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

function FeatureCard({ icon, title, desc }) {
  const [hovered, setHovered] = useState(false)

  return (
    <div
      style={{
        background: 'white',
        padding: '2rem',
        borderRadius: '14px',
        textAlign: 'center',
        transition: 'all 0.3s',
        borderLeft: '4px solid #667eea',
        transform: hovered ? 'translateY(-6px)' : 'translateY(0)',
        boxShadow: hovered
          ? '0 12px 32px rgba(102,126,234,0.2)'
          : '0 2px 10px rgba(0,0,0,0.06)',
        cursor: 'default',
      }}
      onMouseEnter={() => setHovered(true)}
      onMouseLeave={() => setHovered(false)}
    >
      <div style={{ fontSize: '2.8rem', marginBottom: '1rem' }}>{icon}</div>
      <h3
        style={{
          fontSize: '1.15rem',
          marginBottom: '0.75rem',
          color: '#667eea',
          fontFamily: 'var(--font-display)',
          fontWeight: 800,
        }}
      >
        {title}
      </h3>
      <p style={{ color: '#666', fontSize: '0.93rem', lineHeight: 1.65 }}>{desc}</p>
    </div>
  )
}

export default function Features() {
  return (
    <section id="features" style={{ padding: '5rem 2rem', background: '#f9f9fb' }}>
      <div style={{ maxWidth: '1200px', margin: '0 auto' }}>
        <h2
          style={{
            textAlign: 'center',
            fontFamily: 'var(--font-display)',
            fontSize: 'clamp(1.8rem, 4vw, 2.5rem)',
            fontWeight: 900,
            marginBottom: '3rem',
            color: '#1a1a2e',
          }}
        >
          Características Principales
        </h2>
        <div
          style={{
            display: 'grid',
            gridTemplateColumns: 'repeat(auto-fit, minmax(280px, 1fr))',
            gap: '1.75rem',
          }}
        >
          {features.map((f) => (
            <FeatureCard key={f.title} {...f} />
          ))}
        </div>
      </div>
    </section>
  )
}