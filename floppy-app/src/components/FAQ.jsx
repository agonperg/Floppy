import { useState } from 'react'

const faqs = [
  {
    q: '¿Es Floppy totalmente gratuita?',
    a: 'Sí, Floppy es completamente gratuita. Puedes usar todas sus funciones sin pagar ni ver publicidades molestas. Nuestro objetivo es facilitar el acceso a la educación para todos.',
  },
  {
    q: '¿Cómo recupero mi contraseña?',
    a: 'En la pantalla de login, toca el botón "¿Olvidaste tu contraseña?" e ingresa tu correo. Recibirás un enlace para crear una nueva contraseña. Sigue los pasos que te enviaremos por email y podrás acceder nuevamente en minutos.',
  },
  {
    q: '¿Puedo eliminar mi cuenta y mis datos?',
    a: 'Sí. Ve a Ajustes → Cuenta → Eliminar Cuenta. Tendrás 30 días para cambiar de opinión. Después, se eliminará toda tu información incluyendo mensajes, archivos y perfil. Ten cuidado con esta acción.',
  },
  {
    q: '¿Es segura mi información personal?',
    a: 'Utilizamos encriptación SSL de 256 bits y Firebase de Google para proteger tus datos. Nunca compartimos tu información con terceros. Puedes controlar qué información es pública o privada en tu perfil.',
  },
  {
    q: '¿Qué requiere el sistema para usar Floppy?',
    a: 'Necesitas Android 7.0 o superior, 150MB de espacio libre, y una conexión a Internet estable. La aplicación funciona mejor con conexión WiFi, pero también funciona con datos móviles.',
  },
  {
    q: '¿Cómo reporto un contenido inapropiado?',
    a: 'Mantén presionado el contenido problemático y selecciona "Reportar". Puedes elegir la razón del reporte. Nuestro equipo revisará el contenido en 24 horas y tomará medidas si es necesario.',
  },
  {
    q: '¿Puedo usar la app en múltiples dispositivos?',
    a: 'Sí, puedes descargar Floppy en varios dispositivos y acceder con la misma cuenta. Tus datos sincronizarán automáticamente en tiempo real entre dispositivos.',
  },
  {
    q: '¿Hay una versión web de Floppy?',
    a: 'Actualmente, Floppy está disponible solo como aplicación móvil para Android. Estamos trabajando en una versión web que estará disponible próximamente.',
  },
]

function FAQItem({ q, a }) {
  const [open, setOpen] = useState(false)
  const [hovered, setHovered] = useState(false)

  return (
    <div
      style={{
        marginBottom: '1.25rem',
        borderBottom: '1px solid #eee',
        paddingBottom: '1.25rem',
      }}
    >
      <h3
        style={{
          color: hovered || open ? '#764ba2' : '#667eea',
          marginBottom: open ? '0.75rem' : 0,
          cursor: 'pointer',
          display: 'flex',
          justifyContent: 'space-between',
          alignItems: 'center',
          userSelect: 'none',
          transition: 'color 0.2s',
          fontFamily: 'var(--font-display)',
          fontWeight: 700,
          fontSize: '1rem',
          lineHeight: 1.4,
        }}
        onClick={() => setOpen(!open)}
        onMouseEnter={() => setHovered(true)}
        onMouseLeave={() => setHovered(false)}
      >
        <span>{q}</span>
        <span
          style={{
            transition: 'transform 0.3s',
            transform: open ? 'rotate(180deg)' : 'rotate(0)',
            fontSize: '1.1rem',
            marginLeft: '1rem',
            flexShrink: 0,
          }}
        >
          ▼
        </span>
      </h3>
      {open && (
        <p
          style={{
            color: '#666',
            fontSize: '0.95rem',
            lineHeight: 1.7,
            animation: 'slideDown 0.25s ease',
          }}
        >
          {a}
        </p>
      )}
    </div>
  )
}

export default function FAQ() {
  return (
    <section id="faq" style={{ padding: '5rem 2rem', background: 'white' }}>
      <div style={{ maxWidth: '800px', margin: '0 auto' }}>
        <h2
          style={{
            textAlign: 'center',
            fontFamily: 'var(--font-display)',
            fontSize: 'clamp(1.8rem, 4vw, 2.4rem)',
            fontWeight: 900,
            marginBottom: '3rem',
            color: '#1a1a2e',
          }}
        >
          Preguntas Frecuentes
        </h2>
        {faqs.map((item) => (
          <FAQItem key={item.q} {...item} />
        ))}
      </div>
    </section>
  )
}