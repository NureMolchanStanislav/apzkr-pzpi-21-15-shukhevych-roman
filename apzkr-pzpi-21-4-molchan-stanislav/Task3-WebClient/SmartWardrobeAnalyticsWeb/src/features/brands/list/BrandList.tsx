/* eslint-disable react-refresh/only-export-components */
import { observer } from "mobx-react-lite";
import React from "react";
import { Button, Item, ItemContent, ItemExtra, Segment } from "semantic-ui-react";
import { Link } from "react-router-dom";
import { useStore } from "../../../app/stores/store";

export default observer(function BrandList() {
    const { brandStore } = useStore();
    const { brands, deleteBrand } = brandStore;

    const handleDelete = (id) => {
        deleteBrand(id).catch(error => {
            console.error("Failed to delete brand:", error);
        });
    };

    return (
        <Segment>
            <Button as={Link} to={`/create/brand`} floated='right' content={"Add"} color="black" />
            <Item.Group divided>
                {brands.map(brand => (
                    <Item key={brand.id}>
                        <ItemContent>
                            <Item.Header as='a'>{brand.name}</Item.Header>
                            <ItemExtra>
                                <Button as={Link} to={`/update/brand/${brand.id}`} color='orange' content='Edit' />
                                <Button color='red' content='Delete' onClick={() => handleDelete(brand.id)} />
                            </ItemExtra>
                        </ItemContent>
                    </Item>
                ))}
            </Item.Group>
        </Segment>
    );
});
