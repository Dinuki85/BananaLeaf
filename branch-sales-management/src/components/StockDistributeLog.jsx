import React, { useState, useEffect } from 'react';
import { ChevronDownIcon, ChevronUpIcon, ArrowUpTrayIcon } from '@heroicons/react/24/outline';
import api from '../services/api';

const fmt = (dt) => {
    if (!dt) return '—';
    const d = new Date(dt);
    return d.toLocaleString('en-GB', {
        day: '2-digit', month: 'short', year: 'numeric',
        hour: '2-digit', minute: '2-digit', second: '2-digit', hour12: false
    });
};

// Group batches by branch + receivedAt (rounded to nearest minute for grouping)
const groupBatches = (batches) => {
    const groups = [];
    const map = new Map();

    batches.forEach(batch => {
        const branchId = batch.branch?.id;
        const ts = batch.receivedAt ? new Date(batch.receivedAt).toISOString().slice(0, 16) : 'unknown'; // minute precision
        const key = `${branchId}-${ts}`;

        if (!map.has(key)) {
            const group = {
                key,
                branch: batch.branch,
                receivedAt: batch.receivedAt,
                items: [],
                totalItems: 0,
            };
            map.set(key, group);
            groups.push(group);
        }
        const g = map.get(key);
        g.items.push(batch);
        g.totalItems += batch.quantity || 0;
    });

    return groups;
};

const StockDistributeLog = () => {
    const [batches, setBatches] = useState([]);
    const [loading, setLoading] = useState(true);
    const [expanded, setExpanded] = useState({});

    useEffect(() => {
        const fetchLog = async () => {
            setLoading(true);
            try {
                const res = await api.get('/stock/log/distribute');
                setBatches(res.data || []);
            } catch (err) {
                console.error('Error fetching distribute log:', err);
                setBatches([]);
            } finally {
                setLoading(false);
            }
        };
        fetchLog();
    }, []);

    const toggleExpand = (key) =>
        setExpanded(prev => ({ ...prev, [key]: !prev[key] }));

    const groups = groupBatches(batches);

    return (
        <div className="space-y-3">
            {loading ? (
                <div className="flex justify-center py-10">
                    <div className="animate-spin rounded-full h-10 w-10 border-b-2 border-indigo-600" />
                </div>
            ) : groups.length === 0 ? (
                <div className="text-center py-12 text-gray-400 text-sm">No distribution records found.</div>
            ) : (
                <>
                    <div className="text-xs text-gray-400 text-right">{groups.length} distribution event{groups.length !== 1 ? 's' : ''}</div>
                    {groups.map((group) => (
                        <div key={group.key} className="bg-white border border-gray-100 rounded-xl shadow-sm overflow-hidden">
                            {/* Header row */}
                            <div className="flex items-center justify-between px-5 py-4">
                                <div className="flex items-center gap-4">
                                    <div className="h-10 w-10 bg-indigo-50 rounded-lg flex items-center justify-center flex-shrink-0">
                                        <ArrowUpTrayIcon className="h-5 w-5 text-indigo-600" />
                                    </div>
                                    <div>
                                        <div className="text-sm font-semibold text-gray-900">
                                            {group.branch?.name || 'Unknown Branch'}
                                        </div>
                                        <div className="text-xs text-gray-400 mt-0.5">{fmt(group.receivedAt)}</div>
                                    </div>
                                </div>
                                <div className="flex items-center gap-6">
                                    <div className="text-right hidden sm:block">
                                        <div className="text-xs text-gray-400 uppercase tracking-wider">Products</div>
                                        <div className="text-sm font-bold text-gray-900">{group.items.length}</div>
                                    </div>
                                    <div className="text-right hidden sm:block">
                                        <div className="text-xs text-gray-400 uppercase tracking-wider">Total Units</div>
                                        <div className="text-sm font-bold text-indigo-700">{group.totalItems.toLocaleString()}</div>
                                    </div>
                                    <button
                                        onClick={() => toggleExpand(group.key)}
                                        className="flex items-center gap-1.5 text-xs font-medium text-green-700 hover:text-green-900 bg-green-50 hover:bg-green-100 px-3 py-1.5 rounded-lg transition-all"
                                    >
                                        {expanded[group.key] ? (
                                            <><ChevronUpIcon className="w-3.5 h-3.5" /> View Less</>
                                        ) : (
                                            <><ChevronDownIcon className="w-3.5 h-3.5" /> View More</>
                                        )}
                                    </button>
                                </div>
                            </div>

                            {/* Expanded items table */}
                            {expanded[group.key] && (
                                <div className="border-t border-gray-100 bg-gray-50">
                                    <table className="min-w-full text-sm">
                                        <thead>
                                            <tr className="border-b border-gray-200">
                                                <th className="px-5 py-2.5 text-left text-xs font-semibold text-gray-500 uppercase tracking-wider">Product</th>
                                                <th className="px-5 py-2.5 text-right text-xs font-semibold text-gray-500 uppercase tracking-wider">Qty Distributed</th>
                                                <th className="px-5 py-2.5 text-right text-xs font-semibold text-gray-500 uppercase tracking-wider">Remaining</th>
                                                <th className="px-5 py-2.5 text-right text-xs font-semibold text-gray-500 uppercase tracking-wider">Purchase Price</th>
                                                <th className="px-5 py-2.5 text-left text-xs font-semibold text-gray-500 uppercase tracking-wider">Date &amp; Time</th>
                                            </tr>
                                        </thead>
                                        <tbody className="divide-y divide-gray-100">
                                            {group.items.map(item => (
                                                <tr key={item.id} className="hover:bg-white transition-colors">
                                                    <td className="px-5 py-3 font-medium text-gray-900">
                                                        {item.product?.name || '—'}
                                                        <div className="text-xs text-gray-400">{item.product?.code || ''}</div>
                                                    </td>
                                                    <td className="px-5 py-3 text-right font-bold text-gray-900">{item.quantity?.toLocaleString()}</td>
                                                    <td className="px-5 py-3 text-right text-gray-600">{item.remainingQuantity?.toLocaleString()}</td>
                                                    <td className="px-5 py-3 text-right text-indigo-700 font-medium">Rs.{(item.purchasePrice || 0).toLocaleString()}</td>
                                                    <td className="px-5 py-3 text-gray-500 text-xs">{fmt(item.receivedAt)}</td>
                                                </tr>
                                            ))}
                                        </tbody>
                                    </table>
                                </div>
                            )}
                        </div>
                    ))}
                </>
            )}
        </div>
    );
};

export default StockDistributeLog;
