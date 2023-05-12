import React, { useState, useEffect } from 'react';
import axios from 'axios';


export default function Home() {
    const [existed, setData] = useState(null); // this is a livedata to return after api's response and change in frontend
    //it connects existed with setData method, in setData we need to get response from the API and existed will be shown with respect to this response
    const [pw_not_ok, getPW] = useState(null);
    const [phone_not_ok, getPhone] = useState(null);
    const [registered, getRegistered] = useState(null);

    const handleSubmit = async (event) => {
        event.preventDefault();
        console.log(event.target)
        const data = {
          username: event.target.username.value,
          first_name: event.target.first_name.value,
          last_name: event.target.last_name.value,
          email: event.target.email.value,
          phone_number: event.target.phone_number.value,
          password: event.target.password.value,
        };
        const headers = {
          'Content-Type': 'application/json',
        };
        const response = await axios.post(process.env.NEXT_PUBLIC_BACKEND_URL+"/registration",data,headers); //this is the place where we pass the data to api
        const result = await JSON.stringify(response.data);
        console.log(result)
        try {
          const api_result = JSON.parse(result) //I parse the result to arrange it
          setData(api_result.is_exist) // setData method which initializes the user_name from api's response
          getPW(api_result.pw_not_ok) // setData method which initializes the pw_ok from api's response
          getRegistered(api_result.registered)
          getPhone(api_result.phone_not_turkey)
        } catch (error) {
          
        }   
      };
  return (
    <form onSubmit={handleSubmit} className="flex min-h-screen flex-col items-center justify-between p-24">
      <p className="text-red-500 text-xs italic">{(existed||pw_not_ok||phone_not_ok) ?  <>You are not registered please fill the required blanks with respect to warnings</> :  <></> }
      </p>
      <p className="text-red-500 text-xs italic">{!(existed||pw_not_ok||phone_not_ok)&&registered ?  <>You are registered succesfull, please go to Login Page and enter the system !</> :  <></> }
      </p>
  <div className="flex flex-wrap -mx-3 mb-4 justify-content">
    <div className="w-full">
      <label className="block uppercase tracking-wide text-gray-700 text-xs font-bold mb-2" htmlFor="grid-first-name">
        First Name
      </label>
      <input id="first_name" className="appearance-none block w-full bg-gray-200 text-gray-700 border border-red-500 rounded py-3 px-4 mb-3 leading-tight focus:outline-none focus:bg-white" type="text" placeholder="John"/>
    </div>
    <div className="w-full">
      <label className="block uppercase tracking-wide text-gray-700 text-xs font-bold mb-2" htmlFor="grid-last-name">
        Last Name
      </label>
      <input id="last_name" className="appearance-none block w-full bg-gray-200 text-gray-700 border border-gray-200 rounded py-3 px-4 leading-tight focus:outline-none focus:bg-white focus:border-gray-500" type="text" placeholder="Doe"/>
    </div>
    <div className="w-full">
      <label className="block uppercase tracking-wide text-gray-700 text-xs font-bold mb-2" htmlFor="grid-last-name">
        Username
        <p className="text-red-500 text-xs italic">{existed ?  <>Your username is already existed please change it!</> : <></> }
      </p>
      </label>
      <input id="username" className="appearance-none block w-full bg-gray-200 text-gray-700 border border-gray-200 rounded py-3 px-4 leading-tight focus:outline-none focus:bg-white focus:border-gray-500" type="text" placeholder="johndoe"/>
    </div>
    <div className="w-full">
      <label className="block uppercase tracking-wide text-gray-700 text-xs font-bold mb-2" htmlFor="grid-last-name">
        Email (Optional)
      </label>
      <input id="email" className="appearance-none block w-full bg-gray-200 text-gray-700 border border-gray-200 rounded py-3 px-4 leading-tight focus:outline-none focus:bg-white focus:border-gray-500" type="text" placeholder="johndoe@gmail.com"/>
    </div>
    <div className="w-full">
      <label className="block uppercase tracking-wide text-gray-700 text-xs font-bold mb-2" htmlFor="grid-last-name">
        Phone Number
        <p className="text-red-500 text-xs italic">{phone_not_ok ?  <>Your phone number isn't from Turkey please write it as 05********* !</> : <></> } </p>
      </label>
      <input id="phone_number" className="appearance-none block w-full bg-gray-200 text-gray-700 border border-gray-200 rounded py-3 px-4 leading-tight focus:outline-none focus:bg-white focus:border-gray-500" type="text" placeholder="05123456789"/>
    </div>
    <div className="w-full">
      <label className="block uppercase tracking-wide text-gray-700 text-xs font-bold mb-2" htmlFor="grid-password">
        Password
        <p className="text-red-500 text-xs italic">{pw_not_ok ? <>your password must contain at least 8 characters!</> :  <></>}</p>
      </label>
      <input id="password" className="appearance-none block w-full bg-gray-200 text-gray-700 border border-gray-200 rounded py-3 px-4 mb-3 leading-tight focus:outline-none focus:bg-white focus:border-gray-500" type="password" placeholder="******************"/>
    </div>
        <button type='submit' className="bg-blue-500 hover:bg-blue-400 text-white font-bold py-2 px-4 border-b-4 border-blue-700 hover:border-blue-500 rounded">
        <a> Register </a>    
        </button>
    </div>
    <p className="text-sm font-light text-gray-500 dark:text-gray-400">
        Already have an account? <a href= {process.env.NEXT_PUBLIC_FRONTEND_URL+"/login"} className="font-medium text-blue hover:underline dark:text-primary-500">  Log in</a>
    </p>
</form>
  )
}
