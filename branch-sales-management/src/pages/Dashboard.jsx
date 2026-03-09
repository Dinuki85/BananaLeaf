import React, { useEffect, useState } from 'react';
import api from '../services/api';
import SummaryCard from '../components/SummaryCard';
import {
    BuildingStorefrontIcon,
    ShoppingBagIcon,
    CurrencyDollarIcon
} from '@heroicons/react/24/outline';

const Dashboard = () => {
    const [summary, setSummary] = useState({
        offlineBranches: 0,
        totalProducts: 0,
        totalRevenue: 0
    });
    const [loading, setLoading] = useState(true);
    const [error, setError] = useState(null);

    useEffect(() => {
        const fetchSummary = async () => {
            try {
                setLoading(true);
                const response = await api.get('/dashboard/summary');
                setSummary(response.data);
                setError(null);
            } catch (err) {
                console.error('Error fetching dashboard summary:', err);
                setError('Failed to load dashboard data. Please ensure the backend is running.');
            } finally {
                setLoading(false);
            }
        };

        fetchSummary();
    }, []);

    if (loading) {
        return (
            <div className="flex items-center justify-center h-64">
                <div className="w-8 h-8 border-4 border-green-200 border-t-green-600 rounded-full animate-spin"></div>
            </div>
        );
    }

    return (
        <div className="flex flex-col min-h-[calc(100vh-12rem)]">
            <div className="flex-1 space-y-8">
                <div className="flex items-center justify-between">
                    <h2 className="text-2xl font-bold text-gray-800">Dashboard Overview</h2>

                </div>

                {error && (
                    <div className="p-4 bg-red-50 border border-red-100 text-red-600 rounded-xl text-sm">
                        {error}
                    </div>
                )}

                <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-6">
                    <SummaryCard
                        label="Offline Branches"
                        value={summary.offlineBranches}
                        icon={BuildingStorefrontIcon}
                        colorClass="text-red-600"
                    />
                    <SummaryCard
                        label="Total Products"
                        value={summary.totalProducts.toLocaleString()}
                        icon={ShoppingBagIcon}
                        colorClass="text-green-600"
                    />
                    <SummaryCard
                        label="Total Revenue Synced"
                        value={`$${summary.totalRevenue.toLocaleString()}`}
                        icon={CurrencyDollarIcon}
                        colorClass="text-green-700"
                    />
                </div>

                <div className="grid grid-cols-1 lg:grid-cols-2 gap-8">
                    {/* Branch Performance Section */}
                    <div className="bg-white p-6 rounded-2xl shadow-sm border border-gray-100">
                        <div className="flex items-center justify-between mb-6">
                            <h3 className="text-lg font-bold text-gray-800">Branch Performance</h3>

                        </div>
                        <div className="space-y-4">
                            {summary.branchSales && summary.branchSales.length > 0 ? (
                                summary.branchSales.map((branch, index) => (
                                    <div key={index} className="flex items-center justify-between p-4 bg-gray-50 rounded-xl border border-gray-100 hover:border-green-200 transition-colors group">
                                        <div className="flex items-center space-x-3">
                                            <div className="w-10 h-10 bg-white rounded-lg flex items-center justify-center text-green-600 shadow-sm border border-gray-100 group-hover:bg-green-600 group-hover:text-white transition-all">
                                                <BuildingStorefrontIcon className="w-5 h-5" />
                                            </div>
                                            <div>
                                                <p className="font-bold text-gray-800">{branch.branchName}</p>
                                                <div className="w-32 h-1.5 bg-gray-200 rounded-full mt-1 overflow-hidden">
                                                    <div
                                                        className="h-full bg-green-500 rounded-full"
                                                        style={{ width: `${(branch.totalSales / summary.totalRevenue * 100) || 0}%` }}
                                                    ></div>
                                                </div>
                                            </div>
                                        </div>
                                        <div className="text-right">
                                            <p className="font-black text-gray-900">${branch.totalSales.toLocaleString(undefined, { minimumFractionDigits: 2 })}</p>
                                            <p className="text-xs text-gray-400 font-semibold">{((branch.totalSales / summary.totalRevenue * 100) || 0).toFixed(1)}% share</p>
                                        </div>
                                    </div>
                                ))
                            ) : (
                                <div className="text-center py-12 text-gray-400 font-medium italic">
                                    No branch sales data available yet.
                                </div>
                            )}
                        </div>
                    </div>

                    {/* Operational Insights Section */}
                    <div className="bg-white p-8 rounded-2xl shadow-sm border border-gray-100 flex flex-col items-center justify-center text-center space-y-4">
                        <div className="w-16 h-16 bg-green-50 rounded-full flex items-center justify-center text-green-600 shadow-inner">
                            <CurrencyDollarIcon className="w-8 h-8" />
                        </div>
                        <div>
                            <h3 className="text-lg font-bold text-gray-800">Operational Insights</h3>
                            <p className="text-gray-500 text-sm max-w-xs mx-auto mt-2 leading-relaxed">
                                Advanced analytics and branch performance charts will be visualised here once more data is synced from the branches.
                            </p>
                            <button className="mt-6 px-6 py-2 bg-gray-900 text-white rounded-full text-xs font-bold hover:bg-gray-800 transition-colors shadow-lg shadow-gray-200 uppercase tracking-widest">
                                View Analytics
                            </button>
                        </div>
                    </div>
                </div>
            </div>

            {/* Dashboard Specific Footer */}
            <footer className="mt-auto pt-12 pb-6 text-center text-sm text-gray-400 font-medium">
                &copy; 2026 All Rights Reserved
            </footer>
        </div>
    );
};

export default Dashboard;
