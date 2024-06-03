/* eslint-disable react-refresh/only-export-components */
import React, { useEffect } from 'react';
import { observer } from 'mobx-react-lite';
import { Segment, Header, List, Loader, Button } from 'semantic-ui-react';
import { useStore } from '../../../app/stores/store';
import { useNavigate } from 'react-router-dom';

const CollectionListAdmin = observer(() => {
    const { collectionStore } = useStore();
    const { collections, loadAllCollections, deleteCollection, loadingInitial } = collectionStore;
    const navigate = useNavigate();

    useEffect(() => {
        loadAllCollections();
    }, [loadAllCollections]);

    if (loadingInitial) return <Loader active inline='centered' />;

    return (
        <Segment>
            <Header as='h2'>Collection List</Header>
            <Button 
                primary 
                content='Create Collection' 
                onClick={() => navigate('/create/collection')} 
                style={{ marginBottom: '1em' }}
            />
            <List divided relaxed>
                {collections.map(collection => (
                    <List.Item key={collection.id}>
                        <List.Content floated='right'>
                            <Button
                                color='blue'
                                content='Edit'
                                onClick={() => navigate(`/update/collection/${collection.id}`)}
                            />
                            <Button
                                color='red'
                                content='Delete'
                                onClick={() => deleteCollection(collection.id)}
                            />
                        </List.Content>
                        <List.Content>
                            <List.Header>{collection.name}</List.Header>
                            <List.Description>{collection.description}</List.Description>
                        </List.Content>
                    </List.Item>
                ))}
            </List>
        </Segment>
    );
});

export default CollectionListAdmin;