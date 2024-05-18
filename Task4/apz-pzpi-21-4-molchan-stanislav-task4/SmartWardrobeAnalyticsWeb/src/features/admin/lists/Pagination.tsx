import React from 'react';
import { Pagination as SemanticPagination, PaginationProps } from 'semantic-ui-react';
import { observer } from 'mobx-react-lite';
import { useStore } from '../../../app/stores/store';

const Pagination = observer(() => {
    const { itemStore } = useStore();

    const handlePageChange = (event: React.MouseEvent<HTMLAnchorElement>, data: PaginationProps) => {
        if (data.activePage) {
            itemStore.setPageNumber(Number(data.activePage));
        }
    };

    return (
        <SemanticPagination
            activePage={itemStore.pageNumber}
            totalPages={itemStore.totalPages}
            onPageChange={handlePageChange}
        />
    );
});

export default Pagination;