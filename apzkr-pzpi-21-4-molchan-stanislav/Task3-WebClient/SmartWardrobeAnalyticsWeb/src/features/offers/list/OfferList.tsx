import { observer } from 'mobx-react-lite';
import React, { useEffect } from 'react';
import { List, Segment, Header, Table } from 'semantic-ui-react';
import { useStore } from '../../../app/stores/store';
import LoadingComponents from '../../../app/layout/LoadingComponents';

const OfferList = observer(() => {
    const { offerStore } = useStore();
    const { loadOffers, offers, loadingInitial } = offerStore;

    useEffect(() => {
        loadOffers();
    }, [loadOffers]);

    if (loadingInitial) return <LoadingComponents content="Loading offers..." />;

    return (
        <Segment>
            <Header as='h2'>Your Offers</Header>
            <Table celled>
                <Table.Header>
                    <Table.Row>
                        <Table.HeaderCell>Brand Name</Table.HeaderCell>
                        <Table.HeaderCell>Discount</Table.HeaderCell>
                    </Table.Row>
                </Table.Header>
                <Table.Body>
                    {offers.map((offer, index) => (
                        <Table.Row key={index}>
                            <Table.Cell style={{ fontSize: '1.2em', fontWeight: 'bold' }}>{offer.brandName}</Table.Cell>
                            <Table.Cell style={{ fontSize: '1.2em' }}>{offer.discount}%</Table.Cell>
                        </Table.Row>
                    ))}
                </Table.Body>
            </Table>
        </Segment>
    );
});

export default OfferList;