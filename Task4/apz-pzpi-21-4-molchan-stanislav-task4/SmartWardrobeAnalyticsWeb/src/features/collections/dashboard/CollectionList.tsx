/* eslint-disable react-refresh/only-export-components */
import { observer } from "mobx-react-lite";
import { Button, Item, ItemContent, ItemDescription, ItemExtra, Segment } from "semantic-ui-react";
import { useStore } from "../../../app/stores/store";
import { Link } from "react-router-dom";
import { useTranslation } from "react-i18next";

const CollectionList = observer(({ searchTerm }) => {
    const { collectionStore } = useStore();
    const { collections } = collectionStore;
    const { t } = useTranslation();

    const filteredCollections = collections.filter(collection =>
        collection.name.toLowerCase().includes(searchTerm.toLowerCase()) ||
        collection.description.toLowerCase().includes(searchTerm.toLowerCase())
    );

    return (
        <Segment>
            <Button as={Link} to={`/collections/create`} floated='right' content={t('buttons.create')} color="black" />
            <Item.Group divided>
                {filteredCollections.map(collection => (
                    <Item key={collection.id}>
                        <ItemContent>
                            <Item.Header as='a'> {collection.name} </Item.Header>
                            <ItemDescription>
                                <div>{collection.description}</div>
                            </ItemDescription>
                            <ItemExtra>
                                <Button color='red' content={t('buttons.delete')} />
                                <Button as={Link} to={`/update/collections/${collection.id}`} color='pink' content={t('buttons.edit')}></Button>
                                <Button as={Link} to={`/collections/${collection.id}`} floated='right' content={t('buttons.items')} color="grey" />
                            </ItemExtra>
                        </ItemContent>
                    </Item>
                ))}
            </Item.Group>
        </Segment>
    );
});

export default CollectionList;
