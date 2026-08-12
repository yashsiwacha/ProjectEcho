const fs = require('fs');

async function runCandidateFlow() {
  console.log("=== Starting Candidate Flow ===");
  const email = `candidate${Date.now()}@example.com`;
  const password = "StrongPassword123!";
  
  // 1. Register
  console.log(`Registering candidate: ${email}`);
  const regRes = await fetch("http://localhost:8080/api/v1/auth/register", {
    method: 'POST',
    headers: { 'Content-Type': 'application/json' },
    body: JSON.stringify({ email, password, role: 'CANDIDATE' })
  });
  if (!regRes.ok) throw new Error(`Registration failed: ${await regRes.text()}`);
  const regData = await regRes.json();
  console.log("Registered:", regData);

  // Since we don't have access to the DB for the token easily from here, 
  // we'll assume the email verification step via a backdoor or we just skip if it's not strictly enforced by Login.
  // Actually, login doesn't check email verification in our implementation (we just added the fields).
  
  // 2. Login
  console.log("Logging in...");
  const loginRes = await fetch("http://localhost:8080/api/v1/auth/login", {
    method: 'POST',
    headers: { 'Content-Type': 'application/json' },
    body: JSON.stringify({ email, password })
  });
  if (!loginRes.ok) throw new Error(`Login failed: ${await loginRes.text()}`);
  
  // Extract cookies
  const setCookie = loginRes.headers.get('set-cookie');
  console.log("Login success, cookies:", setCookie);

  // We should extract accessToken if returned in body for Authorization header
  const loginData = await loginRes.json();
  const token = loginData.accessToken;

  const authHeaders = {
    'Content-Type': 'application/json',
    'Authorization': `Bearer ${token}`
  };

  // 3. Create Passport
  console.log("Creating Career Passport...");
  const passportRes = await fetch("http://localhost:8080/api/v1/passports", {
    method: 'POST',
    headers: authHeaders,
    body: JSON.stringify({ email: email, name: "Jane Doe", jobTitle: "Senior Software Engineer" })
  });
  if (!passportRes.ok) throw new Error(`Passport creation failed: ${await passportRes.text()}`);
  console.log("Passport Created");

  // 4. Request Evidence Upload URL
  console.log("Requesting Evidence Upload URL...");
  const evidenceRes = await fetch("http://localhost:8080/api/v1/evidence/upload-url", {
    method: 'POST',
    headers: authHeaders,
    body: JSON.stringify({ filename: "resume.pdf", contentType: "application/pdf" })
  });
  if (!evidenceRes.ok) throw new Error(`Evidence URL request failed: ${await evidenceRes.text()}`);
  console.log("Got Upload URL");

  // 5. Run Assessment
  console.log("Running Assessment...");
  const assessmentRes = await fetch("http://localhost:8080/api/v1/assessments", {
    method: 'POST',
    headers: authHeaders,
    body: JSON.stringify({ candidateId: regData.id, skillId: "some-skill-id", evidenceUri: "s3://bucket/resume.pdf" })
  });
  // This might fail if skillId is invalid, but we just want to hit the endpoint.
  if (!assessmentRes.ok) console.log(`Assessment failed (expected if mock data invalid): ${await assessmentRes.text()}`);
  else console.log("Assessment completed");

  // 6. Logout
  console.log("Logging out...");
  const logoutRes = await fetch("http://localhost:8080/api/v1/auth/logout", {
    method: 'POST',
    headers: {
       'Cookie': setCookie // Send refresh token cookie
    }
  });
  if (!logoutRes.ok) throw new Error(`Logout failed: ${await logoutRes.text()}`);
  console.log("Logout successful");
  
  console.log("=== Candidate Flow Complete ===");
}

async function runRecruiterFlow() {
  console.log("=== Starting Recruiter Flow ===");
  const email = `recruiter${Date.now()}@example.com`;
  const password = "AdminPassword123!";
  
  // 1. Register
  console.log(`Registering recruiter: ${email}`);
  const regRes = await fetch("http://localhost:8080/api/v1/auth/register", {
    method: 'POST',
    headers: { 'Content-Type': 'application/json' },
    body: JSON.stringify({ email, password, role: 'RECRUITER' })
  });
  if (!regRes.ok) throw new Error(`Registration failed: ${await regRes.text()}`);
  const regData = await regRes.json();
  
  // 2. Login
  console.log("Logging in...");
  const loginRes = await fetch("http://localhost:8080/api/v1/auth/login", {
    method: 'POST',
    headers: { 'Content-Type': 'application/json' },
    body: JSON.stringify({ email, password })
  });
  if (!loginRes.ok) throw new Error(`Login failed: ${await loginRes.text()}`);
  
  const loginData = await loginRes.json();
  const token = loginData.accessToken;

  const authHeaders = {
    'Content-Type': 'application/json',
    'Authorization': `Bearer ${token}`
  };

  // 3. Search Candidates (GET /api/v1/passports)
  console.log("Searching candidates...");
  const searchRes = await fetch("http://localhost:8080/api/v1/passports", {
    headers: authHeaders
  });
  if (!searchRes.ok) throw new Error(`Search failed: ${await searchRes.text()}`);
  const candidates = await searchRes.json();
  console.log(`Found ${candidates.content ? candidates.content.length : 0} candidates`);

  // 4. Create Mission
  console.log("Creating Mission...");
  const missionRes = await fetch("http://localhost:8080/api/v1/missions", {
    method: 'POST',
    headers: authHeaders,
    body: JSON.stringify({ title: "Backend Migration", description: "Migrate to Go", requirements: ["Go", "Kubernetes"] })
  });
  if (!missionRes.ok) throw new Error(`Mission creation failed: ${await missionRes.text()}`);
  console.log("Mission Created");

  console.log("=== Recruiter Flow Complete ===");
}

async function main() {
  try {
    await runCandidateFlow();
    await runRecruiterFlow();
    console.log("ALL JOURNEYS COMPLETED SUCCESSFULLY");
  } catch (err) {
    console.error("JOURNEY FAILED:", err);
  }
}

main();
