import styles from "@/styles/Home.module.css";
import MapLayout from "@/layouts/MapLayout";
import dynamic from "next/dynamic";
import MapFilterMenu from "@/components/Map/MapFilterMenu";

const Map = dynamic(() => import("@/components/Map/MainMap"), {
  ssr: false,
});

export default function home() {
  return (
    <>
      <Map />
      <MapFilterMenu />
    </>
  );
}
home.getLayout = function getLayout(page) {
  return <MapLayout>{page}</MapLayout>;
};
