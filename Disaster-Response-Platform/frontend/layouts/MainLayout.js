import Link from "next/link";
import { useRouter } from "next/router";

import styles from "./MainLayout.module.scss";
import NavigationBar from "@/components/NavigationBar.js";
import { withIronSessionSsr } from "iron-session/next";
import sessionConfig from "@/lib/sessionConfig";

function MainLayout({ children}) {


  return (
    <div className={styles.main}>
      <NavigationBar />
      <div className={styles.body}>{children}</div>
    </div>
  );
}


export default MainLayout;
