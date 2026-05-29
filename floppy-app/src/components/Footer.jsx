const scrollTo = (id) =>
  document.getElementById(id)?.scrollIntoView({ behavior: 'smooth' })

export default function Footer() {
  return (
    <footer className="footer">
      <div className="footer-top">
        <div className="reveal-left">
          <div className="footer-brand-row">
            <div className="footer-brand-icon">📖</div>
            <span>Floppy</span>
          </div>
          <p className="footer-tagline">
            Tu Red Social Educativa.<br />
            Conectando estudiantes y docentes<br />
            en un espacio de aprendizaje colaborativo.
          </p>
        </div>

        <div className="reveal-right">
          <p className="footer-col-title">Navegación</p>
          <ul className="footer-links">
            <li>
              <a href="#features" onClick={(e) => { e.preventDefault(); scrollTo('features') }}>
                Características
              </a>
            </li>
            <li>
              <a href="#guide" onClick={(e) => { e.preventDefault(); scrollTo('guide') }}>
                Guía de Uso
              </a>
            </li>
            <li>
              <a href="#faq" onClick={(e) => { e.preventDefault(); scrollTo('faq') }}>
                Preguntas Frecuentes
              </a>
            </li>
          </ul>
        </div>
      </div>

      <div className="footer-bottom">
        <p className="footer-copy">
          Floppy © 2026 — <strong>Equipo Davante</strong>
        </p>
        <p className="footer-copy">TFG - Trabajo Fin de Grado</p>
      </div>
    </footer>
  )
}
