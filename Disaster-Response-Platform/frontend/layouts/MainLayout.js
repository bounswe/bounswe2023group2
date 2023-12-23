import Link from "next/link";
import { useRouter } from "next/router";

import styles from "./MainLayout.module.scss";
import NavBar from "@/components/NavBar";

function MainLayout({ children }) {
  return (
    <div className={styles.main}>
      <NavBar labels={children.props?.labels}/>
      <div className={styles.body}>{children}</div>
    </div>
  );
}


export default MainLayout;
