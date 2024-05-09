/* eslint-disable react-refresh/only-export-components */
import { observer } from "mobx-react-lite";
import React, { useEffect } from "react";
import { Grid, List } from "semantic-ui-react";
import { useStore } from "../../../app/stores/store";
import CollectionList from "./CollectionList";


export default observer (function CollectionDashboard(){

    const {collectionStore} = useStore();

    useEffect(() => 
    {
        collectionStore.loadCollections();
    }, [collectionStore])
    
    return(
        <Grid>
            <Grid.Column width='10'>
                <List>
                    <CollectionList />
                </List>
            </Grid.Column>
        </Grid>
    )
})