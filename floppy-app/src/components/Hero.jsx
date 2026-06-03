export default function Hero({ onDownload }) {
  const scrollToGuide = () => {
    document.getElementById('guide')?.scrollIntoView({ behavior: 'smooth' })
  }

  return (
    <section className="hero">
      <div className="hero-orb hero-orb-1" />
      <div className="hero-orb hero-orb-2" />
      <div className="hero-orb hero-orb-3" />

      <div className="hero-inner">
        <div>
          <div className="hero-badge hero-anim-1">🎓 Red Social Educativa</div>
          <h1 className="hero-title hero-anim-2">
            Aprende, Conecta y<br />
            <span className="hero-title-gradient">Crece Juntos</span>
          </h1>
          <p className="hero-desc hero-anim-3">
            Conecta con estudiantes y docentes, comparte materiales educativos,
            únete a academias especializadas y organiza viajes a eventos académicos.
          </p>
          <div className="hero-actions hero-anim-3">
            <button className="btn-primary" onClick={onDownload}>
              ⬇ Descargar APK
            </button>
            <button className="btn-secondary" onClick={scrollToGuide}>
              ▶ Ver Tutorial
            </button>
          </div>
        </div>

        <div className="hero-mockup hero-anim-4">
          <div className="hero-mockup-ring hero-mockup-ring-1" />
          <div className="hero-mockup-ring hero-mockup-ring-2" />
          <img src={`${import.meta.env.BASE_URL}mockup.png`} alt="Floppy App en móvil" />   
        </div>
      </div>
    </section>
  )
}
