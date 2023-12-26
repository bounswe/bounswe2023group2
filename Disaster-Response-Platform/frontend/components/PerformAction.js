import React, { useEffect, useMemo, useState } from "react";
import {
  Modal,
  ModalContent,
  ModalHeader,
  ModalBody,

} from "@nextui-org/modal";
import { Button, Input, Select, SelectItem } from "@nextui-org/react";
import { Controller, set, useForm } from "react-hook-form";
import { toast } from "react-toastify";

import { useRouter } from "next/router";
import actionForm from "./actionForm.json"
import actionService from "@/services/actionService";
import ActivitySimple from "./ActivitySimple";
export default function PerformAction({ isOpen, onOpenChange, type = 'need', labels, selected }) {

  const { reset, handleSubmit, control, formState: { isSubmitting }, setValue } = useForm();
  const [needList, setNeedList] = useState([]);
  const [resourceList, setResourceList] = useState([]);
  const [matchList, setMatchlist] = useState([]);
  const [match, setMatch] = useState({});

  const router = useRouter();
  const getMatchList = async (data) => {
    const response = await fetch(`/api/action/getMatch?id=${'6589c76ad48d6c7a7b3ec9f0'}`, {
      method: 'GET',
    });

    let res = await response.json()
    console.log(res)
    if (response.status === 200) {
      // successful
      setNeedList(res.data.needs)
      setResourceList(res.data.resources)
      toast.success('Success')
      // Usage!

    } else {
      // unknown error
      toast.error('Error')
    }
  }




  const perform = async (data) => {
    const response = await fetch(`/api/action/perform?id=${'6589c76ad48d6c7a7b3ec9f0'}`, {
      method: 'POST',
      body: JSON.stringify({matches:matchList})
    });

    let res = await response.json()
    console.log(res)
    if (response.status === 200) {
      // successful
      toast.success('Success')
      // Usage!

    } else {
      // unknown error
      toast.error()
    }
  }

  useEffect(() => {
    console.log('can')
    if (isOpen)
      getMatchList()
  }, [isOpen])

  const can = async (data) => {

    Object.keys(data).map((key, index) => {
      if (data[key] === '' || key === 'target') { }
      else {
        prepared[key] = data[key]
      }
    })

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
          <ModalHeader className="flex">Perform Action {JSON.stringify(matchList)}</ModalHeader>
       
          <ModalBody className="flex flex-row">
            <div className="flex flex-col">
              
              {needList && needList.map((item, index) => {
                return <ActivitySimple key={index} activityType='need' activity={item} onClick={(e)=>setMatch({...match, need_id:item._id})} />

              })}
            </div>
            <div className="flex flex-col">
              {resourceList && resourceList.map((item, index) => {
                return <ActivitySimple key={index} activityType='resource'  onClick={(e)=>setMatch({...match, resource_id:item._id})} activity={item} />

              })}
            </div>
            <div className="flex flex-col">
              <Input   label='amount'  onChange={(e)=>setMatch({...match, amount:e.target.value})} > </Input>
              <Button   label='OK'  onClick={()=> {setMatchlist([...matchList,match]); setMatch({})}} >Add </Button>

            </div>
            <Button onClick={perform}> Perform</Button>

          </ModalBody>
        </>
      )}
    </ModalContent>
  </Modal>
}