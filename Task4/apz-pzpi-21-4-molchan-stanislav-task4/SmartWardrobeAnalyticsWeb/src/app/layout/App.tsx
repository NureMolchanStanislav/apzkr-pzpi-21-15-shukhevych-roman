import {useEffect, useState } from 'react'
import NavBar from './NavBar'
import { Container } from 'semantic-ui-react'
import { useStore } from '../stores/store'
import { observer } from 'mobx-react-lite'
import { Outlet } from 'react-router-dom'
import LoadingComponents from './LoadingComponents'

function App() {
  
  const {commonStore, userStore} = useStore();

  useEffect(()=>{
    if (commonStore.token) {
      userStore.getUser().finally(()=> commonStore.setAppLoaded())
      console.log("User ауторизовнаий!")
    } else {
      commonStore.setAppLoaded()
      console.log("User ніхто!")
    }
  }, [commonStore, userStore])


  return (
    <>
      <NavBar />
      <Container style={{marginTop: '7em'}}>
        <Outlet/>
      </Container>
    </>
  )
}

export default observer (App);
