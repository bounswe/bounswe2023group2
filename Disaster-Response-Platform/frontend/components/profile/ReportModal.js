import { Button, Modal, ModalContent, ModalHeader, ModalBody, ModalFooter, Textarea } from "@nextui-org/react";
import { ToastContainer, toast } from 'react-toastify';

export default function ReportModal({ isOpen, onOpenChange, reported }) {

  async function reportUser(event) {

    event.preventDefault();
    const form = new FormData(event.target);
    const formData = Object.fromEntries(form.entries());
    const body = {
      reported,
      reason: formData.reason
    };

    const response = await fetch('/api/report-user', {
      method: 'POST',
      headers: {
        "Content-Type": "application/json",
      },
      body: JSON.stringify(body)
    });

    if (!response.ok) {
      toast("Bir hata oluştu :(");
      return;
    }
    toast("Başarıyla raporlandı");

  }

	return (
    <Modal isOpen={isOpen} onOpenChange={onOpenChange}>
      <ModalContent>
        {(onClose) => (
          <form id="report-modal" onSubmit={() => {reportUser(event); onClose()}}>
            <ModalHeader className="flex flex-col gap-1">Raporla</ModalHeader>
            <ModalBody>
            	<Textarea name="reason" id="reason" type="text"
                      className="border-none pb-6"
                      label="Raporlama sebebi"
                      labelPlacement='outside'
                      variant='bordered'
                      required
                  />
            </ModalBody>
            <ModalFooter>
              <Button color="danger" variant="light" onPress={onClose}>
                Vazgeç
              </Button>
              <Button color="primary" type="submit">
                Raporla
              </Button>
            </ModalFooter>
          </form>
        )}
      </ModalContent>
    </Modal>
	);
}