import React from "react";
import {Modal, ModalContent, ModalHeader, ModalBody, ModalFooter, Button, useDisclosure, Divider} from "@nextui-org/react";

export default function ActivityModal({isOpen, onOpen, onOpenChange, activity}) {
  return (
    <>
      <Modal isOpen={isOpen} onOpenChange={onOpenChange} className='text-black'>
        <ModalContent>
          {(onClose) => (
            <>
              <ModalHeader className="flex flex-col gap-1 ">{activity.type}</ModalHeader>
            <Divider/>
              <ModalBody>
                <p>
                  {activity.description ?? "No description"}
                </p>
                {Object.keys(activity).map((key) => {
                  return <p>
                  {key}: {(activity[key]).toString() ?? "No information"}  
                </p>
                })}
                
               
              </ModalBody>
              <ModalFooter>
                <Button color="danger" variant="light" onPress={onClose}>
                  Close
                </Button>
                <Button color="primary" onPress={onClose}>
                  Action
                </Button>
              </ModalFooter>
            </>
          )}
        </ModalContent>
      </Modal>
    </>
  );
}
