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
export default function AddNeedForm({ isOpen, onOpenChange }) {
  const [form, setForm] = useState([]);
  const [subform, setSubform] = useState([]);
  const { reset, handleSubmit, control, formState: { isSubmitting }, setValue } = useForm();
  const [chosen, setChosen] = useState('');
  const [types, setTypes] = useState([]);
  const [fields, setFields] = useState([]);
  const getFrom = async () => {
    const result = await api.get('/api/form_fields/need')
    const desiredForm = result.data.fields;
    setForm(desiredForm)
    desiredForm.map((res) => {
      if (res.name === 'type') {
        console.log(res.options)
        let tmp = []
        res.options.map((e) => {

          tmp = [...tmp, { key: e, value: e, label: e }]
          setTypes((prev) => [...prev, { key: e, value: e, label: e }])
        })
        res.options = tmp
        console.log(res.options)

      }
      else if (res.type === 'select') {
        let tmp = []
        res.options.map((e) => {
          tmp = [...tmp, { key: e, value: e, label: e }]
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
        res.options.map((e) => {
          tmp = [...tmp, { key: e, value: e, label: e }]
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
    Object.keys(data).map((key) => {
      if (key === 'recurrence_rate' | key === 'recurrence_date') {
        if (data[key] === '') { }
        else {
          prepared[key] = data[key]
        }
      }
      else if (data[key].type === 'number') {
        prepared[key] = parseInt(data[key])
      }
      else {
        prepared[key] = data[key]
      }
    })

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
              {types !== [] && <Controller
                name="type"
                control={control}
                defaultValue=""
                label='İhtiyaç Türü'
                onChange={(e) => { setChosen(e.target.value); getSubtypes(e.target.value) }}
                placeholder="Ihtiyacınızı seçiniz"
                render={({ field }) => (<Select
                  id="type" name="type"
                  items={types}
                  label="İhtiyaç Türü"
                  placeholder="İhtiyacınızı seçiniz"
                  className="max-xs"
                  variant={'bordered'}
                  {...field}
                >
                  {(type) => <SelectItem key={type.value} className='text-black'>{type.value}</SelectItem>}
                </Select>
                )}
              />
              }

              {form !== [] && form.map((res) => {
                if (res.name === 'type') return <></>
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
                if (res.name === 'type') return <></>
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