import styles from '@/styles/Home.module.css'
import MainLayout from '@/layouts/MainLayout'
import dynamic from 'next/dynamic'
import ActivityTable from '@/components/ActivityTable'

const Map = dynamic(() => import('../components/Map'), {
  ssr: false,
})

export default function home() {
  return (
    <>
        <Map />
        <ActivityTable />
     
     
      <div className={styles.buttonContainer}>
      </div>
    </>
  )
}
home.getLayout = function getLayout(page) {
  return <MainLayout>{page}</MainLayout>;
};
