import { Modal, ModalContent, ModalHeader, ModalBody } from "@nextui-org/modal";
import { toast } from "react-toastify";
import { Button } from '@nextui-org/react'


export default function AddEvent({ isOpen, onOpenChange }) {
  function addEvent(event) {

  }

  return (
    <Modal isOpen={isOpen} onOpenChange={onOpenChange} className='text-black' scrollBehavior="inside">
      <ModalContent>
        {(onClose) => (
          <>
            <ModalHeader className="flex flex-col gap-1">Olay ekle</ModalHeader>
            <ModalBody>
              <form onSubmit={(event) => {event.preventDefault(); addEvent(event); onClose()}}
                className='flex w-full flex-col  mb-6 md:mb-0 gap-4'  >
                <Button type='submit'>
                  Ekle
                </Button>
              </form>
            </ModalBody>
          </>
        )}
      </ModalContent>
    </Modal>
  );
}