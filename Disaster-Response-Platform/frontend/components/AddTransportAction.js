import React, { useEffect, useMemo, useState } from "react";
import {
  Modal,
  ModalContent,
  ModalHeader,
  ModalBody,

} from "@nextui-org/modal";
import { Button, Input, Select, SelectItem } from "@nextui-org/react";
import { Controller, useForm } from "react-hook-form";
import { toast } from "react-toastify";

import { useRouter } from "next/router";
import actionForm from "./addActionTransport.json"
import actionService from "@/services/actionService";
import ActivitySimple from "./ActivitySimple";
import addressService from "@/services/addressService";
export default function AddTransportAction({ isOpen, onOpenChange, type = 'need', labels, selected }) {

  const [form, setForm] = useState([]);
  const { reset, handleSubmit, control, formState: { isSubmitting }, setValue } = useForm();
  const [list, setList] = useState([]);
  const [chosen, setChosen] = useState('human');
const [resources, setResources] = useState([]);
  const getResources = async () => {
    const response = await fetch('/api/resource/get', { method: 'GET', headers: { "Content-Type": "application/json" } });
    let res = await response.json();
    if (response.ok) {
       
        setResources(res.resources);
        
    } else {
        toast.error(labels.feedback.failure);
    }
}

  const router = useRouter();

  const getForm = () => {
    const desiredForm = actionForm['en'];
    setForm(desiredForm);
    getResources();

  }
  useEffect(() => {
    getForm();
  }, [])

  const can = async (data) => {
    const prepared = {}
    console.log(data)
    Object.keys(data).map((key, index) => {
      if (data[key] === '' || key === 'target') { }
      else {
        prepared[key] = data[key]
      }
    })
    prepared['type'] = 'Transport' 
    prepared['resources'] = [data.resources]
    try {
      let coordinats = await addressService.addressToXY(data.start_location);
      console.log(coordinats)
      prepared['start_x'] = coordinats.payload.x
      prepared['start_y'] = coordinats.payload.y
    }
    catch (e) {
      toast.error("Address should contain city, street, street number")
    }
    try {
      let coordinats = await addressService.addressToXY(data.end_location);
      console.log(coordinats)
      prepared['end_x'] = coordinats.payload.x
      prepared['end_y'] = coordinats.payload.y
    }
    catch (e) {
      toast.error("Address should contain city, street, street number")
    }
    console.log(prepared)
    const response = await fetch('/api/action/add', {
      method: 'POST',
      body: JSON.stringify(prepared)
    });
    if (response.status === 400) {
      toast.error("An unexpected error occurred while saving, please try again")

    } else if (response.ok) {

      toast.success("Successfully saved")

    } else {
      // unknown error
      toast.error("An unexpected error occurred while saving, please try again")
    }
  }
  return <Modal isOpen={isOpen} onOpenChange={onOpenChange} className='text-black' scrollBehavior="inside " size='4xl' >

    <ModalContent>
      {(onClose) => (
        <>
          <ModalHeader className="flex">Add Transportation Action</ModalHeader>
          <ModalBody>
            <form onSubmit={handleSubmit(can)} action="#"
              method="POST" className='grid grid-cols-2  mb-6 md:mb-0 gap-4' items-center  >
              {form.map((res) => {
                if (res.type === 'select') return <Controller
                  name={res.name}
                  control={control}
                  defaultValue=""
                  label={res.label}
                  placeholder={res.label}
                  render={({ field }) => (
                    <Select
                      id={field.name} name={field.name}
                      items={res.options}
                      label={res.label}
                      placeholder={res.label}
                      // className="max-xs"
                      variant={'bordered'}
                      {...field}
                    >
                      {(type) => <SelectItem key={type.value} className='text-black'>{type.label}</SelectItem>}
                    </Select>
                  )}
                />
                return <Controller
                  name={res.name}
                  control={control}
                  defaultValue=""
                  label={res.label}
                  placeholder={res.label}
                  render={({ field }) => (
                    <Input type={res.type}
                      style={{ border: 'none' }}
                      label={res.label}
                      placeholder={res.label}
                      // className="max-xs"
                      variant={'bordered'}
                      {...field}
                    />
                  )}
                />
              })}
              <Controller
                name={'resources'}
                control={control}
                selectionMode="multiple"
                label={'Kaynak seçiniz'}
                placeholder={'Kaynak seçiniz'}
                render={({ field }) => (
                  <Select
                    id={'resources'} name={'resources'}
                    items={resources}
                    label={'Kaynak seçiniz'}
                    placeholder={'Kaynak seçiniz'}

                    variant={'bordered'}

                    {...field}
                  >
                    {(x) => <SelectItem key={x._id} value={x._id} className='text-black'><ActivitySimple activity={x} activityType={'resource'} /></SelectItem>}
                  </Select>
                )}
              />



              <Button type='submit' className="bg-action">
                {isSubmitting ? 'Loading' : "Submit"}
              </Button>
            </form>
          </ModalBody>
        </>
      )}
    </ModalContent>
  </Modal>
}