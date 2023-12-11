import React from "react";
import { Navbar, NavbarBrand, NavbarMenuToggle, NavbarMenuItem, NavbarMenu, NavbarContent, NavbarItem, Link, Dropdown, DropdownTrigger, Avatar, DropdownMenu, DropdownItem } from "@nextui-org/react";
import { CgProfile } from "react-icons/cg";
import { Button, useDisclosure } from '@nextui-org/react'
import AddResourceForm from "./AddResource";
import useUser from "@/lib/useUser";
import { useRouter } from "next/router";
import fetchJson from "@/lib/fetchJson";
import styles from "./NavigationBar.module.scss";
import { FaMapMarkedAlt } from "react-icons/fa";
import add from "../public/icons/add.png";
import dapp_logo_extended_white from "../public/logo/dapp_logo_extended_white.svg";
import Image from "next/image";
import AddNeedForm from "./AddNeed";
import AddEventForm from "./AddEvent";
import AddActionFromId from "./AddActionFromId";
import AddEmergencyForm from "./AddEmergency";
export default function NavBar() {
  const { user, mutateUser } = useUser();
  const router = useRouter();
  const isMapPage = router.pathname === '/map';
  const {
    isOpen: isNeedModalOpen,
    onOpen: onOpenNeedModal,
    onOpenChange: onOpenChangeNeedModal,
  } = useDisclosure();
  const { isOpen, onOpen, onOpenChange } = useDisclosure();
  const {
    isOpen: isEventModalOpen,
    onOpen: onOpenEventModal,
    onOpenChange: onOpenChangeEventModal,
  } = useDisclosure();
  const {
    isOpen: isActionModalOpen,
    onOpen: onOpenActionModal,
    onOpenChange: onOpenChangeActionModal,
  } = useDisclosure();
  const {
    isOpen: isEmergencyModalOpen,
    onOpen: onOpenEmergencyModal,
    onOpenChange: onOpenChangeEmergencyModal,
  } = useDisclosure();


  return (
    <Navbar
      className='w-full justify-center p-0  m-0 max-w-screen-xl text-black bg-blue-700'
      isBordered

    >
      <NavbarContent justify="center" >
        <NavbarBrand className="hover:-translate-y-1 duration-300">
          <Link href={`/`}>
            <Image
              src={dapp_logo_extended_white}
              alt="Logo"
              width={150}
              height={150}
            />
          </Link>
        </NavbarBrand>
        <NavbarItem className="text-center hover:-translate-y-1 duration-300">
          <Link href={`/map`} >
            <FaMapMarkedAlt color='white' size={40} />
          </Link>
        </NavbarItem>
        <NavbarItem className="text-center hover:-translate-y-1 duration-300" >

          {isMapPage ? <></> : (
            <Dropdown placement="bottom-start">
              <DropdownTrigger>
                <Image

                  src={add}
                  alt="Logo"
                  width={40}
                  height={40}
                />
              </DropdownTrigger>
              <DropdownMenu aria-label="Add Activity" className='text-black' variant="flat">
                <DropdownItem key="kaynak" onClick={onOpen} >
                  Kaynak ekle
                </DropdownItem>
                <DropdownItem key="need" onClick={onOpenNeedModal}>
                  İhtiyaç ekle
                </DropdownItem>
                <DropdownItem key="event" onClick={onOpenEventModal}>
                  Olay bildir
                </DropdownItem>
                <DropdownItem key="aksiyon" onClick={onOpenActionModal}>
                  Aksiyon ekle
                </DropdownItem>
              </DropdownMenu>
            </Dropdown>

          )}
        </NavbarItem>
      </NavbarContent>

      <NavbarContent className="" justify="end" >
        <NavbarItem >
          {user?.isLoggedIn === false &&
            <Dropdown placement="bottom-start">
              <DropdownTrigger>
                <Avatar className="hover:-translate-y-1 duration-300"
                  icon={<CgProfile size={40} />}
                  as="button"
                  size="md"
                />
              </DropdownTrigger>
              <DropdownMenu aria-label="Profile Actions" className='text-black' variant="flat">
                <DropdownItem key="profile" >
                  <a href={`/login`}> Giriş yap </a>
                </DropdownItem>
                <DropdownItem key="profile">
                  <a href={`/register`}> Kayıt ol  </a>
                </DropdownItem>
                <DropdownItem key="help_and_feedback">Help & Feedback</DropdownItem>
              </DropdownMenu>
            </Dropdown>
          }
        </NavbarItem>
        <NavbarItem>
          {user?.isLoggedIn === true &&
            <Dropdown placement="bottom-start">
              <DropdownTrigger>
                <Avatar
                  className="hover:-translate-y-1 duration-300 bg-white"
                  icon={<CgProfile size={60} />}
                  as="button"
                  size="md"
                />
              </DropdownTrigger>
              <DropdownMenu aria-label="Profile Actions" variant="flat">
                <DropdownItem key="profile" className="">
                  <a href={`/profile`} className=''>
                    <p className="font-semibold">Profil</p>
                  </a>
                </DropdownItem>
                <DropdownItem key="settings">
                  <a href={"profile/edit"}>
                    Kullanıcı Ayarları
                  </a>
                </DropdownItem>
                <DropdownItem key="help_and_feedback">Help & Feedback</DropdownItem>
                <DropdownItem key="logout" color="danger">
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
                    Çıkış yap
                  </a>

                </DropdownItem>
              </DropdownMenu>
            </Dropdown>
          }
        </NavbarItem>
        <NavbarItem>
          <Button color="primary" className={styles.button} onPress={onOpenEmergencyModal}>
            Acil Durum
          </Button>
        </NavbarItem>
      </NavbarContent>
      <AddResourceForm onOpenChange={onOpenChange} isOpen={isOpen} />
      <AddNeedForm onOpenChange={onOpenChangeNeedModal} isOpen={isNeedModalOpen} />
      <AddEventForm onOpenChange={onOpenChangeEventModal} isOpen={isEventModalOpen} />
      <AddActionFromId onOpenChange={onOpenChangeActionModal} isOpen={isActionModalOpen} />
      <AddEmergencyForm onOpenChange={onOpenChangeEmergencyModal} isOpen={isEmergencyModalOpen} />
    </Navbar>
  );
}
