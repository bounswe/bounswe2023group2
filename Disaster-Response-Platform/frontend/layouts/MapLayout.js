import Link from "next/link";
import { useRouter } from "next/router";
import styles from "./MapLayout.module.scss";

import NavBar from "@/components/NavBar";


function MainLayout({ children }) {
  const router = useRouter();

  return (
    <div className={styles.main}>
      <NavBar  />
      <div className={styles.body}>{children}</div>
    </div>
  );
}


export default MainLayout;
