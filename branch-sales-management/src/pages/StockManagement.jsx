import React, { useState, useEffect } from 'react';
import {
    PlusIcon, ArrowRightIcon, ClipboardDocumentListIcon,
    CubeIcon, MagnifyingGlassIcon
} from '@heroicons/react/24/outline';
import api from '../services/api';
import StockTable from '../components/StockTable';
import StockInwardModal from '../components/StockInwardModal';
import StockDistributionModal from '../components/StockDistributionModal';
import StockInwardLog from '../components/StockInwardLog';
import StockDistributeLog from '../components/StockDistributeLog';

const TABS = [
    { id: 'stock',     label: 'View Stock',  icon: CubeIcon },
    { id: 'add',       label: 'Add Stock',   icon: PlusIcon },
    { id: 'distribute',label: 'Distribute',  icon: ArrowRightIcon },
    { id: 'logs',      label: 'Logs',        icon: ClipboardDocumentListIcon },
];

const TAB_COLORS = {
    stock:      { active: 'bg-green-600  text-white shadow-md', inactive: 'bg-white text-green-700 border border-green-200 hover:bg-green-50' },
    add:        { active: 'bg-indigo-600 text-white shadow-md', inactive: 'bg-white text-indigo-700 border border-indigo-200 hover:bg-indigo-50' },
    distribute: { active: 'bg-blue-600   text-white shadow-md', inactive: 'bg-white text-blue-700 border border-blue-200 hover:bg-blue-50' },
    logs:       { active: 'bg-amber-500  text-white shadow-md', inactive: 'bg-white text-amber-600 border border-amber-200 hover:bg-amber-50' },
};

const LOG_TABS = [
    { id: 'inward',     label: '📥 Stock In Log' },
    { id: 'distribute', label: '📤 Distribute Log' },
];

const StockManagement = () => {
    const [products, setProducts] = useState([]);
    const [dealers, setDealers] = useState([]);
    const [branches, setBranches] = useState([]);
    const [centralStock, setCentralStock] = useState([]);
    const [loading, setLoading] = useState(true);
    const [searchTerm, setSearchTerm] = useState('');

    const [activeTab, setActiveTab] = useState('stock');
    const [isInwardModalOpen, setIsInwardModalOpen]     = useState(false);
    const [isDistributeModalOpen, setIsDistributeModalOpen] = useState(false);
    const [activeLogTab, setActiveLogTab] = useState('inward');

    const fetchData = async () => {
        setLoading(true);
        try { const r = await api.get('/products');  setProducts(r.data || []); }  catch {}
        try { const r = await api.get('/dealers');   setDealers(r.data || []); }   catch {}
        try { const r = await api.get('/branches');  setBranches(r.data || []); }  catch {}
        try {
            const r = await api.get('/stock/central');
            setCentralStock(r.data || []);
        } catch { setCentralStock([]); }
        finally { setLoading(false); }
    };

    useEffect(() => { fetchData(); }, []);

    const filteredProducts = products.filter(p =>
        (p.name?.toLowerCase().includes(searchTerm.toLowerCase()) ||
         p.code?.toLowerCase().includes(searchTerm.toLowerCase()) ||
         p.sku?.toLowerCase().includes(searchTerm.toLowerCase()))
    );

    const handleTabClick = (id) => {
        if (id === 'add') {
            setIsInwardModalOpen(true);
        } else if (id === 'distribute') {
            setIsDistributeModalOpen(true);
        } else {
            setActiveTab(id);
        }
    };

    return (
        <div className="space-y-6">
            {/* Header */}
            <div>
                <h1 className="text-2xl font-bold text-gray-900">Stock Management</h1>
                <p className="text-sm text-gray-500 mt-1">Manage central inventory and branch distribution.</p>
            </div>

            {/* 4-Button Bar */}
            <div className="flex flex-wrap gap-3">
                {TABS.map(({ id, label, icon: Icon }) => {
                    const isActive = activeTab === id && id !== 'add' && id !== 'distribute';
                    const colors = TAB_COLORS[id];
                    return (
                        <button
                            key={id}
                            onClick={() => handleTabClick(id)}
                            className={`flex items-center gap-2 px-5 py-2.5 rounded-xl font-medium text-sm transition-all ${
                                isActive ? colors.active : colors.inactive
                            }`}
                        >
                            <Icon className="w-4 h-4" />
                            {label}
                        </button>
                    );
                })}
            </div>

            {/* === CONTENT AREA === */}

            {/* View Stock */}
            {activeTab === 'stock' && (
                <section className="bg-white p-6 rounded-2xl shadow-sm border border-gray-100">
                    <div className="relative mb-6">
                        <MagnifyingGlassIcon className="absolute left-3 top-1/2 -translate-y-1/2 w-5 h-5 text-gray-400" />
                        <input
                            type="text"
                            placeholder="Search product name, code or SKU..."
                            className="w-full pl-10 pr-4 py-2.5 rounded-xl border border-gray-200 focus:border-green-500 focus:ring-1 focus:ring-green-500 transition-all sm:text-sm"
                            value={searchTerm}
                            onChange={(e) => setSearchTerm(e.target.value)}
                        />
                    </div>
                    <StockTable
                        products={filteredProducts}
                        centralStock={centralStock}
                        loading={loading}
                        branches={branches}
                    />
                </section>
            )}

            {/* Logs */}
            {activeTab === 'logs' && (
                <section className="bg-white rounded-2xl shadow-sm border border-amber-100 overflow-hidden">
                    <div className="flex items-center border-b border-gray-100 bg-amber-50/60 px-6 pt-4 gap-1">
                        <ClipboardDocumentListIcon className="w-5 h-5 text-amber-600 mr-2 flex-shrink-0" />
                        <h2 className="text-base font-bold text-gray-800 mr-4">Addition Logs</h2>
                        {LOG_TABS.map(t => (
                            <button
                                key={t.id}
                                onClick={() => setActiveLogTab(t.id)}
                                className={`px-5 py-2.5 text-sm font-semibold rounded-t-lg border-b-2 transition-all ${
                                    activeLogTab === t.id
                                        ? 'border-amber-500 text-amber-700 bg-white'
                                        : 'border-transparent text-gray-500 hover:text-gray-700 hover:bg-white/60'
                                }`}
                            >
                                {t.label}
                            </button>
                        ))}
                    </div>
                    <div className="p-6">
                        {activeLogTab === 'inward'
                            ? <StockInwardLog dealers={dealers} />
                            : <StockDistributeLog />
                        }
                    </div>
                </section>
            )}

            {/* Modals */}
            <StockInwardModal
                isOpen={isInwardModalOpen}
                onClose={() => setIsInwardModalOpen(false)}
                onSuccess={fetchData}
                products={products}
                dealers={dealers}
            />
            <StockDistributionModal
                isOpen={isDistributeModalOpen}
                onClose={() => setIsDistributeModalOpen(false)}
                onSuccess={fetchData}
                products={products}
                branches={branches}
            />
        </div>
    );
};

export default StockManagement;
