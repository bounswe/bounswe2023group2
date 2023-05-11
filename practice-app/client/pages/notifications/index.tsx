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
const CreateEvent = ({text}) => {
  const [responseData, setResponseData] = useState(null);
  const handleClick = () => {
    // Send a POST request to your API
    const messaging = getMessaging(firebaseApp);
    onMessage(messaging, (payload) => {
      console.log('Message received. ', payload);
      // ...
    });
    
    sendNotification(text)
    .then(response => {
      // Handle the response from the API
      console.log('API response:', response);
    })
    .catch(error => {
      // Handle any error that occurred during the request
      console.error('Error:', error);
    });
  }; 
  return (
    <div>
      <button onClick={handleClick}>{text}</button>
      {responseData && (
        <div>
          {responseData[0].username}
        </div>
      )}
    </div>
  );
};

const SubscriptionButton = ({text, clientToken, functionality}) => {
  
  const handleClick = () => {
    if(functionality === 'subscribe'){
      unsubscribeTopic(clientToken, text)
    }else{
      subscribeTopic(clientToken, text)
    }
  }; 
  return (
    <div>
      <button onClick={handleClick}>{text}</button>
      
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

const sendNotification = async (topic: String) => {
  try {
    const notificationData = {
        body: topic+" Need in Istanbul",
        topic: topic,
        title: "New activity in your subscribed topic: Need-"+ topic
        //link: "https://ef7c-193-140-194-56.ngrok-free.app/notifications"
    };
    console.log("hey");
    const response = await axios.post('http://127.0.0.1:8000/notifications/create_event', JSON.stringify(notificationData), {
      headers: {
        'content-type': 'application/json'
      }
    });
    console.log(response);
  } catch (error) {
    console.error(error);
  }
};

const subscribeTopic = async (token: String, topic: String) => {
  try {
    console.log( "sendind subs",token, topic)
    const subscriptionData = {
        token: token,
        topic: topic
        
    };
    console.log(subscriptionData.token, subscriptionData.topic)
    const response = await axios.post('http://127.0.0.1:8000/notifications/subscriptions', JSON.stringify(subscriptionData), {
      headers: {
        'content-type': 'application/json'
      }
    });
    console.log(response);
  } catch (error) {
    console.error(error);
  }
};

const unsubscribeTopic = async (token: String, topic: String) => {
  try {
    console.log( "Unsubscribing",token, topic)
    const subscriptionData = {
        token: token,
        topic: topic
        
    };
    console.log(subscriptionData.token, subscriptionData.topic)
    const response = await axios.post('http://127.0.0.1:8000/notifications/unsubscribe', JSON.stringify(subscriptionData), {
      headers: {
        'content-type': 'application/json'
      }
    });
    console.log(response);
  } catch (error) {
    console.error(error);
  }
};


export default function Home() {
  let clientToken;
  useEffect(() => {
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
        clientToken=currentToken;
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
      <div>Burasi notifications</div>
      <NotificationPermission/>
      <div style={{ display: 'flex', justifyContent: 'space-between' }}>
      <div>
        Create Need
      <CreateEvent text="Food"/>
      <CreateEvent text="Clothes"/>
      </div>
      <div>
        Subscribe to a topic
        <SubscriptionButton text="Food" clientToken={clientToken} functionality="subscribe"/>
        <SubscriptionButton text="Clothes" clientToken={clientToken} functionality="subscribe"/>
      </div>
      <div>
        UnSubscribe to a topic
        <SubscriptionButton text="Food" clientToken={clientToken} functionality="unsubscribe"/>
        <SubscriptionButton text="Clothes" clientToken={clientToken} functionality="unsubscribe"/>
      </div>
      </div>
    </main>
  );
}

