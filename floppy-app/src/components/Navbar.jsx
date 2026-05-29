import { useState } from 'react'

export default function Navbar({ onDownload }) {
  const [open, setOpen] = useState(false)

  const scrollTo = (id) => {
    setOpen(false)
    document.getElementById(id)?.scrollIntoView({ behavior: 'smooth' })
  }

  return (
    <>
      <nav className="navbar">
        <div className="navbar-inner">
          <a href="#" className="navbar-logo">
            <div className="navbar-logo-icon">📖</div>
            <span>Floppy</span>
          </a>
          <ul className="navbar-links">
            <li>
              <a href="#features" className="navbar-link"
                onClick={(e) => { e.preventDefault(); scrollTo('features') }}>
                Características
              </a>
            </li>
            <li>
              <a href="#guide" className="navbar-link"
                onClick={(e) => { e.preventDefault(); scrollTo('guide') }}>
                Guía
              </a>
            </li>
            <li>
              <a href="#faq" className="navbar-link"
                onClick={(e) => { e.preventDefault(); scrollTo('faq') }}>
                Preguntas
              </a>
            </li>
            <li>
              <button className="navbar-btn" onClick={onDownload}>
                ⬇ Descargar App
              </button>
            </li>
          </ul>
          <button
            className={`hamburger ${open ? 'open' : ''}`}
            onClick={() => setOpen(!open)}
            aria-label="Abrir menú"
          >
            <span />
            <span />
            <span />
          </button>
        </div>
      </nav>

      <div className={`mobile-menu ${open ? 'open' : ''}`}>
        <div className="mobile-menu-inner">
          <a href="#features" className="mobile-link"
            onClick={(e) => { e.preventDefault(); scrollTo('features') }}>
            Características
          </a>
          <a href="#guide" className="mobile-link"
            onClick={(e) => { e.preventDefault(); scrollTo('guide') }}>
            Guía de Uso
          </a>
          <a href="#faq" className="mobile-link"
            onClick={(e) => { e.preventDefault(); scrollTo('faq') }}>
            Preguntas Frecuentes
          </a>
          <button className="mobile-btn" onClick={() => { setOpen(false); onDownload() }}>
            ⬇ Descargar App
          </button>
        </div>
      </div>
    </>
  )
}
