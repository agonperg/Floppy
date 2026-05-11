import { useState } from 'react'

export default function Hero({ onDownload }) {
  const [hoverPrimary, setHoverPrimary] = useState(false)
  const [hoverSecondary, setHoverSecondary] = useState(false)

  const scrollToGuide = () => {
    document.getElementById('guide')?.scrollIntoView({ behavior: 'smooth' })
  }

  return (
    <section
      style={{
        background: 'linear-gradient(135deg, #667eea 0%, #764ba2 100%)',
        color: 'white',
        padding: '5rem 2rem',
        textAlign: 'center',
      }}
    >
      <div
        style={{
          maxWidth: '1200px',
          margin: '0 auto',
          display: 'grid',
          gridTemplateColumns: 'repeat(auto-fit, minmax(280px, 1fr))',
          gap: '3rem',
          alignItems: 'center',
        }}
      >
        {/* Content */}
        <div style={{ textAlign: 'left' }}>
          <h1
            style={{
              fontFamily: 'var(--font-display)',
              fontSize: 'clamp(2rem, 5vw, 3.2rem)',
              fontWeight: 900,
              marginBottom: '1.2rem',
              lineHeight: 1.15,
              letterSpacing: '-1px',
            }}
          >
            Tu Red Social Educativa
          </h1>
          <p
            style={{
              fontSize: '1.15rem',
              marginBottom: '2rem',
              opacity: 0.93,
              lineHeight: 1.7,
              maxWidth: '480px',
            }}
          >
            Conecta con estudiantes y docentes, comparte materiales educativos,
            participa en comunidades de aprendizaje y accede a una biblioteca de
            recursos académicos.
          </p>
          <div style={{ display: 'flex', gap: '1rem', flexWrap: 'wrap' }}>
            {/* Primary button - descarga APK */}
            <button
              style={{
                background: hoverPrimary ? '#f0f0f0' : 'white',
                color: '#667eea',
                padding: '0.85rem 2.2rem',
                borderRadius: '8px',
                fontWeight: 800,
                cursor: 'pointer',
                border: 'none',
                fontSize: '1rem',
                fontFamily: 'var(--font-display)',
                transition: 'all 0.2s',
                transform: hoverPrimary ? 'translateY(-3px)' : 'translateY(0)',
                boxShadow: hoverPrimary
                  ? '0 8px 24px rgba(0,0,0,0.2)'
                  : '0 4px 12px rgba(0,0,0,0.15)',
                display: 'flex',
                alignItems: 'center',
                gap: '0.5rem',
              }}
              onMouseEnter={() => setHoverPrimary(true)}
              onMouseLeave={() => setHoverPrimary(false)}
              onClick={onDownload}
            >
              ⬇ Descargar APK
            </button>

            {/* Secondary button */}
            <button
              style={{
                background: hoverSecondary ? 'white' : 'transparent',
                color: hoverSecondary ? '#667eea' : 'white',
                padding: '0.85rem 2.2rem',
                border: '2px solid white',
                borderRadius: '8px',
                fontWeight: 700,
                cursor: 'pointer',
                fontSize: '1rem',
                fontFamily: 'var(--font-body)',
                transition: 'all 0.2s',
              }}
              onMouseEnter={() => setHoverSecondary(true)}
              onMouseLeave={() => setHoverSecondary(false)}
              onClick={scrollToGuide}
            >
              Ver Tutorial
            </button>
          </div>
        </div>

        {/* Phone mockup */}
        <div style={{ display: 'flex', justifyContent: 'center' }}>
          <img
            src="/mockup.png"
            alt="Floppy App"
            style={{
              width: '220px',
              borderRadius: '32px',
              boxShadow: '0 20px 60px rgba(0,0,0,0.25)',
              animation: 'pulse 3s ease-in-out infinite',
              }}
          />
        </div>
      </div>
    </section>
  )
}