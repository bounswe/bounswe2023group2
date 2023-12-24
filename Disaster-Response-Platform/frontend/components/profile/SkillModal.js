import { Button, Modal, ModalContent, ModalHeader, ModalBody, ModalFooter, Input, RadioGroup, Radio } from "@nextui-org/react";
import { useState } from "react";

export default function SkillModal({ isOpen, onOpenChange, topic, labels }) {

  const [certificateName, setCertificateName] = useState("");

  function onCertificateUpload(event) {
    setCertificateName(event.target.files?.[0]?.name ?? "");
  }

  const skill_labels = labels.profile_lists[topic.api_url];

	return (
      <Modal isOpen={isOpen} onOpenChange={onOpenChange}>
        <ModalContent>
          {(onClose) => (
            <form id="skill-modal" onSubmit={event => {topic.addSkill(event);onClose()}}>
              <ModalHeader className="flex flex-col gap-1">{topic.add_title}</ModalHeader>
              <ModalBody>
              	<Input name={topic.primary} id={topic.primary} type="text"
                       className="border-none pb-6"
                       label={skill_labels.primary_label}
                       labelPlacement='outside'
                       variant='bordered'
                       required
                    />
                {topic.is_link ?
	              	<Input name={topic.secondary} id={topic.secondary} type="url"
	                       className="border-none pb-6"
	                       label={skill_labels.secondary_label}
	                       labelPlacement='outside'
	                       variant='bordered'
	                       required
	                    />
	                :
	                <RadioGroup name={topic.secondary} id={topic.secondary} label={skill_labels.secondary_label}>
	                	{topic.options.map(option => (
	                		<Radio key={`link-option-${option}`} value={option}> {skill_labels.options[option]} </Radio>
	                	))}
	                </RadioGroup>
	              }
                {
                  topic.certificate ? (
                    <div class="my-2 w-full text-center">
                      <Input type="file" name="certificate" id="certificate" accept="image/png, image/jpeg, application/pdf" className="hidden" onChange={onCertificateUpload}/>
                      <button type="button" onClick={() => {document.getElementById("certificate").click()}} class="my-2 mx-auto w-1/2 text-white bg-blue-700 hover:bg-blue-800 focus:ring-4 focus:outline-none focus:ring-blue-300 font-medium rounded-lg text-m w-full sm:w-auto px-4 py-1.5 text-center dark:bg-blue-600 dark:hover:bg-blue-700 dark:focus:ring-blue-800">
                        {labels.profile_pages.upload_certificate}
                      </button>
                      <br />
                      {labels.profile_pages.certificate_uploaded}{certificateName ? certificateName : labels.profile_pages.none}
                    </div>
                  ) : null
                }
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