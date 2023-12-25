import { Modal, ModalContent, ModalHeader, ModalBody, ModalFooter } from "@nextui-org/modal";
import { toast } from "react-toastify";
import { Button, Textarea, RadioGroup, Radio, Input } from '@nextui-org/react'
import { useState } from 'react';
import dynamic from "next/dynamic";

const EmergencyMap = dynamic(() => import("@/components/Map/EmergencyMap"), {
  ssr: false,
});

export default function AddEmergency({ isOpen, onOpenChange, labels }) {
  // position is lat, lng
  const [ position, setPosition ] = useState([41.08714, 29.043474]);

  async function addEmergency(event, onClose) {
    const form = new FormData(event.target);
    const formData = Object.fromEntries(form.entries());

    const sentData = {
      ...formData,
      x: position[1],
      y: position[0],
      location: formData.location || `${Math.abs(position[0])}${position[0] >= 0 ? "N" : "S"} ${Math.abs(position[1])}${position[1] >= 0 ? "E" : "W"}`
    };

    const response = await fetch('/api/add-emergency', {
      method: 'POST',
      headers: {
      "Content-Type": "application/json",
      }, body: JSON.stringify(sentData)
    });

    if (response.status !== 200) {
      toast(labels.feedback.failure);
      return;
    }
    toast(labels.feedback.add_success);
    onClose();

  }

  const emergency_types = ["News", "Debris", "Fire", "Medical Emergency"];

  return (
    <Modal isOpen={isOpen} size="3xl" onOpenChange={onOpenChange} className='text-black' scrollBehavior="inside">
      <ModalContent>
        {(onClose) => (
          <form onSubmit={(event) => {event.preventDefault(); addEmergency(event, onClose)}} >
            <ModalHeader className="flex flex-col gap-1">{labels.activities.report_emergency}</ModalHeader>
            <ModalBody>
              <div className="flex flex-row items-center gap-x-4">
                <div className="flex gap-y-2 flex-col">
                  <RadioGroup name="emergency_type" id="emergency_type" label={labels.activity_table.event_type} orientation="horizontal" required >
                    {emergency_types.map((key) => (
                      <Radio key={`emergency-type-${key}`} value={key}> {labels.forms.emergency_form[key]} </Radio>
                    ))}
                  </RadioGroup>
                  <Input name="contact_name" variant="bordered" label={labels.forms.emergency_form.contact_name} />
                  <Input name="contact_number" variant="bordered" label={labels.forms.emergency_form.contact_number} />
                  <Textarea name="description" variant="bordered" label={labels.forms.emergency_form.description} required />
                  <Input name="location" variant="bordered" label={labels.forms.emergency_form.location} />
                </div>
                <EmergencyMap position={position} setPosition={setPosition} labels={labels}/>
              </div>
              </ModalBody>
              <ModalFooter>
                <Button color="danger" variant="light" onPress={onClose}>
                  {labels.UI.cancel}
                </Button>
                <Button color="primary" type="submit" onSubmit={event => {addEmergency(event); onClose()}}>
                  {labels.UI.submit}
                </Button>
            </ModalFooter>
          </form>
        )}
      </ModalContent>
    </Modal>
  );
}