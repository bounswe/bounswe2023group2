
import MainLayout from '@/layouts/MainLayout'
import { Button, Input } from '@nextui-org/react';
import { useForm } from 'react-hook-form';
import { toast } from 'react-toastify';
import { useRouter } from 'next/navigation'
import { withIronSessionSsr } from 'iron-session/next';
import sessionConfig from '@/lib/sessionConfig';
import getLabels from '@/lib/getLabels';

export default function register({ labels }) {
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
      toast.success(labels.feedback.save_success);
      router.push("/")
    } else {
      // unknown error
      toast.error(labels.feedback.failure);
    }
  }


  const fields = [
    { type: "text", name: "username", required: true },
    { type: "text", name: "first_name", required: true },
    { type: "text", name: "last_name", required: true  },
    { type: "password", name: "password", required: true },
    { type: "phone_number", name: "phone_number", required: true },
    { type: "email", name: "email", required: true },
]
  return <form className="rounded-xl bg-gray-200 p-10 w-8/12" onSubmit={handleSubmit(onSubmit)} >
    <h1 className='text-6xl font-normal leading-normal mt-0 mb-1 text-center text-emerald-800'>{labels.auth.register}</h1>
          {fields.map(field => {
              return <>
                  <Input type={field.type} 
                      {...register(field.name, { required: field.required })}
                      style={{ border: 'none' }}
                      label={labels.profile[field.name]}
                      labelPlacement={'outside'}
                      variant={'faded'}
                      className='mb-6'
                      placeholder={labels.placeholders[field.name] ? labels.placeholders[field.name] : " "}
                  />
                  <div className="error" >{errors[field.name]?.message}</div>
              </>
          })}
          <Button disabled={isSubmitting} type='submit'>
              {isSubmitting ? labels.UI.loading : labels.UI.submit}
          </Button>
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