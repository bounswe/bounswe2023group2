
import Link from "next/link";
import { useRouter } from "next/router";
import { useEffect, useState } from "react";
import styles from "./MainLayout.module.scss";
import {FaMapMarkedAlt} from "react-icons/fa";
import {BsSearch} from "react-icons/bs";
import {Button} from "@nextui-org/react";
import {CgProfile} from "react-icons/cg";
function MainLayout({ children }) {
    const router = useRouter();
   
  
    return (
        <main className={styles.main}>
            <div className={styles.navbar}>
              <div className={styles.leftbar}>

                <nav className={styles.nav}>
                    <Link href={`/`}>
                      <h1>DaRP   </h1>
                    </Link>
                </nav>
              </div>
              <div className={styles.rightbar}>
                <nav className={styles.nav}>
                    <Link href={`/map`}>
                      <FaMapMarkedAlt size={30} />
                   
                    </Link>
                </nav>
                <nav className={styles.nav}>
                    <Link href={`search`}>
                      <BsSearch size={25} />
                    </Link>
                </nav>
                <nav className={styles.nav}>
                    <Link href={`/profile`}>
                      <CgProfile size={30} />
                    </Link>
                </nav>
                <nav>
                <Button color="primary" className={styles.button}>
                    Acil Durum
                  </Button>
                </nav>
              </div>
                
            </div>

            <div className={styles.body}>{children}</div>
        </main>
    );
}

export default MainLayout;