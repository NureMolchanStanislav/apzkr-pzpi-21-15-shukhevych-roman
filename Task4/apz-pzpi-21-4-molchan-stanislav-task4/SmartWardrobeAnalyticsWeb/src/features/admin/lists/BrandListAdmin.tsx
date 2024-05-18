/* eslint-disable react-refresh/only-export-components */
import React, { useEffect } from 'react';
import { observer } from 'mobx-react-lite';
import { Segment, Header, List, Loader, Button } from 'semantic-ui-react';
import { useStore } from '../../../app/stores/store';
import { useNavigate } from 'react-router-dom';

const BrandListAdmin = observer(() => {
    const { brandStore } = useStore();
    const { brands, loadBrands, deleteBrand, loadingInitial } = brandStore;
    const navigate = useNavigate();

    useEffect(() => {
        loadBrands();
    }, [loadBrands]);

    if (loadingInitial) return <Loader active inline='centered' />;

    return (
        <Segment>
            <Header as='h2'>Brand List</Header>
            <Button 
                primary 
                content='Create Brand' 
                onClick={() => navigate('/create/brand')} 
                style={{ marginBottom: '1em' }}
            />
            <List divided relaxed>
                {brands.map(brand => (
                    <List.Item key={brand.id}>
                        <List.Content floated='right'>
                            <Button
                                color='blue'
                                content='Edit'
                                onClick={() => navigate(`/update/brand/${brand.id}`)}
                            />
                            <Button
                                color='red'
                                content='Delete'
                                onClick={() => deleteBrand(brand.id)}
                            />
                        </List.Content>
                        <List.Content>
                            <List.Header>{brand.name}</List.Header>
                        </List.Content>
                    </List.Item>
                ))}
            </List>
        </Segment>
    );
});

export default BrandListAdmin;