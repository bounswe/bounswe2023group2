import { Button, Modal, ModalContent, ModalHeader, ModalBody, ModalFooter, RadioGroup, Radio, Textarea } from "@nextui-org/react";
import { ToastContainer, toast } from 'react-toastify';

export default function ProficiencyModal({ isOpen, onOpenChange, labels }) {

  async function addProficiency(event) {

    event.preventDefault();
    const form = new FormData(event.target);
    const formData = Object.fromEntries(form.entries());

    const response = await fetch('/api/add-proficiency', {
      method: 'POST',
      headers: {
        "Content-Type": "application/json",
      },
      body: JSON.stringify(formData)
    });

    if (!response.ok) {
      toast(labels.feedback.failure);
      return;
    }
    toast(labels.feedback.add_success);

  }

  const proficiency_types = ["doctor", "driver", "pharmacist", "rescue_member", "infrastructure_engineer", "it_specialist", "other"];

	return (
    <Modal isOpen={isOpen} onOpenChange={onOpenChange}>
      <ModalContent>
        {(onClose) => (
          <form id="add-proficiency-modal" onSubmit={() => {addProficiency(event); onClose()}}>
            <ModalHeader className="flex flex-col gap-1">{labels.proficiency.add_proficiency}</ModalHeader>
            <ModalBody>
              <RadioGroup name="proficiency" id="proficiency" label={labels.proficiency.field} isRequired>
                {proficiency_types.map(prof => (
                  <Radio key={`prof-type-${prof}`} value={prof}> {labels.proficiency[prof]} </Radio>
                ))}
              </RadioGroup>
            	<Textarea name="details" id="details" type="text"
                      className="border-none pb-6"
                      label={labels.UI.details}
                      labelPlacement='outside'
                      variant='bordered'
                  />
            </ModalBody>
            <ModalFooter>
              <Button color="danger" variant="light" onPress={onClose}>
                {labels.UI.cancel}
              </Button>
              <Button color="primary" type="submit">
                {labels.UI.add}
              </Button>
            </ModalFooter>
          </form>
        )}
      </ModalContent>
    </Modal>
	);
}