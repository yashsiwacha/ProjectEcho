// Native fetch is available in Node 18+

async function runLoadTest(url, method = 'GET', body = null, headers = {}, requests = 100) {
  const times = [];
  
  console.log(`Starting load test: ${requests} requests to ${url}`);
  
  for (let i = 0; i < requests; i++) {
    const start = Date.now();
    try {
      const res = await fetch(url, {
        method,
        headers,
        body: body ? JSON.stringify(body) : null
      });
      await res.text(); // consume body
      times.push(Date.now() - start);
    } catch (e) {
      console.log("Request failed");
    }
  }
  
  if (times.length === 0) {
    console.log("All requests failed.");
    return;
  }
  
  times.sort((a, b) => a - b);
  const p50 = times[Math.floor(times.length * 0.5)];
  const p95 = times[Math.floor(times.length * 0.95)];
  const p99 = times[Math.floor(times.length * 0.99)];
  
  console.log(`Results for ${url}:`);
  console.log(`Total Requests: ${times.length}`);
  console.log(`P50: ${p50} ms`);
  console.log(`P95: ${p95} ms`);
  console.log(`P99: ${p99} ms`);
}

async function main() {
  const apiUrl = "http://localhost:8080/api/v1";
  
  // Create user
  const email = `loaduser${Date.now()}@test.com`;
  await fetch(`${apiUrl}/auth/register`, {
    method: 'POST',
    headers: {'Content-Type':'application/json'},
    body: JSON.stringify({email, password:"Password123!", role:"CANDIDATE"})
  });
  
  const loginRes = await fetch(`${apiUrl}/auth/login`, {
    method: 'POST',
    headers: {'Content-Type':'application/json'},
    body: JSON.stringify({email, password:"Password123!"})
  });
  const data = await loginRes.json();
  const token = data.accessToken;
  
  const authHeaders = {
    'Authorization': `Bearer ${token}`
  };
  
  await runLoadTest(`${apiUrl}/passports`, 'GET', null, authHeaders, 50);
}

main();
