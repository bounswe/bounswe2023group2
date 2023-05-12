import React, { useState, useEffect } from 'react';
import axios from 'axios';
import { useRouter } from "next/router";

export default function dummyHome() {
  const [username, getUsername] = useState(null);
  const handleSubmit = async (event) => {
    event.preventDefault();
    const data = {
      username: event.target.username,
    };
    const headers = {
      'Content-Type': 'application/json',
    };
    const response = await axios.get(process.env.NEXT_PUBLIC_FRONTEND_URL+"/dummyhome?username="+data.username,headers);
    const result = await JSON.stringify(response.data);
    console.log(result)
    try {
      const api_result = JSON.parse(result) //I parse the result to arrange it
      getUsername(api_result.username) //get username from result
    } catch (error) {
      
    }
  };


  return (
    <main className="flex min-h-screen flex-col items-center justify-between p-24">
    <form onSubmit={handleSubmit} className="space-y-4 md:space-y-6" action="#">
      <div> Welcome to our system, you logged in succesfully! </div>
      <button type='submit' className="bg-red-500 hover:bg-blue-400 text-white font-bold py-2 px-4 border-b-4 border-blue-700 hover:border-blue-500 rounded">
        <a href= {process.env.NEXT_PUBLIC_FRONTEND_URL+"/login"}> Logout </a>    
        </button>
        </form>
    </main>
  )
}
