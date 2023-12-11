import { Modal, ModalContent, ModalHeader, ModalBody } from "@nextui-org/modal";
import { toast } from "react-toastify";
import { Button, RadioGroup, Radio, Input, Checkbox, Textarea } from '@nextui-org/react'


export default function AddEvent({ isOpen, onOpenChange }) {
  function addEvent(event) {

  }

  const event_types = [
    {"key": "Debris", "label_EN": "Debris", "label_TR": "Enkaz"},
    {"key": "Infrastructure", "label_EN": "Infrastructure", "label_TR": "Altyapı"},
    {"key": "Disaster", "label_EN": "Disaster", "label_TR": "Afet"},
    {"key": "Help-Arrived", "label_EN": "Help Arrived", "label_TR": "Yardım Ulaştı"}
  ];

  const fields = [
    {"Component": Input, "key": "event_time", "label_EN": "Event time", "label_TR": "Olay zamanı", "type": "date", "placeholder": " "},
    {"Component": Checkbox, "key": "is_active", "label_EN": "Still active", "label_TR": "Hala mevcut"},
    {"Component": Input, "key": "center_location_x", "label_EN": "Center longitude (West-East)", "label_TR": "Merkez boylamı (Batı-Doğu)", "type": "number", "placeholder": "29", max: "180", min: "-180", step: "any"},
    {"Component": Input, "key": "center_location_y", "label_EN": "Center latitude (North-South)", "label_TR": "Merkez enlemi (Kuzey-Güney)", "type": "number", "placeholder": "41", max: "90", min: "-90", step: "any"},
    {"Component": Input, "key": "max_distance_x", "label_EN": "Affected distance (West-East)", "label_TR": "Etkilenen uzaklık (Batı-Doğu)", "type": "number", "placeholder": "1", max: "360", min: "0", step: "any", endContent: "km"},
    {"Component": Input, "key": "max_distance_y", "label_EN": "Affected distance (North-South)", "label_TR": "Etkilenen uzaklık (Kuzey-Güney)", "type": "number", "placeholder": "1", max: "180", min: "0", step: "any", endContent: "km"},
    {"Component": Textarea, "key": "short_description", "label_EN": "Short description", "label_TR": "Kısa açıklama"},
    {"Component": Textarea, "key": "note", "label_EN": "Extra notes", "label_TR": "Ek notlar"}
  ];

  return (
    <Modal isOpen={isOpen} onOpenChange={onOpenChange} className='text-black' scrollBehavior="inside">
      <ModalContent>
        {(onClose) => (
          <>
            <ModalHeader className="flex flex-col gap-1">Olay bildir</ModalHeader>
            <ModalBody>
              <form onSubmit={(event) => {event.preventDefault(); addEvent(event); onClose()}}
                    className='flex w-full flex-col  mb-6 md:mb-0 gap-4'>

                <RadioGroup name="event_type" id="event_type" label="Olay Türü">
                  {event_types.map(({key, label_TR}) => (
                    <Radio key={`event-type-${key}`} value={key}> {label_TR} </Radio>
                  ))}
                </RadioGroup>

                {fields.map(
                  (field) => {
                    const Component = field.Component;
                    const children = Component === Checkbox ? field.label_TR : undefined
                    return (
                      <Component name={field.key} id={`event-${field.key}`} key={`event-${field.key}`}
                                 label={field.label_TR}
                                 labelPlacement={"inside"}
                                 type={field.type}
                                 placeholder={field.placeholder}
                                 max={field.max}
                                 min={field.min}
                                 step={field.step}
                                 endContent={field.endContent}
                      >
                        {children}
                      </Component>
                    );
                  }
                )}

                <Button type='submit'>
                  Bildir
                </Button>

              </form>
            </ModalBody>
          </>
        )}
      </ModalContent>
    </Modal>
  );
}