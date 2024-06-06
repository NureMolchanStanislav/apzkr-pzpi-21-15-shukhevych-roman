/* eslint-disable react-refresh/only-export-components */
import { observer } from "mobx-react-lite";
import React, { useState, useEffect, ChangeEvent } from "react";
import { useParams, useNavigate } from "react-router-dom";
import { Segment, Button, Form, Dropdown } from "semantic-ui-react";
import LoadingComponents from "../../../app/layout/LoadingComponents";
import { useStore } from "../../../app/stores/store";

export default observer(function ItemForm() {
    const { itemStore, brandStore, collectionStore, userStore } = useStore();
    const { selectedItem, loadItem, createItem, updateItem, loading: itemLoading, loadingInitial: itemLoadingInitial } = itemStore;
    const { brands, loadBrands, loading: brandLoading, loadingInitial: brandLoadingInitial } = brandStore;
    const { collections, loadCollections, loadAllCollections, loading: collectionLoading, loadingInitial: collectionLoadingInitial } = collectionStore;
    const { user } = userStore;
    
    const navigate = useNavigate();
    const { id } = useParams();

    const [item, setItem] = useState({
        name: '',
        description: '',
        categories: 0,
        brandId: '',
        collectionId: ''
    });

    const [error, setError] = useState('');

    useEffect(() => {
        if (brands.length === 0) loadBrands();
        if (id) {
            loadItem(id).then(loadedItem => setItem(loadedItem || {
                name: '',
                description: '',
                categories: 0,
                brandId: '',
                collectionId: ''
            })).catch(err => {
                console.error("Failed to load item:", err);
                setError("Failed to load item.");
            });
        }

        // Load collections based on user role
        if (user?.roles.some(role => role.name === 'Admin')) {
            loadAllCollections();
        } else {
            loadCollections();
        }
    }, [id, loadBrands, loadCollections, loadAllCollections, loadItem, brands.length, user]);

    function handleSubmit() {
        const dataToSend = id ? {
            ...item,
            id
        } : {
            ...item,
        };
    
        const action = id ? updateItem : createItem;
        action(dataToSend).then(() => {
            if (user?.roles.some(role => role.name === 'Admin')) {
                navigate('/admin');
            } else {
                navigate('/collections');
            }
        }).catch(err => {
            console.error("Failed to save item:", err);
            setError("Failed to save item. Check your data.");
        });
    }

    function handleChange(event: ChangeEvent<HTMLInputElement | HTMLTextAreaElement>) {
        const { name, value } = event.target;
        setItem({ ...item, [name]: value });
    }

    function handleDropdownChange(e: React.SyntheticEvent<HTMLElement, Event>, data: any) {
        setItem({ ...item, [data.name]: data.value });
    }

    if (itemLoadingInitial || brandLoadingInitial || collectionLoadingInitial) return <LoadingComponents content="Loading data..." />;
    if (error) return <Segment clearing><div>Error: {error}</div></Segment>;

    const brandOptions = brands.map(brand => ({ key: brand.id, text: brand.name, value: brand.id }));
    const collectionOptions = collections.map(collection => ({ key: collection.id, text: collection.name, value: collection.id }));

    return (
        <Segment clearing>
            <Form onSubmit={handleSubmit} autoComplete="off">
                <Form.Input 
                    placeholder='Name' 
                    value={item.name} 
                    name='name' 
                    onChange={handleChange} 
                />
                <Form.TextArea 
                    placeholder='Description' 
                    value={item.description} 
                    name='description' 
                    onChange={handleChange} 
                />
                <Form.Dropdown 
                    placeholder='Select Brand' 
                    fluid 
                    selection 
                    options={brandOptions} 
                    name='brandId' 
                    value={item.brandId}
                    onChange={handleDropdownChange} 
                />
                <Form.Dropdown 
                    placeholder='Select Collection' 
                    fluid 
                    selection 
                    options={collectionOptions} 
                    name='collectionId' 
                    value={item.collectionId}
                    onChange={handleDropdownChange} 
                />
                <Button loading={itemLoading} floated="right" positive type='submit' content='Submit' />
                <Button floated="right" color='blue' onClick={() => navigate('/items')} content='Cancel' />
            </Form>
        </Segment>
    );
});
