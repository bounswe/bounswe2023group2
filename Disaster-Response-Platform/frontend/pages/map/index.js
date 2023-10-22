import styles from "@/styles/Home.module.css";
import MapLayout from "@/layouts/MapLayout";
import dynamic from "next/dynamic";
import MapFilterMenu from "@/components/Map/MapFilterMenu";

const Map = dynamic(() => import("@/components/Map/MainMap"), {
  ssr: false,
});

export default function mapPage() {
  return (
    <>
      <Map />
      <MapFilterMenu />
    </>
  );
}
mapPage.getLayout = function getLayout(page) {
  return <MapLayout>{page}</MapLayout>;
};
