import React, { useEffect, useState } from "react";
import {
  Modal,
  ModalContent,
  ModalHeader,
  ModalBody,
} from "@nextui-org/modal";
import { Button, Input, Select, SelectItem, Textarea } from "@nextui-org/react";
import needForm from "./needForm.json"
import { Controller, useForm } from "react-hook-form";
import { toast } from "react-toastify";
import { api } from "@/lib/apiUtils";
import { useRouter } from "next/router";
import { withIronSessionSsr } from "iron-session/next";
import sessionConfig from "@/lib/sessionConfig";
import addressService from "@/services/addressService";
import AddRecurrenceForm from "./AddRecurrence";
export default function AddNeedForm({ isOpen, onOpenChange, lang, ...props }) {
  const [form, setForm] = useState([]);
  const [isRecurrenct, setIsRecurrence] = useState(false);
  const [isSuccess, setIsSuccess] = useState(false);
  const [subform, setSubform] = useState([]);
  const { reset, handleSubmit, control, formState: { isSubmitting }, setValue } = useForm();
  const [chosen, setChosen] = useState('');
  const [types, setTypes] = useState({});
  const [fields, setFields] = useState([]);
  const [id, setID] = useState('');
  const router = useRouter();

  const getForm = () => {
    const desiredForm = needForm['tr'];
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

    getForm(lang);

  }, [])

  const can = async (data) => {
    const prepared = {}
    Object.keys(data).map((key, index) => {
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
    try {
      let coordinats = await addressService.addressToXY(data.open_address);
      console.log(coordinats)
      prepared['x'] = coordinats.payload.x
      prepared['y'] = coordinats.payload.y
    }
    catch (e) {
      toast.error("Address should contain city, street, street number")
    }
    prepared['unsuppliedQuantity'] = prepared['initialQuantity']
    const response = await fetch('/api/need/add', {
      method: 'POST',
      body: JSON.stringify(prepared)
    });

    if (response.status === 400) {
      toast.error("An unexpected error occurred while saving, please try again")
    } else if (response.ok) {
      // successful
      setIsSuccess(true)
     let result =  await response.json()
      setID(result.data.needs[0]._id)
      toast.success("Successfully saved")
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
            {!isSuccess && <form onSubmit={handleSubmit(can)} action="#"
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
                  {(type) => <SelectItem value={type.value} className='text-black'>{type.label}</SelectItem>}
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
                      {(type) => <SelectItem key={type.value} className='text-black'>{type.label}</SelectItem>}
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
                      {(type) => <SelectItem key={type.value} className='text-black'>{type.label}</SelectItem>}
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
              <Button type='submit' color='primary'>
                {isSubmitting ? 'Loading' : "Submit"}
              </Button>
            </form>
}           
{isSuccess && <Button onClick={() => { setIsRecurrence(!isRecurrenct); reset() }}>Schedule your need</Button> }
{isRecurrenct && <AddRecurrenceForm activity_id={id} activityType='Need'/>}

          </ModalBody>
        </>
      )}

    </ModalContent>
  </Modal>
}