
import MainLayout from '@/layouts/MainLayout'
import { Button, Input } from '@nextui-org/react';
import { useForm } from 'react-hook-form';

export default function login() {
  const { register, reset, handleSubmit, setError, formState: { isSubmitting, errors } } = useForm();
  const onSubmit = async (data) => {
    console.log(data)
    const { result } = await fetch('/api/register', { method: 'POST',
    headers: {
      "Content-Type": "application/json",
    }, body: JSON.stringify(data) });
    console.log(result)
  }

  const fields = [
    { type: "text", name: "username", required: true, label: "Username", placeholder: "Food" },
    { type: "text", name: "first_name", required: true, label: "First name", placeholder: "Pasta" },
    { type: "text", name: "last_name", required: true, label: "Last name"  },
    { type: "password", name: "password", required: true, label: "Password" },
    { type: "phone_number", name: "phone_number", required: true, label: "Phone Number" },
    { type: "email", name: "email", required: true, label: "Email" },
]
  return <form className="rounded-xl bg-gray-400 p-10 w-8/12" onSubmit={handleSubmit(onSubmit)} >
          {fields.map(field => {
              return <>
                  <Input type={field.type} 
                      {...register(field.name, { required: field.required })}
                      style={{ border: 'none' }}
                      label={field.label}
                      labelPlacement={'outside'}
                      variant={'bordered'}
                      className='mb-6'
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
  return <MainLayout>{page}</MainLayout>;
};
