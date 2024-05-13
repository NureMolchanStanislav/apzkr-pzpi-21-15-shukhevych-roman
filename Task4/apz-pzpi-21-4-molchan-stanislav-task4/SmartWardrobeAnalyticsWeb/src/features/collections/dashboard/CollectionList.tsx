/* eslint-disable react-refresh/only-export-components */
import { observer } from "mobx-react-lite";
import { Button, Item, ItemContent, ItemDescription, ItemExtra, Segment } from "semantic-ui-react";
import { useStore } from "../../../app/stores/store";
import { Link } from "react-router-dom";


export default observer (function CollectionList() {
    const { collectionStore } = useStore();
    const { collections } = collectionStore;

    return(
        <Segment>
            <Button as={Link} to={`/collections/create`} floated='right' content={"Create"} color="black" />
            <Item.Group divided>
                {collections.map(collection =>(
                    <Item key={collection.id}>
                        <ItemContent>
                            <Item.Header as='a'> {collection.name} </Item.Header>
                            <ItemDescription>
                                <div>{collection.description}</div>
                            </ItemDescription>
                            <ItemExtra>
                                <Button as={Link} to={`/update/collections/${collection.id}`} color='pink' content='Edit'></Button>
                                <Button as={Link} to={`/collections/${collection.id}`} floated='right' content={"Items"} color="grey" />
                            </ItemExtra>
                        </ItemContent>
                    </Item>
                ))}
            </Item.Group>
        </Segment>
    )
})
