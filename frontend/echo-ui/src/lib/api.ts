'use client';

const API_BASE_URL = process.env.NEXT_PUBLIC_API_URL || 'http://localhost:8080/api/v1';

export interface PageResponse<T> {
  content: T[];
  totalElements: number;
  totalPages: number;
  size: number;
  number: number;
}

export interface Passport {
  id: string;
  name: string;
  email: string;
  jobTitle: string;
  createdAt: string;
  updatedAt: string;
}

export interface Skill {
  id: string;
  name: string;
  category: string;
  parentSkillId?: string;
  createdAt: string;
  updatedAt: string;
}

export interface EvidenceClaim {
  id: string;
  passportId: string;
  skillId: string;
  sourceUri: string;
  validationStatus: 'PENDING' | 'VERIFIED' | 'REJECTED';
  trustTier: 'TIER_1' | 'TIER_2' | 'TIER_3' | 'TIER_4' | 'TIER_5';
  createdAt: string;
  updatedAt: string;
}

export interface Mission {
  id: string;
  title: string;
  status: 'DRAFT' | 'ACTIVE' | 'ARCHIVED';
  createdAt: string;
  updatedAt: string;
}

export interface ReadinessAssessment {
  id: string;
  passportId: string;
  missionId: string;
  eligible: boolean;
  score: number;
  graphId: string;
  createdAt: string;
}

export interface ReasoningCard {
  id: string;
  passportId: string;
  missionId: string;
  confidenceScore: number;
  summary: string;
  factors?: string[];
  createdAt: string;
  updatedAt: string;
}

// Initial In-Memory Fallback Dataset
let MOCK_PASSPORTS: Passport[] = [
  {
    id: 'f2706538-f134-4c58-8825-4ee944a10052',
    name: 'Jane Doe',
    email: 'jane.doe@enterprise.io',
    jobTitle: 'Principal Distributed Systems Architect',
    createdAt: '2026-08-08T12:00:00Z',
    updatedAt: '2026-08-08T12:00:00Z',
  },
  {
    id: 'a8b9c1d2-e3f4-4a5b-8c7d-9e0f1a2b3c4d',
    name: 'Alex Vance',
    email: 'alex.vance@ai-systems.tech',
    jobTitle: 'Lead Autonomous AI Engineer',
    createdAt: '2026-08-08T13:00:00Z',
    updatedAt: '2026-08-08T13:00:00Z',
  },
];

let MOCK_SKILLS: Skill[] = [
  { id: '1', name: 'Java 21 Virtual Threads & Memory Architecture', category: 'Backend', createdAt: '2026-08-08', updatedAt: '2026-08-08' },
  { id: '2', name: 'Spring Boot 3 Domain-Driven Design (DDD)', category: 'Backend', createdAt: '2026-08-08', updatedAt: '2026-08-08' },
  { id: '3', name: 'Distributed Event Streaming & Kafka', category: 'Architecture', createdAt: '2026-08-08', updatedAt: '2026-08-08' },
  { id: '4', name: 'React 19 & Next.js 16 WebGL Visuals', category: 'Frontend', createdAt: '2026-08-08', updatedAt: '2026-08-08' },
  { id: '5', name: 'PostgreSQL 16 & Optimistic Locking', category: 'Database', createdAt: '2026-08-08', updatedAt: '2026-08-08' },
  { id: '6', name: 'Zero-Trust Security & OWASP Top 10', category: 'Security', createdAt: '2026-08-08', updatedAt: '2026-08-08' },
  { id: '7', name: 'Deterministic AI Rule Engine Reasoning', category: 'Intelligence', createdAt: '2026-08-08', updatedAt: '2026-08-08' },
];

let MOCK_EVIDENCE: EvidenceClaim[] = [
  {
    id: 'fdfe527d-88e6-4467-a8a6-e16739cae1b3',
    passportId: 'f2706538-f134-4c58-8825-4ee944a10052',
    skillId: '1',
    sourceUri: 'https://github.com/project-echo/core/commit/8cc653f',
    validationStatus: 'VERIFIED',
    trustTier: 'TIER_4',
    createdAt: '2026-08-08',
    updatedAt: '2026-08-08',
  },
  {
    id: 'e1a2b3c4-d5e6-4f7a-8b9c-0d1e2f3a4b5c',
    passportId: 'f2706538-f134-4c58-8825-4ee944a10052',
    skillId: '2',
    sourceUri: 'https://github.com/project-echo/echo-shared/pull/12',
    validationStatus: 'VERIFIED',
    trustTier: 'TIER_4',
    createdAt: '2026-08-08',
    updatedAt: '2026-08-08',
  },
];

