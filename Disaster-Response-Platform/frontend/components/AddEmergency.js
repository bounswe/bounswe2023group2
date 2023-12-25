import { Modal, ModalContent, ModalHeader, ModalBody, ModalFooter } from "@nextui-org/modal";
import { toast } from "react-toastify";
import { Button, Textarea, RadioGroup, Radio, Input } from '@nextui-org/react'
import { useState } from 'react';
import dynamic from "next/dynamic";

const EmergencyMap = dynamic(() => import("@/components/Map/EmergencyMap"), {
  ssr: false,
});

export default function AddEmergency({ isOpen, onOpenChange, labels }) {
  const [ position, setPosition ] = useState([41.08714, 29.043474]);

  function addEmergency(event) {

  }

  const emergency_types = ["News", "Debris", "Fire", "Medical Emergency"];

  return (
    <Modal isOpen={isOpen} onOpenChange={onOpenChange} className='text-black' scrollBehavior="inside">
      <ModalContent>
        {(onClose) => (
          <form onSubmit={(event) => {event.preventDefault(); addEmergency(event); onClose()}} >
            <ModalHeader className="flex flex-col gap-1">{labels.activities.report_emergency}</ModalHeader>
            <ModalBody>
              <RadioGroup name="emergency_type" id="emergency_type" label={labels.activity_table.event_type} orientation="horizontal">
                {emergency_types.map((key) => (
                  <Radio key={`emergency-type-${key}`} value={key}> {labels.forms.emergency_form[key]} </Radio>
                ))}
              </RadioGroup>
              <EmergencyMap position={position} setPosition={setPosition} labels={labels}/>
              <Textarea name="description" label={labels.forms.emergency_form.description} />
              <Input name="location" label={labels.forms.emergency_form.location} />
            </ModalBody>
            <ModalFooter>
              <Button color="danger" variant="light" onPress={onClose}>
                {labels.UI.cancel}
              </Button>
              <Button color="primary" onPress={() => {submitAvatar(); onClose()}}>
                {labels.UI.submit}
              </Button>
            </ModalFooter>
          </form>
        )}
      </ModalContent>
    </Modal>
  );
}