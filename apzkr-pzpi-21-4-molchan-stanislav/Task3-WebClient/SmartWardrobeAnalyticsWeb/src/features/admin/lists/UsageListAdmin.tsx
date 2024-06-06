/* eslint-disable react-refresh/only-export-components */
import React, { useEffect } from 'react';
import { observer } from 'mobx-react-lite';
import { Segment, Header, List, Loader, Button } from 'semantic-ui-react';
import { useStore } from '../../../app/stores/store';
import { useNavigate } from 'react-router-dom';

const UsageListAdmin = observer(() => {
    const { usageStore } = useStore();
    const { usages, loadUsages, deleteUsage, loadingInitial } = usageStore;
    const navigate = useNavigate();

    useEffect(() => {
        loadUsages();
    }, [loadUsages]);

    if (loadingInitial) return <Loader active inline='centered' />;

    return (
        <Segment>
            <Header as='h2'>Usage List</Header>
            <Button 
                primary 
                content='Create Usage' 
                onClick={() => navigate('/create/usage')} 
                style={{ marginBottom: '1em' }}
            />
            <List divided relaxed>
                {Array.isArray(usages) && usages.length > 0 ? (
                    usages.map(usage => (
                        <List.Item key={usage.id}>
                            <List.Content floated='right'>
                                <Button
                                    color='blue'
                                    content='Edit'
                                    onClick={() => navigate(`/update/usage/${usage.id}`)}
                                />
                                <Button
                                    color='red'
                                    content='Delete'
                                    onClick={() => deleteUsage(usage.id)}
                                />
                            </List.Content>
                            <List.Content>
                                <List.Header>{usage.lastEvent}</List.Header>
                                <List.Description>Total Count: {usage.totalCount}</List.Description>
                            </List.Content>
                        </List.Item>
                    ))
                ) : (
                    <List.Item>
                        <List.Content>
                            <List.Header>No usages found</List.Header>
                        </List.Content>
                    </List.Item>
                )}
            </List>
        </Segment>
    );
});

export default UsageListAdmin;
