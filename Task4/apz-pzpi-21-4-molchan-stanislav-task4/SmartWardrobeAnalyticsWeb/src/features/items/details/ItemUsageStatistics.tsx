/* eslint-disable react-refresh/only-export-components */
import React, { useEffect, useState } from 'react';
import { observer } from 'mobx-react-lite';
import { useStore } from '../../../app/stores/store';
import { BarChart, Bar, XAxis, YAxis, Tooltip, Legend, ResponsiveContainer, Table } from 'recharts';
import { Segment, Header, Table as SuiTable } from 'semantic-ui-react';
import LoadingComponents from '../../../app/layout/LoadingComponents';

// Define the type for usage statistics data
interface UsageData {
  month: string;
  usageCount: number;
}

// Define the type for item usage instances
interface ItemUsageInstance {
  name: string;
  date: string;
}

// Define the props for the ItemUsageStatistics component
interface ItemUsageStatisticsProps {
  itemId: string;
}

export default observer(function ItemUsageStatistics({ itemId }: ItemUsageStatisticsProps) {
  const { itemStore } = useStore();
  const [usageData, setUsageData] = useState<UsageData[] | null>(null);
  const [itemUsages, setItemUsages] = useState<ItemUsageInstance[] | null>(null);

  useEffect(() => {
    if (itemId && usageData === null) {
        itemStore.getMonthlyItemUsageStatistics(itemId)
            .then(data => setUsageData(data as UsageData[]))
            .catch(error => console.error("Error loading item usage data:", error));

        itemStore.getUsages(itemId)
            .then(data => setItemUsages(data))
            .catch(error => console.error("Error loading item usages:", error));
    }
}, [itemId, itemStore, usageData]);

if (!usageData || !itemUsages) return <LoadingComponents content="Loading usage data..." />;

  // Convert the usage data into a format suitable for recharts
  const chartData = usageData.map(stat => ({
    Month: stat.month,
    'Usage Count': stat.usageCount,
  }));

  return (
    <Segment>
      <Header>Total Usage: {usageData.reduce((acc, stat) => acc + stat.usageCount, 0)}</Header>
      <ResponsiveContainer width="100%" height={400}>
        <BarChart data={chartData} key={Date.now()}>
          <XAxis dataKey="Month" />
          <YAxis />
          <Tooltip />
          <Legend />
          <Bar dataKey="Usage Count" fill="#8884d8" />
        </BarChart>
      </ResponsiveContainer>
      <Header as='h3'>Item Usage Instances</Header>
      <SuiTable celled>
        <SuiTable.Header>
          <SuiTable.Row>
            <SuiTable.HeaderCell>Name</SuiTable.HeaderCell>
            <SuiTable.HeaderCell>Date</SuiTable.HeaderCell>
          </SuiTable.Row>
        </SuiTable.Header>
        <SuiTable.Body>
          {itemUsages.map((usage, index) => (
            <SuiTable.Row key={index}>
              <SuiTable.Cell>{usage.name}</SuiTable.Cell>
              <SuiTable.Cell>{usage.date}</SuiTable.Cell>
            </SuiTable.Row>
          ))}
        </SuiTable.Body>
      </SuiTable>
    </Segment>
  );
});
