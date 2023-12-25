import Link from "next/link";
import { useRouter } from "next/router";

import styles from "./MainLayout.module.scss";
import NavBar from "@/components/NavBar";
import { ToastContainer } from "react-toastify";

function MainLayout({ children }) {
  return (
    <div className={styles.main}>
      <NavBar labels={children.props?.labels}/>
      <div className={styles.body}>{children}</div>
      <ToastContainer position="bottom-center"/>
    </div>
  );
}


export default MainLayout;
