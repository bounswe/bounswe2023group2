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
import AddRecurrenceForm from "./AddRecurrence";


export default function NavBar({ labels }) {
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
  const {
    isOpen: isRecurrenceModalOpen,
    onOpen: onOpenRecurrenceModal,
    onOpenChange: onOpenChangeRecurrenceModal,
  
  } = useDisclosure();


  async function toggleLanguage() {
    const response = await fetch('/api/set-language', {
      method: 'POST',
      headers: {
        "Content-Type": "application/json",
      },
      body: JSON.stringify({"language": labels.other_language})
    });
    // need to do a full reload to re-run server-side rendering with new language
    router.reload();
  }


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
                  {labels.activities.add_resource}
                </DropdownItem>
                <DropdownItem key="need" onClick={onOpenNeedModal}>
                  {labels.activities.add_need}
                </DropdownItem>
                <DropdownItem key="event" onClick={onOpenEventModal}>
                  {labels.activities.report_event}
                </DropdownItem>
                <DropdownItem key="aksiyon" onClick={onOpenActionModal}>
                  {labels.activities.add_action}
                </DropdownItem>
                <DropdownItem key="recurrence" onClick={onOpenRecurrenceModal}>
                  {labels.activities.add_action}
                </DropdownItem>
              </DropdownMenu>
            </Dropdown>

          )}
        </NavbarItem>
      </NavbarContent>

      <NavbarContent className="" justify="end" >
        <NavbarItem >
          <Button className="hover:-translate-y-1 duration-300 bg-white font-bold rounded-full" onPress={toggleLanguage}>
            {labels.other_language}
          </Button>
        </NavbarItem>
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
                <DropdownItem key="login" >
                  <a href={`/login`}> {labels.auth.login} </a>
                </DropdownItem>
                <DropdownItem key="register">
                  <a href={`/register`}> {labels.auth.register}  </a>
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
                    <p className="font-semibold">{labels.navbar.profile}</p>
                  </a>
                </DropdownItem>
                <DropdownItem key="settings">
                  <a href={"profile/edit"}>
                    {labels.navbar.edit_profile}
                  </a>
                </DropdownItem>
                <DropdownItem key="help_and_feedback">{labels.navbar.help_feedback}</DropdownItem>
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
                    {labels.auth.logout}
                  </a>

                </DropdownItem>
              </DropdownMenu>
            </Dropdown>
          }
        </NavbarItem>
        <NavbarItem>
          <Button color="primary" className={styles.button} onPress={onOpenEmergencyModal}>
            <span className="font-bold">
              {labels.activities.EMERGENCY}
            </span>
          </Button>
        </NavbarItem>
      </NavbarContent>
      <AddResourceForm onOpenChange={onOpenChange} isOpen={isOpen} />
      <AddNeedForm onOpenChange={onOpenChangeNeedModal} isOpen={isNeedModalOpen} />
      <AddEventForm onOpenChange={onOpenChangeEventModal} isOpen={isEventModalOpen} labels={labels} />
      <AddActionFromId onOpenChange={onOpenChangeActionModal} isOpen={isActionModalOpen} />
      <AddEmergencyForm onOpenChange={onOpenChangeEmergencyModal} isOpen={isEmergencyModalOpen} />
      <AddRecurrenceForm onOpenChange={onOpenChangeRecurrenceModal} isOpen={isRecurrenceModalOpen} />
    </Navbar>
  );
}
