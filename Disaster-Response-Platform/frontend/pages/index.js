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
import Filter from '@/components/Filter';
import { withIronSessionSsr } from 'iron-session/next';
import sessionConfig from '@/lib/sessionConfig';
import getLabels from '@/lib/getLabels';

const Map = dynamic(() => import('../components/Map/Map'), {
  ssr: false,
})
export default function home({ labels }) {
 
  const [chosenActivityType, setChosenActivityType] = useState("resources");
  
  
  return (
    <>
      <Map />
      <ToastContainer position="bottom-center" />
      <div className='flex flex-row m-3'>
        <Button color='warning' className='mr-5' onClick={(e) => { setChosenActivityType("needs") }}>{labels.activities.needs}</Button>
        <Button color='success' className='mr-5' onClick={(e) => { setChosenActivityType("resources") }} >{labels.activities.resources}</Button>
        <Button color='primary' onClick={(e) => { setChosenActivityType("events") }} >{labels.activities.events}</Button>
      </div>
      <ActivityTable chosenActivityType={chosenActivityType} labels={labels}/>

      <div className={styles.buttonContainer}>
      </div>
    </>
  )
}
home.getLayout = function getLayout(page) {
  return <MainLayout>{page}</MainLayout>;
};

export const getServerSideProps = withIronSessionSsr(
  async function getServerSideProps({ req }) {
    const labels = await getLabels(req.session.language);
    return {
      props: {
        labels
      }
    };
  },
  sessionConfig
)