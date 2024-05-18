/* eslint-disable react-refresh/only-export-components */
import { observer } from "mobx-react-lite";
import React, { useEffect, useState } from "react";
import { Grid, List, Message, Input } from "semantic-ui-react";
import { useStore } from "../../../app/stores/store";
import CollectionList from "./CollectionList";
import { useTranslation } from "react-i18next";

export default observer(function CollectionDashboard() {
    const { collectionStore, userStore } = useStore();
    const { isLoggedIn } = userStore;
    const { t } = useTranslation();
    const [searchTerm, setSearchTerm] = useState('');

    useEffect(() => {
        if (isLoggedIn) {
            collectionStore.loadCollections();
        }
    }, [collectionStore, isLoggedIn]);

    const handleSearchChange = (e) => {
        setSearchTerm(e.target.value);
    };

    if (!isLoggedIn) {
        return (
            <Message warning>
                <Message.Header>{t('collectionDashboard.authRequired')}</Message.Header>
                <p>
                    {t('collectionDashboard.please')} <a href="/login">{t('collectionDashboard.login')}</a> {t('collectionDashboard.or')} <a href="/register">{t('collectionDashboard.register')}</a> {t('collectionDashboard.toView')}.
                </p>
            </Message>
        );
    }

    return (
        <Grid>
            <Grid.Column width='10'>
                <Input
                    icon='search'
                    placeholder={t('search.placeholder')}
                    value={searchTerm}
                    onChange={handleSearchChange}
                    style={{ marginBottom: '1em' }}
                />
                <List>
                    <CollectionList searchTerm={searchTerm} />
                </List>
            </Grid.Column>
        </Grid>
    );
});
