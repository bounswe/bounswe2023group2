import React, { useEffect, useState } from "react";
import {
  Modal,
  ModalContent,
  ModalHeader,
  ModalBody,

} from "@nextui-org/modal";
import resourceForm from "./resourceForm.json"
import { Button, Input, Select, SelectItem, Textarea } from "@nextui-org/react";
import { Controller, useForm } from "react-hook-form";
import { toast } from "react-toastify";
import { api } from "@/lib/apiUtils";
import { useRouter } from "next/router";
import addressService from "@/services/addressService";
export default function AddResourceForm({ isOpen, onOpenChange }) {
  const [form, setForm] = useState([]);
  const [subform, setSubform] = useState([]);
  const { reset, handleSubmit, control, formState: { isSubmitting }, setValue } = useForm();
  const [chosen, setChosen] = useState('');
  const [types, setTypes] = useState({});
  const [fields, setFields] = useState([]);
  const router = useRouter();
  
  const getForm = () => {
    const desiredForm = resourceForm['tr'];
    setForm(desiredForm);
  }

  const getSubtypes = async (value) => {
    const result = await api.get(`/api/form_fields/type/${value}`)
    const _subform = result.data.fields;
    _subform.map((res) => {
      if (res.type === 'select') {
        let tmp = []
        res.options.map((e, index) => {
          tmp = [...tmp, { key: e, value: e, label: e, index: index }]
        })
        res.options = tmp
      }
    })
    setSubform(_subform)
  }
  useEffect(() => {
    getForm();
  }, [])

  const can = async (data) => {
    console.log(data)
    const prepared = {}
    Object.keys(data).map((key, index) => {
     
      if (data[key] === '') { }
      else if (types[key] === 'number') {
        prepared[key] = parseInt(data[key])
      }
      else {
        prepared[key] = data[key]
      }
    })
    prepared['type'] = chosen
    prepared['currentQuantity'] = prepared['initialQuantity']
    try {
      let coordinats = await addressService.addressToXY(data.open_address);
      prepared['x'] = coordinats.payload.x
      prepared['y'] = coordinats.payload.y
    }
    catch (e) {
      toast.error("Address should contain city, street, street number")
    }
    const response = await fetch('/api/resource/add', {
      method: 'POST',
      body: JSON.stringify(prepared)
    });

    if (response.status === 400) {
      toast.error("An unexpected error occurred while saving, please try again")

    } else if (response.ok) {
      // successful
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
          <ModalHeader className="flex flex-col gap-1">Add Resource</ModalHeader>
          <ModalBody>
            <form onSubmit={handleSubmit(can)} action="#"
              method="POST" className='flex w-full flex-col  mb-6 md:mb-0 gap-4'  >

              {form !== [] && form.map((res) => {
                if (res.name === 'type') return <Select
                  id="type" name="type"
                  items={res.options}
                  label="Kaynak Türü"
                  placeholder="Kaynağınızı seçiniz"
                  className="max-xs"
                  variant={'bordered'}
                  onChange={(e) => { setChosen(e.target.value); getSubtypes(e.target.value); console.log(e) }}
                >
                  {(type) => <SelectItem value={type.value} className='text-black'>{type.value}</SelectItem>}
                </Select>
                else if (res.type === 'select') return <Controller
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
                      {(type) => <SelectItem key={type.value} className='text-black'>{type.value}</SelectItem>}
                    </Select>
                  )}
                />
                else if (res.name === 'description') return <Controller
                  name={`details.${res.name}`}
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
              {subform !== [] && subform.map((res) => {

                if (res.type === 'select') return <Controller
                  name={`details.${res.name}`}
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
                      {(type) => <SelectItem key={type.value} className='text-black'>{type.value}</SelectItem>}
                    </Select>
                  )}
                />
                else
                  return <Controller
                    name={`details.${res.name}`}
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


              <Button type='submit' color='success'>
                {isSubmitting ? 'Loading' : "Submit"}
              </Button>
            </form>
          </ModalBody>
        </>
      )}
    </ModalContent>
  </Modal>
}