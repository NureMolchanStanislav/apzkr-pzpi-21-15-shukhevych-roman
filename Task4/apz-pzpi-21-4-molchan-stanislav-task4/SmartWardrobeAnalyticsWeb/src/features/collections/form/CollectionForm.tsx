/* eslint-disable react-refresh/only-export-components */
import { observer } from "mobx-react-lite";
import { useState, useEffect, ChangeEvent } from "react";
import { useParams, useNavigate } from "react-router-dom";
import { Segment, Button, Form } from "semantic-ui-react";
import LoadingComponents from "../../../app/layout/LoadingComponents";
import { useStore } from "../../../app/stores/store";


export default observer(function CollectionForm() {
    const { collectionStore, userStore } = useStore();
    const { selectedCollection, createCollection, updateCollection, loading, loadCollection, loadingInitial } = collectionStore;
    const { id } = useParams();
    const navigate = useNavigate();

    const [collection, setCollection] = useState({
        id: '',
        name: '',
        description: ''
    });

    const [error, setError] = useState('');

    useEffect(() => {
        if (id) {
            loadCollection(id)
                .then(collection => setCollection(collection || { id: '', name: '', description: '' }))
                .catch(err => {
                    console.error("Failed to load collection:", err);
                    setError("Failed to load collection.");
                });
        }
    }, [id, loadCollection]);

    function handleSubmit() {
        // Якщо id не встановлено, видаляємо його з даних, що відправляються
        const dataToSend = id ? {
            id: id,
            name: collection.name,
            description: collection.description
        } : {
            name: collection.name,
            description: collection.description
        };
    
        const action = id ? updateCollection : createCollection;
        action(dataToSend).then(() => {
            navigate(`/collections`);
        }).catch(err => {
            console.error("Failed to save collection:", err);
            setError("Failed to save collection. Check your data.");
        });
    }

    function handleChange(event: ChangeEvent<HTMLInputElement | HTMLTextAreaElement>) {
        const { name, value } = event.target;
        setCollection({ ...collection, [name]: value });
    }

    if (loadingInitial) return <LoadingComponents content="Loading collection..." />;
    if (error) return <Segment clearing><div>Error: {error}</div></Segment>;

    return (
        <Segment clearing>
            <Form onSubmit={handleSubmit} autoComplete="off">
                <Form.Input placeholder='Name' value={collection.name} name='name' onChange={handleChange} />
                <Form.TextArea placeholder='Description' value={collection.description} name='description' onChange={handleChange} />
                <Button loading={loading} floated="right" positive type='submit' content='Submit' />
                <Button floated="right" color='blue' onClick={() => navigate('/collections')} content='Cancel' />
            </Form>
        </Segment>
    );
});