import { Button, Modal, ModalContent, ModalHeader, ModalBody, ModalFooter, Textarea } from "@nextui-org/react";
import { ToastContainer, toast } from 'react-toastify';

export default function ReportModal({ isOpen, onOpenChange, reported, reported_type, labels }) {

  async function submitReport(event) {

    event.preventDefault();
    const form = new FormData(event.target);
    const formData = Object.fromEntries(form.entries());
    const body = {
      report_type_id: reported,
      report_type: reported_type,
      description: formData.description
    };

    const response = await fetch('/api/reports/create', {
      method: 'POST',
      headers: {
        "Content-Type": "application/json",
      },
      body: JSON.stringify(body)
    });

    if (!response.ok) {
      if (response.json()?.ErrorDetail === "Report already exists") {
        if (reported_type === "users") {
          toast(labels.admin.already_reported_user);
        } else {
          toast(labels.admin.already_reported_activity);
        }
      } else {
        toast(labels.feedback.failure);
      }
      return;
    }
    toast(labels.feedback.report_success);

  }

	return (
    <Modal isOpen={isOpen} onOpenChange={onOpenChange}>
      <ModalContent>
        {(onClose) => (
          <form id="report-modal" onSubmit={() => {submitReport(event); onClose()}}>
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