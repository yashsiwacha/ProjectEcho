import http from 'k6/http';
import { sleep, check } from 'k6';

export const options = {
  stages: [
    { duration: '5s', target: 20 },  // 20 concurrent users
    { duration: '5s', target: 50 },  // ramp up to 50 concurrent users
    { duration: '5s', target: 100 }, // ramp up to 100 concurrent users
    { duration: '5s', target: 0 },   // cool down
  ],
  thresholds: {
    http_req_duration: ['p(95)<200'], // 95% of requests must complete below 200ms (AGENTS.md rule)
    http_req_failed: ['rate<0.01'],    // less than 1% errors
  },
};

const BASE_URL = 'http://localhost:8081';

export default function () {
  // 1. Health Probe
  const healthRes = http.get(`${BASE_URL}/actuator/health`);
  check(healthRes, {
    'health status is 200': (r) => r.status === 200,
    'health is UP': (r) => r.body.includes('UP'),
  });

  // 2. Auth Login (simulate authentication flow)
  const loginPayload = JSON.stringify({
    username: 'admin',
    password: 'adminpwd',
  });
  const loginParams = {
    headers: {
      'Content-Type': 'application/json',
    },
  };
  const loginRes = http.post(`${BASE_URL}/api/v1/auth/login`, loginPayload, loginParams);
  check(loginRes, {
    'login is 200': (r) => r.status === 200,
    'token is present': (r) => JSON.parse(r.body).token !== undefined,
  });

  // 3. API Read with JWT (simulate authenticated skill listing)
  if (loginRes.status === 200) {
    const token = JSON.parse(loginRes.body).token;
    const authParams = {
      headers: {
        'Authorization': `Bearer ${token}`,
      },
    };
    const skillsRes = http.get(`${BASE_URL}/api/v1/skills?page=0&size=5`, authParams);
    check(skillsRes, {
      'skills retrieve is 200 or 429': (r) => r.status === 200 || r.status === 429,
    });
  }

  sleep(0.1); // pacing between requests
}
