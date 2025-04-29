import React from 'react';
import ReactDOM from 'react-dom/client';
import './index.css';
import App from './App';
import reportWebVitals from './reportWebVitals';
import { initializeApp } from "firebase/app";
import { getAnalytics } from "firebase/analytics";
import { getAuth } from "firebase/auth";

// Your web app's Firebase configuration
const firebaseConfig = {
  apiKey: "AIzaSyBIzJxyefR7IsXG3sZBC5RZ5BdNhjNAhrU",
  authDomain: "project-abb72.firebaseapp.com",
  projectId: "project-abb72",
  storageBucket: "project-abb72.firebasestorage.app",
  messagingSenderId: "865860344067",
  appId: "1:865860344067:web:3570c5935ff191add5b136",
  measurementId: "G-13MT6WF2F4"
};

// Initialize Firebase
const app = initializeApp(firebaseConfig);
const auth = getAuth(app);

const root = ReactDOM.createRoot(document.getElementById('root'));
root.render(
  <React.StrictMode>

    <App />
  </React.StrictMode>
);

// If you want to start measuring performance in your app, pass a function
// to log results (for example: reportWebVitals(console.log))
// or send to an analytics endpoint. Learn more: https://bit.ly/CRA-vitals
reportWebVitals();
