import React from "react";
import GenericForm from "./GenericForm";
import {
    Modal,
    ModalContent,
    ModalHeader,
    ModalBody,

} from "@nextui-org/modal";
import styles from "./AddResource.module.scss";
import { Button, Input } from "@nextui-org/react";
export default function AddResourceForm({ isOpen, onOpenChange }) {
    const fields = [
        { type: "text", name: "type", required: true, label: "Type", placeholder: "Food" },
        { type: "text", name: "sub_type", required: true, label: "Sub Type", placeholder: "Pasta" },
        { type: "date", name: "due_date", required: true,  },
        { type: "text", name: "location", required: true, label: "Location" },
    ]
    const renderForm = ({ register, errors, isSubmitting }) => {
        return <>
            {fields.map(field => {
                return <>
                    <Input type={field.type} 
                        {...register(field.name, { required: field.required })}
                        style={{ border: 'none' }}
                        label={field.label}
                        labelPlacement={'outside'}
                        variant={'bordered'}
                    />
                    <div className="error" >{errors[field.name]?.message}</div>
                </>
            })}
            <Button disabled={isSubmitting} type='submit' >
                {isSubmitting ? 'Loading' : "Submit"}
            </Button>
        </>;
    }
    return <Modal isOpen={isOpen} onOpenChange={onOpenChange} className='text-black'>
        <ModalContent>
            {(onClose) => (
                <>
                    <ModalHeader className="flex flex-col gap-1">Add Resource</ModalHeader>
                    <ModalBody> <GenericForm url="/api/form" renderForm={renderForm} />
                    </ModalBody>
                </>
            )}
        </ModalContent>
    </Modal>
}