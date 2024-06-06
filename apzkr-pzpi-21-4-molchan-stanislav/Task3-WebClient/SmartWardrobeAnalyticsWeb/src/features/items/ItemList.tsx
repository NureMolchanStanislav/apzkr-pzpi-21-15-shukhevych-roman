/* eslint-disable react-refresh/only-export-components */
import { observer } from "mobx-react-lite";
import React, { useState } from "react";
import { Button, Item, ItemContent, ItemDescription, ItemExtra, Segment, Modal, Dropdown } from "semantic-ui-react";
import { Link } from "react-router-dom";
import { useStore } from "../../app/stores/store";

export default observer(function ItemList() {
    const { itemStore, tagStore } = useStore();
    const { items, deleteItem } = itemStore;
    const { tags, loadTags, updateTagWithNewItem } = tagStore;

    const [isModalOpen, setIsModalOpen] = useState(false);
    const [selectedTag, setSelectedTag] = useState("");
    const [activeItemId, setActiveItemId] = useState("");

    const openModal = (itemId) => {
        loadTags();
        setActiveItemId(itemId);
        setIsModalOpen(true);
    };

    const handleTagChange = (e, { value }) => setSelectedTag(value);

    const updateTagForItem = () => {
        updateTagWithNewItem(selectedTag, activeItemId)
            .then(() => {
                setIsModalOpen(false);
                console.log("Tag updated successfully.");
            })
            .catch(error => {
                console.error("Failed to update tag:", error);
            });
    };

    const handleDelete = (id) => {
        deleteItem(id).catch(error => {
            console.error("Failed to delete item:", error);
        });
    };

    return (
        <Segment>
            <Button as={Link} to={`/create/item`} floated='right' content={"Add"} color="black" />
            <Item.Group divided>
                {items.map(item => (
                    <Item key={item.id}>
                        <ItemContent>
                            <Item.Header as='a'>{item.name}</Item.Header>
                            <ItemDescription>
                                <div>{item.description}</div>
                            </ItemDescription>
                            <ItemExtra>
                                <Button as={Link} to={`/update/item/${item.id}`} color='orange' content='Edit' />
                                <Button floated='right' content={"Details"} color="black" as={Link} to={`/item/${item.id}`} />
                                <Button color='red' content='Delete' onClick={() => handleDelete(item.id)} />
                                <Button icon='tag' onClick={() => openModal(item.id)} />
                            </ItemExtra>
                        </ItemContent>
                    </Item>
                ))}
            </Item.Group>
            <Modal open={isModalOpen} onClose={() => setIsModalOpen(false)}>
                <Modal.Header>Select a Tag for the Item</Modal.Header>
                <Modal.Content>
                    <Dropdown
                        placeholder='Select Tag'
                        fluid
                        selection
                        options={tags.map(tag => ({ key: tag.id, text: tag.tagId, value: tag.id }))}
                        onChange={handleTagChange}
                    />
                </Modal.Content>
                <Modal.Actions>
                    <Button color='green' onClick={updateTagForItem}>Update</Button>
                    <Button onClick={() => setIsModalOpen(false)}>Cancel</Button>
                </Modal.Actions>
            </Modal>
        </Segment>
    );
});
