/* eslint-disable react-refresh/only-export-components */
import React, { useEffect, useState } from 'react';
import { observer } from 'mobx-react-lite';
import { useStore } from '../../app/stores/store';
import { Table, Segment, Header, Loader, Dropdown, Form } from 'semantic-ui-react';
import { PieChart, Pie, Tooltip, Cell, ResponsiveContainer, Legend, BarChart, Bar, XAxis, YAxis } from 'recharts';

const COLORS = ['#0088FE', '#00C49F', '#FFBB28', '#FF8042'];

const SeasonalItemUsageStatistics = observer(() => {
    const { statisticsStore, brandStore } = useStore();
    const { seasonalItemUsage, loadSeasonalItemUsage, loading } = statisticsStore;
    const { brands, loadForUserBrands } = brandStore;

    const [selectedBrand, setSelectedBrand] = useState('');

    useEffect(() => {
        loadForUserBrands();
    }, [loadForUserBrands]);

    const handleBrandChange = (e, { value }) => {
        setSelectedBrand(value);
        loadSeasonalItemUsage(value);
    };

    if (loading) return <Loader active inline='centered' />;

    const brandOptions = brands.map(brand => ({ key: brand.id, text: brand.name, value: brand.id }));

    const chartData = seasonalItemUsage.map(seasonData => ({
        name: seasonData.season,
        value: seasonData.totalUsages,
    }));

    const barChartData = seasonalItemUsage.map(seasonData => ({
        season: seasonData.season,
        totalUsages: seasonData.totalUsages,
    }));

    return (
        <Segment>
            <Header as='h2'>Seasonal Item Usage Statistics</Header>
            <Form>
                <Form.Field>
                    <label>Select Brand</label>
                    <Dropdown
                        placeholder='Select Brand'
                        fluid
                        selection
                        options={brandOptions}
                        onChange={handleBrandChange}
                    />
                </Form.Field>
            </Form>
            {seasonalItemUsage.length === 0 ? (
                <Header as='h3' textAlign='center'>No statistics available for the selected brand</Header>
            ) : (
                <>
                    <Table celled>
                        <Table.Header>
                            <Table.Row>
                                <Table.HeaderCell>Season</Table.HeaderCell>
                                <Table.HeaderCell>Item</Table.HeaderCell>
                                <Table.HeaderCell>Usage Count</Table.HeaderCell>
                            </Table.Row>
                        </Table.Header>
                        <Table.Body>
                            {seasonalItemUsage.map((seasonData, index) => (
                                <React.Fragment key={index}>
                                    {Object.entries(seasonData.itemUsages).map(([item, count], i) => (
                                        <Table.Row key={i}>
                                            {i === 0 && (
                                                <Table.Cell rowSpan={Object.keys(seasonData.itemUsages).length}>
                                                    {seasonData.season}
                                                </Table.Cell>
                                            )}
                                            <Table.Cell>{item}</Table.Cell>
                                            <Table.Cell>{count}</Table.Cell>
                                        </Table.Row>
                                    ))}
                                </React.Fragment>
                            ))}
                        </Table.Body>
                    </Table>
                    <Header as='h3'>Total Usages by Season (Pie Chart)</Header>
                    <ResponsiveContainer width="100%" height={400}>
                        <PieChart>
                            <Pie
                                data={chartData}
                                cx="50%"
                                cy="50%"
                                labelLine={false}
                                label={({ name, percent }) => `${name}: ${(percent * 100).toFixed(0)}%`}
                                outerRadius={150}
                                fill="#8884d8"
                                dataKey="value"
                            >
                                {chartData.map((entry, index) => (
                                    <Cell key={`cell-${index}`} fill={COLORS[index % COLORS.length]} />
                                ))}
                            </Pie>
                            <Tooltip />
                            <Legend />
                        </PieChart>
                    </ResponsiveContainer>
                    <Header as='h3'>Total Usages by Season (Bar Chart)</Header>
                    <ResponsiveContainer width="100%" height={400}>
                        <BarChart data={barChartData} margin={{ top: 20, right: 30, left: 20, bottom: 5 }}>
                            <XAxis dataKey="season" />
                            <YAxis />
                            <Tooltip />
                            <Legend />
                            <Bar dataKey="totalUsages" fill="#82ca9d" />
                        </BarChart>
                    </ResponsiveContainer>
                </>
            )}
        </Segment>
    );
});

export default SeasonalItemUsageStatistics;
