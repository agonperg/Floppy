const tags = [
  'Android Studio',
  'Java/Kotlin',
  'Firebase (Auth, Firestore, Storage)',
  'Google Play Services',
  'Material Design',
  'Android DataStore',
  'Cloudinary (Gestión de imágenes)',
]

export default function TechStack() {
  return (
    <section style={{ padding: '3rem 2rem', background: '#f9f9fb', textAlign: 'center' }}>
      <div style={{ maxWidth: '1000px', margin: '0 auto' }}>
        <h3
          style={{
            fontSize: '1.2rem',
            marginBottom: '1.2rem',
            color: '#1a1a2e',
            fontFamily: 'var(--font-display)',
            fontWeight: 800,
          }}
        >
          Tecnologías Utilizadas
        </h3>
        <div
          style={{
            display: 'flex',
            flexWrap: 'wrap',
            gap: '0.75rem',
            justifyContent: 'center',
            marginBottom: '1.2rem',
          }}
        >
          {tags.map((tag) => (
            <span
              key={tag}
              style={{
                background: 'white',
                padding: '0.45rem 1rem',
                borderRadius: '20px',
                border: '1px solid #ddd',
                fontSize: '0.88rem',
                color: '#667eea',
                fontWeight: 600,
                fontFamily: 'var(--font-body)',
              }}
            >
              {tag}
            </span>
          ))}
        </div>
        <p style={{ color: '#777', fontSize: '0.93rem', lineHeight: 1.65, maxWidth: '700px', margin: '0 auto' }}>
          Floppy ha sido desarrollada con las últimas tecnologías de Android, utilizando Firebase como backend
          para garantizar escalabilidad, seguridad y sincronización en tiempo real de datos.
        </p>
      </div>
    </section>
  )
}