
import MainLayout from '@/layouts/MainLayout'
import { Button, Input, useDisclosure } from '@nextui-org/react';
import { useForm } from 'react-hook-form';
import { toast } from 'react-toastify';
import { useRouter } from 'next/navigation'

import { withIronSessionSsr } from 'iron-session/next';
import sessionConfig from '@/lib/sessionConfig';
import getLabels from '@/lib/getLabels';
import addressService from '@/services/addressService';
import { useEffect, useState } from 'react';
import recurrenceService from '@/services/recurrenceService';
export default function deneme() {
  const router = useRouter()
  const [list, setList] = useState([])

  const { register, reset, handleSubmit, setError, formState: { isSubmitting, errors } } = useForm();
  const getRecurrences = async (data) => {
    const response = await recurrenceService.list();
    console.log(response)
    if (response.status=== 200) {
      setList(response.payload?.recurrences)
      // successful
      
      toast.success('Success')
      // Usage!
 
    } else {
      // unknown error
      toast.error('Error')
    }
  }
  const start = async (data) => {
    const response = await recurrenceService.start(data._id);
    console.log(response)
    if (response.status=== 200) {
      // successful
      
      toast.success('Success')
      // Usage!
 
    } else {
      // unknown error
      toast.error('Error')
    }
  }
  useEffect(() => {
    getRecurrences();
  } , [])
  const attach = async (data) => {
    const response = await fetch('/api/recurrence/attach', {
      method: 'POST',
      body: JSON.stringify(data)
    });
    console.log(response)
    if (response.status=== 200) {
      // successful
      
      toast.success('Success')
      // Usage!
 
    } else {
      // unknown error
      toast.error('Error')
    }
  }
  return <>
  <form onSubmit={handleSubmit(attach)}>
    <Input {...register('activity_id')} />
    <Input {...register('recurrence_id')} />
    <Input {...register('activity_type')} />

    <Button type='submit'>Submit</Button>
  </form>
  <form onSubmit={handleSubmit(start)}>
    <Input {...register('_id')} />
    <Button type='submit'>Submit</Button>
  </form>

{list.map((item) => {
  return <div key={item._id}>
    <p>{item.type}</p>
    <p>{item.location}</p>
    <p>{item.description}</p>
    <p>{item.recurrence_rate}</p>
    <p>{item.created_by}</p>
    <p>{item.created_at}</p>
    <p>{item._id}</p>
  </div>
})}
  </>
 
}
deneme.getLayout = function getLayout(page) {
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
