import dynamic from 'next/dynamic'

function HomePage() {
  const TimeZone = dynamic(
    () => import('./timezone'), // replace '@components/map' with your component's location
    { ssr: false } // This line is important. It's what prevents server-side render
  )
  return <TimeZone />
}

export default HomePage