import React, { useState, useEffect } from 'react';
import { Inter } from 'next/font/google';
import axios from 'axios';
import { initializeApp } from "firebase/app";
import { getMessaging,onMessage, getToken } from "firebase/messaging";

const inter = Inter({ subsets: ['latin'] });
const firebaseConfig = {
  apiKey: "AIzaSyAN1pGfWXeLnZ77kNU7nhOoaQN0tN1BsLU",
  authDomain: "dpr-project-5e1a8.firebaseapp.com",
  projectId: "dpr-project-5e1a8",
  storageBucket: "dpr-project-5e1a8.appspot.com",
  messagingSenderId: "593061627004",
  appId: "1:593061627004:web:cd066c526ae27f8c0575f9",
  measurementId: "G-FDMHJ53604"
};



const CreateEvent = ({text}) => {
  const [responseData, setResponseData] = useState(null);
  const handleClick = () => {
    // Send a POST request to your API
    const app = initializeApp(firebaseConfig);
    const messaging = getMessaging(app);
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




const Subscribe = ({text}) => {
  const [responseData, setResponseData] = useState(null);
  const handleClick = () => {
    // Send a POST request to your API
    const app = initializeApp(firebaseConfig);
    const messaging = getMessaging();
    getToken(messaging, { vapidKey: 'BM2SKWurNY8H7fjxKLPF0Sqn9wPPr-QFz7SxgDkdNTuYPR5u-ewM7kdGyHcL5td1ze9WtvH8AFEw1msYlUFdnS4' }).then((currentToken) => {
      if (currentToken) {
        // Send the token to your server and update the UI if necessary
        console.log( "token:",currentToken, text)
        subscribeTopic(currentToken, text)
      } else {
        console.log('No registration token available. Request permission to generate one.');
      }
    }).catch((err) => {
      console.log('An error occurred while retrieving token. ', err);
    });
  }; 
  return (
    <div>
      <button onClick={handleClick}>{text}</button>
      
    </div>
  );
};


const UnSubscribe = ({text}) => {
  const [responseData, setResponseData] = useState(null);
  const handleClick = () => {
    // Send a POST request to your API
    const app = initializeApp(firebaseConfig);
    const messaging = getMessaging();
    getToken(messaging, { vapidKey: 'BM2SKWurNY8H7fjxKLPF0Sqn9wPPr-QFz7SxgDkdNTuYPR5u-ewM7kdGyHcL5td1ze9WtvH8AFEw1msYlUFdnS4' }).then((currentToken) => {
      if (currentToken) {
        // Send the token to your server and update the UI if necessary
        console.log( "token:",currentToken, text)
        unsubscribeTopic(currentToken, text)
      } else {
        console.log('No registration token available. Request permission to generate one.');
      }
    }).catch((err) => {
      console.log('An error occurred while retrieving token. ', err);
    });
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
        <Subscribe text="Food"/>
        <Subscribe text="Clothes"/>
      </div>
      <div>
        UnSubscribe to a topic
        <UnSubscribe text="Food"/>
        <UnSubscribe text="Clothes"/>
      </div>
      </div>
    </main>
  );
}

