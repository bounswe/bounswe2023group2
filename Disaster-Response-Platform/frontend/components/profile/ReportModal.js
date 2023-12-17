import { Button, Modal, ModalContent, ModalHeader, ModalBody, ModalFooter, Textarea } from "@nextui-org/react";
import { ToastContainer, toast } from 'react-toastify';

export default function ReportModal({ isOpen, onOpenChange, reported, labels }) {

  async function reportUser(event) {

    event.preventDefault();
    const form = new FormData(event.target);
    const formData = Object.fromEntries(form.entries());
    const body = {
      reported,
      description: formData.description
    };

    const response = await fetch('/api/report-user', {
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
    toast(labels.feedback.report_success);

  }

	return (
    <Modal isOpen={isOpen} onOpenChange={onOpenChange}>
      <ModalContent>
        {(onClose) => (
          <form id="report-modal" onSubmit={() => {reportUser(event); onClose()}}>
            <ModalHeader className="flex flex-col gap-1">{labels.admin.report}</ModalHeader>
            <ModalBody>
            	<Textarea name="description" id="description" type="text"
                      className="border-none pb-6"
                      label={labels.admin.description}
                      labelPlacement='outside'
                      variant='bordered'
                      required
                  />
            </ModalBody>
            <ModalFooter>
              <Button color="danger" variant="light" onPress={onClose}>
                {labels.UI.cancel}
              </Button>
              <Button color="primary" type="submit">
                {labels.admin.report}
              </Button>
            </ModalFooter>
          </form>
        )}
      </ModalContent>
    </Modal>
	);
}