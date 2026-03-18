import React from 'react';
import StatusBadge from './StatusBadge';
import { PencilSquareIcon } from '@heroicons/react/24/outline';

const DealerTable = ({ dealers, onEdit, onDelete, loading }) => {
    if (loading) {
        return (
            <div className="flex justify-center py-10">
                <div className="w-8 h-8 border-4 border-green-200 border-t-green-600 rounded-full animate-spin"></div>
            </div>
        );
    }

    if (!dealers || dealers.length === 0) {
        return (
            <div className="text-center py-10 text-gray-500 italic bg-gray-50 rounded-2xl border border-dashed border-gray-200">
                No dealers found matching your filter.
            </div>
        );
    }

    return (
        <div className="overflow-x-auto rounded-2xl border border-gray-100 shadow-sm">
            <table className="w-full text-left bg-white">
                <thead className="bg-gray-50 text-gray-600 text-xs uppercase tracking-wider font-bold">
                    <tr>
                        <th className="px-6 py-4">Dealer Name</th>
                        <th className="px-6 py-4">Phone Number</th>
                        <th className="px-6 py-4">Status</th>
                        <th className="px-6 py-4 text-right">Actions</th>
                    </tr>
                </thead>
                <tbody className="divide-y divide-gray-50">
                    {dealers.map((dealer) => (
                        <tr key={dealer.id} className="hover:bg-gray-50 transition-colors group">
                            <td className="px-6 py-4 font-semibold text-gray-800">{dealer.name}</td>
                            <td className="px-6 py-4 text-gray-600">{dealer.phone || 'N/A'}</td>
                            <td className="px-6 py-4">
                                <span className={`px-2 py-1 rounded-full text-[10px] font-bold uppercase tracking-tighter ${
                                    dealer.isActive 
                                    ? 'bg-green-100 text-green-700' 
                                    : 'bg-red-100 text-red-700'
                                }`}>
                                    {dealer.isActive ? 'Active' : 'Inactive'}
                                </span>
                            </td>
                            <td className="px-6 py-4 text-right">
                                <div className="flex justify-end space-x-2">
                                    <button
                                        onClick={() => onEdit(dealer)}
                                        className="p-2 text-gray-400 hover:text-green-600 hover:bg-green-50 rounded-lg transition-all"
                                        title="Edit Dealer"
                                    >
                                        <PencilSquareIcon className="w-5 h-5" />
                                    </button>
                                </div>
                            </td>
                        </tr>
                    ))}
                </tbody>
            </table>
        </div>
    );
};

export default DealerTable;
