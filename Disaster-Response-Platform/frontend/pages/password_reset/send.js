
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
import { api } from '@/lib/apiUtils';
export default function login({ labels }) {
  const { register, handleSubmit, formState: { isSubmitting, errors } } = useForm();
  const router = useRouter()

  const onSubmit = async (data) => {
    //TODO backend implementation
    const response = await api.post(`/api/forgot_password/send?email=${data.email}`, {});
    if (response.status ===200) {
      // successful
      toast.success(labels.feedback.save_success)
      router.push({ pathname: '/password_reset/reset', query: { email: data.email } })
    } else {
      // unknown error
      toast.error(`${labels.feedback.failure} (${response.statusText})`)
    }
  }

  return <form className='flex flex-col border-2 m-5 p-10 ' onSubmit={handleSubmit(onSubmit)} >
    <Image
              src={dapp_logo_extended_white}
              alt="Logo"
              width={150}
              className='self-center'
            />
    
        <Input type={'text'}
          {...register('email', { required: true })}
          style={{ border: 'none' }}
          label={'enter your email'}
          placeholder={'123@example.com'}
          labelPlacement={'outside'}
          variant={'faded'}
          className='w-80 mt-4'
          isRequired
        />
        <div className="text-center" >{errors['email']?.message}</div>
    <Button disabled={isSubmitting} color='primary' type='submit' className='m-3 self-end mr-0 w-10'>
      {isSubmitting ? labels.UI.loading : labels.UI.submit}
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