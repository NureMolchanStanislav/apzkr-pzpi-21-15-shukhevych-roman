/* eslint-disable react-refresh/only-export-components */
import { observer } from "mobx-react-lite";
import React, { useState, useEffect, ChangeEvent } from "react";
import { useParams, useNavigate } from "react-router-dom";
import { Segment, Button, Form } from "semantic-ui-react";
import LoadingComponents from "../../../app/layout/LoadingComponents";
import { useStore } from "../../../app/stores/store";

export default observer(function BrandForm() {
    const { brandStore } = useStore();
    const { selectedBrand, loadBrand, createBrand, updateBrand, loading: brandLoading, loadingInitial: brandLoadingInitial } = brandStore;
    
    const navigate = useNavigate();
    const { id } = useParams();

    const [brand, setBrand] = useState({
        name: '',
    });

    const [error, setError] = useState('');

    useEffect(() => {
        if (id) {
            loadBrand(id).then(loadedBrand => setBrand(loadedBrand || {
                name: '',
            })).catch(err => {
                console.error("Failed to load brand:", err);
                setError("Failed to load brand.");
            });
        }
    }, [id, loadBrand]);

    function handleSubmit() {
        const dataToSend = id ? {
            ...brand,
            id
        } : {
            ...brand,
        };
    
        const action = id ? updateBrand : createBrand;
        action(dataToSend).then(() => {
            navigate(`/brands`);
        }).catch(err => {
            console.error("Failed to save brand:", err);
            setError("Failed to save brand. Check your data.");
        });
    }

    function handleChange(event: ChangeEvent<HTMLInputElement | HTMLTextAreaElement>) {
        const { name, value } = event.target;
        setBrand({ ...brand, [name]: value });
    }

    if (brandLoadingInitial) return <LoadingComponents content="Loading data..." />;
    if (error) return <Segment clearing><div>Error: {error}</div></Segment>;

    return (
        <Segment clearing>
            <Form onSubmit={handleSubmit} autoComplete="off">
                <Form.Input 
                    placeholder='Name' 
                    value={brand.name} 
                    name='name' 
                    onChange={handleChange} 
                />
                <Button loading={brandLoading} floated="right" positive type='submit' content='Submit' />
                <Button floated="right" color='blue' onClick={() => navigate('/brands')} content='Cancel' />
            </Form>
        </Segment>
    );
});
