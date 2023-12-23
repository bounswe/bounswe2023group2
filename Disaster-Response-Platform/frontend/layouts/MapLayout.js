import Link from "next/link";
import { useRouter } from "next/router";
import styles from "./MapLayout.module.scss";

import NavBar from "@/components/NavBar";


function MapLayout({ children }) {
  const router = useRouter();

  return (
    <div className={styles.main}>
      <NavBar labels={children.props?.labels}/>
      <div className={styles.body}>{children}</div>
    </div>
  );
}


export default MapLayout;
