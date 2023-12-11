import { Modal, ModalContent, ModalHeader, ModalBody } from "@nextui-org/modal";
import { toast } from "react-toastify";
import { Button } from '@nextui-org/react'


export default function AddEmergency({ isOpen, onOpenChange }) {
  function addEmergency(event) {

  }

  return (
    <Modal isOpen={isOpen} onOpenChange={onOpenChange} className='text-black' scrollBehavior="inside">
      <ModalContent>
        {(onClose) => (
          <>
            <ModalHeader className="flex flex-col gap-1">Acil durum bildir</ModalHeader>
            <ModalBody>
              <form onSubmit={(event) => {event.preventDefault(); addEmergency(event); onClose()}}
                className='flex w-full flex-col  mb-6 md:mb-0 gap-4'  >
                <Button type='submit'>
                  Bildir
                </Button>
              </form>
            </ModalBody>
          </>
        )}
      </ModalContent>
    </Modal>
  );
}