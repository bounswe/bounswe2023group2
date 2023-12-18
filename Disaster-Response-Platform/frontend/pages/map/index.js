import styles from "@/styles/Home.module.css";
import MapLayout from "@/layouts/MapLayout";
import dynamic from "next/dynamic";

import MapFilterMenu from "@/components/Map/MapFilterMenu";
import { useState } from "react";
import { withIronSessionSsr } from 'iron-session/next';
import sessionConfig from '@/lib/sessionConfig';
import getLabels from '@/lib/getLabels';

const Map = dynamic(() => import("@/components/Map/MainMap"), {
  ssr: false,
});

export default function mapPage({ labels }) {
  const [isClickActivated, setIsClickActivated] = useState(false);
  const [resourceApiData, setResourceApiData] = useState([])

  const activateClick = () => {
    setIsClickActivated(!isClickActivated);
  };



  return (
    <>
      <Map isClickActivated={isClickActivated} activateClick={activateClick} resourceApiData={resourceApiData} labels={labels}/>
      <MapFilterMenu activateClick={activateClick} setResourceApiData={setResourceApiData} labels={labels}/>
    </>
  );
}
mapPage.getLayout = function getLayout(page) {
  return <MapLayout>{page}</MapLayout>;
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