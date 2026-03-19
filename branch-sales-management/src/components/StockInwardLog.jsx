import React, { useState, useEffect } from 'react';
import { ChevronDownIcon, ChevronUpIcon, FunnelIcon, ArrowDownTrayIcon } from '@heroicons/react/24/outline';
import api from '../services/api';

const fmt = (dt) => {
    if (!dt) return '—';
    const d = new Date(dt);
    return d.toLocaleString('en-GB', {
        day: '2-digit', month: 'short', year: 'numeric',
        hour: '2-digit', minute: '2-digit', second: '2-digit', hour12: false
    });
};

const StockInwardLog = ({ dealers }) => {
    const [entries, setEntries] = useState([]);
    const [loading, setLoading] = useState(true);
    const [filterDealer, setFilterDealer] = useState('');
    const [filterDate, setFilterDate] = useState('');
    const [expanded, setExpanded] = useState({});

    const fetchLog = async () => {
        setLoading(true);
        try {
            const params = {};
            if (filterDealer) params.dealerId = filterDealer;
            if (filterDate) params.date = filterDate;
            const res = await api.get('/stock/log/inward', { params });
            setEntries(res.data || []);
        } catch (err) {
            console.error('Error fetching inward log:', err);
            setEntries([]);
        } finally {
            setLoading(false);
        }
    };

    useEffect(() => { fetchLog(); }, [filterDealer, filterDate]);

    const toggleExpand = (id) =>
        setExpanded(prev => ({ ...prev, [id]: !prev[id] }));

    const clearFilters = () => { setFilterDealer(''); setFilterDate(''); };

    return (
        <div className="space-y-4">
            {/* Filter Bar */}
            <div className="flex flex-wrap items-center gap-3 bg-green-50 border border-green-100 rounded-xl px-4 py-3">
                <div className="flex items-center gap-1.5 text-green-700 font-semibold text-sm">
                    <FunnelIcon className="w-4 h-4" />
                    Filter:
                </div>
                <select
                    className="pl-3 pr-8 py-2 rounded-lg text-sm border border-green-200 bg-white focus:ring-2 focus:ring-green-400 outline-none"
                    value={filterDealer}
                    onChange={e => setFilterDealer(e.target.value)}
                >
                    <option value="">All Dealers</option>
                    {(dealers || []).map(d => (
                        <option key={d.id} value={d.id}>{d.name}</option>
                    ))}
                </select>
                <input
                    type="date"
                    className="px-3 py-2 rounded-lg text-sm border border-green-200 bg-white focus:ring-2 focus:ring-green-400 outline-none"
                    value={filterDate}
                    onChange={e => setFilterDate(e.target.value)}
                />
                {(filterDealer || filterDate) && (
                    <button
                        onClick={clearFilters}
                        className="text-xs text-red-500 hover:text-red-700 font-medium underline"
                    >
                        Clear Filters
                    </button>
                )}
                <span className="ml-auto text-xs text-gray-400">{entries.length} record{entries.length !== 1 ? 's' : ''}</span>
            </div>

            {/* Entries */}
            {loading ? (
                <div className="flex justify-center py-10">
                    <div className="animate-spin rounded-full h-10 w-10 border-b-2 border-green-600" />
                </div>
            ) : entries.length === 0 ? (
                <div className="text-center py-12 text-gray-400 text-sm">No stock-in records found.</div>
            ) : (
                <div className="space-y-3">
                    {entries.map((entry) => (
                        <div key={entry.id} className="bg-white border border-gray-100 rounded-xl shadow-sm overflow-hidden">
                            {/* Header row */}
                            <div className="flex items-center justify-between px-5 py-4">
                                <div className="flex items-center gap-4">
                                    <div className="h-10 w-10 bg-green-50 rounded-lg flex items-center justify-center flex-shrink-0">
                                        <ArrowDownTrayIcon className="h-5 w-5 text-green-600" />
                                    </div>
                                    <div>
                                        <div className="text-sm font-semibold text-gray-900">
                                            {entry.dealer?.name || 'Unknown Dealer'}
                                        </div>
                                        <div className="text-xs text-gray-400 mt-0.5">{fmt(entry.createdAt)}</div>
                                    </div>
                                </div>
                                <div className="flex items-center gap-6">
                                    <div className="text-right hidden sm:block">
                                        <div className="text-xs text-gray-400 uppercase tracking-wider">Quantity</div>
                                        <div className="text-sm font-bold text-gray-900">{entry.quantity?.toLocaleString()}</div>
                                    </div>
                                    <div className="text-right hidden sm:block">
                                        <div className="text-xs text-gray-400 uppercase tracking-wider">Cost Price</div>
                                        <div className="text-sm font-bold text-green-700">Rs.{(entry.costPrice || 0).toLocaleString()}</div>
                                    </div>
                                    <button
                                        onClick={() => toggleExpand(entry.id)}
                                        className="flex items-center gap-1.5 text-xs font-medium text-indigo-600 hover:text-indigo-800 bg-indigo-50 hover:bg-indigo-100 px-3 py-1.5 rounded-lg transition-all"
                                    >
                                        {expanded[entry.id] ? (
                                            <><ChevronUpIcon className="w-3.5 h-3.5" /> View Less</>
                                        ) : (
                                            <><ChevronDownIcon className="w-3.5 h-3.5" /> View More</>
                                        )}
                                    </button>
                                </div>
                            </div>

                            {/* Expanded detail */}
                            {expanded[entry.id] && (
                                <div className="border-t border-gray-100 bg-gray-50 px-5 py-4">
                                    <div className="grid grid-cols-2 sm:grid-cols-4 gap-4 text-sm">
                                        <div>
                                            <div className="text-xs text-gray-400 uppercase tracking-wider mb-1">Product</div>
                                            <div className="font-semibold text-gray-900">{entry.product?.name || '—'}</div>
                                            <div className="text-xs text-gray-400">Code: {entry.product?.code || '—'}</div>
                                        </div>
                                        <div>
                                            <div className="text-xs text-gray-400 uppercase tracking-wider mb-1">Quantity Added</div>
                                            <div className="font-bold text-gray-900">{entry.quantity?.toLocaleString()}</div>
                                        </div>
                                        <div>
                                            <div className="text-xs text-gray-400 uppercase tracking-wider mb-1">Cost Price / Unit</div>
                                            <div className="font-bold text-green-700">Rs.{(entry.costPrice || 0).toLocaleString()}</div>
                                        </div>
                                        <div>
                                            <div className="text-xs text-gray-400 uppercase tracking-wider mb-1">Remaining</div>
                                            <div className="font-bold text-gray-900">{entry.remainingQuantity?.toLocaleString()}</div>
                                        </div>
                                        <div className="col-span-2 sm:col-span-4">
                                            <div className="text-xs text-gray-400 uppercase tracking-wider mb-1">Date &amp; Time Added</div>
                                            <div className="font-medium text-gray-700">{fmt(entry.createdAt)}</div>
                                        </div>
                                    </div>
                                </div>
                            )}
                        </div>
                    ))}
                </div>
            )}
        </div>
    );
};

export default StockInwardLog;
