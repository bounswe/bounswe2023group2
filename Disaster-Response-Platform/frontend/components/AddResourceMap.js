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
export default function AddResourceForm({ isOpen, onOpenChange,fetchData }) {
    const fields = [
        { type: "text", name: "type", required: true, label: "İlan türü", placeholder: "Food" },
        { type: "text", name: "sub_type", required: true, label: "İçerik Türü", placeholder: "Pasta" },
        { type: "date", name: "due_date", required: true,  },
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
                {isSubmitting ? 'Yükleniyor' : "Oluştur"}
            </Button>
        </>;
    }
    return <Modal isOpen={isOpen} onOpenChange={onOpenChange} className='text-black'>
        <ModalContent>
            {(onClose) => (
                <>
                    <ModalHeader className="flex flex-col gap-1">İlan Oluştur</ModalHeader>
                    <ModalBody> <GenericForm url="/api/form" renderForm={renderForm} fetchData={fetchData} />
                    </ModalBody>
                </>
            )}
        </ModalContent>
    </Modal>
}