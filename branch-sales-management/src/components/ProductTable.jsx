import React from 'react';
import DataTable from './DataTable';
import { PencilSquareIcon } from '@heroicons/react/24/outline';

const ProductTable = ({ products, loading, onEdit }) => {
    const columns = [
        { header: 'Product Name', accessor: 'name' },
        {
            header: 'Default Price',
            render: (row) => `Rs.${(row.sellingPrice || 0).toLocaleString(undefined, { minimumFractionDigits: 2 })}`
        },
        {
            header: 'Central Stock Breakdown',
            render: (row) => (
                <div className="space-y-1">
                    {row.centralStock?.map((batch, idx) => (
                        <div key={idx} className="flex items-center space-x-2 text-xs">
                            <span className="px-1.5 py-0.5 bg-gray-100 rounded text-gray-700 font-bold">{batch.remainingQuantity.toLocaleString()}</span>
                            <span className="text-gray-500">at</span>
                            <span className="font-medium text-emerald-600">Rs.{(batch.costPrice || 0).toLocaleString()}</span>
                            <span className="text-[10px] text-gray-400 italic">({batch.dealerName})</span>
                        </div>
                    ))}
                    {(!row.centralStock || row.centralStock.length === 0) && (
                        <span className="text-xs text-red-500 italic">Out of Stock (Central)</span>
                    )}
                </div>
            )
        },
        {
            header: 'Actions',
            render: (row) => (
                <button
                    onClick={() => onEdit(row)}
                    className="p-2 text-blue-600 hover:bg-blue-50 rounded-lg transition-colors"
                    title="Edit Branch Settings"
                >
                    <PencilSquareIcon className="w-5 h-5" />
                </button>
            )
        }
    ];

    return (
        <section className="bg-white rounded-2xl shadow-md border border-gray-100 overflow-hidden">
            <DataTable columns={columns} data={products} loading={loading} />
        </section>
    );
};

export default ProductTable;
