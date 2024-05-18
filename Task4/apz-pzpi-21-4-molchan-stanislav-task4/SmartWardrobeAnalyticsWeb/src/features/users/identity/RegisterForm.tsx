/* eslint-disable react-refresh/only-export-components */
import { ErrorMessage, Form, Formik } from "formik";
import { Button, Label, Checkbox } from "semantic-ui-react";
import { observer } from "mobx-react-lite";
import * as Yup from 'yup';
import MyTextInput from "../../../app/common/MyTextInput";
import { useStore } from "../../../app/stores/store";
import { useTranslation } from "react-i18next";

export default observer(function RegisterForm() {
    const { userStore } = useStore();
    const { t } = useTranslation();

    return (
        <Formik 
            initialValues={{firstName: '', lastName: '', phone: '', email:'', password: '', agreeToDataProcessing: false, error: null}}
            onSubmit={(values, {setErrors}) => userStore.register(values)
                .catch(error => setErrors({error: t('register.invalidCredentials')}))}
            validationSchema={Yup.object({
                firstName: Yup.string().required(t('register.required')),
                lastName: Yup.string().required(t('register.required')),
                email: Yup.string().required(t('register.required')),
                password: Yup.string().required(t('register.required')),
                phone: Yup.string(),
                agreeToDataProcessing: Yup.bool().oneOf([true], t('register.agreeToDataProcessing'))
            })}
        >
            {({handleSubmit, isSubmitting, errors, values, setFieldValue}) => (
                <Form className="ui form" onSubmit={handleSubmit} autoComplete="off">
                    <MyTextInput placeholder={t('register.email')} name="email"/>
                    <MyTextInput placeholder={t('register.firstName')} name="firstName"/>
                    <MyTextInput placeholder={t('register.lastName')} name="lastName"/>
                    <MyTextInput placeholder={t('register.phone')} name="phone"/>
                    <MyTextInput placeholder={t('register.password')} name="password" type="password"/>
                    <div style={{ marginBottom: '1em' }}>
                        <Checkbox
                            label={t('register.agreeToDataProcessing')}
                            name="agreeToDataProcessing"
                            checked={values.agreeToDataProcessing}
                            onChange={(e, data) => setFieldValue("agreeToDataProcessing", data.checked)}
                        />
                    </div>
                    <ErrorMessage
                        name='error' render={() => 
                        <Label style={{marginBottom: 10}} basic color="red" content={errors.error}/>}
                    />
                    <Button loading={isSubmitting} positive content={t('navbar.register')} type="submit" fluid/>
                </Form>
            )}
        </Formik>
    );
});
