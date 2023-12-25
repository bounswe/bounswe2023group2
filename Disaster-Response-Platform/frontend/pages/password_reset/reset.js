
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
import { useState } from 'react';
export default function login({ labels }) {
  const { register, handleSubmit, watch, formState: { isSubmitting, errors } } = useForm();
  const router = useRouter()
  const [verified, setVerified] = useState(false)

  const onSubmit = async (data) => {
    if(!verified){
      const response = await api.get(`/api/forgot_password/reset_password?email=${data.email}&token=${data.token}`, {});
      if (response.status ===200) {
        // successful
        toast.success('Verification token is correct')
        setVerified(true)
      } else {
        // unknown error
        toast.error(`${labels.feedback.failure} (${response.statusText})`)
      }
    }
    else{

      const response = await api.post(`/api/forgot_password/reset?email=${data.email}&token=${data.token}`, {new_password:data.new_password});
      if (response.status ===200) {
        // successful
        toast.success('Password has been changed')
        router.push('/login')
      } else {
        // unknown error
        toast.error(`${labels.feedback.failure} (${response.statusText})`)
      }

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
        <Input type={'text'}
          {...register('token', { required: true })}
          style={{ border: 'none' }}
          label={'enter the verification token'}
          placeholder={'123456'}
          labelPlacement={'outside'}
          variant={'faded'}
          className='w-80 mt-4'
          isRequired
        />
         {verified && <>
         <Input type={'text'}
          {...register('password', { required: true })}
          style={{ border: 'none' }}
          label={'enter your new password'}
          placeholder={'strong12345'}
          labelPlacement={'outside'}
          variant={'faded'}
          className='w-80 mt-4'
          isRequired
          />
           <Input type={'text'}
          {...register('new_password', { required: true, validate: (val) => {
            if (watch('password') != val) {
              return "Your passwords do no match";
            }
          },
         })}
          style={{ border: 'none' }}
          label={'confirm your new password'}
          placeholder={'strong12345'}
          labelPlacement={'outside'}
          variant={'faded'}
          className='w-80 mt-4'
          isRequired
          />
           <p className='error'>{errors.new_password && errors.new_password.message}</p>
          </> 
          }
        <div className="text-center" >{errors['email']?.message}</div>
    <Button disabled={isSubmitting} color='primary' type='submit' className='m-3 self-end mr-0 w-10'>
      {isSubmitting ? labels.UI.loading : labels.UI.submit}
    </Button>
    <ToastContainer />
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