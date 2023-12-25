import styles from "@/styles/Home.module.css";
import MapLayout from "@/layouts/MapLayout";
import dynamic from "next/dynamic";

import MapFilterMenu from "@/components/Map/MapFilterMenu";
import { useState } from "react";
import { withIronSessionSsr } from "iron-session/next";
import sessionConfig from "@/lib/sessionConfig";
import getLabels from "@/lib/getLabels";

const Map = dynamic(() => import("@/components/Map/MainMap"), {
  ssr: false,
});

export default function mapPage({ labels }) {
  const [isClickActivated, setIsClickActivated] = useState(false);
  const [resourceApiData, setResourceApiData] = useState([]);
  const [needApiData, setNeedApiData] = useState([]);
  const [eventApiData, setEventApiData] = useState([]);
  const [bounds,setBounds] = useState(null);
  const [chosenActivityType, setChosenActivityType] = useState("all");
  const activateClick = () => {
    setIsClickActivated(!isClickActivated);
  };

  return (
    <>
      <Map
        isClickActivated={isClickActivated}
        activateClick={activateClick}
        resourceApiData={resourceApiData}
        needApiData={needApiData}
        eventApiData={eventApiData}
        labels={labels}
        setBounds={setBounds}
        chosenActivityType={chosenActivityType}
        setResourceApiData={setResourceApiData}
        setNeedApiData={setNeedApiData}
        setEventApiData={setEventApiData}
      />
      <MapFilterMenu
        activateClick={activateClick}
        setResourceApiData={setResourceApiData}
        setNeedApiData={setNeedApiData}
        setEventApiData={setEventApiData}
        labels={labels}
        bounds={bounds}
        setChosenActivityType={setChosenActivityType}
        chosenActivityType={chosenActivityType}
      />
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
        labels,
      },
    };
  },
  sessionConfig
);
