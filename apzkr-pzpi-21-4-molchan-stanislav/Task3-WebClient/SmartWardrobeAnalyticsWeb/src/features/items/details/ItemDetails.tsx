/* eslint-disable react-refresh/only-export-components */
import { observer } from "mobx-react-lite";
import React, { useEffect } from "react";
import { useParams } from "react-router-dom";
import { Grid, Header, Segment } from "semantic-ui-react";
import LoadingComponents from "../../../app/layout/LoadingComponents";
import { useStore } from "../../../app/stores/store";
import ItemUsageStatistics from "./ItemUsageStatistics";

export default observer(function ItemDetails() {
  const { itemStore } = useStore();
  const { selectedItem: item, loadItem, loadingInitial } = itemStore;
  const { id } = useParams();

  useEffect(() => {
    if (id && !item) {
      loadItem(id);
    }
  }, [id, loadItem, item]);  // Ensure that you don't reload if 'item' is already set

  if (loadingInitial) return <LoadingComponents />;

  if (!item) return <Segment><Header>Item not found</Header></Segment>;  // Handle the case when item is null after loading

  return (
    <Grid>
      <Grid.Column width={10}>
        <Segment>
          <Header size='large'>{item.name}</Header>
          <p>{item.description}</p>
        </Segment>
        <ItemUsageStatistics itemId={id} />
      </Grid.Column>
    </Grid>
  );
});