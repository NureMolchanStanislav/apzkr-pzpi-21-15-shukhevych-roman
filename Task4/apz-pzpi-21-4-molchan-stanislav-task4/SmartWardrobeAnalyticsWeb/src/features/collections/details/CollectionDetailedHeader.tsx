/* eslint-disable react-hooks/rules-of-hooks */
/* eslint-disable react-refresh/only-export-components */
import { observer } from "mobx-react-lite";
import { useState, useEffect } from "react";
import { useTranslation } from "react-i18next";
import { Link, useParams } from "react-router-dom";
import { Segment, Item, Header, Button } from "semantic-ui-react";
import { useStore } from "../../../app/stores/store";
import { Collection } from "../../../app/models/collection";
import LoadingComponents from "../../../app/layout/LoadingComponents";


interface Props {
    collectionProps: Collection | undefined
}

export default observer(function CollectionDetailedHeader({collectionProps}: Props) {

    const {userStore: {user, logout, isLoggedIn}} = useStore();
    const {collectionStore} = useStore();
    const {loadCollection, loadingInitial} = collectionStore;
    const {id} = useParams();

    useEffect(() => {
        console.log(collectionProps.name);
    });

    return (
        <Segment.Group>
            <Segment basic attached='top' style={{padding: '0'}}>
                <Segment>
                    <Item.Group>
                        <Item>
                            <Item.Content>
                                <Header
                                    size='huge'
                                    content={collectionProps.name}
                                    style={{color: 'black'}}
                                />
                            </Item.Content>
                            <Button as={Link} to={`/update/collections/${collectionProps.id}`} color='pink' content='Edit'></Button>
                        </Item>
                    </Item.Group>
                </Segment>
            </Segment>
        </Segment.Group>
    );
});