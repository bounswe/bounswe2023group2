import styles from "./NavigationBar.module.scss";
import {FaMapMarkedAlt} from "react-icons/fa";
import {BsSearch} from "react-icons/bs";
import {Button} from "@nextui-org/react";
import {CgProfile} from "react-icons/cg";
import Link from "next/link";



export default function NavigationBar() {
    return (
        <main className={styles.main}>
            <div className={styles.navbar}>
              <div className={styles.leftbar}>

                <nav className="p-4 h-14 w-full text-center hover:-translate-y-1 duration-300">
                    <Link href={`/`}>
                      <h1>DaRP   </h1>
                    </Link>
                </nav>
              </div>
              <div className={styles.rightbar}>
                <nav  class= " p-4 h-14 w-full text-center hover:-translate-y-1 duration-300">
                    <Link href={`/map`}>
                      <FaMapMarkedAlt size={30} />
                   
                    </Link>
                </nav>
                <nav className="p-4 h-14 w-full text-center hover:-translate-y-1 duration-300" >
                    <Link href={`search`}>
                      <BsSearch size={25} />
                    </Link>
                </nav>
                <nav className="p-4 h-14 w-full text-center hover:-translate-y-1 duration-300">
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

            
        </main>
    )

}