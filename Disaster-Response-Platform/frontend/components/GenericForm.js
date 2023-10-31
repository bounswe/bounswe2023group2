import { FieldValues, useForm, UseFormRegister } from "react-hook-form";
import { ToastContainer, toast } from 'react-toastify';
import 'react-toastify/dist/ReactToastify.css';
import useSWR from 'swr';
import React, { useEffect } from "react";


const fetcher = (url) => fetch(url).then(r => r.json())

function GenericForm({ url, renderForm,fetchData }) {
  // Fetch our initial form data
  const { data, error } = useSWR(url, fetcher)

  const { register, reset, handleSubmit, setError, formState: { isSubmitting, errors } } = useForm();
  async function saveFormData(data, url) {
    return await fetch(url, {
      body: JSON.stringify(data),
      headers: { "Content-Type": "application/json" },
      method: "POST"
    })
  }
  // Submit handler which displays errors + success messages to the user
  const onSubmit = async (data) => {
    fetchData(data);
    console.log(data)
    const response = await saveFormData(data, url)

    if (response.status === 400) {
      // Validation error, expect response to be a JSON response {"field": "error message for that field"}
      const fieldToErrorMessage = await response.json()
      for (const [fieldName, errorMessage] of Object.entries(fieldToErrorMessage)) {
        setError(fieldName, { type: 'custom', message: errorMessage })
      }
    } else if (response.ok) {
      // successful
      toast.success("Successfully saved")
    } else {
      // unknown error
      toast.error("An unexpected error occurred while saving, please try again")
    }
  }

  // Sets the default value of the form once it's available
  useEffect(() => {
    if (data === undefined) {
      return; // loading
    }
    reset(data);
  }, [reset, data]);

  // Handle errors + loading state
  if (error) {
    return <div>An unexpected error occurred while loading, please try again</div>
  } else if (!data) {
    return <div>Loading...</div>
  }

  return <form  onSubmit={handleSubmit(onSubmit)} className='flex w-full flex-col  mb-6 md:mb-0 gap-4'  >
    {renderForm({ register, errors, isSubmitting })}
    
  </form>
  ;
}

export default GenericForm