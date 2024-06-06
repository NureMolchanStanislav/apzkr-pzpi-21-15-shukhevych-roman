import React, { useState, useEffect } from 'react';
import { Formik, Form, ErrorMessage } from 'formik';
import { Button, Segment, Header, Label } from 'semantic-ui-react';
import { observer } from 'mobx-react-lite';
import { useStore } from '../../../app/stores/store';
import MyTextInput from '../../../app/common/MyTextInput';

const UserProfileForm = observer(() => {
  const { userStore } = useStore();
  const { user, updateUserInfo } = userStore;

  const [initialValues, setInitialValues] = useState({
    email: user?.email || '',
    password: '',
    phone: user?.phone || '',
    firstName: user?.firstName || '',
    lastName: user?.lastName || ''
  });

  useEffect(() => {
    if (user) {
      setInitialValues({
        email: user.email,
        password: '',
        phone: user.phone || '',
        firstName: user.firstName || '',
        lastName: user.lastName || ''
      });
    }
  }, [user]);

  return (
    <Segment>
      <Header as='h2'>Змінити дані користувача</Header>
      <Formik
        initialValues={initialValues}
        onSubmit={(values, { setSubmitting, setErrors }) => {
          updateUserInfo(values).catch((error) => {
            setErrors({ submit: error.message });
          }).finally(() => setSubmitting(false));
        }}
        enableReinitialize
      >
        {({ handleSubmit, isSubmitting, errors }) => (
          <Form onSubmit={handleSubmit} className="ui form">
            <MyTextInput name="email" placeholder="Електронна пошта" />
            <MyTextInput name="password" placeholder="Новий пароль" type="password" />
            <MyTextInput name="phone" placeholder="Телефон" />
            <MyTextInput name="firstName" placeholder="Ім'я" />
            <MyTextInput name="lastName" placeholder="Прізвище" />

            <ErrorMessage name='submit' render={() => <Label basic color='red' content={errors.submit} />} />
            <Button type="submit" positive loading={isSubmitting} content='Зберегти' />
          </Form>
        )}
      </Formik>
    </Segment>
  );
});

export default UserProfileForm;