import {Fragment, useEffect, useState } from 'react'
import NavBar from './NavBar'
import { Container } from 'semantic-ui-react'
import { useStore } from '../stores/store'
import { observer } from 'mobx-react-lite'
import { Outlet } from 'react-router-dom'
import LoadingComponents from './LoadingComponents'
import CookieConsent from '../../features/common/CookieConsent'

function App() {
  
  const {commonStore, userStore} = useStore();

  useEffect(()=>{
    if (commonStore.token) {
      userStore.getUser().finally(()=> commonStore.setAppLoaded())
    } else {
      commonStore.setAppLoaded()
    }
  }, [commonStore, userStore])


  if(!commonStore.appLoaded) return <LoadingComponents content='App loading...' />

  return (
    <Fragment>
      <NavBar />
      <Container style={{marginTop: '7em'}}>
        <Outlet />
        <CookieConsent />
      </Container>
    </Fragment>
  );
}

export default observer (App);
