/* eslint-disable react-refresh/only-export-components */
import { ErrorMessage, Form, Formik } from "formik";
import { Button, Label } from "semantic-ui-react";
import { observer } from "mobx-react-lite";
import { useStore } from "../../../app/stores/store";
import MyTextInput from "../../../app/common/MyTextInput";


export default observer(function LoginForm() {
    const {userStore} = useStore();

    console.log("userStore:", userStore); // Додайте це логування для перевірки

    return (
        <Formik 
            initialValues={{email:'', password: '', error: null}}
            onSubmit={(values, {setErrors}) => {
                console.log("Submitting form with:", values); // Подивіться, які значення передаються
                if (userStore.login) { // Перевірте, чи є метод login
                    userStore.login(values)
                    .catch(() => setErrors({error: 'Invalid email or password'}))
                } else {
                    console.error("userStore.login is not defined!");
                }
            }}
        >
            {({handleSubmit, isSubmitting, errors}) => (
                <Form className="ui form" onSubmit={handleSubmit} autoComplete="off">
                    <MyTextInput placeholder={('login.email')} name="email"/>
                    <MyTextInput placeholder={('login.password')} name="password" type="password"/>
                    <ErrorMessage
                        name='error' render={() => 
                        <Label style={{marginBottom: 10}} basic color="red" content={errors.error}/>}
                    />
                    <Button loading={isSubmitting} positive content = {('navbar.login')} type="submit" fluid/>
                </Form>
            )}
        </Formik>
    );
});