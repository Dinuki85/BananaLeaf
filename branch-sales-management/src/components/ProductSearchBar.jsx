import React from 'react';
import { MagnifyingGlassIcon } from '@heroicons/react/24/outline';

const ProductSearchBar = ({ searchTerm, setSearchTerm }) => {
    return (
        <div className="relative max-w-md w-full">
            <span className="absolute inset-y-0 left-0 pl-3 flex items-center text-gray-400">
                <MagnifyingGlassIcon className="w-5 h-5" />
            </span>
            <input
                type="text"
                placeholder="Search products by name..."
                className="w-full pl-10 pr-4 py-2 bg-white border border-gray-200 rounded-xl focus:ring-2 focus:ring-green-500/20 focus:border-green-600 outline-none transition-all shadow-sm"
                value={searchTerm}
                onChange={(e) => setSearchTerm(e.target.value)}
            />
        </div>
    );
};

export default ProductSearchBar;
