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

async function fetchApi<T>(endpoint: string, options?: RequestInit): Promise<T> {
  const controller = new AbortController();
  const timeoutId = setTimeout(() => controller.abort(), 10000);

  const headers: Record<string, string> = {
    'Content-Type': 'application/json',
    ...options?.headers as Record<string, string>,
  };

  const res = await fetch(`${API_BASE_URL}${endpoint}`, {
    signal: controller.signal,
    headers,
    credentials: 'include',
    ...options,
  });

  clearTimeout(timeoutId);

  if (!res.ok) {
    throw new Error(`HTTP ${res.status}`);
  }

  return await res.json();
}

export const api = {
  login: (data: any) => fetchApi<any>('/auth/login', { method: 'POST', body: JSON.stringify(data) }),
  signup: (data: any) => fetchApi<any>('/auth/signup', { method: 'POST', body: JSON.stringify(data) }),

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
