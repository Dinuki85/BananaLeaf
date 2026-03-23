import React from 'react';
import ToggleSwitch from './ToggleSwitch';

const BranchPriceTable = ({ branchSettings, onPriceChange, onAvailabilityToggle, defaultPrice }) => {
    return (
        <div className="overflow-x-auto">
            <table className="w-full text-left border-collapse">
                <thead>
                    <tr className="bg-gray-50 text-gray-600 uppercase text-xs font-semibold">
                        <th className="px-4 py-3 border-b border-gray-100">Branch Name</th>
                        <th className="px-4 py-3 border-b border-gray-100">Branch Price</th>
                        <th className="px-4 py-3 border-b border-gray-100 text-center">Availability</th>
                        <th className="px-4 py-3 border-b border-gray-100 text-center">Status</th>
                    </tr>
                </thead>
                <tbody className="divide-y divide-gray-50">
                    {branchSettings.map((setting) => (
                        <tr key={setting.branchId} className="hover:bg-gray-50/50 transition-colors">
                            <td className="px-4 py-4 font-medium text-gray-700">{setting.branchName}</td>
                            <td className="px-4 py-4">
                                <div className="relative max-w-[120px]">
                                    <span className="absolute left-3 top-1/2 -translate-y-1/2 text-gray-400">$</span>
                                    <input
                                        type="number"
                                        step="0.01"
                                        className="w-full pl-7 pr-3 py-1.5 bg-white border border-gray-200 rounded-lg focus:ring-2 focus:ring-green-500/20 focus:border-green-600 outline-none text-sm"
                                        value={setting.branchPrice === null ? '' : setting.branchPrice}
                                        placeholder={defaultPrice ? `$${defaultPrice.toFixed(2)} (Default)` : "0.00"}
                                        onChange={(e) => onPriceChange(setting.branchId, e.target.value)}
                                    />
                                </div>
                            </td>
                            <td className="px-4 py-4">
                                <div className="flex justify-center">
                                    <ToggleSwitch
                                        enabled={setting.isAvailable}
                                        setEnabled={() => onAvailabilityToggle(setting.branchId)}
                                    />
                                </div>
                            </td>
                            <td className="px-4 py-4 text-center">
                                {setting.isPriceUpdated ? (
                                    <span className="px-2 py-1 text-[10px] font-bold uppercase tracking-wider rounded-full bg-blue-100 text-blue-700">
                                        Overridden
                                    </span>
                                ) : (
                                    <span className="px-2 py-1 text-[10px] font-bold uppercase tracking-wider rounded-full bg-gray-100 text-gray-500">
                                        Default
                                    </span>
                                )}
                            </td>
                        </tr>
                    ))}
                </tbody>
            </table>
        </div>
    );
};

export default BranchPriceTable;
