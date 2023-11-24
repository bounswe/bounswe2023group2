import { Button, Modal, ModalContent, ModalHeader, ModalBody, ModalFooter, Textarea } from "@nextui-org/react";

export default function ReportModal({ isOpen, onOpenChange }) {
	return (
      <Modal isOpen={isOpen} onOpenChange={onOpenChange}>
        <ModalContent>
          {(onClose) => (
            <form id="report-modal" onSubmit={event => {}}>
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