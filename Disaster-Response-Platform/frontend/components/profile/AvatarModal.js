import { Button, Modal, ModalContent, ModalHeader, ModalBody, ModalFooter } from "@nextui-org/react";
import { toast } from 'react-toastify';
import Image from 'next/image';

export default function AvatarModal({ isOpen, onOpenChange, file, labels }) {

  async function submitAvatar() {

    const body = {};

    toast("So far so good!")

    /*const response = await fetch('/api/profiles/upload-user-profile-picture', {
      method: 'POST',
      headers: {
        "Content-Type": "application/json",
      },
      body: JSON.stringify(body)
    });

    if (!response.ok) {
      toast(labels.feedback.failure);
      return;
    }
    toast(labels.feedback.save_success);*/

  }

  const pictureURL = file ? URL.createObjectURL(file) : "";

	return (
    <Modal isOpen={isOpen} onOpenChange={onOpenChange}>
      <ModalContent>
        {(onClose) => (
          <>
            <ModalHeader className="flex flex-col gap-1">{labels.profile_pages.upload_avatar}</ModalHeader>
            <ModalBody className="text-center">
              <h3 class="object-top text-xl"> {labels.profile_pages.upload_avatar_confirm} </h3>
            	<Image
                src={pictureURL}
                className="block mx-auto"
                height={256}
                width={256}
                  />
            </ModalBody>
            <ModalFooter>
              <Button color="danger" variant="light" onPress={onClose}>
                {labels.UI.cancel}
              </Button>
              <Button color="primary" onPress={() => {submitAvatar(); onClose()}}>
                {labels.UI.submit}
              </Button>
            </ModalFooter>
          </>
        )}
      </ModalContent>
    </Modal>
	);
}