
import MainLayout from '@/layouts/MainLayout'
import { Button, Input } from '@nextui-org/react';
import { useForm } from 'react-hook-form';
import { toast } from 'react-toastify';
import { useRouter } from 'next/navigation'

export default function login() {
  const router = useRouter()
  const { register, reset, handleSubmit, setError, formState: { isSubmitting, errors } } = useForm();


  const onSubmit = async (data) => {
    const  response = await fetch('/api/register', { method: 'POST',
    headers: {
      "Content-Type": "application/json",
    }, body: JSON.stringify(data) });
    
    if (response.status === 400) {
      const fieldToErrorMessage = await response.json()
      for (const [fieldName, errorMessage] of Object.entries(fieldToErrorMessage)) {
        setError(fieldName, { type: 'custom', message: errorMessage })
      }
    } else if (response.ok) {
      // successful
      toast.success("Successfully saved")
      router.push("/")
    } else {
      // unknown error
      toast.error("An unexpected error occurred while saving, please try again")
    }
  }


  const fields = [
    { type: "text", name: "username", required: true, label: "Kullanıcı adı", placeholder: "Food" },
    { type: "text", name: "first_name", required: true, label: "İsim", placeholder: "Pasta" },
    { type: "text", name: "last_name", required: true, label: "Soyadı", placeholder: "dede"  },
    { type: "password", name: "password", required: true, label: "Şifre", placeholder: "strong12345" },
    { type: "phone_number", name: "phone_number", required: true, label: "Telefon numarası", placeholder: "05321234567" },
    { type: "email", name: "email", required: true, label: "Email", placeholder: "Email" },
]
  return <form className="rounded-xl bg-gray-200 p-10 w-8/12" onSubmit={handleSubmit(onSubmit)} >
    <h1 className='text-6xl font-normal leading-normal mt-0 mb-1 text-center text-emerald-800'> Kayıt ol</h1>
          {fields.map(field => {
              return <>
                  <Input type={field.type} 
                      {...register(field.name, { required: field.required })}
                      style={{ border: 'none' }}
                      label={field.label}
                      labelPlacement={'outside'}
                      variant={'faded'}
                      className='mb-6'
                      placeholder={field.placeholder}
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
