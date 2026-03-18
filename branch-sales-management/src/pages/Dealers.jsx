import React, { useState, useEffect } from 'react';
import api from '../services/api';
import DealerTable from '../components/DealerTable';
import DealerModal from '../components/DealerModal';
import { PlusIcon, UserGroupIcon, MagnifyingGlassIcon, FunnelIcon } from '@heroicons/react/24/outline';

const Dealers = () => {
    const [dealers, setDealers] = useState([]);
    const [filteredDealers, setFilteredDealers] = useState([]);
    const [loading, setLoading] = useState(true);
    const [submitting, setSubmitting] = useState(false);
    const [isModalOpen, setIsModalOpen] = useState(false);
    const [selectedDealer, setSelectedDealer] = useState(null);
    const [searchTerm, setSearchTerm] = useState('');
    const [statusFilter, setStatusFilter] = useState('all'); // all, active, inactive

    const fetchDealers = async () => {
        try {
            setLoading(true);
            const response = await api.get('/dealers');
            setDealers(response.data);
            setFilteredDealers(response.data);
        } catch (err) {
            console.error('Error fetching dealers:', err);
        } finally {
            setLoading(false);
        }
    };

    useEffect(() => {
        fetchDealers();
    }, []);

    useEffect(() => {
        let result = dealers;

        if (statusFilter !== 'all') {
            const isActive = statusFilter === 'active';
            result = result.filter(d => d.isActive === isActive);
        }

        if (searchTerm) {
            result = result.filter(d => 
                d.name.toLowerCase().includes(searchTerm.toLowerCase()) ||
                (d.phone && d.phone.includes(searchTerm))
            );
        }

        setFilteredDealers(result);
    }, [dealers, searchTerm, statusFilter]);

    const handleSave = async (formData) => {
        try {
            setSubmitting(true);
            if (selectedDealer) {
                await api.put(`/dealers/${selectedDealer.id}`, formData);
            } else {
                await api.post('/dealers', formData);
            }
            fetchDealers();
            setIsModalOpen(false);
        } catch (err) {
            console.error('Error saving dealer:', err);
        } finally {
            setSubmitting(false);
        }
    };

    const openModal = (dealer = null) => {
        setSelectedDealer(dealer);
        setIsModalOpen(true);
    };

    return (
        <div className="space-y-8 pb-12">
            <div className="flex flex-col md:flex-row md:items-center justify-between gap-4">
                <div>
                    <h2 className="text-2xl font-bold text-gray-800 flex items-center">
                        <UserGroupIcon className="w-8 h-8 mr-3 text-green-600" />
                        Dealer Network
                    </h2>
                    <p className="text-sm text-gray-500 mt-1">Manage external partners and supplier records.</p>
                </div>
                <button
                    onClick={() => openModal()}
                    className="flex items-center space-x-2 bg-green-600 hover:bg-green-700 text-white px-6 py-2.5 rounded-xl font-bold transition-all shadow-md active:scale-95"
                >
                    <PlusIcon className="w-5 h-5" />
                    <span>Register Dealer</span>
                </button>
            </div>

            {/* Controls Section */}
            <div className="grid grid-cols-1 md:grid-cols-3 gap-6">
                <div className="md:col-span-2 relative">
                    <span className="absolute inset-y-0 left-0 pl-3 flex items-center text-gray-400">
                        <MagnifyingGlassIcon className="w-5 h-5" />
                    </span>
                    <input
                        type="text"
                        placeholder="Search by dealer name or phone..."
                        className="w-full pl-10 pr-4 py-3 bg-white border border-gray-200 rounded-2xl shadow-sm focus:ring-2 focus:ring-green-500/20 focus:border-green-600 outline-none transition-all"
                        value={searchTerm}
                        onChange={(e) => setSearchTerm(e.target.value)}
                    />
                </div>

                <div className="relative">
                    <span className="absolute inset-y-0 left-0 pl-3 flex items-center text-gray-400">
                        <FunnelIcon className="w-5 h-5" />
                    </span>
                    <select
                        className="w-full pl-10 pr-4 py-3 bg-white border border-gray-200 rounded-2xl shadow-sm focus:ring-2 focus:ring-green-500/20 focus:border-green-600 outline-none transition-all appearance-none font-semibold text-gray-700"
                        value={statusFilter}
                        onChange={(e) => setStatusFilter(e.target.value)}
                    >
                        <option value="all">All Statuses</option>
                        <option value="active">Active Only</option>
                        <option value="inactive">Inactive Only</option>
                    </select>
                    <div className="absolute inset-y-0 right-0 pr-3 flex items-center pointer-events-none text-gray-400">
                        <svg className="h-5 w-5" fill="none" viewBox="0 0 24 24" stroke="currentColor font-bold">
                            <path strokeLinecap="round" strokeLinejoin="round" strokeWidth="2" d="M19 9l-7 7-7-7" />
                        </svg>
                    </div>
                </div>
            </div>

            <section className="bg-white p-2 rounded-3xl shadow-sm border border-gray-100">
                <DealerTable 
                    dealers={filteredDealers} 
                    loading={loading}
                    onEdit={openModal}
                />
            </section>

            <DealerModal 
                isOpen={isModalOpen}
                onClose={() => setIsModalOpen(false)}
                dealer={selectedDealer}
                onSave={handleSave}
                submitting={submitting}
            />
        </div>
    );
};

export default Dealers;
