
// Scripts for firebase and firebase messaging
importScripts('https://www.gstatic.com/firebasejs/8.2.0/firebase-app.js');
importScripts('https://www.gstatic.com/firebasejs/8.2.0/firebase-messaging.js');

// Initialize the Firebase app in the service worker by passing the generated config

self.addEventListener('notificationclick', function (event) {
  const msg = event?.notification?.data?.FCM_MSG;
  if (!msg) return;

  let url = 'https://practice-app.online/notifications';
 

  event.notification.close(); // Android needs explicit close.
  event.waitUntil(
    clients.matchAll({ type: 'window' }).then((windowClients) => {
      // Check if there is already a window/tab open with the target URL
      for (var i = 0; i < windowClients.length; i++) {
        var client = windowClients[i];
        // If so, just focus it.
        if (client.url === url && 'focus' in client) {
          return client.focus();
        }
      }
      // If not, then open the target URL in a new window/tab.
      if (clients.openWindow) {
        return clients.openWindow(url);
      }
    }),
  );
});

const firebaseConfig = {
    apiKey: "AIzaSyAN1pGfWXeLnZ77kNU7nhOoaQN0tN1BsLU",
    authDomain: "dpr-project-5e1a8.firebaseapp.com",
    projectId: "dpr-project-5e1a8",
    storageBucket: "dpr-project-5e1a8.appspot.com",
    messagingSenderId: "593061627004",
    appId: "1:593061627004:web:cd066c526ae27f8c0575f9",
    measurementId: "G-FDMHJ53604"
};
  

firebase.initializeApp(firebaseConfig);

// Retrieve firebase messaging
const messaging = firebase.messaging();

messaging.onBackgroundMessage(messaging, (payload) => {
    console.log('[firebase-messaging-sw.js] Received background message ', payload);
    // Customize notification here
    const notificationTitle = 'Background Message Title';
    const notificationOptions = {
      body: 'Background Message body.',
      icon: '/firebase-logo.png'
    };
  
    self.registration.showNotification(notificationTitle,
      notificationOptions);
  });