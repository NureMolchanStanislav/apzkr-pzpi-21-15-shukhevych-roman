/* eslint-disable react-refresh/only-export-components */
import { observer } from "mobx-react-lite";
import React, { useEffect } from "react";
import { Segment, Button, Header, List } from "semantic-ui-react";
import { Link, useNavigate } from "react-router-dom";
import LoadingComponents from "../../../app/layout/LoadingComponents";
import { useStore } from "../../../app/stores/store";

export default observer(function BonusDashboard() {
    const { bonusStore, brandStore } = useStore();
    const { bonuses, loadBonuses, deleteBonus, loadingInitial, loading } = bonusStore;
    const { brands, loadForUserBrands } = brandStore;
    const navigate = useNavigate();

    useEffect(() => {
        if (brands.length === 0) loadForUserBrands();
        loadBonuses();
    }, [loadBonuses, loadForUserBrands, brands.length]);

    if (loadingInitial) return <LoadingComponents content="Loading bonuses..." />;

    return (
        <Segment>
            <Header as='h2'>Brand Bonuses</Header>
            <Button as={Link} to='/create/bonus' positive content='Create Bonus' />
            <List divided relaxed>
                {bonuses.map(bonus => (
                    <Segment key={bonus.id}>
                        <Header>{brands.find(brand => brand.id === bonus.brandId)?.name}</Header>
                        <p>Usages: {bonus.conditionalNumberOfUsages}</p>
                        <p>Discount: {bonus.conditionalDiscount}%</p>
                        <p>Complexity: {bonus.conditionalComplexity}</p>
                        <p>Max Discount: {bonus.maxDiscount}%</p>
                        <Button as={Link} to={`/update/bonus/${bonus.id}`} color='blue' content='Edit' />
                        <Button color='red' content='Delete' loading={loading} onClick={() => deleteBonus(bonus.id)} />
                    </Segment>
                ))}
            </List>
        </Segment>
    );
});