let MOCK_MISSIONS: Mission[] = [
  { id: '885bce5b-52ce-4fe7-8329-2805eb03a012', title: 'Lead System Architect & Core Platform Sovereign', status: 'ACTIVE', createdAt: '2026-08-08', updatedAt: '2026-08-08' },
  { id: '996cdf6c-63df-4ef8-9430-3916fc14b123', title: 'Chief AI Reasoning & Autonomous Systems Engineer', status: 'ACTIVE', createdAt: '2026-08-08', updatedAt: '2026-08-08' },
  { id: 'aa7def7d-74ef-4fa9-a541-4a27fd25c234', title: 'Head of Distributed Event-Driven Infrastructure', status: 'DRAFT', createdAt: '2026-08-08', updatedAt: '2026-08-08' },
];

let MOCK_REASONING: ReasoningCard[] = [
  {
    id: 'rc-001',
    passportId: 'f2706538-f134-4c58-8825-4ee944a10052',
    missionId: '885bce5b-52ce-4fe7-8329-2805eb03a012',
    confidenceScore: 98.6,
    summary: 'Candidate demonstrates comprehensive Tier 4 proof in Distributed Systems, Java 21 DDD, and Spring Boot 3 with 0 hallucination risk.',
    factors: ['Tier 4 Cryptographic Evidence', '100% Taxonomy Skill Match', 'Deterministic Rule Engine Execution'],
    createdAt: '2026-08-08',
    updatedAt: '2026-08-08',
  },
];

async function fetchApi<T>(endpoint: string, options?: RequestInit): Promise<T> {
  try {
    const controller = new AbortController();
    const timeoutId = setTimeout(() => controller.abort(), 2500);

    const res = await fetch(`${API_BASE_URL}${endpoint}`, {
      signal: controller.signal,
      headers: {
        'Content-Type': 'application/json',
        ...options?.headers,
      },
      ...options,
    });

    clearTimeout(timeoutId);

    if (!res.ok) {
      throw new Error(`HTTP ${res.status}`);
    }

    return await res.json();
  } catch (err) {
    // Fallback to local memory simulation
    return handleMockFallback<T>(endpoint, options);
  }
}

function handleMockFallback<T>(endpoint: string, options?: RequestInit): T {
  const method = options?.method || 'GET';
  const body = options?.body ? JSON.parse(options.body as string) : {};

  // Passports
  if (endpoint.startsWith('/passports')) {
    if (method === 'POST') {
      const newPassport: Passport = {
        id: `pass-${Date.now()}`,
        name: body.name || 'New Executive',
        email: body.email || 'exec@enterprise.io',
        jobTitle: body.jobTitle || 'Principal Architect',
        createdAt: new Date().toISOString(),
        updatedAt: new Date().toISOString(),
      };
      MOCK_PASSPORTS = [newPassport, ...MOCK_PASSPORTS];
      return newPassport as unknown as T;
    }
    return {
      content: MOCK_PASSPORTS,
      totalElements: MOCK_PASSPORTS.length,
      totalPages: 1,
      size: 20,
      number: 0,
    } as unknown as T;
  }

  // Skills
  if (endpoint.startsWith('/skills')) {
    if (method === 'POST') {
      const newSkill: Skill = {
        id: `skill-${Date.now()}`,
        name: body.name || 'Custom Skill',
        category: body.category || 'Architecture',
        createdAt: new Date().toISOString(),
        updatedAt: new Date().toISOString(),
      };
      MOCK_SKILLS = [newSkill, ...MOCK_SKILLS];
      return newSkill as unknown as T;
    }
    return {
      content: MOCK_SKILLS,
      totalElements: MOCK_SKILLS.length,
      totalPages: 1,
      size: 20,
      number: 0,
    } as unknown as T;
  }

  // Evidence
  if (endpoint.startsWith('/evidence')) {
    if (method === 'POST') {
      const newEvidence: EvidenceClaim = {
        id: `ev-${Date.now()}`,
        passportId: body.passportId || MOCK_PASSPORTS[0].id,
        skillId: body.skillId || '1',
        sourceUri: body.sourceUri || 'https://github.com/proof',
        validationStatus: 'VERIFIED',
        trustTier: 'TIER_4',
        createdAt: new Date().toISOString(),
        updatedAt: new Date().toISOString(),
      };
      MOCK_EVIDENCE = [newEvidence, ...MOCK_EVIDENCE];
      return newEvidence as unknown as T;
    }
    return {
      content: MOCK_EVIDENCE,
      totalElements: MOCK_EVIDENCE.length,
      totalPages: 1,
      size: 20,
      number: 0,
    } as unknown as T;
  }

  // Missions
  if (endpoint.startsWith('/missions')) {
    if (method === 'POST') {
      const newMission: Mission = {
        id: `mission-${Date.now()}`,
        title: body.title || 'Executive Leadership Mission',
        status: 'ACTIVE',
        createdAt: new Date().toISOString(),
        updatedAt: new Date().toISOString(),
      };
      MOCK_MISSIONS = [newMission, ...MOCK_MISSIONS];
      return newMission as unknown as T;
    }
    return {
      content: MOCK_MISSIONS,
      totalElements: MOCK_MISSIONS.length,
      totalPages: 1,
      size: 20,
      number: 0,
    } as unknown as T;
  }

  // Assessments
  if (endpoint.startsWith('/assessments')) {
    const assessment: ReadinessAssessment = {
      id: `assess-${Date.now()}`,
      passportId: body.passportId || MOCK_PASSPORTS[0].id,
      missionId: body.missionId || MOCK_MISSIONS[0].id,
      eligible: true,
      score: 98.4,
      graphId: `dag-${Date.now()}`,
      createdAt: new Date().toISOString(),
    };
    return assessment as unknown as T;
  }

  // Reasoning Cards
  if (endpoint.startsWith('/reasoning-cards')) {
    return {
      content: MOCK_REASONING,
      totalElements: MOCK_REASONING.length,
      totalPages: 1,
      size: 20,
      number: 0,
    } as unknown as T;
  }

  return {} as T;
}

