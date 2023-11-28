import React, { useEffect, useMemo, useState } from "react";
import {
  Modal,
  ModalContent,
  ModalHeader,
  ModalBody,

} from "@nextui-org/modal";
import { Button, Dropdown, DropdownItem, DropdownMenu, DropdownTrigger, Input, Select, SelectItem } from "@nextui-org/react";
import { Controller, useForm } from "react-hook-form";
import { toast } from "react-toastify";
import { api } from "@/lib/apiUtils";
import { useRouter } from "next/router";
export default function AddActionForm({ isOpen, onOpenChange, table_need, need_type}) {
  const [form, setForm] = useState([]);
  const needTypes = ['food', 'water', 'shelter', 'medicine', 'cloth', 'transportation', 'other'];
  const { reset, handleSubmit, control, formState: { isSubmitting }, setValue } = useForm();
  const [chosen, setChosen] = useState('');
  const [types, setTypes] = useState({});
  const [needs, setNeeds] = useState([]);
  const [resources, setResources] = useState([]);
  const [selectedNeeds, setSelectedNeeds] = useState([]);

  const selectedValue = useMemo(
    () => Array.from(selectedNeeds).join(", ").replaceAll("_", " "),
    [selectedNeeds]
  );

  const router = useRouter();
  const getFrom = async () => {
    const result = await api.get('/api/form_fields/action')
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

      }
    })
  }

  useEffect(() => {
    getFrom();
  }, [])
  
  const filterActivities = async () => {
    let response
    let responseNeed = await api.get(`/api/needs/?types=${table_need.type}&status=active`, { headers: { "Content-Type": "application/json" } });
    let resNeed = responseNeed.data;
    if (responseNeed.status === 200) {
      setNeeds(resNeed.needs)
    } else {
      toast.error("An unexpected error occurred while saving, please try again")
    }
    response = await api.get(`/api/resources/?types=${table_need.type}&status=active`, { headers: { "Content-Type": "application/json" } });
    let res = response.data;
    if (response.status === 200) {
      setResources(res.resources)
      console.log(resources)
    } else {

      toast.error("An unexpected error occurred while saving, please try again")
    }
  }
  const can = async (data) => {

    const prepared = {}
    console.log(data)
    Object.keys(data).map((key, index) => {
      console.log(types, key)
      if (data[key] === '' || key === 'resource') { }
      else {
        prepared[key] = data[key]
      }
      if (types[key] === 'number' && key !== 'resource') {
        prepared[key] = parseInt(data[key])
      }
      else if( key !== 'resource') {
        prepared[key] = data[key]
      }

    })
    prepared['type'] = chosen
    let action_temp=  {"related_needs": [table_need._id], "related_resources": [data['resource']]}
    prepared["related_groups"] = [action_temp, ...prepared["related_groups"]]
    console.log(prepared)

    const response = await fetch('/api/action/add', {
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
          <ModalHeader className="flex flex-col gap-1">Create Action</ModalHeader>
          <ModalBody>
            {/* <Dropdown>
              <DropdownTrigger>
                <Button
                  variant="bordered"
                  className="capitalize"
                >
                  {selectedValue}
                </Button>
              </DropdownTrigger>
              <DropdownMenu
                aria-label="Multiple selection example"
                variant="flat"
                closeOnSelect={true}
                selectionMode="single"
                disallowEmptySelection
                selectedKeys={selectedNeeds}
                onSelectionChange={(e)=>{setSelectedNeeds(e); filterActivities(Array.from(e).join(", "));console.log(e)}}

              >
                {needTypes.map((need) => (<DropdownItem key={need}> {need}</DropdownItem>))}
              </DropdownMenu>
            </Dropdown> */}
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
                  onChange={(e) => { setChosen(e.target.value); console.log(e) }}


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

                // else if (res.type === 'array') return <Controller
                //   name={res.name}
                //   control={control}
                //   defaultValue=""
                //   label={res.label}
                //   placeholder={res.label}
                //   render={({ field }) => (
                //     <Select
                //       id={field.name} name={field.name}
                //       items={res.options}
                //       label={res.label}
                //       placeholder={res.label}
                //       className="max-xs"
                //       variant={'bordered'}
                //       {...field}
                //     >
                //       {(type) => <SelectItem key={type.value} className='text-black'>{type.value}</SelectItem>}
                //     </Select>
                //   )}
                // />
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
              {/* {subform !== [] && subform.map((res) => {
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
              })} */}
              <Button onPress={filterActivities}>Kaynakları getir</Button>
              <Controller
                  name={'resource'}
                  control={control}
                  selectionMode="multiple"
                  label={'Kaynak seçiniz'} 
                  placeholder={'Kaynak seçiniz'}
                  render={({ field }) => (
                    <Select
                      id={'resource'} name={'resource'}
                      items={resources}
                      label={'Kaynak seçiniz'} 
                      placeholder={'Kaynak seçiniz'}
                      className="max-xs"
                      variant={'bordered'}
                      
                      {...field}
                    >
                      {(type) => <SelectItem key={type._id} value={type.value} className='text-black'>{type.type}: {type.created_by}</SelectItem>}
                    </Select>
                  )}
                />
                <span>
                  {table_need['created_by']} : { table_need['unsuppliedQuantity']}
                </span>


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