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
    const [branches, setBranches] = useState([]);
    const [loading, setLoading] = useState(true);
    const [error, setError] = useState(null);

    useEffect(() => {
        const fetchData = async () => {
            try {
                setLoading(true);
                const [summaryRes, branchesRes] = await Promise.all([
                    api.get('/dashboard/summary'),
                    api.get('/dashboard/branch-sales')
                ]);
                setSummary(summaryRes.data);
                setBranches(branchesRes.data);
                setError(null);
            } catch (err) {
                console.error('Error fetching dashboard data:', err);
                setError('Failed to load dashboard data. Please ensure the backend is running.');
            } finally {
                setLoading(false);
            }
        };

        fetchData();
    }, []);

    if (loading) {
        return (
            <div className="flex flex-col items-center justify-center h-64 space-y-4">
                <div className="w-12 h-12 border-4 border-green-200 border-t-green-600 rounded-full animate-spin"></div>
                <p className="text-gray-500 font-medium animate-pulse">Fetching today's sales data...</p>
            </div>
        );
    }

    return (
        <div className="flex flex-col min-h-[calc(100vh-12rem)]">
            <div className="flex-1 space-y-10">
                <div className="flex flex-col md:flex-row md:items-end justify-between gap-4">
                    <div>
                        <h2 className="text-3xl font-black text-gray-900 tracking-tight"> Dashboard</h2>
                        <p className="text-gray-500 font-medium">Real-time revenue monitoring across all active branches</p>
                    </div>
                    <div className="flex items-center space-x-2 bg-green-50 px-4 py-2 rounded-full border border-green-100">
                        <div className="w-2.5 h-2.5 bg-green-500 rounded-full animate-pulse"></div>
                        <span className="text-green-700 text-xs font-bold uppercase tracking-widest">Live Sync Alpha</span>
                    </div>
                </div>

                {error && (
                    <div className="p-4 bg-red-50 border border-red-100 text-red-600 rounded-2xl text-sm flex items-center space-x-3 animate-shake">
                        <div className="w-8 h-8 bg-red-100 rounded-full flex items-center justify-center">
                            <span className="font-bold">!</span>
                        </div>
                        <span>{error}</span>
                    </div>
                )}

                {/* Primary Stats Grid */}
                <div className="grid grid-cols-1 md:grid-cols-3 gap-6">
                    <SummaryCard
                        label="Inactive Branches"
                        value={summary.offlineBranches}
                        icon={BuildingStorefrontIcon}
                        colorClass="text-rose-600"
                    />
                    <SummaryCard
                        label="Portfolio Items"
                        value={summary.totalProducts.toLocaleString()}
                        icon={ShoppingBagIcon}
                        colorClass="text-indigo-600"
                    />
                    <SummaryCard
                        label="Aggregate Revenue"
                        value={`Rs. ${summary.totalRevenue.toLocaleString(undefined, { minimumFractionDigits: 2 })}`}
                        icon={CurrencyDollarIcon}
                        colorClass="text-emerald-700"
                    />
                </div>

                {/* Branch Sales Section - THE MAIN FOCUS */}
                <div className="space-y-8">
                    <div className="flex items-center justify-between">
                        <div className="flex items-center space-x-3">
                            <div className="h-8 w-5 bg-green-600 rounded-full shadow-[0_0_15px_rgba(22,163,74,0.5)]"></div>
                            <h3 className="text-2xl font-black text-gray-900 tracking-tight">Branch Performance <span className="text-green-600">Live</span></h3>
                        </div>
                        <div className="text-xs font-bold text-gray-400 uppercase tracking-widest hidden md:block">
                            Updated {new Date().toLocaleTimeString()}
                        </div>
                    </div>

                    <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-5 gap-8">
                        {branches.map((branch, index) => (
                            <div
                                key={branch.branchId}
                                className="group relative bg-white/70 backdrop-blur-xl rounded-[2.5rem] border border-white/40 p-8 shadow-[0_8px_30px_rgb(0,0,0,0.04)] hover:shadow-[0_20px_50px_rgba(34,197,94,0.15)] transition-all duration-700 hover:-translate-y-3 overflow-hidden flex flex-col min-h-[320px]"
                            >
                                {/* Glassmorphism accent */}
                                <div className={`absolute -right-10 -bottom-10 w-40 h-40 rounded-full blur-3xl opacity-10 group-hover:opacity-20 transition-all duration-700 ${index % 5 === 0 ? 'bg-green-400' :
                                        index % 5 === 1 ? 'bg-blue-400' :
                                            index % 5 === 2 ? 'bg-indigo-400' :
                                                index % 5 === 3 ? 'bg-purple-400' :
                                                    'bg-rose-400'
                                    }`}></div>

                                <div className="relative z-10 flex flex-col h-full">
                                    <div className="flex items-center justify-between mb-8">
                                        <div className={`w-14 h-14 rounded-[1.25rem] flex items-center justify-center text-white shadow-xl transition-all group-hover:scale-110 group-hover:rotate-6 duration-500 bg-gradient-to-br ${index % 5 === 0 ? 'from-green-400 to-green-600 shadow-green-200' :
                                                index % 5 === 1 ? 'from-blue-400 to-blue-600 shadow-blue-200' :
                                                    index % 5 === 2 ? 'from-indigo-400 to-indigo-600 shadow-indigo-200' :
                                                        index % 5 === 3 ? 'from-purple-400 to-purple-600 shadow-purple-200' :
                                                            'from-rose-400 to-rose-600 shadow-rose-200'
                                            }`}>
                                            <BuildingStorefrontIcon className="w-7 h-7" />
                                        </div>
                                        <div className="flex flex-col items-end">
                                            <div className="px-3 py-1 bg-green-50/50 backdrop-blur-md text-green-600 text-[10px] font-black rounded-full uppercase tracking-tighter border border-green-100 flex items-center space-x-1">
                                                <div className="w-1 h-1 bg-green-500 rounded-full animate-ping"></div>
                                                <span>Active</span>
                                            </div>
                                        </div>
                                    </div>

                                    <div className="mb-6">
                                        <h2 className="text-gray-400 text-[10px] font-black uppercase tracking-[0.3em] mb-2">{branch.branchName}</h2>
                                        <div className="flex items-baseline">
                                            <span className="text-gray-900 text-lg font-bold mr-1">Rs.</span>
                                            <p className="text-4xl font-black text-gray-900 tracking-tighter transition-all group-hover:tracking-tight duration-500">
                                                {branch.totalSales.toLocaleString(undefined, { minimumFractionDigits: 0, maximumFractionDigits: 0 })}
                                            </p>
                                            <span className="text-gray-400 text-sm font-bold opacity-0 group-hover:opacity-100 transition-opacity duration-500">.{(branch.totalSales % 1).toFixed(2).split('.')[1]}</span>
                                        </div>
                                    </div>

                                    <div className="mt-auto space-y-4">
                                        {/* Invoice Count Metric */}
                                        <div className="flex items-center justify-between p-3 bg-gray-50/50 rounded-2xl border border-gray-100 group-hover:border-white transition-colors duration-500">
                                            <div className="flex items-center space-x-2">
                                                <div className="w-2 h-2 rounded-full bg-gray-300"></div>
                                                <span className="text-[10px] font-bold text-gray-500 uppercase tracking-widest">Invoices</span>
                                            </div>
                                            <span className="text-sm font-black text-gray-800">{branch.invoiceCount || 0}</span>
                                        </div>

                                        {/* Contribution Section */}
                                        <div className="space-y-2">
                                            <div className="flex items-center justify-between">
                                                <p className="text-[10px] font-bold text-gray-400 uppercase tracking-widest">Revenue Share</p>
                                                <p className="text-xs font-black text-gray-800">
                                                    {((branch.totalSales / summary.totalRevenue * 100) || 0).toFixed(1)}%
                                                </p>
                                            </div>
                                            <div className="h-1.5 w-full bg-gray-100 rounded-full overflow-hidden">
                                                <div
                                                    className={`h-full rounded-full transition-all duration-1000 delay-300 ease-out ${index % 5 === 0 ? 'bg-green-500' :
                                                            index % 5 === 1 ? 'bg-blue-500' :
                                                                index % 5 === 2 ? 'bg-indigo-500' :
                                                                    index % 5 === 3 ? 'bg-purple-500' :
                                                                        'bg-rose-500'
                                                        }`}
                                                    style={{ width: `${(branch.totalSales / summary.totalRevenue * 100) || 0}%` }}
                                                ></div>
                                            </div>
                                        </div>
                                    </div>
                                </div>
                            </div>
                        ))}
                    </div>
                </div>

                {/* Footer Insight */}
                <div className="mt-8 bg-gray-900 rounded-[2.5rem] p-10 text-white relative overflow-hidden group">
                    <div className="absolute top-0 right-0 w-64 h-64 bg-green-500 blur-[120px] opacity-20 group-hover:opacity-40 transition-opacity duration-700"></div>
                    <div className="relative z-10 flex flex-col md:flex-row items-center justify-between gap-8">
                        <div className="text-center md:text-left">
                            <h3 className="text-2xl font-black mb-2 tracking-tight">Ready for Deeper Insights?</h3>
                            <p className="text-gray-400 max-w-md font-medium">Your branch sales are synced in real-time. Review the detailed analytics to optimize your inventory and staffing levels across all locations.</p>
                        </div>
                        <button className="whitespace-nowrap px-10 py-4 bg-white text-gray-900 rounded-full font-black hover:scale-105 active:scale-95 transition-all shadow-xl shadow-green-900/20 uppercase tracking-widest text-xs">
                            Launch Sales Analytics
                        </button>
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
