
import MainLayout from '@/layouts/MainLayout'
import { Button, Input } from '@nextui-org/react';
import { useRouter } from 'next/router';
import { useForm } from 'react-hook-form';
import { toast, ToastContainer } from 'react-toastify';
import { withIronSessionSsr } from 'iron-session/next';
import sessionConfig from '@/lib/sessionConfig';
import getLabels from '@/lib/getLabels';
import dapp_logo_extended_white from "../../public/logo/dapp_logo_main_blue.svg";
import Image from 'next/image';
import Link from 'next/link';
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
      toast.success(labels.feedback.save_success)
      // Usage!
      router.push('/');
    } else {
      // unknown error
      toast.error(`${labels.feedback.failure} (${response.statusText})`)
    }
  }
  const fields = [
    { type: "text", name: "username_or_email_or_phone", required: true, label: labels.auth.phone_email_username},
    { type: "password", name: "password", required: true, label: labels.profile.password},

  ]
  return <form className='flex flex-col border-2 m-5 p-10 ' onSubmit={handleSubmit(onSubmit)} >
    <Image
              src={dapp_logo_extended_white}
              alt="Logo"
              width={150}
              className='self-center'
            />
    {fields.map(field => {
      return <>
        <Input type={field.type}
          {...register(field.name, { required: field.required })}
          style={{ border: 'none' }}
          label={field.label}
          placeholder={field.label}
          labelPlacement={'outside'}
          variant={'faded'}
          className='w-80 mt-4'
          isRequired
        />
        <div className="text-center" >{errors[field.name]?.message}</div>
      </>
    })}
     <Link className='self-end mr-0 mt-1 underline text-sky-600 text-xs ' href={`/password_reset`}> {labels.UI.forgot}</Link>
    <Button disabled={isSubmitting} color='primary' type='submit' className='m-3 self-end mr-0 w-10'>
      {isSubmitting ? labels.UI.loading : labels.UI.submit}
    </Button>
   
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