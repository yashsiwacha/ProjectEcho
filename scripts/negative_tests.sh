#!/bin/bash
echo "=== Starting Negative Tests ==="
BASE_URL="http://localhost:8080/api/v1"

echo "1. Invalid JWT test..."
RESPONSE=$(curl -s -o /dev/null -w "%{http_code}" -H "Authorization: Bearer invalidtoken123" $BASE_URL/passports)
if [ "$RESPONSE" -eq 401 ] || [ "$RESPONSE" -eq 403 ]; then
  echo "✅ Invalid JWT correctly rejected ($RESPONSE)"
else
  echo "❌ Invalid JWT test failed ($RESPONSE)"
fi

echo "2. Missing JWT test..."
RESPONSE=$(curl -s -o /dev/null -w "%{http_code}" $BASE_URL/passports)
if [ "$RESPONSE" -eq 401 ] || [ "$RESPONSE" -eq 403 ]; then
  echo "✅ Missing JWT correctly rejected ($RESPONSE)"
else
  echo "❌ Missing JWT test failed ($RESPONSE)"
fi

echo "3. Invalid Upload Extension..."
# For the evidence upload, we just request a URL, but we can test invalid mime types if we had authentication
# Let's mock a logged-in user first to test authorized endpoints
USER_EMAIL="hacker${RANDOM}@example.com"
curl -s -X POST $BASE_URL/auth/register -H "Content-Type: application/json" -d "{\"email\":\"$USER_EMAIL\",\"password\":\"Pass123!\",\"role\":\"CANDIDATE\"}" > /dev/null
LOGIN_RES=$(curl -s -X POST $BASE_URL/auth/login -H "Content-Type: application/json" -d "{\"email\":\"$USER_EMAIL\",\"password\":\"Pass123!\"}")
TOKEN=$(echo $LOGIN_RES | grep -o '"accessToken":"[^"]*' | cut -d'"' -f4)

if [ -n "$TOKEN" ]; then
  echo "✅ Got token for auth testing"
  
  RESPONSE=$(curl -s -o /dev/null -w "%{http_code}" -X POST -H "Authorization: Bearer $TOKEN" -H "Content-Type: application/json" -d "{\"filename\":\"hack.exe\",\"contentType\":\"application/x-msdownload\"}" $BASE_URL/evidence/upload-url)
  if [ "$RESPONSE" -eq 400 ] || [ "$RESPONSE" -eq 415 ]; then
    echo "✅ Invalid upload correctly rejected ($RESPONSE)"
  else
    echo "❌ Invalid upload test failed ($RESPONSE)"
  fi
  
  echo "4. Role Access Test (Candidate accessing Recruiter endpoint)..."
  # Try to create a mission
  RESPONSE=$(curl -s -o /dev/null -w "%{http_code}" -X POST -H "Authorization: Bearer $TOKEN" -H "Content-Type: application/json" -d "{\"title\":\"Test\"}" $BASE_URL/missions)
  if [ "$RESPONSE" -eq 403 ]; then
    echo "✅ Unauthorized role access correctly rejected ($RESPONSE)"
  else
    echo "❌ Role access test failed ($RESPONSE)"
  fi
else
  echo "❌ Failed to get auth token for deeper tests"
fi

echo "=== Negative Tests Complete ==="
