const http = require('http');

const post = (path, data) => {
    return new Promise((resolve, reject) => {
        const options = {
            hostname: 'localhost',
            port: 8080,
            path: path,
            method: 'POST',
            headers: {
                'Content-Type': 'application/json'
            }
        };
        const req = http.request(options, (res) => {
            let body = '';
            res.on('data', (chunk) => body += chunk);
            res.on('end', () => resolve({ status: res.statusCode, body }));
        });
        req.on('error', reject);
        req.write(JSON.stringify(data));
        req.end();
    });
};

const get = (path) => {
    return new Promise((resolve, reject) => {
        http.get(`http://localhost:8080${path}`, (res) => {
            let body = '';
            res.on('data', (chunk) => body += chunk);
            res.on('end', () => {
                try {
                    resolve(JSON.parse(body));
                } catch (e) {
                    resolve(body);
                }
            });
        }).on('error', reject);
    });
};

async function run() {
    try {
        console.log('--- Starting Verification ---');
        
        // 1. Stock Inward
        console.log('Performing Stock Inward...');
        const inwardRes = await post('/api/stock/inward', {
            productId: 21,
            dealerId: 1,
            quantity: 50,
            costPrice: 45
        });
        console.log('Inward Result Status:', inwardRes.status);

        // 2. Check Central Stock
        console.log('Checking Central Stock...');
        const central = await get('/api/stock/central');
        if (Array.isArray(central)) {
            console.log('Central Stock Count:', central.length);
            const batch = central.find(b => b.product && b.product.id === 21);
            if (batch) {
                console.log('Tea Packets Batch Remaining:', batch.remainingQuantity);
            }
        } else {
            console.log('Central response:', central);
        }

        // 3. Distribution
        console.log('Performing Bulk Distribution (30 units to Branch 3)...');
        const distRes = await post('/api/stock/distribute', {
            productId: 21,
            distributions: [
                { branchId: 3, quantity: 30 }
            ]
        });
        console.log('Distribution Result Status:', distRes.status);
        if (distRes.status !== 200) {
            console.log('Distribution Error:', distRes.body);
        }

        // 5. Test Insufficient Stock
        console.log('Testing Insufficient Stock (requesting 1000 units)...');
        const failRes = await post('/api/stock/distribute', {
            productId: 21,
            distributions: [
                { branchId: 1, quantity: 1000 }
            ]
        });
        console.log('Fail Case Result Status:', failRes.status);
        console.log('Fail Case Message:', failRes.body);

        console.log('--- Verification Complete ---');
    } catch (err) {
        console.error('Verification Failed:', err);
    }
}

run();
