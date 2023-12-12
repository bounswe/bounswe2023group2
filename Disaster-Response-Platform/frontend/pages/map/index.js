import styles from "@/styles/Home.module.css";
import MapLayout from "@/layouts/MapLayout";
import dynamic from "next/dynamic";

import MapFilterMenu from "@/components/Map/MapFilterMenu";
import { useState } from "react";

const Map = dynamic(() => import("@/components/Map/MainMap"), {
  ssr: false,
});

export default function mapPage() {
  const [isClickActivated, setIsClickActivated] = useState(false);
  const [resourceApiData, setResourceApiData] = useState([])

  const activateClick = () => {
    setIsClickActivated(!isClickActivated);
  };



  return (
    <>
      <Map isClickActivated={isClickActivated} activateClick={activateClick} resourceApiData={resourceApiData}/>
      <MapFilterMenu activateClick={activateClick} setResourceApiData={setResourceApiData}/>
    </>
  );
}
mapPage.getLayout = function getLayout(page) {
  return <MapLayout>{page}</MapLayout>;
};
