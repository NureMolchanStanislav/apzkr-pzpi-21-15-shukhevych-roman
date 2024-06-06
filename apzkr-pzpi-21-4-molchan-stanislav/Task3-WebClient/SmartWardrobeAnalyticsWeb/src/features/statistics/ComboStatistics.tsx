/* eslint-disable react-refresh/only-export-components */
import '../../app/layout/styles.css';
import React, { useEffect, useState } from 'react';
import { observer } from 'mobx-react-lite';
import { useStore } from '../../app/stores/store';
import { Table, Segment, Header, Loader, Checkbox } from 'semantic-ui-react';

const ComboStatistics = observer(() => {
    const { statisticsStore } = useStore();
    const { comboStatistics, loadComboStatistics, loading } = statisticsStore;
    const [is24HourFormat, setIs24HourFormat] = useState(true);

    useEffect(() => {
        loadComboStatistics();
    }, [loadComboStatistics]);

    const handleFormatChange = () => {
        setIs24HourFormat(!is24HourFormat);
    };

    const formatDate = (dateString) => {
        const date = new Date(dateString);
        return is24HourFormat
            ? date.toLocaleString('en-GB') // 24-hour format
            : date.toLocaleString('en-US', { hour12: true }); // AM/PM format
    };

    if (loading) return <Loader active inline='centered' />;

    return (
        <Segment>
            <Header as='h2'>Combo Statistics</Header>
            <Checkbox 
                toggle 
                label={`Show time in ${is24HourFormat ? 'AM/PM' : '24-hour'} format`} 
                checked={is24HourFormat} 
                onChange={handleFormatChange} 
                style={{ marginBottom: '1em' }}
                className="black-toggle"
            />
            <Table celled>
                <Table.Header>
                    <Table.Row>
                        <Table.HeaderCell>Combination</Table.HeaderCell>
                        <Table.HeaderCell>Usage Count</Table.HeaderCell>
                        <Table.HeaderCell>Date</Table.HeaderCell>
                    </Table.Row>
                </Table.Header>
                <Table.Body>
                    {comboStatistics.map((combo, index) => (
                        <Table.Row key={index}>
                            <Table.Cell>
                                {combo.combination.join(', ')}
                            </Table.Cell>
                            <Table.Cell>{combo.usageCount}</Table.Cell>
                            <Table.Cell>{formatDate(combo.date)}</Table.Cell>
                        </Table.Row>
                    ))}
                </Table.Body>
            </Table>
        </Segment>
    );
});

export default ComboStatistics;
