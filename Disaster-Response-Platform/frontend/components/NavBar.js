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
export default function NavBar() {
  const { user, mutateUser } = useUser();
  const router = useRouter();
  const isMapPage = router.pathname === '/map';
  const { isOpen, onOpen, onOpenChange } = useDisclosure();
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
            <FaMapMarkedAlt color='white'  size={40} />
          </Link>
        </NavbarItem>
        <NavbarItem className="text-center hover:-translate-y-1 duration-300" >

          {isMapPage ? <></> : (
            <Image
              onClick={onOpen}
              src={add}
              alt="Logo"
              width={40}
              height={40}
            />
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
          <Button color="primary" className={styles.button}>
            Acil Durum
          </Button>
        </NavbarItem>
      </NavbarContent>
      <AddResourceForm onOpenChange={onOpenChange} isOpen={isOpen} />
     
    </Navbar>
  );
}