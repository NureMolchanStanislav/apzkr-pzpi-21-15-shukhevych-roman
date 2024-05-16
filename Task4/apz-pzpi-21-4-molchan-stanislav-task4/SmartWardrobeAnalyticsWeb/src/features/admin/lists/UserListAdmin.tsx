/* eslint-disable react-refresh/only-export-components */
import React, { useEffect } from 'react';
import { observer } from 'mobx-react-lite';
import { Segment, Header, List, Loader, Button } from 'semantic-ui-react';
import { useStore } from '../../../app/stores/store';

const UserListAdmin = observer(() => {
    const { userStore } = useStore();
    const { users, loadUsers, loadingInitial, ban, unban } = userStore;

    useEffect(() => {
        loadUsers();
    }, [loadUsers]);

    if (loadingInitial) return <Loader active inline='centered' />;

    return (
        <Segment>
            <Header as='h2'>User List</Header>
            <List divided relaxed>
                {users.map(user => (
                    <List.Item key={user.id}>
                        <List.Content floated='right'>
                            {user.isDeleted ? (
                                <Button
                                    color='green'
                                    content='Unban'
                                    onClick={() => unban(user.id)}
                                />
                            ) : (
                                <Button
                                    color='red'
                                    content='Ban'
                                    onClick={() => ban(user.id)}
                                />
                            )}
                        </List.Content>
                        <List.Content>
                            <List.Header>{user.email}</List.Header>
                        </List.Content>
                    </List.Item>
                ))}
            </List>
        </Segment>
    );
});

export default UserListAdmin;
