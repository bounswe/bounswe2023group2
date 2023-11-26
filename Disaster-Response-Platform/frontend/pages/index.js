import styles from '@/styles/Home.module.css'
import MainLayout from '@/layouts/MainLayout'
import dynamic from 'next/dynamic'
import ActivityTable from '@/components/ActivityTable'
import { ToastContainer } from 'react-toastify'
import { toast } from 'react-toastify'
import { useEffect, useState } from 'react'
import { Button } from '@nextui-org/react'
import { FaFilter } from "react-icons/fa";
import { FaSort } from "react-icons/fa";
import Filter from '@/components/Filter'

const Map = dynamic(() => import('../components/Map/Map'), {
  ssr: false,
})
export default function home() {
 
  const [needFilter, setNeedFilter] = useState(false);
  const [resourceFilter, setResourceFilter] = useState(true);
  
  
  return (
    <>
      <Map />
      <ToastContainer position="bottom-center" />
      <div className='flex flex-row m-3'>
        <Button color='warning' className='mr-5' onClick={(e) => { setNeedFilter(true); setResourceFilter(false) }}>İhtiyaç</Button>
        <Button color='success' onClick={(e) => { setResourceFilter(true); setNeedFilter(false) }} >Kaynaklar</Button>
      </div>
      <ActivityTable needFilter={needFilter} resourceFilter={resourceFilter} />

      <div className={styles.buttonContainer}>
      </div>
    </>
  )
}
home.getLayout = function getLayout(page) {
  return <MainLayout>{page}</MainLayout>;
};
