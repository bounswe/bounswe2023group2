import styles from '@/styles/Home.module.css'
import MainLayout from '@/layouts/MainLayout'
import dynamic from 'next/dynamic'
import ActivityTable from '@/components/ActivityTable'
import { ToastContainer } from 'react-toastify'

const Map = dynamic(() => import('../components/Map/Map'), {
  ssr: false,
})

export default function home() {
  return (
    <>
        <Map />
        <ActivityTable />
        <ToastContainer position="bottom-center" />
     
     
      <div className={styles.buttonContainer}>
      </div>
    </>
  )
}
home.getLayout = function getLayout(page) {
  return <MainLayout>{page}</MainLayout>;
};
