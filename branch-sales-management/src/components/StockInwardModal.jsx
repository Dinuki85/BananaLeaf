import React, { useState } from 'react';
import Modal from './Modal';
import { CubeIcon, UserIcon, CalculatorIcon, CurrencyDollarIcon, ChevronRightIcon } from '@heroicons/react/24/outline';
import api from '../services/api';

const StockInwardModal = ({ isOpen, onClose, onSuccess, products, dealers }) => {
    const [formData, setFormData] = useState({
        productId: '',
        dealerId: '',
        quantity: '',
        costPrice: ''
    });
    const [loading, setLoading] = useState(false);

    const handleSubmit = async (e) => {
        e.preventDefault();
        setLoading(true);
        try {
            await api.post('/stock/inward', {
                ...formData,
                productId: Number(formData.productId),
                dealerId: Number(formData.dealerId),
                quantity: Number(formData.quantity),
                costPrice: Number(formData.costPrice)
            });
            onSuccess();
            onClose();
            setFormData({ productId: '', dealerId: '', quantity: '', costPrice: '' });
        } catch (err) {
            console.error('Error recording stock inward:', err);
        } finally {
            setLoading(false);
        }
    };

    return (
        <Modal isOpen={isOpen} onClose={onClose} title="Stock Inward Entry">
            <form onSubmit={handleSubmit} className="space-y-6">
                <div className="space-y-4">
                    <div className="space-y-2">
                        <label className="text-xs font-bold text-gray-500 uppercase tracking-widest px-1">Select Product</label>
                        <div className="relative">
                            <span className="absolute inset-y-0 left-0 pl-3 flex items-center text-gray-400">
                                <CubeIcon className="w-5 h-5" />
                            </span>
                            <select
                                required
                                className="w-full pl-10 pr-4 py-2.5 bg-gray-50 border border-gray-200 rounded-xl focus:ring-2 focus:ring-green-500/20 focus:border-green-600 outline-none transition-all appearance-none"
                                value={formData.productId}
                                onChange={(e) => setFormData({ ...formData, productId: e.target.value })}
                            >
                                <option value="">Choose a product...</option>
                                {products.map(p => (
                                    <option key={p.id} value={p.id}>{p.name}</option>
                                ))}
                            </select>
                        </div>
                    </div>

                    <div className="space-y-2">
                        <label className="text-xs font-bold text-gray-500 uppercase tracking-widest px-1">Source Dealer</label>
                        <div className="relative">
                            <span className="absolute inset-y-0 left-0 pl-3 flex items-center text-gray-400">
                                <UserIcon className="w-5 h-5" />
                            </span>
                            <select
                                required
                                className="w-full pl-10 pr-4 py-2.5 bg-gray-50 border border-gray-200 rounded-xl focus:ring-2 focus:ring-green-500/20 focus:border-green-600 outline-none transition-all appearance-none"
                                value={formData.dealerId}
                                onChange={(e) => setFormData({ ...formData, dealerId: e.target.value })}
                            >
                                <option value="">Choose a dealer...</option>
                                {dealers.map(d => (
                                    <option key={d.id} value={d.id}>{d.name}</option>
                                ))}
                            </select>
                        </div>
                    </div>

                    <div className="grid grid-cols-2 gap-4">
                        <div className="space-y-2">
                            <label className="text-xs font-bold text-gray-500 uppercase tracking-widest px-1">Quantity</label>
                            <div className="relative">
                                <span className="absolute inset-y-0 left-0 pl-3 flex items-center text-gray-400">
                                    <CalculatorIcon className="w-5 h-5" />
                                </span>
                                <input
                                    type="number"
                                    required
                                    min="0.01"
                                    step="0.01"
                                    placeholder="0.00"
                                    className="w-full pl-10 pr-4 py-2.5 bg-gray-50 border border-gray-200 rounded-xl focus:ring-2 focus:ring-green-500/20 focus:border-green-600 outline-none transition-all"
                                    value={formData.quantity}
                                    onChange={(e) => setFormData({ ...formData, quantity: e.target.value })}
                                />
                            </div>
                        </div>
                        <div className="space-y-2">
                            <label className="text-xs font-bold text-gray-500 uppercase tracking-widest px-1">Purchase Price</label>
                            <div className="relative">
                                <span className="absolute inset-y-0 left-0 pl-3 flex items-center text-gray-400">
                                    <CurrencyDollarIcon className="w-5 h-5" />
                                </span>
                                <input
                                    type="number"
                                    required
                                    min="0.01"
                                    step="0.01"
                                    placeholder="0.00"
                                    className="w-full pl-10 pr-4 py-2.5 bg-gray-50 border border-gray-200 rounded-xl focus:ring-2 focus:ring-green-500/20 focus:border-green-600 outline-none transition-all"
                                    value={formData.costPrice}
                                    onChange={(e) => setFormData({ ...formData, costPrice: e.target.value })}
                                />
                            </div>
                        </div>
                    </div>
                </div>

                <div className="flex justify-end pt-4">
                    <button
                        type="submit"
                        disabled={loading}
                        className={`group flex items-center space-x-2 px-8 py-3 rounded-xl font-bold text-white transition-all shadow-lg ${
                            loading 
                            ? 'bg-green-400 cursor-not-allowed' 
                            : 'bg-green-600 hover:bg-green-700 active:scale-95 hover:shadow-green-600/30'
                        }`}
                    >
                        <span>{loading ? 'Recording...' : 'Record Inward'}</span>
                        {!loading && <ChevronRightIcon className="w-4 h-4 group-hover:translate-x-1 transition-transform" />}
                    </button>
                </div>
            </form>
        </Modal>
    );
};

export default StockInwardModal;
