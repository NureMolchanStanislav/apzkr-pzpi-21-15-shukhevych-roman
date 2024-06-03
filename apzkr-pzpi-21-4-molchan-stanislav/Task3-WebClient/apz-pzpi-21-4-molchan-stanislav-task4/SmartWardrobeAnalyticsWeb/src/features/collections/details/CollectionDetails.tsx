/* eslint-disable react-refresh/only-export-components */
import { observer } from "mobx-react-lite";
import { useState, useEffect } from "react";
import { useParams } from "react-router-dom";
import { Grid, Header, Item, Segment } from "semantic-ui-react";
import LoadingComponents from "../../../app/layout/LoadingComponents";
import { useStore } from "../../../app/stores/store";
import CollectionDetailedHeader from "./CollectionDetailedHeader";


export default observer(function CollectionDetails(){
  const {collectionStore} = useStore();
  const {selectedCollection: collection, loadCollection, loadingInitial} = collectionStore;
  const {id} = useParams();

  useEffect(() => {
    if (id && !collection) {
        console.log("Loading collection with ID: " + id);
        loadCollection(id);
    }
  }, [id, loadCollection, collection]);

  useEffect(() => {
    if (collection) {
        console.log("Updated collection: ", collection);
        console.log("Name collection: ", collection.name);
    }
  }, [collection]);

  if (loadingInitial || !collection) return <LoadingComponents />;

  return (
    <Grid>
        <Grid.Column width={10}>
            <CollectionDetailedHeader collectionProps={collection} />
        </Grid.Column>
    </Grid>
);
});