#!/bin/bash

BASE_URL="http://localhost:8080/api/finance/data"

echo "🚀 Starting API Tests..."
echo "================================"

# 1. Health Check
echo -e "\n1️⃣  Health Check:"
curl -s $BASE_URL/health | jq '.status'

# 2. Latest Rates (dengan USD_BuySpread_IDR)
echo -e "\n2️⃣  Latest IDR Rates:"
curl -s $BASE_URL/latest_idr_rates | jq '.date, .rates.USD_BuySpread_IDR'

# 3. Historical IDR-USD (default dari application.yml)
echo -e "\n3️⃣  Historical IDR-USD (default):"
curl -s $BASE_URL/historical_idr_usd | jq 'length'

# 4. Historical IDR-USD (custom date range)
echo -e "\n4️⃣  Historical IDR-USD (custom dates):"
curl -s "$BASE_URL/historical_idr_usd?startDate=2024-01-10&endDate=2024-01-12" | jq 'length'

# 5. Supported Currencies
echo -e "\n5️⃣  Supported Currencies:"
curl -s $BASE_URL/supported_currencies | jq 'length'

# 6. Error Handling (invalid resource)
echo -e "\n6️⃣  Error Handling (invalid resource):"
curl -s $BASE_URL/invalid_resource | jq '.error'

echo -e "\n✅ All tests completed!"