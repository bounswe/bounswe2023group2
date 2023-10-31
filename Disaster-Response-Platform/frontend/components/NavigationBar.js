import styles from "./NavigationBar.module.scss";
import { FaMapMarkedAlt } from "react-icons/fa";
import { BsSearch } from "react-icons/bs";
import { CgProfile } from "react-icons/cg";
import Link from "next/link";
import { Button, useDisclosure } from '@nextui-org/react'
import AddResourceForm from "./AddResource";
import useUser from "@/lib/useUser";


export default function NavigationBar() {
  const { user, mutateUser } = useUser();
  
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
          <nav className="p-4 h-14  text-center hover:-translate-y-1 duration-300" >
            <Link href={`search`}>
              <BsSearch size={25} />
            </Link>
          </nav>
        </div>
        <div className={styles.rightbar}>
          
          <nav>
            <Button onPress={onOpen}>Add Resource</Button>
          </nav>
          <AddResourceForm onOpenChange={onOpenChange} isOpen={isOpen} />
          {user?.isLoggedIn === false &&  <nav>
          <Link href={`register`}>
            <Button variant="solid" color="primary" >Sign up</Button>
            </Link>
          </nav>}
          {user?.isLoggedIn === false && <nav>
          <Link href={`login`}>
            <Button variant="solid" color='primary' >Sign in</Button>
            </Link>
          </nav>}
          {user?.isLoggedIn === true && <nav className="p-4 h-14  text-center hover:-translate-y-1 duration-300">
            <Link href={`/profile`}>
              <CgProfile size={30} />
            </Link>
          </nav>}
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
