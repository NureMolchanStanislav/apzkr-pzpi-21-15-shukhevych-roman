/* eslint-disable react-refresh/only-export-components */
import { ErrorMessage, Form, Formik } from "formik";
import { Button, Label } from "semantic-ui-react";
import { observer } from "mobx-react-lite";
import { useStore } from "../../../app/stores/store";
import MyTextInput from "../../../app/common/MyTextInput";
import { useTranslation } from "react-i18next";

export default observer(function LoginForm() {
    const { userStore } = useStore();
    const { t } = useTranslation();

    return (
        <Formik 
            initialValues={{ email: '', password: '', error: null }}
            onSubmit={(values, { setErrors }) => {
                userStore.login(values)
                    .then(() => {
                        window.location.reload();
                    })
                    .catch(() => {
                        setErrors({error: t('login.invalidCredentials')});
                    });
            }}
        >
            {({ handleSubmit, isSubmitting, errors }) => (
                <Form className="ui form" onSubmit={handleSubmit} autoComplete="off">
                    <MyTextInput placeholder={t('login.email')} name="email"/>
                    <MyTextInput placeholder={t('login.password')} name="password" type="password"/>
                    <ErrorMessage
                        name='error'
                        render={() => 
                            <Label style={{ marginBottom: 10 }} basic color="red" content={errors.error} />
                        }
                    />
                    <Button loading={isSubmitting} positive content={t('navbar.login')} type="submit" fluid/>
                </Form>
            )}
        </Formik>
    );
});
