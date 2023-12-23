import { Modal, ModalContent, ModalHeader, ModalBody } from "@nextui-org/modal";
import { toast } from "react-toastify";
import { Button, RadioGroup, Radio, Input, Checkbox, Textarea } from '@nextui-org/react'

export default function AddEvent({ isOpen, onOpenChange, labels }) {

  async function addEvent(event, onClose) {
    event.preventDefault();
    const form = new FormData(event.target);
    const formData = Object.fromEntries(form.entries());
    formData.is_active = ("is_active" in formData);
    formData.max_distance_y /= 111.133; // km-to-latitude conversion
    const equator_angle = formData.y*Math.PI/180
    formData.max_distance_x /= 111.133 * Math.cos(equator_angle); // km-to-longitude conversion
    const current_time = new Date();
    current_time.setSeconds(0, 0);
    formData.created_time = current_time.toISOString();

    const response = await fetch('/api/event/add', {
      method: 'POST',
      headers: {
      "Content-Type": "application/json",
      }, body: JSON.stringify(formData)
    });

    if (!response.ok) {
      toast(labels.feedback.failure);
      return;
    }
    toast(labels.feedback.add_success);
    onClose();
  }

  const event_types = [
    {"key": "Debris", "label_EN": "Debris", "label_TR": "Enkaz"},
    {"key": "Infrastructure", "label_EN": "Infrastructure", "label_TR": "Altyapı"},
    {"key": "Disaster", "label_EN": "Disaster", "label_TR": "Afet"},
    {"key": "Help-Arrived", "label_EN": "Help Arrived", "label_TR": "Yardım Ulaştı"}
  ];

  const fields = [
    {"Component": Input, "key": "event_time", "type": "datetime-local", "placeholder": " "},
    {"Component": Checkbox, "key": "is_active"},
    {"Component": Input, "key": "x", "type": "number", "placeholder": "29", max: "180", min: "-180", step: "any"},
    {"Component": Input, "key": "y", "type": "number", "placeholder": "41", max: "90", min: "-90", step: "any"},
    {"Component": Input, "key": "max_distance_x", "type": "number", "placeholder": "1", max: "360", min: "0", step: "any", endContent: "km"},
    {"Component": Input, "key": "max_distance_y", "type": "number", "placeholder": "1", max: "180", min: "0", step: "any", endContent: "km"},
    {"Component": Textarea, "key": "short_description"},
    {"Component": Textarea, "key": "note"}
  ];

  return (
    <Modal isOpen={isOpen} onOpenChange={onOpenChange} className='text-black' scrollBehavior="inside">
      <ModalContent>
        {(onClose) => (
          <>
            <ModalHeader className="flex flex-col gap-1">{labels.activities.report_event}</ModalHeader>
            <ModalBody>
              <form onSubmit={(event) => {addEvent(event, onClose)}}
                    className='flex w-full flex-col  mb-6 md:mb-0 gap-4'>

                <RadioGroup name="event_type" id="event_type" label={labels.activity_table.event_type} orientation="horizontal">
                  {event_types.map(({key, label_TR}) => (
                    <Radio key={`event-type-${key}`} value={key}> {label_TR} </Radio>
                  ))}
                </RadioGroup>

                {fields.map(
                  (field) => {
                    const Component = field.Component;
                    const children = Component === Checkbox ? labels.forms.event_form[field.key] : undefined
                    return (
                      <Component name={field.key} id={`event-${field.key}`} key={`event-${field.key}`}
                                 label={labels.forms.event_form[field.key]}
                                 labelPlacement={"inside"}
                                 type={field.type}
                                 placeholder={field.placeholder}
                                 max={field.max}
                                 min={field.min}
                                 step={field.step}
                                 endContent={field.endContent}
                                 required={field.key==="note" ? undefined : ""}
                      >
                        {children}
                      </Component>
                    );
                  }
                )}

                <Button type='submit'>
                  {labels.UI.submit}
                </Button>

              </form>
            </ModalBody>
          </>
        )}
      </ModalContent>
    </Modal>
  );
}