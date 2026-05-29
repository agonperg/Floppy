export default function DownloadCTA({ onDownload }) {
  return (
    <section className="cta-section">
      <div className="cta-orb-1" />
      <div className="cta-orb-2" />
      <div className="cta-inner reveal">
        <div className="cta-tag">📱 Disponible para Android</div>
        <h2 className="cta-title">
          Únete a la comunidad<br />
          <span>educativa de Floppy</span>
        </h2>
        <p className="cta-desc">
          Descarga la app de forma gratuita y empieza a conectar con estudiantes y docentes,
          acceder a materiales exclusivos y participar en comunidades de aprendizaje.
        </p>
        <button className="cta-btn" onClick={onDownload}>
          ⬇ Descargar Floppy APK
        </button>
      </div>
    </section>
  )
}
