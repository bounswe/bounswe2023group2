import styles from "./NavigationBar.module.scss";
import { FaMapMarkedAlt } from "react-icons/fa";
import { BsSearch } from "react-icons/bs";
import { CgProfile } from "react-icons/cg";
import Link from "next/link";
import { Button, useDisclosure } from '@nextui-org/react'
import AddResourceForm from "./AddResource";
import useUser from "@/lib/useUser";
import { useRouter } from "next/router";
import fetchJson from "@/lib/fetchJson";


export default function NavigationBar() {
  const { user, mutateUser } = useUser();
  const router = useRouter();
  const isMapPage = router.pathname === '/map';
  const { isOpen, onOpen, onOpenChange } = useDisclosure();
  return (
    <main className={styles.main}>
      <div className={styles.navbar}>
        <div className={styles.leftbar}>
          <nav className="p-4 h-14  text-center hover:-translate-y-1 duration-300">
            <Link href={`/`}>
              <h1>DaRP   </h1>
            </Link>
          </nav>
          <nav className=" p-4 h-14  text-center hover:-translate-y-1 duration-300">
            <Link href={`/map`}>
              <FaMapMarkedAlt size={30} />
            </Link>
          </nav>
        </div>
        <div className={styles.rightbar}>
          <nav>
            {isMapPage ? <></> : (
              <Button onPress={onOpen}>İlan Oluştur</Button>
            )}
          </nav>
          
          <AddResourceForm onOpenChange={onOpenChange} isOpen={isOpen} />
          {user?.isLoggedIn === false && <nav>
            <Link href={`register`}>
              <Button variant="solid" color="primary" >Kayıt ol</Button>
            </Link>
          </nav>}
          {user?.isLoggedIn === false && <nav>
            <Link href={`login`}>
              <Button variant="solid" color='primary' >Giriş yap</Button>
            </Link>
          </nav>}
          {user?.isLoggedIn === true && <nav className="p-4 h-14  text-center hover:-translate-y-1 duration-300">
            <Link href={`/profile`}>
              <CgProfile size={30} />
            </Link>
          </nav>}
          {user?.isLoggedIn === true && <nav className="p-4 h-14  text-center hover:-translate-y-1 duration-300">
          <a
                  href="/api/logout"
                  onClick={async (e) => {
                    e.preventDefault();
                    mutateUser(
                      await fetchJson("/api/logout", { method: "POST" }),
                      false,
                    );
                    router.push("/");
                  }}
                >
                  Logout
                </a>
          </nav>}
          <nav>
            <Button color="primary" className={styles.button}>
              Acil Durum
            </Button>
          </nav>
        </div>
        
      </div>
     
    </main >
  )

}
