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
export default function home({ labels }) {
 
  const [chosenActivityType, setChosenActivityType] = useState("resources");
  
  
  return (
    <>
      <Map />
      <ToastContainer position="bottom-center" />
      <Divider className="my-6" />
      <Tabs
          selectedKey={chosenActivityType}
          onSelectionChange={setChosenActivityType}
          size="lg"
          color="primary"
          variant="underlined"
          classNames={{tab: "py-8"}}
      >
        <Tab key="needs" titleValue={labels.activities.needs} title={(
          <div className="bg-need dark:bg-need-dark px-2 py-1.5 rounded-xl text-black">
            {labels.activities.needs}
          </div>
        )}/>
        <Tab key="resources" titleValue={labels.activities.resources} title={(
          <div className="bg-resource dark:bg-resource-dark px-2 py-1.5 rounded-xl text-black">
            {labels.activities.resources}
          </div>
        )}/>
        <Tab key="events" titleValue={labels.activities.events} title={(
          <div className="bg-event dark:bg-event-dark px-2 py-1.5 rounded-xl text-black">
            {labels.activities.events}
          </div>
        )}/>
      </Tabs>
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