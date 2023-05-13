import React, { useState, useEffect } from 'react';
import { Inter } from 'next/font/google';
import axios from 'axios';
import { initializeApp } from "firebase/app";
import { getMessaging,onMessage, getToken } from "firebase/messaging";

const inter = Inter({ subsets: ['latin'] });
const firebaseConfig = {
  apiKey: process.env.NEXT_PUBLIC_API_KEY,
  authDomain: process.env.NEXT_PUBLIC_AUTH_DOMAIN,
  projectId: process.env.NEXT_PUBLIC_PROJECT_ID,
  storageBucket: process.env.NEXT_PUBLIC_STORAGE_BUCKET,
  messagingSenderId: process.env.NEXT_PUBLIC_MESSAGING_SENDER_ID,
  appId: process.env.NEXT_PUBLIC_APP_ID,
  measurementId: process.env.NEXT_PUBLIC_MEASUREMENT_ID
};

const firebaseApp = initializeApp(firebaseConfig);

export default function Home() {
  const [result, setResult] = useState(null);
  const handleSubmit = async (event) => {
    event.preventDefault();
    const messaging = getMessaging(firebaseApp);
      onMessage(messaging, (payload) => {
        console.log('Message received. ', payload);
        new Notification(payload.notification?.title);
      });
      try {
        const notificationData = {
            body: event.target.topic.value+" Need in Istanbul",
            topic: event.target.topic.value,
            title: "New activity in your subscribed topic: Need-"+ event.target.topic.value
        };

        const response = await axios.post(`${process.env.NEXT_PUBLIC_BACKEND_URL}/notifications/send_notification`, JSON.stringify(notificationData), {

          headers: {
            'content-type': 'application/json'
          }
        });
        console.log(response);
        setResult("Created a need for: " +event.target.topic.value)

      } catch (error) {
        console.error(error);
        setResult(error)
      }
  };
  const handleSubscribe = async (event, type, token) => { 
    event.preventDefault();
    try {
      console.log( "sendind subs",token, event.target.topic.value)
      const subscriptionData = {
          token: token,
          topic: event.target.topic.value   
      };
      console.log("subscribe:"+subscriptionData.token, subscriptionData.topic)
      if(type==='Subscribe'){

        
        const response = await axios.post(`${process.env.NEXT_PUBLIC_BACKEND_URL}/notifications/subscriptions`, JSON.stringify(subscriptionData), {

        headers: {
          'content-type': 'application/json'
        }
        });
        console.log(response);
        setResult("Subscribed successfully to "+ event.target.topic.value+" :)")
      }else{

        const response = await axios.post(`${process.env.NEXT_PUBLIC_BACKEND_URL}/notifications/unsubscribe`, JSON.stringify(subscriptionData), {

          headers: {
            'content-type': 'application/json'
          }
        });
        console.log(response);
        setResult("Unsubscribed from "+ event.target.topic.value+" :(")
      }
      getSubscriptions()
    } catch (error) {
      console.error(error);
      setResult("Failed to subscribe")
    }  
  };
  const getSubscriptions=async ()=>{
    try {
      if(!token) return
      const response = await axios.get(`${process.env.NEXT_PUBLIC_BACKEND_URL}/notifications/subscriptions/${token}`, {
      headers: {
        'content-type': 'application/json'
      }
      });
      console.log(response);
      response.data
      setSubscriptions(response.data)
      
    } catch (error) {
      console.error(error);
      
    } 
    
  }
  const [token, setToken] = useState(null);
  const [subscriptions, setSubscriptions] = useState([]);
  useEffect(()=>{
    getSubscriptions()
    console.log("token has changed");
    
  },[token])
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
    const firebaseConfig = {
      apiKey: process.env.NEXT_PUBLIC_API_KEY,
      authDomain: process.env.NEXT_PUBLIC_AUTH_DOMAIN,
      projectId: process.env.NEXT_PUBLIC_PROJECT_ID,
      storageBucket: process.env.NEXT_PUBLIC_STORAGE_BUCKET,
      messagingSenderId: process.env.NEXT_PUBLIC_MESSAGING_SENDER_ID,
      appId: process.env.NEXT_PUBLIC_APP_ID,
      measurementId: process.env.NEXT_PUBLIC_MEASUREMENT_ID
    };
    const firebaseApp = initializeApp(firebaseConfig);
    const messaging = getMessaging(firebaseApp);

    getToken(messaging, { vapidKey: process.env.NEXT_PUBLIC_VAPID_KEY }).then((currentToken) => {
      if (currentToken) {
        console.log( "token:",currentToken);
        setToken(currentToken)
      } else {
        console.log('No registration token available. Request permission to generate one.');
      }
    }).catch((err) => {
      console.log('An error occurred while retrieving token. ', err);
    });

    return () => {
 
    };
  }, []);
  return (
    <main className="flex min-h-screen flex-col items-center justify-center p-24">
      {result && (
        <div className="mb-8"> 
          <div className="inline-block relative w-64 mr-10">
            <pre>{JSON.stringify(result, null, 2)}</pre>
          </div>
        </div>
      )}
      <div className="flex  flex-row items-center justify-between ">
        <form onSubmit={ (e) => {
          handleSubmit(e)
        }}>
          <div className="inline-block relative w-64 mr-10">
            <select id='topic' className="block appearance-none w-full border border-gray-400 text-black hover:border-gray-500 px-4 py-2 pr-8 rounded leading-tight focus:outline-none focus:shadow-outline">
              <option value="Food">Food</option>
              <option value="Clothes">Clothes</option>
              <option value="Driver">Driver</option>
            </select>
            <div className="pointer-events-none absolute inset-y-0 right-0 flex items-center px-2 text-gray-700">
              <svg className="fill-current h-4 w-4" xmlns="http://www.w3.org/2000/svg" viewBox="0 0 20 20"><path d="M9.293 12.95l.707.707L15.657 8l-1.414-1.414L10 10.828 5.757 6.586 4.343 8z" /></svg>
            </div>
          </div>
          <button type='submit' className="bg-blue-500 hover:bg-blue-700 text-white font-bold py-2 px-4 rounded "> Create Need</button>
        </form>

        <form onSubmit={ (e) => {
          handleSubscribe(e, "Subscribe", token)
        }}>
          <div className="inline-block relative w-64 mr-10">
            <select id='topic' className="block appearance-none w-full border border-gray-400 text-black hover:border-gray-500 px-4 py-2 pr-8 rounded leading-tight focus:outline-none focus:shadow-outline">
              <option value="Food">Food</option>
              <option value="Clothes">Clothes</option>
              <option value="Driver">Driver</option>
            </select>
            <div className="pointer-events-none absolute inset-y-0 right-0 flex items-center px-2 text-gray-700">
              <svg className="fill-current h-4 w-4" xmlns="http://www.w3.org/2000/svg" viewBox="0 0 20 20"><path d="M9.293 12.95l.707.707L15.657 8l-1.414-1.414L10 10.828 5.757 6.586 4.343 8z" /></svg>
            </div>
          </div>
          <button type='submit' className="bg-blue-500 hover:bg-blue-700 text-white font-bold py-2 px-4 rounded "> Subscribe Topic</button>        
        </form>
        <form onSubmit={ (e) => {
          handleSubscribe(e, "Unsubscribe", token)
        }}>
          <div className="inline-block relative w-64 mr-10">
            <select id='topic' className="block appearance-none w-full border border-gray-400 text-black hover:border-gray-500 px-4 py-2 pr-8 rounded leading-tight focus:outline-none focus:shadow-outline">
              <option value="Food">Food</option>
              <option value="Clothes">Clothes</option>
              <option value="Driver">Driver</option>
            </select>
            <div className="pointer-events-none absolute inset-y-0 right-0 flex items-center px-2 text-gray-700">
              <svg className="fill-current h-4 w-4" xmlns="http://www.w3.org/2000/svg" viewBox="0 0 20 20"><path d="M9.293 12.95l.707.707L15.657 8l-1.414-1.414L10 10.828 5.757 6.586 4.343 8z" /></svg>
            </div>
          </div>
          <button type='submit' className="bg-blue-500 hover:bg-blue-700 text-white font-bold py-2 px-4 rounded "> Unsubscribe Topic</button>
        </form>

      </div>
      
      <div>
      <h1 className="text-2xl font-bold mt-8">Subscriptions</h1>
      <ul className="list-disc">
        {subscriptions.map((subscription, index) => (
          <li key={index} className="mb-2">{subscription}</li>
        ))}
      </ul>
    </div>

    </main>
  );
}

