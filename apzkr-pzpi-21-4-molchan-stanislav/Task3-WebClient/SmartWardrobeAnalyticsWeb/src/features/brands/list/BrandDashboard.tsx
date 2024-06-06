/* eslint-disable react-refresh/only-export-components */
import { observer } from "mobx-react-lite";
import React, { useEffect } from "react";
import { Grid, List } from "semantic-ui-react";
import { useStore } from "../../../app/stores/store";
import BrandList from "./BrandList";

export default observer(function BrandDashboard() {
    const { brandStore } = useStore();

    useEffect(() => {
        brandStore.loadForUserBrands();
    }, [brandStore]);

    return (
        <Grid>
            <Grid.Column width='10'>
                <List>
                    <BrandList />
                </List>
            </Grid.Column>
        </Grid>
    );
});
