// LanguageSelector.tsx
import React from 'react';
import { useTranslation } from 'react-i18next';
import { Dropdown } from 'semantic-ui-react';

const LanguageSelector: React.FC = () => {
  const { i18n } = useTranslation();

  const changeLanguage = (lng: string) => {
    i18n.changeLanguage(lng);
  };

  const languageOptions = [
    { key: 'en', value: 'en', text: 'English', flag: 'us' },
    { key: 'uk', value: 'uk', text: 'Українська', flag: 'ua' },
  ];

  return (
    <Dropdown
      button
      className="icon"
      floating
      labeled
      icon="world"
      options={languageOptions}
      search
      text={i18n.language === 'en' ? 'English' : 'Українська'}
      onChange={(_, data) => changeLanguage(data.value as string)}
    />
  );
};

export default LanguageSelector;