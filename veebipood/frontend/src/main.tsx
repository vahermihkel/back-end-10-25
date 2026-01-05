import { StrictMode } from 'react'
import { createRoot } from 'react-dom/client'
import 'bootstrap/dist/css/bootstrap.min.css';
import './index.css'
import './i18n';
import App from './App.tsx'
import { BrowserRouter } from 'react-router-dom'
import { CartSumContextProvider } from './context/CartSumContextProvider.tsx';
import { AuthContextProvider } from './context/AuthContextProvider.tsx';

// let sum = 0;
// sum = 3;

// .class {color: white}
// .class {color: black} ---> see jääb

// Navigeerimiseks (URL vahetamiseks)
// 1. npm i react-router-dom (vajalikud koodifailid panema node_modules kausta)
// 2. import {BrowserRouter} (importima äsjalisatud failidest BrowserRouteri)
// 3. <BrowserRouter><App/></> (ümbritsema App faili BrowserRouteriga)
// 4. tegema App.jsx failis URLi ja faili vahelisi seoseid

createRoot(document.getElementById('root')!).render(
  <StrictMode>
    <BrowserRouter>
      <CartSumContextProvider>
        <AuthContextProvider>
          <App />
        </AuthContextProvider>
      </CartSumContextProvider>
    </BrowserRouter>
  </StrictMode>,
)
