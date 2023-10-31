import styles from '@/styles/Home.module.css'
import MainLayout from '@/layouts/MainLayout'
import dynamic from 'next/dynamic'
import ActivityTable from '@/components/ActivityTable'
import { ToastContainer } from 'react-toastify'
import { toast } from 'react-toastify'
import { useEffect, useState } from 'react'
import { Button } from '@nextui-org/react'

const Map = dynamic(() => import('../components/Map/Map'), {
  ssr: false,
})

export default function home() {
  const [resource, setResource] = useState([]); 
  const [needFilter, setNeedFilter] = useState(false);
  const [resourceFilter, setResourceFilter] = useState(true);
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
        <ToastContainer position="bottom-center" />
        <div className='flex flex-row m-3'>
        <Button color='warning' className='mr-5' onClick={(e)=>{setNeedFilter(true); setResourceFilter(false)}}>İhtiyaç</Button>
        <Button color='success' onClick={(e)=>{setResourceFilter(true); setNeedFilter(true)}} >Kaynaklar</Button>
        </div>
        <ActivityTable needFilter={needFilter} resourceFilter={resourceFilter} resource={resource} />
      <div className={styles.buttonContainer}>
      </div>
    </>
  )
}
home.getLayout = function getLayout(page) {
  return <MainLayout>{page}</MainLayout>;
};
