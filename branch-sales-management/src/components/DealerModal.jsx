import React, { useState, useEffect } from 'react';
import Modal from './Modal';
import { UserIcon, PhoneIcon, ChevronRightIcon } from '@heroicons/react/24/outline';

const DealerModal = ({ isOpen, onClose, dealer, onSave, submitting }) => {
    const [formData, setFormData] = useState({
        name: '',
        phone: '',
        isActive: true
    });

    useEffect(() => {
        if (dealer) {
            setFormData({
                name: dealer.name || '',
                phone: dealer.phone || '',
                isActive: dealer.isActive !== undefined ? dealer.isActive : true
            });
        } else {
            setFormData({ name: '', phone: '', isActive: true });
        }
    }, [dealer, isOpen]);

    const handleSubmit = (e) => {
        e.preventDefault();
        onSave(formData);
    };

    return (
        <Modal
            isOpen={isOpen}
            onClose={onClose}
            title={dealer ? 'Update Dealer Details' : 'Register New Dealer'}
        >
            <form onSubmit={handleSubmit} className="space-y-6">
                <div className="space-y-4">
                    <div className="space-y-2">
                        <label className="text-xs font-bold text-gray-500 uppercase tracking-widest px-1">Dealer Name</label>
                        <div className="relative">
                            <span className="absolute inset-y-0 left-0 pl-3 flex items-center text-gray-400">
                                <UserIcon className="w-5 h-5" />
                            </span>
                            <input
                                type="text"
                                required
                                placeholder="e.g. ABC Suppliers"
                                className="w-full pl-10 pr-4 py-2.5 bg-gray-50 border border-gray-200 rounded-xl focus:ring-2 focus:ring-green-500/20 focus:border-green-600 outline-none transition-all"
                                value={formData.name}
                                onChange={(e) => setFormData({ ...formData, name: e.target.value })}
                            />
                        </div>
                    </div>

                    <div className="space-y-2">
                        <label className="text-xs font-bold text-gray-500 uppercase tracking-widest px-1">Phone Number</label>
                        <div className="relative">
                            <span className="absolute inset-y-0 left-0 pl-3 flex items-center text-gray-400">
                                <PhoneIcon className="w-5 h-5" />
                            </span>
                            <input
                                type="text"
                                placeholder="e.g. 0112223334"
                                className="w-full pl-10 pr-4 py-2.5 bg-gray-50 border border-gray-200 rounded-xl focus:ring-2 focus:ring-green-500/20 focus:border-green-600 outline-none transition-all"
                                value={formData.phone}
                                onChange={(e) => setFormData({ ...formData, phone: e.target.value })}
                            />
                        </div>
                    </div>

                    <div className="flex items-center justify-between p-4 bg-gray-50 rounded-2xl border border-gray-100">
                        <div className="space-y-0.5">
                            <p className="text-sm font-bold text-gray-800">Active Status</p>
                            <p className="text-xs text-gray-500">Deactivated dealers won't show in selection lists</p>
                        </div>
                        <button
                            type="button"
                            onClick={() => setFormData({ ...formData, isActive: !formData.isActive })}
                            className={`relative inline-flex h-6 w-11 items-center rounded-full transition-colors focus:outline-none focus:ring-2 focus:ring-green-500/20 ${
                                formData.isActive ? 'bg-green-600' : 'bg-gray-300'
                            }`}
                        >
                            <span
                                className={`inline-block h-4 w-4 transform rounded-full bg-white transition-transform ${
                                    formData.isActive ? 'translate-x-6' : 'translate-x-1'
                                }`}
                            />
                        </button>
                    </div>
                </div>

                <div className="flex justify-end pt-4">
                    <button
                        type="submit"
                        disabled={submitting}
                        className={`group flex items-center space-x-2 px-8 py-3 rounded-xl font-bold text-white transition-all shadow-lg ${
                            submitting 
                            ? 'bg-green-400 cursor-not-allowed' 
                            : 'bg-green-600 hover:bg-green-700 active:scale-95 hover:shadow-green-600/30'
                        }`}
                    >
                        <span>{dealer ? 'Save Changes' : 'Register Dealer'}</span>
                        {!submitting && <ChevronRightIcon className="w-4 h-4 group-hover:translate-x-1 transition-transform" />}
                    </button>
                </div>
            </form>
        </Modal>
    );
};

export default DealerModal;
