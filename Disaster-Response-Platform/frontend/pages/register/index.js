
import MainLayout from '@/layouts/MainLayout'
import { Button, Input } from '@nextui-org/react';
import { useForm } from 'react-hook-form';
import { toast, ToastContainer } from 'react-toastify';
import { useRouter } from 'next/navigation'
import { withIronSessionSsr } from 'iron-session/next';
import sessionConfig from '@/lib/sessionConfig';
import getLabels from '@/lib/getLabels';
import Image from 'next/image';
import dapp_logo_extended_white from "../../public/logo/dapp_logo_main_blue.svg";
export default function register({ labels }) {
  const router = useRouter()
  const { register, reset, handleSubmit, setError, formState: { isSubmitting, errors } } = useForm();


  const onSubmit = async (data) => {
    console.log(data)
    const  response = await fetch('/api/register', { method: 'POST',
    headers: {
      "Content-Type": "application/json",
    }, body: JSON.stringify(data) });
    console.log(response)
   if (response.status == 200) {
      // successful
      toast.success(labels.feedback.save_success);
      router.push("/")
    } else {
      // unknown error
      toast.error(`${labels.feedback.failure} ${response?.data?.ErrorDetail}`);
    }
  }
  const fields = [
    { type: "text", name: "username", required: true },
    { type: "text", name: "first_name", required: true },
    { type: "text", name: "last_name", required: true  },
    { type: "password", name: "password", required: true },
    { type: "phone_number", name: "phone_number", required: false },
    { type: "email", name: "email", required: false },
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
                      label={labels.profile[field.name]}
                      labelPlacement={'outside'}
                      variant={'faded'}
                      className='w-80 mt-4'
                      isRequired = {field.required}
                      placeholder={labels.placeholders[field.name] ? labels.placeholders[field.name] : " "}
                  />
                  <div className="error" >{errors[field.name]?.message}</div>
                  {field.name == 'password' && <span  className='mt-3 text-slate-400' > {labels.auth.at_least_one} </span> }
              </>
          })}
          <Button disabled={isSubmitting} color='primary' type='submit' className='m-3 self-end mr-0 w-10'>
              {isSubmitting ? labels.UI.loading : labels.UI.submit}
          </Button>
          <ToastContainer/>
      </form>;
}

register.getLayout = function getLayout(page) {
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