import Link from "next/link";
import { useRouter } from "next/router";
import { useEffect, useState } from "react";
import styles from "./MapLayout.module.scss";
import { FaMapMarkedAlt } from "react-icons/fa";
import { BsSearch } from "react-icons/bs";
import { Button } from "@nextui-org/react";
import { CgProfile } from "react-icons/cg";
import NavigationBar from "@/components/NavigationBar.js";

function MainLayout({ children }) {
  const router = useRouter();

  return (
    <div className={styles.main}>
      <NavigationBar />
      <div className={styles.body}>{children}</div>
    </div>
  );
}


export default MainLayout;
