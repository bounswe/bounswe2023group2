import React, { useState, useEffect } from 'react';
import { Inter } from 'next/font/google';
import axios from 'axios';
import { initializeApp } from "firebase/app";
import { getMessaging,onMessage, getToken } from "firebase/messaging";
import { subscribe } from 'diagnostics_channel';


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
// const CreateEvent = ({text}) => {
//   const [responseData, setResponseData] = useState(null);
//   const handleClick = () => {
//     // Send a POST request to your API
//     const messaging = getMessaging(firebaseApp);
//     onMessage(messaging, (payload) => {
//       console.log('Message received. ', payload);
//       // ...
//     });
    
//     sendNotification(text)
//     .then(response => {
//       // Handle the response from the API
//       console.log('API response:', response);
//     })
//     .catch(error => {
//       // Handle any error that occurred during the request
//       console.error('Error:', error);
//     });
//   }; 
//   return (
//     <div>
//       <button onClick={handleClick}>{text}</button>
//       {responseData && (
//         <div>
//           {responseData[0].username}
//         </div>
//       )}
//     </div>
//   );
// };


// const SubscriptionButton = ({text, clientToken, functionality}) => {

//   const handleClick = () => {
//     if(functionality === 'subscribe'){
//       console.log("subscribe",clientToken)
//       subscribeTopic(clientToken, text)
//     }else{
//       unsubscribeTopic(clientToken, text)
//     }
//   }; 
//   return (
//     <div>
//       <button  className='bg-blue-500 hover:bg-blue-700 text-white font-bold py-2 px-4 rounded-full' onClick={handleClick}>{text}</button>
      
//     </div>
//   );
// };



// const sendNotification = async (topic: String) => {
//   try {
//     const notificationData = {
//         body: topic+" Need in Istanbul",
//         topic: topic,
//         title: "New activity in your subscribed topic: Need-"+ topic
//         //link: "https://ef7c-193-140-194-56.ngrok-free.app/notifications"
//     };
//     console.log("hey");
//     const response = await axios.post('http://127.0.0.1:8000/notifications/create_event', JSON.stringify(notificationData), {
//       headers: {
//         'content-type': 'application/json'
//       }
//     });
//     console.log(response);
//   } catch (error) {
//     console.error(error);
//   }
// };

// const subscribeTopic = async (token: String, topic: String) => {
//   console.log("hey hey you you"+topic)
//   try {
//     console.log( "sendind subs",token, topic)
//     const subscriptionData = {
//         token: token,
//         topic: topic
        
//     };
//     console.log("subscribe:"+subscriptionData.token, subscriptionData.topic)
//     const response = await axios.post('http://127.0.0.1:8000/notifications/subscriptions', JSON.stringify(subscriptionData), {
//       headers: {
//         'content-type': 'application/json'
//       }
//     });
//     console.log(response);
//   } catch (error) {
//     console.error(error);
//   }
// };

// const unsubscribeTopic = async (token: String, topic: String) => {
//   try {
//     console.log( "Unsubscribing",token, topic)
//     const subscriptionData = {
//         token: token,
//         topic: topic
        
//     };
//     console.log(subscriptionData.token, subscriptionData.topic)
//     const response = await axios.post('http://127.0.0.1:8000/notifications/unsubscribe', JSON.stringify(subscriptionData), {
//       headers: {
//         'content-type': 'application/json'
//       }
//     });
//     console.log(response);
//   } catch (error) {
//     console.error(error);
//   }
// };



export default function Home() {
  const [result, setResult] = useState(null);
  
  const handleSubmit = async (event) => {
      
    event.preventDefault();
    const messaging = getMessaging(firebaseApp);
      onMessage(messaging, (payload) => {
        console.log('Message received. ', payload);
        // ...
      });
      try {
        const notificationData = {
            body: event.target.topic.value+" Need in Istanbul",
            topic: event.target.topic.value,
            title: "New activity in your subscribed topic: Need-"+ event.target.topic.value
            
        };
        
        const response = await axios.post('http://127.0.0.1:8000/notifications/create_event', JSON.stringify(notificationData), {
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
        const response = await axios.post('http://127.0.0.1:8000/notifications/subscriptions', JSON.stringify(subscriptionData), {
        headers: {
          'content-type': 'application/json'
        }
        });
        console.log(response);
        setResult("Subscribed successfully to"+ event.target.topic.value+":)")
      }else{
        const response = await axios.post('http://127.0.0.1:8000/notifications/unsubscribe', JSON.stringify(subscriptionData), {
          headers: {
            'content-type': 'application/json'
          }
        });
        console.log(response);
        setResult("Unsubscribed successfully from"+ event.target.topic.value+":)")
      }
      
      
    } catch (error) {
      console.error(error);
      setResult(response)
    }
   
  };
  let clientToken;
  const [token, setToken] = useState(null);
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

    // You can use the `messaging` instance here or pass it as a prop to child components
    getToken(messaging, { vapidKey: process.env.NEXT_PUBLIC_VAPID_KEY }).then((currentToken) => {
      if (currentToken) {
        // Send the token to your server and update the UI if necessary
        console.log( "token:",currentToken);
        setToken(currentToken)
      } else {
        console.log('No registration token available. Request permission to generate one.');
      }
    }).catch((err) => {
      console.log('An error occurred while retrieving token. ', err);
    });

    return () => {
      // Clean up or perform any necessary actions when the component unmounts
    };
  }, []);
  
  
 
  
  return (
    <main className="flex min-h-screen flex-col items-center justify-between p-24">
      
      
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
      {result && (
            <div className="inline-block relative w-64 mr-10">
              
              <pre>{JSON.stringify(result, null, 2)}</pre>
            </div>
          )}

      
      

    </main>
  );
}

