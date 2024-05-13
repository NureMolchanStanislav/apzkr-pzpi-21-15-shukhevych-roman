/* eslint-disable react-refresh/only-export-components */
import { observer } from "mobx-react-lite";
import React, { useEffect } from "react";
import { Grid, List, Message } from "semantic-ui-react";
import { useStore } from "../../../app/stores/store";
import CollectionList from "./CollectionList";

export default observer(function CollectionDashboard() {
    const { collectionStore, userStore } = useStore();
    const { isLoggedIn } = userStore;

    useEffect(() => {
        if (isLoggedIn) {
            collectionStore.loadCollections();
        }
    }, [collectionStore, isLoggedIn]);
    
    if (!isLoggedIn) {
        return (
            <Message warning>
                <Message.Header>Необхідна авторизація</Message.Header>
                <p>Будь ласка, <a href="/login">увійдіть</a> або <a href="/register">зареєструйтеся</a>, щоб переглянути колекції.</p>
            </Message>
        );
    }

    return (
        <Grid>
            <Grid.Column width='10'>
                <List>
                    <CollectionList />
                </List>
            </Grid.Column>
        </Grid>
    );
});