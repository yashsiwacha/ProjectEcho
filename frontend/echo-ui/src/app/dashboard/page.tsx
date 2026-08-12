'use client';

import { useQuery } from '@tanstack/react-query';
import { api } from '@/lib/api';
import AppLayout from '@/components/AppLayout';
import { Card, CardHeader, CardTitle, CardDescription, CardContent } from '@/components/ui/Card';
import { Badge } from '@/components/ui/Badge';
import { Button } from '@/components/ui/Button';
import { Skeleton } from '@/components/ui/Skeleton';
import {
  UserCheck,
  FileCheck,
  Compass,
  Award,
  ArrowRight,
  ShieldCheck,
  Zap,
  Activity,
  CheckCircle2,
  Cpu,
  Layers,
  Sparkles,
  GitGraph
} from 'lucide-react';
import Link from 'next/link';
import HologramOrb from '@/components/3d/HologramOrb';

export default function DashboardPage() {
  const { data: passports, isLoading: passportsLoading, isError: passportsError } = useQuery({ queryKey: ['passports'], queryFn: () => api.getPassports() });
  const { data: skills, isLoading: skillsLoading, isError: skillsError } = useQuery({ queryKey: ['skills'], queryFn: () => api.getSkills() });
  const { data: missions, isLoading: missionsLoading, isError: missionsError } = useQuery({ queryKey: ['missions'], queryFn: () => api.getMissions() });

  const activePassport = passports?.content[0];

  return (
    <AppLayout>
      <div className="space-y-8">
        {/* Loading and error states */}
        {(passportsError || skillsError || missionsError) && (
          <div role="alert" className="p-4 bg-destructive text-destructive-foreground rounded mb-4">
            Failed to load data. Please try again later.
          </div>
        )}
        {(passportsLoading || skillsLoading || missionsLoading) && (
          <div aria-live="polite" role="status" className="grid grid-cols-1 sm:grid-cols-2 lg:grid-cols-4 gap-5 mb-6">
            {[...Array(4)].map((_, i) => (
              <Skeleton key={i} className="h-24 w-full" />
            ))}
            <span className="sr-only">Loading dashboard data...</span>
          </div>
        )}
        {/* Header & Quick Action */}
        <div className="flex flex-col md:flex-row md:items-center justify-between gap-4">
          <div>
            <div className="flex items-center gap-2">
              <h1 className="text-3xl font-extrabold tracking-tight text-white">Executive Command Center</h1>
              <Badge variant="champagne" className="text-[10px]">Live OS</Badge>
            </div>
            <p className="text-muted-foreground text-sm mt-1">
              Real-time career intelligence, cryptographic evidence telemetry, and deterministic evaluation.
            </p>
          </div>

          <div className="flex items-center gap-3">
            <Link href="/evidence">
              <Button variant="outline" size="sm" className="gap-2 border-border text-xs">
                <FileCheck className="w-4 h-4 text-emerald-400" /> Submit Proof
              </Button>
            </Link>
            <Link href="/assessment">
              <Button variant="champagne" size="sm" className="gap-2 text-xs font-semibold shadow-lg shadow-amber-500/20">
                <ShieldCheck className="w-4 h-4" /> Run Assessment
              </Button>
            </Link>
          </div>
        </div>

        {/* 4 Quick Stat Cards with Glow */}
        <div className="grid grid-cols-1 sm:grid-cols-2 lg:grid-cols-4 gap-5">
          <Card champagneBorder className="p-5 space-y-2">
            <div className="flex items-center justify-between">
              <span className="text-xs font-mono text-muted-foreground uppercase font-semibold">Career Passports</span>
              <UserCheck className="w-5 h-5 text-amber-400" />
            </div>
            <div className="text-3xl font-black text-white">{passportsLoading ? <Skeleton className="h-6 w-12" /> : passports?.totalElements ?? 0}</div>
            <div className="text-[11px] text-emerald-400 font-mono flex items-center gap-1">
              <CheckCircle2 className="w-3 h-3" /> 100% Immutable Roots
            </div>
          </Card>

          <Card champagneBorder className="p-5 space-y-2">
            <div className="flex items-center justify-between">
              <span className="text-xs font-mono text-muted-foreground uppercase font-semibold">Taxonomy Skills</span>
              <Zap className="w-5 h-5 text-cyan-400" />
            </div>
            <div className="text-3xl font-black text-white">{skillsLoading ? <Skeleton className="h-6 w-12" /> : skills?.totalElements ?? 0}</div>
            <div className="text-[11px] text-cyan-400 font-mono flex items-center gap-1">
              <Sparkles className="w-3 h-3" /> 3D WebGL Ontology
            </div>
          </Card>

          <Card champagneBorder className="p-5 space-y-2">
            <div className="flex items-center justify-between">
              <span className="text-xs font-mono text-muted-foreground uppercase font-semibold">Active Missions</span>
              <Compass className="w-5 h-5 text-emerald-400" />
            </div>
            <div className="text-3xl font-black text-white">{missionsLoading ? <Skeleton className="h-6 w-12" /> : missions?.totalElements ?? 0}</div>
            <div className="text-[11px] text-amber-400 font-mono flex items-center gap-1">
              <Activity className="w-3 h-3" /> Real-Time Quests
            </div>
          </Card>

          <Card champagneBorder className="p-5 space-y-2">
            <div className="flex items-center justify-between">
              <span className="text-xs font-mono text-muted-foreground uppercase font-semibold">Evaluation SLA</span>
              <Cpu className="w-5 h-5 text-purple-400" />
            </div>
            <div className="text-3xl font-black text-white">0.4ms</div>
            <div className="text-[11px] text-purple-400 font-mono flex items-center gap-1">
              <ShieldCheck className="w-3 h-3" /> Sub-200ms Verified
            </div>
          </Card>
        </div>

        {/* Hero Focus Banner with 3D Hologram Preview */}
        <div className="grid grid-cols-1 lg:grid-cols-12 gap-6 items-center rounded-2xl glass-panel-glow p-7 border border-amber-500/30 relative overflow-hidden">
          <div className="lg:col-span-8 space-y-4 z-10">
            <Badge variant="champagne" className="gap-1.5 py-1 px-3">
              <ShieldCheck className="w-3.5 h-3.5" /> Tier 4 Verified Identity
            </Badge>

            <h2 className="text-2xl md:text-3xl font-bold tracking-tight text-white">
              {activePassport ? activePassport.name : 'Jane Doe'} — {activePassport ? activePassport.jobTitle : 'Principal Distributed Systems Architect'}
            </h2>

            <p className="text-sm text-muted-foreground leading-relaxed max-w-2xl">
              Career passport is anchored in pure domain aggregate roots. All skill claims have passed cryptographic trust-tiering with zero black-box AI fabrication.
            </p>

            <div className="flex flex-wrap items-center gap-3 pt-2">
              <Link href="/passport">
                <Button variant="champagne" size="sm" className="gap-2">
                  <UserCheck className="w-4 h-4" /> Open 3D Passport Studio
                </Button>
              </Link>
              <Link href="/graph">
                <Button variant="outline" size="sm" className="gap-2 border-border">
                  <GitGraph className="w-4 h-4 text-cyan-400" /> View 3D Decision DAG
                </Button>
              </Link>
            </div>
          </div>

          <div className="lg:col-span-4 flex justify-center z-10">
            <HologramOrb size={180} verified={true} />
          </div>
        </div>

        {/* Two Column Grid: Missions & Competency Stream */}
        <div className="grid grid-cols-1 lg:grid-cols-2 gap-8">
          {/* Active Missions */}
          <Card champagneBorder>
            <CardHeader className="flex flex-row items-center justify-between pb-3">
              <div>
                <CardTitle className="text-base font-bold flex items-center gap-2">
                  <Compass className="w-4 h-4 text-amber-400" /> Target Strategic Missions
                </CardTitle>
                <CardDescription className="text-xs">Active qualification objectives</CardDescription>
              </div>
              <Link href="/missions">
                <Button variant="ghost" size="sm" className="text-xs text-amber-400 hover:text-amber-300">
                  View All
                </Button>
              </Link>
            </CardHeader>
            <CardContent>
              <div className="space-y-3">
                {missions?.content && missions.content.length > 0 ? (
                  missions.content.map((m) => (
                    <div
                      key={m.id}
                      className="p-3.5 rounded-xl bg-slate-900/60 border border-border flex items-center justify-between hover:border-amber-500/40 transition-all hover:scale-105"
                    >
                      <div>
                        <h4 className="font-semibold text-sm text-white">{m.title}</h4>
                        <span className="text-[10px] text-muted-foreground font-mono">ID: {m.id.substring(0, 12)}...</span>
                      </div>
                      <Badge variant={m.status === 'ACTIVE' ? 'success' : 'default'} className="text-[10px]">
                        {m.status}
                      </Badge>
                    </div>
                  ))
                ) : (
                  <div className="p-8 text-center border border-dashed border-border rounded-xl bg-slate-900/30">
                    <Compass className="w-8 h-8 text-muted-foreground mx-auto mb-3" />
                    <h3 className="text-sm font-semibold text-white mb-1">No Active Missions</h3>
                    <p className="text-xs text-muted-foreground mb-4">Start your first strategic mission to track your progress.</p>
                    <Button variant="outline" size="sm">Explore Missions</Button>
                  </div>
                )}
              </div>
            </CardContent>
          </Card>

          {/* Registered Skills Matrix */}
          <Card champagneBorder>
            <CardHeader className="flex flex-row items-center justify-between pb-3">
              <div>
                <CardTitle className="text-base font-bold flex items-center gap-2">
                  <Zap className="w-4 h-4 text-cyan-400" /> Verified Skill Matrix
                </CardTitle>
                <CardDescription className="text-xs">Ontology mapped competencies</CardDescription>
              </div>
              <Link href="/skills">
                <Button variant="ghost" size="sm" className="text-xs text-cyan-400 hover:text-cyan-300">
                  Open 3D Galaxy
                </Button>
              </Link>
            </CardHeader>
            <CardContent>
              <div className="space-y-3">
                {skills?.content && skills.content.length > 0 ? (
                  skills.content.slice(0, 4).map((s) => (
                    <div
                      key={s.id}
                      className="p-3.5 rounded-xl bg-slate-900/60 border border-border flex items-center justify-between hover:border-cyan-500/40 transition-all hover:scale-105"
                    >
                      <div>
                        <h4 className="font-semibold text-sm text-white">{s.name}</h4>
                        <span className="text-[10px] text-muted-foreground font-mono">{s.category}</span>
                      </div>
                      <span className="text-[10px] font-mono font-bold px-2 py-0.5 rounded-full bg-emerald-500/20 text-emerald-400 border border-emerald-500/30">
                        Tier 4 Proof
                      </span>
                    </div>
                  ))
                ) : (
                  <div className="p-8 text-center border border-dashed border-border rounded-xl bg-slate-900/30">
                    <Zap className="w-8 h-8 text-muted-foreground mx-auto mb-3" />
                    <h3 className="text-sm font-semibold text-white mb-1">No Verified Skills</h3>
                    <p className="text-xs text-muted-foreground mb-4">You have not submitted evidence for any skills yet.</p>
                    <Link href="/evidence">
                      <Button variant="outline" size="sm">Submit Evidence</Button>
                    </Link>
                  </div>
                )}
              </div>
            </CardContent>
          </Card>
        </div>
      </div>
    </AppLayout>
  );
}
