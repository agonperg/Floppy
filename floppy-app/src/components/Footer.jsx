export default function Footer() {
  return (
    <footer
      style={{
        background: '#1a1a2e',
        color: 'white',
        textAlign: 'center',
        padding: '2.5rem 2rem',
        marginTop: '3rem',
        fontFamily: 'var(--font-body)',
      }}
    >
      <p style={{ marginBottom: '0.5rem', fontFamily: 'var(--font-display)', fontWeight: 800, fontSize: '1.1rem' }}>
        Floppy © 2024 — Tu Red Social Educativa
      </p>
      <p style={{ marginBottom: '0.75rem', color: 'rgba(255,255,255,0.7)', fontSize: '0.9rem' }}>
        Desarrollado por <strong style={{ color: 'white' }}>Equipo Davante</strong>
      </p>
      <p style={{ marginBottom: '0.75rem' }}>
        <a href="#" style={{ color: '#667eea', textDecoration: 'none', marginRight: '1rem' }}>Política de Privacidad</a>
        <a href="#" style={{ color: '#667eea', textDecoration: 'none', marginRight: '1rem' }}>Términos de Servicio</a>
        <a href="#" style={{ color: '#667eea', textDecoration: 'none' }}>Contacto</a>
      </p>
      <p style={{ color: 'rgba(255,255,255,0.6)', fontSize: '0.88rem' }}>
        ¿Problemas? Envíanos un email a{' '}
        <strong style={{ color: 'white' }}>soporte@floppy.com</strong>
      </p>
    </footer>
  )
}