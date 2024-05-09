/* eslint-disable react-refresh/only-export-components */
import { observer } from "mobx-react-lite";
import { Button, Item, ItemContent, ItemDescription, ItemExtra, Segment } from "semantic-ui-react";
import { useStore } from "../../../app/stores/store";


export default observer (function CollectionList() {
    const { collectionStore } = useStore();
    const { collections } = collectionStore;

    return(
        <Segment>
            <Item.Group divided>
                {collections.map(collection =>(
                    <Item key={collection.id}>
                        <ItemContent>
                            <Item.Header as='a'> {collection.name} </Item.Header>
                            <ItemDescription>
                                <div>{collection.description}</div>
                            </ItemDescription>
                            <ItemExtra>
                                <Button floated="right" content="View" color="grey"/>
                            </ItemExtra>
                        </ItemContent>
                    </Item>
                ))}
            </Item.Group>
        </Segment>
    )
})
