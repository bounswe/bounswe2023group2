import styles from '@/styles/Home.module.css'
import MainLayout from '@/layouts/MainLayout'
import dynamic from 'next/dynamic'
import ActivityTable from '@/components/ActivityTable'
import { ToastContainer } from 'react-toastify'
import { toast } from 'react-toastify'
import { useEffect, useState } from 'react'
import { Button, Tabs, Tab, Card, CardBody, Divider } from '@nextui-org/react'
import { FaFilter } from "react-icons/fa";
import { FaSort } from "react-icons/fa";
import Filter from '@/components/Filter';
import { withIronSessionSsr } from 'iron-session/next';
import sessionConfig from '@/lib/sessionConfig';
import getLabels from '@/lib/getLabels';


const Map = dynamic(() => import('../components/Map/Map'), {
  ssr: false,
})
const colors = ['primary', 'success', 'warning', 'error', 'dark', 'secondary', 'info', 'light'];
export default function home({ labels }) {

  const [chosenActivityType, setChosenActivityType] = useState("resource");
  return (
    <>
      <ToastContainer position="bottom-center" />
      <Divider className="my-6" />
      <Tabs
        selectedKey={chosenActivityType}
        radius="full"
        onSelectionChange={setChosenActivityType}
        classNames={{
          cursor: `bg-${chosenActivityType}`,
        }}
        size="lg"

      >
        <Tab key="need" titleValue={labels.activities.needs} title={
          labels.activities.needs
        } />
        <Tab key="resource" titleValue={labels.activities.resources} title={
          labels.activities.resources
        } />
        <Tab key="event" titleValue={labels.activities.events} title={  
         labels.activities.events
     }/>
      </Tabs>
      <ActivityTable chosenActivityType={chosenActivityType} labels={labels} />
    
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