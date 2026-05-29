import { useState } from 'react'

const faqs = [
  {
    q: '¿Es Floppy totalmente gratuita?',
    a: 'Sí, Floppy es completamente gratuita. Puedes usar todas sus funciones sin pagar ni ver publicidades molestas. Nuestro objetivo es facilitar el acceso a la educación para todos.',
  },
  {
    q: '¿Cómo recupero mi contraseña?',
    a: 'En la pantalla de inicio de sesión, toca "¿Olvidaste tu contraseña?" e ingresa tu correo electrónico. Recibirás un enlace para crear una nueva contraseña en pocos minutos.',
  },
  {
    q: '¿Puedo eliminar mi cuenta y mis datos?',
    a: 'Sí. Ve a Ajustes → Cuenta → Eliminar Cuenta. Tendrás 30 días para cambiar de opinión. Después se eliminará toda tu información: mensajes, archivos y perfil.',
  },
  {
    q: '¿Es segura mi información personal?',
    a: 'Utilizamos encriptación SSL y Firebase de Google para proteger tus datos. Nunca compartimos tu información con terceros. Puedes controlar qué datos son públicos o privados desde tu perfil.',
  },
  {
    q: '¿Qué requiere el sistema para usar Floppy?',
    a: 'Necesitas Android 7.0 o superior, 150MB de espacio libre y conexión a Internet. La app funciona mejor con WiFi, pero también funciona con datos móviles.',
  },
  {
    q: '¿Cómo reporto un contenido inapropiado?',
    a: 'Mantén presionado el contenido y selecciona "Reportar". Elige la razón del reporte y nuestro equipo lo revisará en menos de 24 horas.',
  },
  {
    q: '¿Puedo usar Floppy en varios dispositivos?',
    a: 'Sí, puedes instalar Floppy en todos tus dispositivos y acceder con la misma cuenta. Tus datos se sincronizan automáticamente en tiempo real.',
  },
  {
    q: '¿Habrá versión web de Floppy?',
    a: 'Actualmente Floppy está disponible solo para Android. Estamos trabajando en una versión web que llegará próximamente.',
  },
]

function FAQItem({ q, a, delay }) {
  const [open, setOpen] = useState(false)

  return (
    <div className={`faq-item reveal ${delay}`}>
      <button
        className={`faq-question ${open ? 'open' : ''}`}
        onClick={() => setOpen(!open)}
      >
        <span>{q}</span>
        <span className={`faq-icon ${open ? 'open' : ''}`}>▼</span>
      </button>
      <div className={`faq-body ${open ? 'open' : ''}`}>
        <p className="faq-answer">{a}</p>
      </div>
    </div>
  )
}

const delays = ['d1', 'd2', 'd3', 'd4', 'd5', 'd6', 'd1', 'd2']

export default function FAQ() {
  return (
    <section id="faq" className="faq">
      <div className="faq-inner">
        <div className="section-header reveal">
          <span className="section-tag">Dudas frecuentes</span>
          <h2 className="section-title">Preguntas Frecuentes</h2>
          <div className="section-title-line" />
          <p className="section-subtitle">
            ¿Tienes alguna duda? Aquí encontrarás respuestas a las preguntas más comunes sobre Floppy.
          </p>
        </div>
        {faqs.map((item, i) => (
          <FAQItem key={item.q} {...item} delay={delays[i]} />
        ))}
      </div>
    </section>
  )
}
