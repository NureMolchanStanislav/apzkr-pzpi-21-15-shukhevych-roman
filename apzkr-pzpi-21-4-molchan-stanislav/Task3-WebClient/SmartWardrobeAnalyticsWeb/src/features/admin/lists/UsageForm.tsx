/* eslint-disable react-refresh/only-export-components */
import { observer } from "mobx-react-lite";
import React, { useState, useEffect, ChangeEvent } from "react";
import { useParams, useNavigate } from "react-router-dom";
import { Segment, Button, Form } from "semantic-ui-react";
import LoadingComponents from "../../../app/layout/LoadingComponents";
import { useStore } from "../../../app/stores/store";

export default observer(function UsageForm() {
    const { usageStore } = useStore();
    const { selectedUsage, loadUsage, createUsage, updateUsage, loading: usageLoading, loadingInitial: usageLoadingInitial } = usageStore;
    
    const navigate = useNavigate();
    const { id } = useParams();

    const [usage, setUsage] = useState({
        lastEvent: '',
        totalCount: 0,
        itemId: ''
    });

    const [error, setError] = useState('');

    useEffect(() => {
        if (id) {
            loadUsage(id).then(loadedUsage => setUsage(loadedUsage || {
                lastEvent: '',
                totalCount: 0,
                itemId: ''
            })).catch(err => {
                console.error("Failed to load usage:", err);
                setError("Failed to load usage.");
            });
        }
    }, [id, loadUsage]);

    function handleSubmit() {
        const dataToSend = id ? {
            ...usage,
            id
        } : {
            ...usage,
        };
    
        const action = id ? updateUsage : createUsage;
        action(dataToSend).then(() => {
            navigate('/admin');
        }).catch(err => {
            console.error("Failed to save usage:", err);
            setError("Failed to save usage. Check your data.");
        });
    }

    function handleChange(event: ChangeEvent<HTMLInputElement | HTMLTextAreaElement>) {
        const { name, value } = event.target;
        setUsage({ ...usage, [name]: value });
    }

    if (usageLoadingInitial) return <LoadingComponents content="Loading data..." />;
    if (error) return <Segment clearing><div>Error: {error}</div></Segment>;

    return (
        <Segment clearing>
            <Form onSubmit={handleSubmit} autoComplete="off">
                <Form.Input 
                    placeholder='Last Event' 
                    value={usage.lastEvent} 
                    name='lastEvent' 
                    onChange={handleChange} 
                />
                <Form.Input 
                    placeholder='Total Count' 
                    value={usage.totalCount} 
                    name='totalCount' 
                    type='number'
                    onChange={handleChange} 
                />
                <Form.Input 
                    placeholder='Item ID' 
                    value={usage.itemId} 
                    name='itemId' 
                    onChange={handleChange} 
                />
                <Button loading={usageLoading} floated="right" positive type='submit' content='Submit' />
                <Button floated="right" color='blue' onClick={() => navigate('/admin')} content='Cancel' />
            </Form>
        </Segment>
    );
});
