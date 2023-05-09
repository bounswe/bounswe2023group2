import React, { useState, useEffect } from 'react';
import { Inter } from 'next/font/google';
import axios from 'axios';
import { initializeApp } from "firebase/app";
import { getMessaging,onMessage, getToken } from "firebase/messaging";

const inter = Inter({ subsets: ['latin'] });
let client_token=""
const firebaseConfig = {
  apiKey: "AIzaSyAN1pGfWXeLnZ77kNU7nhOoaQN0tN1BsLU",
  authDomain: "dpr-project-5e1a8.firebaseapp.com",
  projectId: "dpr-project-5e1a8",
  storageBucket: "dpr-project-5e1a8.appspot.com",
  messagingSenderId: "593061627004",
  appId: "1:593061627004:web:cd066c526ae27f8c0575f9",
  measurementId: "G-FDMHJ53604"
};



const MyButton = () => {
  const [responseData, setResponseData] = useState(null);
  const handleClick = () => {
    // Send a POST request to your API
    const app = initializeApp(firebaseConfig);
    const messaging = getMessaging();
    getToken(messaging, { vapidKey: 'BM2SKWurNY8H7fjxKLPF0Sqn9wPPr-QFz7SxgDkdNTuYPR5u-ewM7kdGyHcL5td1ze9WtvH8AFEw1msYlUFdnS4' }).then((currentToken) => {
      if (currentToken) {
        // Send the token to your server and update the UI if necessary
        //TODO 
       
        axios.get('http://127.0.0.1:8000/user/')
        console.log(currentToken)
        client_token=currentToken
       
      } else {
        // Show permission request UI
        console.log('No registration token available. Request permission to generate one.');
        // ...
      }
    }).catch((err) => {
      console.log('An error occurred while retrieving token. ', err);
      // ...
    });
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
const NotificationPermission = () => {
  useEffect(() => {
    if (typeof window !== 'undefined' && 'Notification' in window) {
      if (Notification.permission !== 'granted') {
        Notification.requestPermission().then(permission => {
          if (permission === 'granted') {
            console.log('Notification permission granted.');
          } else {
            console.log('Notification permission denied.');
          }
        });
      }
    }
  }, []);

  return null;
};

export default function Home() {
  return (
    <main className="flex min-h-screen flex-col items-center justify-between p-24">
      <div>Burasi notifications</div>
      <NotificationPermission/>
      <MyButton />
      
      

    </main>
  );
}

