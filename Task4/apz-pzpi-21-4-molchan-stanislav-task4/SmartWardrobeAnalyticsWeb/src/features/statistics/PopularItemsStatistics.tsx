/* eslint-disable react-refresh/only-export-components */
import React, { useEffect, useState } from 'react';
import { observer } from 'mobx-react-lite';
import { useStore } from '../../app/stores/store';
import { Table, Segment, Header, Loader, Dropdown, Form, Button } from 'semantic-ui-react';
import DatePicker from 'react-datepicker';
import 'react-datepicker/dist/react-datepicker.css';
import { format } from 'date-fns';
import { PieChart, Pie, Tooltip, Cell, ResponsiveContainer, Legend, BarChart, Bar, XAxis, YAxis } from 'recharts';

const COLORS = ['#0088FE', '#00C49F', '#FFBB28', '#FF8042'];

const PopularItemsStatistics = observer(() => {
    const { statisticsStore, brandStore } = useStore();
    const { popularItems, loadPopularItems, loading } = statisticsStore;
    const { brands, loadForUserBrands } = brandStore;

    const [selectedBrand, setSelectedBrand] = useState('');
    const [startDate, setStartDate] = useState(null);
    const [endDate, setEndDate] = useState(null);
    const [topCount, setTopCount] = useState(5);

    useEffect(() => {
        loadForUserBrands();
    }, [loadForUserBrands]);

    const handleBrandChange = (e, { value }) => setSelectedBrand(value);
    const handleStartDateChange = (date) => setStartDate(date);
    const handleEndDateChange = (date) => setEndDate(date);
    const handleTopCountChange = (e, { value }) => setTopCount(value);

    const handleSubmit = () => {
        if (startDate && endDate) {
            const formattedStartDate = format(startDate, "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
            const formattedEndDate = format(endDate, "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
            loadPopularItems(selectedBrand, formattedStartDate, formattedEndDate, topCount);
        }
    };

    if (loading) return <Loader active inline='centered' />;

    const brandOptions = brands.map(brand => ({ key: brand.id, text: brand.name, value: brand.id }));

    const chartData = popularItems.map(item => ({
        name: item.itemName,
        value: item.usageCount,
    }));

    return (
        <Segment>
            <Header as='h2'>Popular Items Statistics</Header>
            <Form onSubmit={handleSubmit}>
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
                <Form.Field>
                    <label>Select Start Date</label>
                    <DatePicker
                        selected={startDate}
                        onChange={handleStartDateChange}
                        dateFormat="yyyy-MM-dd"
                        placeholderText="Start Date"
                        isClearable
                        showTimeSelect
                        timeFormat="HH:mm"
                        timeIntervals={15}
                        timeCaption="time"
                    />
                </Form.Field>
                <Form.Field>
                    <label>Select End Date</label>
                    <DatePicker
                        selected={endDate}
                        onChange={handleEndDateChange}
                        dateFormat="yyyy-MM-dd"
                        placeholderText="End Date"
                        isClearable
                        showTimeSelect
                        timeFormat="HH:mm"
                        timeIntervals={15}
                        timeCaption="time"
                    />
                </Form.Field>
                <Form.Field>
                    <label>Select Top Count</label>
                    <Dropdown
                        placeholder='Select Top Count'
                        fluid
                        selection
                        options={[1, 3, 5, 10].map(count => ({ key: count, text: count, value: count }))}
                        onChange={handleTopCountChange}
                        defaultValue={5}
                    />
                </Form.Field>
                <Button type='submit' positive>Get Statistics</Button>
            </Form>
            {popularItems.length === 0 ? (
                <Header as='h3' textAlign='center'>No statistics available for the selected criteria</Header>
            ) : (
                <>
                    <Table celled>
                        <Table.Header>
                            <Table.Row>
                                <Table.HeaderCell>Item Name</Table.HeaderCell>
                                <Table.HeaderCell>Usage Count</Table.HeaderCell>
                            </Table.Row>
                        </Table.Header>
                        <Table.Body>
                            {popularItems.map((item, index) => (
                                <Table.Row key={index}>
                                    <Table.Cell>{item.itemName}</Table.Cell>
                                    <Table.Cell>{item.usageCount}</Table.Cell>
                                </Table.Row>
                            ))}
                        </Table.Body>
                    </Table>
                    <Header as='h3'>Total Usages by Item (Pie Chart)</Header>
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
                    <Header as='h3'>Total Usages by Item (Bar Chart)</Header>
                    <ResponsiveContainer width="100%" height={400}>
                        <BarChart data={chartData} margin={{ top: 20, right: 30, left: 20, bottom: 5 }}>
                            <XAxis dataKey="name" />
                            <YAxis />
                            <Tooltip />
                            <Legend />
                            <Bar dataKey="value" fill="#82ca9d" />
                        </BarChart>
                    </ResponsiveContainer>
                </>
            )}
        </Segment>
    );
});

export default PopularItemsStatistics;
