/* eslint-disable react-refresh/only-export-components */
import React, { useEffect, useState } from 'react';
import { observer } from 'mobx-react-lite';
import { Segment, Header, List, Loader, Button, Input, Dropdown } from 'semantic-ui-react';
import { useStore } from '../../../app/stores/store';
import { useTranslation } from 'react-i18next';

const UserListAdmin = observer(() => {
    const { userStore } = useStore();
    const { users, loadUsers, loadingInitial, ban, unban, addToRole, removeFromRole } = userStore;
    const [searchTerm, setSearchTerm] = useState('');
    const [roleSelections, setRoleSelections] = useState<{ [key: string]: string }>({});
    const { t } = useTranslation();

    useEffect(() => {
        loadUsers();
    }, [loadUsers]);

    const handleSearchChange = (e: React.ChangeEvent<HTMLInputElement>) => {
        setSearchTerm(e.target.value);
    };

    const handleRoleChange = (userId: string, roleName: string) => {
        setRoleSelections(prevState => ({
            ...prevState,
            [userId]: roleName,
        }));
    };

    const handleAddRole = (userId: string) => {
        const roleName = roleSelections[userId];
        if (userId && roleName) {
            addToRole(userId, roleName).then(() => {
                setRoleSelections(prevState => ({
                    ...prevState,
                    [userId]: '',
                }));
            });
        }
    };

    const handleRemoveRole = (userId: string) => {
        const roleName = roleSelections[userId];
        if (userId && roleName) {
            removeFromRole(userId, roleName).then(() => {
                setRoleSelections(prevState => ({
                    ...prevState,
                    [userId]: '',
                }));
            });
        }
    };

    // Ensure users is always an array
    const filteredUsers = Array.isArray(users) ? users.filter(user =>
        user.id.toLowerCase().includes(searchTerm.toLowerCase()) ||
        user.email.toLowerCase().includes(searchTerm.toLowerCase())
    ) : [];

    if (loadingInitial) return <Loader active inline='centered' />;

    const roleOptions = [
        { key: 'User', text: 'User', value: 'User' },
        { key: 'Business', text: 'Business', value: 'Business' },
        { key: 'Admin', text: 'Admin', value: 'Admin' }
    ];

    return (
        <Segment>
            <Header as='h2'>{t('admin.userList')}</Header>
            <Input
                icon='search'
                placeholder={t('admin.search.placeholder')}
                value={searchTerm}
                onChange={handleSearchChange}
                style={{ marginBottom: '1em' }}
            />
            <List divided relaxed>
                {filteredUsers.map(user => (
                    <List.Item key={user.id}>
                        <List.Content floated='right'>
                            {user.isDeleted ? (
                                <Button
                                    color='green'
                                    content={t('admin.unban')}
                                    onClick={() => unban(user.id)}
                                />
                            ) : (
                                <Button
                                    color='red'
                                    content={t('admin.ban')}
                                    onClick={() => ban(user.id)}
                                />
                            )}
                        </List.Content>
                        <List.Content>
                            <List.Header>{user.email}</List.Header>
                            <Dropdown
                                placeholder={t('admin.selectRole')}
                                selection
                                options={roleOptions}
                                value={roleSelections[user.id] || ''}
                                onChange={(e, { value }) => handleRoleChange(user.id, value as string)}
                            />
                            <Button
                                color='blue'
                                content={t('admin.addRole')}
                                onClick={() => handleAddRole(user.id)}
                            />
                            <Button
                                color='orange'
                                content={t('admin.removeRole')}
                                onClick={() => handleRemoveRole(user.id)}
                            />
                        </List.Content>
                    </List.Item>
                ))}
            </List>
        </Segment>
    );
});

export default UserListAdmin;
