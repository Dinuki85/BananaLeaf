import React, { useState, useEffect } from 'react';
import BranchPriceTable from './BranchPriceTable';
import { XMarkIcon } from '@heroicons/react/24/outline';
import api from '../services/api';

const EditProductModal = ({ isOpen, onClose, product, onUpdateSuccess }) => {
    const [branchSettings, setBranchSettings] = useState([]);
    const [originalSettings, setOriginalSettings] = useState([]);
    const [loading, setLoading] = useState(false);
    const [saving, setSaving] = useState(false);

    useEffect(() => {
        if (isOpen && product) {
            fetchBranchSettings();
        }
    }, [isOpen, product]);

    const fetchBranchSettings = async () => {
        try {
            setLoading(true);
            const response = await api.get(`/products/${product.id}/branches`);
            setBranchSettings(response.data);
            setOriginalSettings(JSON.parse(JSON.stringify(response.data)));
        } catch (err) {
            console.error('Error fetching branch settings:', err);
        } finally {
            setLoading(false);
        }
    };

    const handlePriceChange = (branchId, value) => {
        setBranchSettings(prev => prev.map(s => 
            s.branchId === branchId ? { ...s, branchPrice: value === '' ? null : parseFloat(value) } : s
        ));
    };

    const handleAvailabilityToggle = (branchId) => {
        setBranchSettings(prev => prev.map(s => 
            s.branchId === branchId ? { ...s, isAvailable: !s.isAvailable } : s
        ));
    };

    const handleUpdate = async () => {
        const updates = branchSettings.filter((setting, index) => {
            const original = originalSettings[index];
            return setting.branchPrice !== original.branchPrice || setting.isAvailable !== original.isAvailable;
        });

        if (updates.length === 0) {
            onClose();
            return;
        }

        try {
            setSaving(true);
            await api.put('/branch-products/update', { updates });
            onUpdateSuccess();
            onClose();
        } catch (err) {
            console.error('Error updating branch products:', err);
            alert('Failed to update branch settings.');
        } finally {
            setSaving(false);
        }
    };

    if (!isOpen) return null;

    return (
        <div className="fixed inset-0 z-50 flex items-center justify-center p-4 bg-black/50 backdrop-blur-sm transition-all">
            <div className="bg-white w-full max-w-4xl rounded-3xl shadow-2xl overflow-hidden flex flex-col max-h-[90vh]">
                {/* Header */}
                <div className="p-6 border-b border-gray-100 flex items-center justify-between bg-white sticky top-0 z-10">
                    <div>
                        <h3 className="text-xl font-bold text-gray-800">
                            Configure: {product?.name}
                        </h3>
                        <p className="text-sm text-gray-500 mt-1">
                            Modify branch-specific pricing and availability.
                        </p>
                    </div>
                    <button
                        onClick={onClose}
                        className="p-2 hover:bg-gray-100 rounded-full transition-colors text-gray-400 hover:text-gray-600"
                    >
                        <XMarkIcon className="w-6 h-6" />
                    </button>
                </div>

                {/* Content */}
                <div className="flex-1 overflow-y-auto p-6">
                    {loading ? (
                        <div className="flex flex-col items-center justify-center py-12 space-y-4">
                            <div className="w-12 h-12 border-4 border-green-600/20 border-t-green-600 rounded-full animate-spin"></div>
                            <p className="text-gray-500 font-medium">Loading branch data...</p>
                        </div>
                    ) : (
                        <BranchPriceTable 
                            branchSettings={branchSettings}
                            onPriceChange={handlePriceChange}
                            onAvailabilityToggle={handleAvailabilityToggle}
                            defaultPrice={product?.sellingPrice}
                        />
                    )}
                </div>

                {/* Footer */}
                <div className="p-6 border-t border-gray-100 bg-gray-50 flex justify-end space-x-3">
                    <button
                        onClick={onClose}
                        className="px-6 py-2.5 rounded-xl font-semibold text-gray-600 hover:bg-gray-200 transition-all"
                    >
                        Cancel
                    </button>
                    <button
                        onClick={handleUpdate}
                        disabled={saving || loading}
                        className={`px-8 py-2.5 rounded-xl font-semibold text-white shadow-lg shadow-green-600/20 transition-all ${
                            saving ? 'bg-green-400' : 'bg-green-600 hover:bg-green-700 active:scale-95'
                        }`}
                    >
                        {saving ? 'Updating...' : 'Update Records'}
                    </button>
                </div>
            </div>
        </div>
    );
};

export default EditProductModal;
