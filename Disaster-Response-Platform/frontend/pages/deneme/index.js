
import MainLayout from '@/layouts/MainLayout'
import { Button, Input, useDisclosure } from '@nextui-org/react';
import { useForm } from 'react-hook-form';
import { toast } from 'react-toastify';
import { useRouter } from 'next/navigation'
import AddNeedForm from '@/components/AddNeed';

export default function deneme() {
  const router = useRouter()
  const { isOpen, onOpen, onOpenChange } = useDisclosure();
  return <>
  <Button onPress={onOpen}>İlan Oluştur</Button>
  <AddNeedForm onOpenChange={onOpenChange} isOpen={isOpen} />
  </> 
}

login.getLayout = function getLayout(page) {
  return <MainLayout>{page}</MainLayout>;
};
