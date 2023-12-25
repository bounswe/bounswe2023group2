import React, { useEffect, useState } from "react";
import {
  Modal,
  ModalContent,
  ModalHeader,
  ModalBody,
} from "@nextui-org/modal";
import { Button, Input, Select, SelectItem, Textarea } from "@nextui-org/react";
import recurrenceForm from "./recurrenceForm.json"
import { Controller, useForm } from "react-hook-form";
import { toast } from "react-toastify";

import { useRouter } from "next/router";
import { withIronSessionSsr } from "iron-session/next";
import sessionConfig from "@/lib/sessionConfig";
import { ToastContainer } from "react-toastify";
export default function AddRecurrenceForm({ isOpen, onOpenChange, lang, ...props }) {
  const [form, setForm] = useState([]);
  const { reset, handleSubmit, control, formState: { isSubmitting }, setValue } = useForm();
  const [types, setTypes] = useState({});
  const router = useRouter();
  const getForm = () => {
    const desiredForm = recurrenceForm['tr'];
    setForm(desiredForm);
  }
  useEffect(() => {
    getForm(lang);

  }, [])

  const can = async (data) => {
    let prepared = {}
    Object.keys(data).map((key, index) => {
      if (data[key] === '') { }
      else if (types[key] === 'number') {
        prepared[key] = parseInt(data[key])
      }
      else {
        prepared[key] = data[key]
      }
    })
    const response = await fetch('/api/recurrence/add', {
      method: 'POST',
      body: JSON.stringify(prepared)
    });

    if (response.status === 400) {
      toast.error("An unexpected error occurred while saving, please try again")
    } else if (response.ok) {
    
      toast.success("Successfully saved")
      router.push("/")
    } else {
      // unknown error
      toast.error("An unexpected error occurred while saving, please try again")
    }
  }
  return <Modal isOpen={isOpen} onOpenChange={onOpenChange} className='text-black' scrollBehavior="inside">
    <ModalContent>
      {(onClose) => (
        <>
          <ModalHeader className="flex flex-col gap-1">Add Need</ModalHeader>
          <ModalBody>
            <form onSubmit={handleSubmit(can)} action="#"
              method="POST" className='flex w-full flex-col  mb-6 md:mb-0 gap-4'  >
              {form !== [] && form.map((res) => {


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
                      className="max-xs"
                      variant={'bordered'}
                      {...field}
                    >
                      {(type) => <SelectItem key={type.value} className='text-black'>{type.label}</SelectItem>}
                    </Select>
                  )}
                />
                else if (res.name === 'description') return <Controller
                  name={res.name}
                  control={control}
                  defaultValue=""
                  label={res.label}
                  placeholder={res.label}
                  render={({ field }) => (
                    <Textarea type={res.type}
                      style={{ border: 'none' }}
                      label={res.label}
                      placeholder={res.label}
                      className="max-xs"
                      variant={'bordered'}
                      {...field}
                    />
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
                      className="max-xs"
                      variant={'bordered'}
                      {...field}
                    />
                  )}
                />
              })}
              <Button type='submit' color='primary'>
                {isSubmitting ? 'Loading' : "Submit"}
              </Button>
            </form>
          </ModalBody>
        </>
      )}
    </ModalContent>
    <ToastContainer />
  </Modal>
}