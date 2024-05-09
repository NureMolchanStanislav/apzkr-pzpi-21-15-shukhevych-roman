/* eslint-disable react-refresh/only-export-components */
import { ErrorMessage, Form, Formik } from "formik";
import { Button, Label } from "semantic-ui-react";
import { observer } from "mobx-react-lite";
import * as Yup from 'yup'
import MyTextInput from "../../../app/common/MyTextInput";
import { useStore } from "../../../app/stores/store";


export default observer (function RegisterForm() {
    const {userStore} = useStore();
    return (
        <Formik 
            initialValues={{firstName: '', lastName: '', phone: '', email:'', password: '', error: null}}
            onSubmit={(values, {setErrors}) => userStore.register(values)
            .catch(error => setErrors({error: 'Invalid email or password'}))}
            validationSchema={Yup.object({
                firstName: Yup.string().required(),
                lastName: Yup.string().required(),
                email: Yup.string().required(),
                password: Yup.string().required(),
                number: Yup.number(),
            })}
        >
            {({handleSubmit, isSubmitting, errors}) => (
                <Form className="ui form" onSubmit={handleSubmit} autoComplete="off">
                    <MyTextInput placeholder="Email" name="email"/>
                    <MyTextInput placeholder="First Name" name="firstName"/>
                    <MyTextInput placeholder="Last Name" name="lastName"/>
                    <MyTextInput placeholder="Phone Number" name="phone"/>
                    <MyTextInput placeholder="Password" name="password" type="password"/>
                    <ErrorMessage
                        name='error' render={() => 
                        <Label style={{marginBottom: 10}} basic color="red" content={errors.error}/>}
                    />
                    <Button loading={isSubmitting} positive content = 'Register' type="submit" fluid/>
                </Form>
            )}
        </Formik>
    )
})