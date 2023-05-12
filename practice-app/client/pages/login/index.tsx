import React, { useState, useEffect } from 'react';
import axios from 'axios';
import { useRouter } from "next/router";

export default function Home() {
    const [credential_not_ok, getPW] = useState(null);
    const [username, getUsername] = useState(null);
    const router = useRouter(); //this is a router to direct another page

    const handleSubmit = async (event) => {
        event.preventDefault();
        console.log(event.target)
        const data = {
          username: event.target.username.value,
          password: event.target.password.value,
        };
        const headers = {
          'Content-Type': 'application/json',
        };
        const response = await axios.post(process.env.NEXT_PUBLIC_FRONTEND_URL+"/login",data,headers);
        const result = await JSON.stringify(response.data);
        console.log(result)
        try {
            const api_result = JSON.parse(result) //I parse the result to arrange it
            getPW(api_result.credentials_not_ok) // setData method which initializes the credential_ok from api's response
            getUsername(api_result.username) //get username from result
          } catch (error) {
            
          }
          if (!credential_not_ok) {
            try {
            router.push(process.env.NEXT_PUBLIC_FRONTEND_URL+"/dummyhome?username="+JSON.parse(result).username); // Navigate to another page
            } catch (error) {
                
            }
          } else {
            // Do something else if the state is not OK
          }
      };

    return (
        <section  className="bg-gray-50 dark:bg-gray-900" >
            <div className="flex flex-col items-center justify-center px-6 py-8 mx-auto md:h-screen lg:py-0">
                <div className="w-full bg-white rounded-lg shadow dark:border md:mt-0 sm:max-w-md xl:p-0 dark:bg-gray-800 dark:border-gray-700">
                    <div className="p-6 space-y-4 md:space-y-6 sm:p-8">
                        <h1 className="text-xl font-bold leading-tight tracking-tight text-gray-900 md:text-2xl dark:text-white">
                            Sign in to your DARP account
                        </h1>
                        <form onSubmit={handleSubmit} className="space-y-4 md:space-y-6" action="#">
                        <p className="text-red-500 text-xs italic">{credential_not_ok ? <>Your credentials are incorrect, please try again!</> : <> </>  } </p>
                            <div>
                                <label htmlFor="username" className="block mb-2 text-sm font-medium text-gray-900 dark:text-white">Your username</label>
                                <input type="username" name="username" id="username" className="bg-gray-50 border border-gray-300 text-gray-900 sm:text-sm rounded-lg focus:ring-primary-600 focus:border-primary-600 block w-full p-2.5 dark:bg-gray-700 dark:border-gray-600 dark:placeholder-gray-400 dark:text-white dark:focus:ring-blue-500 dark:focus:border-blue-500" placeholder="johndoe" />
                            </div>
                            <div>
                                <label htmlFor="password" className="block mb-2 text-sm font-medium text-gray-900 dark:text-white">Password</label>
                                <input type="password" name="password" id="password" placeholder="••••••••" className="bg-gray-50 border border-gray-300 text-gray-900 sm:text-sm rounded-lg focus:ring-primary-600 focus:border-primary-600 block w-full p-2.5 dark:bg-gray-700 dark:border-gray-600 dark:placeholder-gray-400 dark:text-white dark:focus:ring-blue-500 dark:focus:border-blue-500" />
                            </div>
                            <div className="flex items-center justify-between">
                                <div className="flex items-start">
                                    <div className="flex items-center h-5">
                                    <input id="remember" aria-describedby="remember" type="checkbox" className="w-4 h-4 border border-gray-300 rounded bg-gray-50 focus:ring-3 focus:ring-primary-300 dark:bg-gray-700 dark:border-gray-600 dark:focus:ring-primary-600 dark:ring-offset-gray-800" />
                                    </div>
                                    <div className="ml-3 text-sm">
                                    <label htmlFor="remember" className="text-gray-500 dark:text-gray-300">Remember me</label>
                                    </div>
                                </div>
                                <a href="#" className="text-sm font-medium text-primary-600 hover:underline dark:text-primary-500">Forgot password?</a>
                            </div>
                            <div>
                                <button type="submit" className="w-full text-white font-bold text-lg bg-blue-500 hover:bg-primary-700 focus:ring-4 focus:outline-none focus:ring-primary-300 font-medium rounded-lg text-sm px-5 py-2.5 text-center dark:bg-primary-600 dark:hover:bg-primary-700 dark:focus:ring-primary-800">
                                    Login
                                </button>
                            </div>
                            <p className="text-sm font-light text-gray-500 dark:text-gray-400">
                                Don’t have an account yet? <a href={process.env.NEXT_PUBLIC_FRONTEND_URL+"/registration"} className="font-medium text-primary-600 hover:underline dark:text-primary-500">Sign up</a>
                            </p>
                        </form>
                    </div>
                </div>
            </div>
        </section>
    )
}

