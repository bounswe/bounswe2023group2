
import MainLayout from '@/layouts/MainLayout'
import { Button, Input } from '@nextui-org/react';
import { useRouter } from 'next/router';
import { useForm } from 'react-hook-form';
import { toast, ToastContainer } from 'react-toastify';
import { withIronSessionSsr } from 'iron-session/next';
import sessionConfig from '@/lib/sessionConfig';
import getLabels from '@/lib/getLabels';

export default function login({ labels }) {
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
      
      router.push('/');
    } else {
      // unknown error
      toast.error(response.statusText)
    }
  }
  const fields = [
    { type: "text", name: "username_or_email_or_phone", required: true, label: "telefon numarası, email ya da kullanıcı adı", placeholder: "telefon numarası, email ya da kullanıcı adı" },
    { type: "password", name: "password", required: true, label: "Şifre" , placeholder: "strong12345"},

  ]
  return <form className="rounded-xl bg-gray-200 p-6 w-8/12 center" onSubmit={handleSubmit(onSubmit)} >
      <h1 className='text-6xl font-normal leading-normal mt-0 mb-2 text-center text-emerald-800'> Giriş yap</h1>
    {fields.map(field => {
      return <>
        <Input type={field.type}
          {...register(field.name, { required: field.required })}
          style={{ border: 'none' }}
          label={field.label}
          labelPlacement={'outside'}
          variant={'faded'}
          className='mb-2'
          placeholder={field.placeholder}
          isRequired
        />
        <div className="text-center" >{errors[field.name]?.message}</div>
      </>
    })}
    <Button disabled={isSubmitting} type='submit' className='m-3 ml-0'>
      {isSubmitting ? 'Loading' : "Submit"}
    </Button>
    <ToastContainer position="bottom-center" />
  </form>;
}

login.getLayout = function getLayout(page) {
  return <MainLayout>{page}</MainLayout>;
};

export const getServerSideProps = withIronSessionSsr(
  async function getServerSideProps({ req }) {
    const labels = await getLabels(req.session.language);
    return {
      props: {
        labels
      }
    };
  },
  sessionConfig
)