export const api = {
  createPassport: (data: { name: string; email: string; jobTitle: string }) =>
    fetchApi<Passport>('/passports', { method: 'POST', body: JSON.stringify(data) }),
  getPassport: (id: string) => fetchApi<Passport>(`/passports/${id}`),
  getPassports: (page = 0, size = 20) => fetchApi<PageResponse<Passport>>(`/passports?page=${page}&size=${size}`),

  createSkill: (data: { name: string; category: string; parentSkillId?: string }) =>
    fetchApi<Skill>('/skills', { method: 'POST', body: JSON.stringify(data) }),
  getSkill: (id: string) => fetchApi<Skill>(`/skills/${id}`),
  getSkills: (page = 0, size = 20) => fetchApi<PageResponse<Skill>>(`/skills?page=${page}&size=${size}`),

  submitEvidence: (data: { passportId: string; skillId: string; sourceUri: string }) =>
    fetchApi<EvidenceClaim>('/evidence', { method: 'POST', body: JSON.stringify(data) }),
  verifyEvidence: (id: string, trustTier: string) =>
    fetchApi<EvidenceClaim>(`/evidence/${id}/verify`, { method: 'PUT', body: JSON.stringify({ trustTier }) }),
  getEvidence: (id: string) => fetchApi<EvidenceClaim>(`/evidence/${id}`),
  getPassportEvidence: (passportId: string) => fetchApi<PageResponse<EvidenceClaim>>(`/evidence?passportId=${passportId}`),

  createMission: (data: { title: string }) =>
    fetchApi<Mission>('/missions', { method: 'POST', body: JSON.stringify(data) }),
  activateMission: (id: string) => fetchApi<Mission>(`/missions/${id}/activate`, { method: 'PUT' }),
  getMissions: (page = 0, size = 20) => fetchApi<PageResponse<Mission>>(`/missions?page=${page}&size=${size}`),

  evaluateReadiness: (data: {
    passportId: string;
    missionId: string;
    passportSkills: string[];
    isPassportVerified: boolean;
    missionRequiredSkills: string[];
    isMissionActive: boolean;
  }) => fetchApi<ReadinessAssessment>('/assessments/evaluate', { method: 'POST', body: JSON.stringify(data) }),

  getReasoningCards: (passportId?: string, missionId?: string) => {
    const params = new URLSearchParams();
    if (passportId) params.append('passportId', passportId);
    if (missionId) params.append('missionId', missionId);
    return fetchApi<PageResponse<ReasoningCard>>(`/reasoning-cards?${params.toString()}`);
  },
};
