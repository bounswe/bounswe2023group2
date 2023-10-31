import styles from '@/styles/Home.module.css'
import MainLayout from '@/layouts/MainLayout'
import dynamic from 'next/dynamic'
import ActivityTable from '@/components/ActivityTable'
import { ToastContainer } from 'react-toastify'
import { useState,useEffect } from 'react'

const Map = dynamic(() => import('../components/Map/Map'), {
  ssr: false,
})

export default function home() {
  const [resource, setResource] = useState([]); 
  const resources = async () => {
    const  response = await  fetch('/api/resource', { method: 'GET', headers: { "Content-Type": "application/json" }});
  let res = await response.json();
     if (response.ok) {

      setResource(res.resources)
    } else {
      // unknown error
      toast.error("An unexpected error occurred while saving, please try again")
    }
  }
  useEffect(()=>{
    resources();
  
    
  },[resource])

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
