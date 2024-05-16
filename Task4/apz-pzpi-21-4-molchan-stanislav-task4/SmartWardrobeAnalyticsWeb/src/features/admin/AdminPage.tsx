/* eslint-disable react-refresh/only-export-components */
import React, { useState } from 'react';
import { observer } from 'mobx-react-lite';
import { Segment, Header, Dropdown, Container } from 'semantic-ui-react';
import ItemListAdmin from './lists/ItemListAdmin';
import UserListAdmin from './lists/UserListAdmin';
import UsageListAdmin from './lists/UsageListAdmin';
import CollectionListAdmin from './lists/CollectionListAdmin';
import BrandListAdmin from './lists/BrandListAdmin';

const tableOptions = [
    { key: 'collections', text: 'Collections', value: 'collections' },
    { key: 'brands', text: 'Brands', value: 'brands' },
    { key: 'items', text: 'Items', value: 'items' },
    { key: 'users', text: 'Users', value: 'users' },
    { key: 'usages', text: 'Usages', value: 'usages' },
];

const AdminPage = observer(() => {
    const [selectedTable, setSelectedTable] = useState<string | null>(null);

    const handleTableChange = (e: React.SyntheticEvent<HTMLElement, Event>, { value }: any) => {
        setSelectedTable(value);
    };

    const renderTableComponent = () => {
        switch (selectedTable) {
            case 'collections':
                return <CollectionListAdmin />;
            case 'brands':
                return <BrandListAdmin />;
            case 'items':
                return <ItemListAdmin />;
            case 'users':
                return <UserListAdmin />;
            case 'usages':
                return <UsageListAdmin />;
            default:
                return null;
        }
    };

    return (
        <Container>
            <Segment>
                <Header as='h2'>Admin Page</Header>
                <Dropdown
                    placeholder='Select Table'
                    fluid
                    selection
                    options={tableOptions}
                    onChange={handleTableChange}
                />
            </Segment>
            {renderTableComponent()}
        </Container>
    );
});

export default AdminPage;
