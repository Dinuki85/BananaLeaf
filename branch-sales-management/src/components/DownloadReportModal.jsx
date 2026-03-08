import React, { useState, useEffect } from 'react';
import api from '../services/api';
import Modal from './Modal';
import { 
    CalendarIcon, 
    BuildingStorefrontIcon, 
    ArrowDownTrayIcon 
} from '@heroicons/react/24/outline';

const DownloadReportModal = ({ isOpen, onClose }) => {
    const [branches, setBranches] = useState([]);
    const [branchId, setBranchId] = useState('');
    const [startDate, setStartDate] = useState('');
    const [endDate, setEndDate] = useState('');
    const [loading, setLoading] = useState(false);

    useEffect(() => {
        if (isOpen) {
            fetchBranches();
            // Set default dates (current month)
            const now = new Date();
            const firstDay = new Date(now.getFullYear(), now.getMonth(), 1);
            const lastDay = new Date(now.getFullYear(), now.getMonth() + 1, 0);
            setStartDate(firstDay.toISOString().split('T')[0]);
            setEndDate(lastDay.toISOString().split('T')[0]);
        }
    }, [isOpen]);

    const fetchBranches = async () => {
        try {
            const response = await api.get('/branches');
            setBranches(response.data);
        } catch (err) {
            console.error('Error fetching branches:', err);
        }
    };

    const handleDownload = async () => {
        if (!startDate || !endDate) {
            alert('Please select both start and end dates.');
            return;
        }

        setLoading(true);
        try {
            const params = { startDate, endDate };
            if (branchId) {
                params.branchId = branchId;
            }

            const response = await api.get('/reports/sales/download', {
                params,
                responseType: 'blob'
            });

            // Create a blob from the response data
            const file = new Blob([response.data], { type: 'application/pdf' });
            
            // Create a link element, hide it, direct it to the blob, and click it
            const fileURL = URL.createObjectURL(file);
            const link = document.createElement('a');
            link.href = fileURL;
            link.setAttribute('download', `sales_report_${startDate}_to_${endDate}.pdf`);
            document.body.appendChild(link);
            link.click();
            
            // Clean up
            document.body.removeChild(link);
            URL.revokeObjectURL(fileURL);
            
            onClose();
        } catch (err) {
            console.error('Error downloading report:', err);
            alert('Failed to download report. Please try again.');
        } finally {
            setLoading(false);
        }
    };

    return (
        <Modal
            isOpen={isOpen}
            onClose={onClose}
            title="Download Sales Report"
        >
            <div className="space-y-6">
                <p className="text-gray-500 text-sm">
                    Select a branch and date range to filter the sales report. The report will be generated as a professional PDF.
                </p>

                <div className="space-y-4">
                    {/* Branch Selection */}
                    <div>
                        <label className="block text-sm font-bold text-gray-700 mb-2 flex items-center">
                            <BuildingStorefrontIcon className="w-4 h-4 mr-2 text-green-600" />
                            Select Branch
                        </label>
                        <select
                            value={branchId}
                            onChange={(e) => setBranchId(e.target.value)}
                            className="w-full p-3 rounded-xl border border-gray-200 focus:ring-2 focus:ring-green-500 focus:border-green-500 outline-none transition-all bg-gray-50"
                        >
                            <option value="">All Branches</option>
                            {branches.map((branch) => (
                                <option key={branch.id} value={branch.id}>
                                    {branch.name}
                                </option>
                            ))}
                        </select>
                    </div>

                    {/* Date Range */}
                    <div className="grid grid-cols-2 gap-4">
                        <div>
                            <label className="block text-sm font-bold text-gray-700 mb-2 flex items-center">
                                <CalendarIcon className="w-4 h-4 mr-2 text-green-600" />
                                Start Date
                            </label>
                            <input
                                type="date"
                                value={startDate}
                                onChange={(e) => setStartDate(e.target.value)}
                                className="w-full p-3 rounded-xl border border-gray-200 focus:ring-2 focus:ring-green-500 focus:border-green-500 outline-none transition-all bg-gray-50"
                            />
                        </div>
                        <div>
                            <label className="block text-sm font-bold text-gray-700 mb-2 flex items-center">
                                <CalendarIcon className="w-4 h-4 mr-2 text-green-600" />
                                End Date
                            </label>
                            <input
                                type="date"
                                value={endDate}
                                onChange={(e) => setEndDate(e.target.value)}
                                className="w-full p-3 rounded-xl border border-gray-200 focus:ring-2 focus:ring-green-500 focus:border-green-500 outline-none transition-all bg-gray-50"
                            />
                        </div>
                    </div>
                </div>

                {/* Single Action Button */}
                <div className="pt-4">
                    <button
                        onClick={handleDownload}
                        disabled={loading}
                        className={`w-full flex items-center justify-center space-x-2 py-4 rounded-2xl font-bold text-white shadow-lg transition-all active:scale-95 ${
                            loading 
                            ? 'bg-gray-400 cursor-not-allowed' 
                            : 'bg-green-600 hover:bg-green-700 shadow-green-200'
                        }`}
                    >
                        {loading ? (
                            <div className="w-5 h-5 border-2 border-white/30 border-t-white rounded-full animate-spin"></div>
                        ) : (
                            <ArrowDownTrayIcon className="w-6 h-6" />
                        )}
                        <span>{loading ? 'Generating Report...' : 'Download Report'}</span>
                    </button>
                </div>
            </div>
        </Modal>
    );
};

export default DownloadReportModal;
