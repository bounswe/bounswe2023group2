import React, { useState } from 'react';
import { Inter } from 'next/font/google';
import axios from 'axios';

const inter = Inter({ subsets: ['latin'] });

const MyButton = () => {
  const [responseData, setResponseData] = useState(null);
  const handleClick = () => {
    // Send a POST request to your API
    axios.get('http://127.0.0.1:8000/user/')
    .then(response => {
      // Handle the response from the API
      console.log('API response:', response.data);
      setResponseData(response.data);
    })
    .catch(error => {
      // Handle any error that occurred during the request
      console.error('Error:', error);
    });
  };

  return (
    <div>
      <button onClick={handleClick}>Click Me</button>
      {responseData && (
        <div>
          {responseData[0].username}
        </div>
      )}
    </div>
  );
};

export default function Home() {
  return (
    <main className="flex min-h-screen flex-col items-center justify-between p-24">
      <div>Burasi notifications</div>
      <MyButton />
    </main>
  );
}
