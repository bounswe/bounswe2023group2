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
import { api } from "@/lib/apiUtils";
import { useRouter } from "next/router";
export default function AddNeedForm({ isOpen, onOpenChange }) {
  const [form, setForm] = useState([]);
  const [subform, setSubform] = useState([]);
  const { reset, handleSubmit, control, formState: { isSubmitting }, setValue } = useForm();
  const [chosen, setChosen] = useState('');
  const [types, setTypes] = useState({});
  const [fields, setFields] = useState([]);
  const router = useRouter();
  const getFrom = async () => {
    const result = await api.get('/api/form_fields/need')
    const desiredForm = result.data.fields;
    setForm(desiredForm)
    desiredForm.map((res) => {
      let can3 = {}
      can3[res.name] = res.type
      setTypes((prev) => { return { ...prev, ...can3 } })

      if (res.name === 'type') {
        let tmp = []
        res.options.map((e, index) => {

          tmp = [...tmp, { key: e, value: e, label: e, index: index }]

        })
        res.options = tmp
      }
      else if (res.type === 'select') {
        let tmp = []
        res.options.map((e, index) => {
          tmp = [...tmp, { key: e, value: e, label: e, index: index }]
        })
        res.options = tmp
        console.log(res.options)
      }
    })
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
        console.log(res.options)
      }
    })
    setSubform(_subform)
  }



  useEffect(() => {
    getFrom();
  }, [])

  const can = async (data) => {
    console.log(data)
    const prepared = {}
    Object.keys(data).map((key, index) => {
      console.log(types, key)

      if (data[key] === '') { }
      else if (types[key] === 'number' | key === 'urgency' | key === 'recurrence_rate') {
        prepared[key] = parseInt(data[key])
      }
      else {
        prepared[key] = data[key]
      }
    })

    prepared['type'] = chosen
    console.log(prepared)
    prepared['unsuppliedQuantity'] = prepared['initialQuantity']
    const response = await fetch('/api/need/add', {
      method: 'POST',
      body: JSON.stringify(prepared)
    });

    if (response.status === 400) {
      toast.error("An unexpected error occurred while saving, please try again")
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
  return <Modal isOpen={isOpen} onOpenChange={onOpenChange} className='text-black' scrollBehavior="inside">
    <ModalContent>
      {(onClose) => (
        <>
          <ModalHeader className="flex flex-col gap-1">Add Need</ModalHeader>
          <ModalBody>
            <form onSubmit={handleSubmit(can)} action="#"
              method="POST" className='flex w-full flex-col  mb-6 md:mb-0 gap-4'  >
              {form !== [] && form.map((res) => {
                if (res.name === 'type') return <Select
                  id="type" name="type"
                  items={res.options}
                  label="İhtiyaç Türü"
                  placeholder="İhtiyacınızı seçiniz"
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