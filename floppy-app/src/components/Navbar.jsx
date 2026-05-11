import { useState } from 'react'

const navStyles = {
  nav: {
    background: 'linear-gradient(135deg, #667eea 0%, #764ba2 100%)',
    padding: '1rem 2rem',
    position: 'sticky',
    top: 0,
    zIndex: 100,
    boxShadow: '0 2px 16px rgba(102,126,234,0.25)',
  },
  container: {
    maxWidth: '1200px',
    margin: '0 auto',
    display: 'flex',
    justifyContent: 'space-between',
    alignItems: 'center',
  },
  logo: {
    color: 'white',
    fontSize: '1.8rem',
    fontWeight: 900,
    display: 'flex',
    alignItems: 'center',
    gap: '0.6rem',
    fontFamily: 'var(--font-display)',
    letterSpacing: '-0.5px',
    textDecoration: 'none',
  },
  logoIcon: {
    width: '38px',
    height: '38px',
    background: 'white',
    borderRadius: '50%',
    display: 'flex',
    alignItems: 'center',
    justifyContent: 'center',
    fontSize: '1.3rem',
    boxShadow: '0 2px 8px rgba(0,0,0,0.15)',
  },
  ul: {
    listStyle: 'none',
    display: 'flex',
    gap: '2rem',
    alignItems: 'center',
  },
  link: {
    color: 'rgba(255,255,255,0.92)',
    textDecoration: 'none',
    fontWeight: 600,
    fontSize: '0.95rem',
    transition: 'opacity 0.2s',
    fontFamily: 'var(--font-body)',
  },
  btn: {
    background: 'white',
    color: '#667eea',
    padding: '0.55rem 1.4rem',
    borderRadius: '25px',
    fontWeight: 800,
    cursor: 'pointer',
    border: 'none',
    fontSize: '0.9rem',
    fontFamily: 'var(--font-display)',
    transition: 'transform 0.2s, box-shadow 0.2s',
    boxShadow: '0 2px 8px rgba(0,0,0,0.12)',
  },
}

export default function Navbar({ onDownload }) {
  const [hoverBtn, setHoverBtn] = useState(false)

  const scrollTo = (id) => {
    document.getElementById(id)?.scrollIntoView({ behavior: 'smooth' })
  }

  return (
    <nav style={navStyles.nav}>
      <div style={navStyles.container}>
        <a href="#" style={navStyles.logo}>
          <div style={navStyles.logoIcon}>📚</div>
          <span>Floppy</span>
        </a>
        <ul style={navStyles.ul}>
          <li>
            <a href="#features" style={navStyles.link} onClick={(e) => { e.preventDefault(); scrollTo('features') }}>
              Características
            </a>
          </li>
          <li>
            <a href="#guide" style={navStyles.link} onClick={(e) => { e.preventDefault(); scrollTo('guide') }}>
              Guía de Uso
            </a>
          </li>
          <li>
            <a href="#faq" style={navStyles.link} onClick={(e) => { e.preventDefault(); scrollTo('faq') }}>
              Preguntas
            </a>
          </li>
          <li>
            <button
              style={{
                ...navStyles.btn,
                transform: hoverBtn ? 'scale(1.06)' : 'scale(1)',
                boxShadow: hoverBtn ? '0 4px 16px rgba(0,0,0,0.18)' : '0 2px 8px rgba(0,0,0,0.12)',
              }}
              onMouseEnter={() => setHoverBtn(true)}
              onMouseLeave={() => setHoverBtn(false)}
              onClick={onDownload}
            >
              ⬇ Descargar App
            </button>
          </li>
        </ul>
      </div>
    </nav>
  )
}