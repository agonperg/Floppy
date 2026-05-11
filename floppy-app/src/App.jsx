import Navbar from './components/Navbar'
import Hero from './components/Hero'
import Features from './components/Features'
import Guide from './components/Guide'
import FAQ from './components/FAQ'
import TechStack from './components/TechStack'
import Footer from './components/Footer'

function App() {
  const handleDownload = () => {
    const link = document.createElement('a')
    link.href = '/floppy.apk'
    link.download = 'floppy.apk'
    document.body.appendChild(link)
    link.click()
    document.body.removeChild(link)
  }

  return (
    <>
      <Navbar onDownload={handleDownload} />
      <Hero onDownload={handleDownload} />
      <Features />
      <Guide />
      <FAQ />
      <TechStack />
      <Footer />
    </>
  )
}

export default App