import React from 'react'
import ReactDOM from 'react-dom/client'
import './app/layout/styles.css'
import { StoreContext, store } from './app/stores/store'
import { RouterProvider } from 'react-router-dom'
import { router } from './app/router/Routes'
import { I18nextProvider } from 'react-i18next'
import i18n from './i18n'


ReactDOM.createRoot(document.getElementById('root')!).render(
  <React.StrictMode>
    <I18nextProvider i18n={i18n}>
      <StoreContext.Provider value={store}>
        <RouterProvider router={router} />
      </StoreContext.Provider>
    </I18nextProvider>
  </React.StrictMode>,
)
