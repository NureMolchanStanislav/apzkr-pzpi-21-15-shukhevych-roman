/* eslint-disable react-refresh/only-export-components */
import { observer } from "mobx-react-lite";
import React, { useEffect } from "react";
import { Grid, List } from "semantic-ui-react";
import { useStore } from "../../app/stores/store";
import ItemList from "./ItemList";
import { useParams } from "react-router-dom";

export default observer(function ItemDashboard() {
    const { itemStore } = useStore();
    const {id} = useParams();

    useEffect(() => {
        console.log(id)
        itemStore.loadItemForCollection(id);
    }, [id, itemStore]);

    return (
        <Grid>
            <Grid.Column width='10'>
                <List>
                    <ItemList />
                </List>
            </Grid.Column>
        </Grid>
    );
});