import React, { useEffect, useState } from "react";
import GenericForm from "./GenericForm";
import {
  Modal,
  ModalContent,
  ModalHeader,
  ModalBody,

} from "@nextui-org/modal";
import { Button, Input, Select, SelectItem } from "@nextui-org/react";
import forms from "./forms.json"
import { Controller, useForm } from "react-hook-form";
import { toast } from "react-toastify";
export default function AddNeedForm({ isOpen, onOpenChange }) {
  const [data, setData] = useState([]);
  const {reset, handleSubmit, control, formState: { isSubmitting }, setValue } = useForm();

  const [types, setTypes] = useState([]);
  const [fields, setFields] = useState([]);
  const getFrom = async () => {
    const typeList = Object.keys(forms).map(
      (key) => {
        return { label: key };
      })
    setTypes(typeList);
    setData(forms);
  }

  useEffect(() => {
    getFrom();
  }, [])

  const can = async (data) => {
    console.log(data)
    const response = await fetch('/api/need/add', {
      method: 'POST',
      body: JSON.stringify(data)
    });
    console.log(response,"response")
    if (response.status === 400) {
      // const fieldToErrorMessage = await response.json()
      // for (const [fieldName, errorMessage] of Object.entries(fieldToErrorMessage)) {
      //   setError(fieldName, { type: 'custom', message: errorMessage })
      // }
    } else if (response.ok) {
      // successful
      toast.success("Successfully saved")
      router.push("/")
    } else {
      // unknown error
      toast.error("An unexpected error occurred while saving, please try again")
    }
  }
  return <Modal isOpen={isOpen} onOpenChange={onOpenChange} className='text-black'>
    <ModalContent>
      {(onClose) => (
        <>
          <ModalHeader className="flex flex-col gap-1">Add Need</ModalHeader>
          <ModalBody>
            <form onSubmit={handleSubmit(can)} action="#"
              method="POST" className='flex w-full flex-col  mb-6 md:mb-0 gap-4'  >
              {types !== [] && <Select
                id="type" name="type" onChange={(e) => { reset(); setFields(data[e.target.value]?.fields ?? [] ), setValue('type',e.target.value ) }}
                items={types}
                label="İhtiyaç Türü"
                placeholder="İhtiyacınızı seçiniz"
                className="max-xs"
                variant={'bordered'}
              >
                {(type) => <SelectItem key={type.label}>{type.label}</SelectItem>}
              </Select>}
              {fields!==[] &&  fields.map((res) => {
                return  <Controller 
                name={res.name}
                control={control}
                defaultValue=""
                label={res.label}
                placeholder={res.label}
                render={({ field }) => (
                  <Input type={res.type}  
                  // id={field.name} name={field.name}
                  style={{ border: 'none' }}
                  label={res.label}
                  placeholder={res.label}
                  className="max-xs"
                  variant={'bordered'}
                  {...field}
                  />)} 
                />
              })}
              
              <Button type='submit'>
                {isSubmitting ? 'Loading' : "Submit"}
              </Button>
            </form>
          </ModalBody>
        </>
      )}
    </ModalContent>
  </Modal>
}