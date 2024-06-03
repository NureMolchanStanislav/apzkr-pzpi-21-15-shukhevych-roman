import React, { useState, useEffect } from 'react';
import { Button, Segment, Message } from 'semantic-ui-react';

const CookieConsent = () => {
  const [isVisible, setIsVisible] = useState(false);

  useEffect(() => {
    const consentGiven = localStorage.getItem('cookieConsent');
    if (!consentGiven) {
      setIsVisible(true);
    }
  }, []);

  const handleAccept = () => {
    localStorage.setItem('cookieConsent', 'true');
    setIsVisible(false);
  };

  const handleReject = () => {
    localStorage.setItem('cookieConsent', 'false');
    setIsVisible(false);
  };

  if (!isVisible) {
    return null;
  }

  return (
    <Segment
      style={{
        position: 'fixed',
        bottom: '50px', // підняти вікно над футером
        left: '10%',
        right: '10%',
        zIndex: 1000,
      }}
    >
      <Message>
        <Message.Header>Кукі та ваша приватність</Message.Header>
        <p>
          Ми використовуємо кукі, щоб покращити ваш досвід користування нашим сайтом. 
          Продовжуючи використовувати наш сайт, ви даєте згоду на нашу політику кукі.
        </p>
        <Button color='green' onClick={handleAccept}>Прийняти</Button>
        <Button color='red' onClick={handleReject}>Відхилити</Button>
      </Message>
    </Segment>
  );
};

export default CookieConsent;
