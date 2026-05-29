import { useEffect, useState } from 'react'
import Navbar from './components/Navbar'
import Hero from './components/Hero'
import Features from './components/Features'
import Guide from './components/Guide'
import DownloadCTA from './components/DownloadCTA'
import FAQ from './components/FAQ'
import TechStack from './components/TechStack'
import Footer from './components/Footer'

function App() {
  const [scrollProgress, setScrollProgress] = useState(0)

  useEffect(() => {
    // Scroll progress bar
    const onScroll = () => {
      const total = document.documentElement.scrollHeight - window.innerHeight
      setScrollProgress(total > 0 ? (window.scrollY / total) * 100 : 0)
    }
    window.addEventListener('scroll', onScroll, { passive: true })

    // Scroll reveal with IntersectionObserver
    const observer = new IntersectionObserver(
      (entries) => entries.forEach((e) => {
        if (e.isIntersecting) {
          e.target.classList.add('is-visible')
          observer.unobserve(e.target)
        }
      }),
      { threshold: 0.1, rootMargin: '0px 0px -50px 0px' }
    )
    const targets = document.querySelectorAll('.reveal, .reveal-left, .reveal-right')
    targets.forEach((el) => observer.observe(el))

    return () => {
      window.removeEventListener('scroll', onScroll)
      observer.disconnect()
    }
  }, [])

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
      <div className="scroll-progress" style={{ width: `${scrollProgress}%` }} />
      <Navbar onDownload={handleDownload} />
      <Hero onDownload={handleDownload} />
      <Features />
      <Guide />
      <DownloadCTA onDownload={handleDownload} />
      <FAQ />
      <TechStack />
      <Footer />
    </>
  )
}

export default App
