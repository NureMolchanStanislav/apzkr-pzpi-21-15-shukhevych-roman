/* eslint-disable react-refresh/only-export-components */
import React, { useEffect, useState } from 'react';
import { observer } from 'mobx-react-lite';
import { Segment, Header, List, Loader, Pagination, Button, Input, Dropdown } from 'semantic-ui-react';
import { useStore } from '../../../app/stores/store';
import { useNavigate } from 'react-router-dom';

const ItemListAdmin = observer(() => {
    const { itemStore } = useStore();
    const { items, loadItems, loadingInitial, deleteItem, totalPages, setPageNumber, pageNumber, setPageSize } = itemStore;
    const [searchTerm, setSearchTerm] = useState('');
    const navigate = useNavigate();

    useEffect(() => {
        loadItems();
    }, [loadItems]);

    if (loadingInitial) return <Loader active inline='centered' />;

    const handleSearchChange = (e) => {
        setSearchTerm(e.target.value);
    };

    const filteredItems = items.filter(item =>
        (item.name?.toLowerCase().includes(searchTerm.toLowerCase()) || 
        item.description?.toLowerCase().includes(searchTerm.toLowerCase()))
    );

    const handleDelete = (id) => {
        deleteItem(id).then(() => {
            if (filteredItems.length === 1 && pageNumber > 1) {
                setPageNumber(pageNumber - 1);
            }
        });
    };

    const handlePageChange = (e, { activePage }) => {
        setPageNumber(activePage);
    };

    const handlePageSizeChange = (e, { value }) => {
        setPageSize(value);
        setPageNumber(1); // Reset to first page when page size changes
    };

    return (
        <Segment>
            <Header as='h2'>Item List</Header>
            <Button 
                primary 
                content='Create Item' 
                onClick={() => navigate('/create/item')} 
                style={{ marginBottom: '1em' }}
            />
            <Input
                icon='search'
                placeholder='Search...'
                value={searchTerm}
                onChange={handleSearchChange}
                style={{ marginBottom: '1em' }}
            />
            <Dropdown
                placeholder='Items per page'
                selection
                options={[5, 10, 20, 50, 100].map(size => ({ key: size, text: size.toString(), value: size }))}
                onChange={handlePageSizeChange}
                defaultValue={20}
                style={{ marginBottom: '1em' }}
            />
            <List divided relaxed>
                {filteredItems.map(item => (
                    <List.Item key={item.id}>
                        <List.Content floated='right'>
                            <Button
                                color='blue'
                                content='Edit'
                                onClick={() => navigate(`/update/item/${item.id}`)}
                            />
                            <Button
                                color='red'
                                content='Delete'
                                onClick={() => handleDelete(item.id)}
                            />
                        </List.Content>
                        <List.Content>
                            <List.Header>{item.name}</List.Header>
                            <List.Description>{item.description}</List.Description>
                        </List.Content>
                    </List.Item>
                ))}
            </List>
            <Pagination
                activePage={pageNumber}
                totalPages={totalPages}
                onPageChange={handlePageChange}
                style={{ marginTop: '1em' }}
            />
        </Segment>
    );
});

export default ItemListAdmin;
