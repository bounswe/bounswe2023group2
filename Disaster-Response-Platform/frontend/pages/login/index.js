
import MainLayout from '@/layouts/MainLayout'
import { Button, Input } from '@nextui-org/react';
import { useRouter } from 'next/router';
import { useForm } from 'react-hook-form';
import { toast, ToastContainer } from 'react-toastify';

export default function login() {
  const { register, reset, handleSubmit, setError, formState: { isSubmitting, errors } } = useForm();
  const router = useRouter()

  const onSubmit = async (data) => {
    const response = await fetch('/api/login', {
      method: 'POST',
      headers: {
        "Content-Type": "application/json",
      }, body: JSON.stringify(data)
    });
    if (response.ok) {
      // successful
      toast.success("Successfully saved")
      // Usage!
      
      router.push('/profile');
    } else {
      // unknown error
      toast.error(response.statusText)
    }
  }
  const fields = [
    { type: "text", name: "username_or_email_or_phone", required: true, label: "username, email or phone", },
    { type: "password", name: "password", required: true, label: "Password" },

  ]
  return <form className="rounded-xl bg-gray-400 p-4 w-8/12" onSubmit={handleSubmit(onSubmit)} >

    {fields.map(field => {
      return <>
        <Input type={field.type}
          {...register(field.name, { required: field.required })}
          style={{ border: 'none' }}
          label={field.label}
          labelPlacement={'outside'}
          variant={'bordered'}
          className='mb-2'
        />
        <div className="error" >{errors[field.name]?.message}</div>
      </>
    })}
    <Button disabled={isSubmitting} type='submit'>
      {isSubmitting ? 'Loading' : "Submit"}
    </Button>
  </form>;
}


login.getLayout = function getLayout(page) {
  return <MainLayout>{page}   <ToastContainer position="bottom-center" /></MainLayout>;
};
