import { Button, Modal, ModalContent, ModalHeader, ModalBody, ModalFooter } from "@nextui-org/react";
import { toast } from 'react-toastify';
import Image from 'next/image';
import { api } from '@/lib/apiUtils';

export default function AvatarModal({ isOpen, onOpenChange, file, username, accessToken, optional_fields, labels }) {

  async function submitAvatar() {

    /*
      Normally, doing uploads in an API route might have been a better idea
      However Node.js with the pages router doesn't support req.formData() or req.json() from the fetch API
      and that massively complicates things when it comes to reading the request
    */

    const extension = file.name.substring(file.name.lastIndexOf(".")+1);
    const filename = `${username}-pic.${extension}`;
    const renamed_file = new File([file], filename, {type: file.type});

    const body = new FormData();
    body.set("file", renamed_file);

    const upload_response = await api.post("/api/uploadfile", body, {
        headers: {
          'Authorization': `Bearer ${accessToken}`,
          'Content-Type': 'multipart/form-data'
        }
    });

    if (upload_response.status !== 200) {
      toast(`${labels.feedback.failure} (picture upload: ${upload_response.statusText})`);
      return;
    }

    const profile_response = await api.post("/api/profiles/user-optional-infos/add-user-optional-info", 
      { ...optional_fields, profile_picture: upload_response?.data?.url},
      { headers: { 'Authorization': `Bearer ${accessToken}` } }
    );

    if (upload_response.status !== 200) {
      toast(`${labels.feedback.failure} (picture URL save: ${upload_response.statusText})`);
      return;
    }

    toast(labels.feedback.add_success);

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