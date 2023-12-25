import { Button, Modal, ModalContent, ModalHeader, ModalBody, ModalFooter } from "@nextui-org/react";
import { toast } from 'react-toastify';
import { useRouter } from 'next/navigation';

export default function DeleteUserModal({ isOpen, onOpenChange, reported, labels }) {
  const router = useRouter();

  async function deleteAccount() {

    const response = await fetch('/api/delete-user', { method: 'DELETE' });

    if (!response.ok) {
      toast(labels.feedback.failure);
      return;
    }
    toast(labels.feedback.account_deleted);
    router.push('/');
  }

	return (
    <Modal isOpen={isOpen} onOpenChange={onOpenChange}>
      <ModalContent>
        {(onClose) => (
          <>
            <ModalHeader className="flex flex-col gap-1">{labels.profile_pages.are_you_sure_delete}</ModalHeader>
            <ModalBody />
            <ModalFooter>
              <Button color="default" variant="light" onPress={onClose}>
                {labels.UI.cancel}
              </Button>
              <Button color="danger" type="button" onPress={deleteAccount}>
                {labels.UI.delete}
              </Button>
            </ModalFooter>
          </>
        )}
      </ModalContent>
    </Modal>
	);
}