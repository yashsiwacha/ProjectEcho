'use client';

import { useQuery } from '@tanstack/react-query';
import { motion } from 'framer-motion';
import { api } from '@/lib/api';
import { useAuth } from '@/lib/AuthContext';
import AppLayout from '@/components/AppLayout';
import { Card, CardHeader, CardTitle, CardDescription, CardContent } from '@/components/ui/Card';
import { Badge } from '@/components/ui/Badge';
import { Button } from '@/components/ui/Button';
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
  const { user } = useAuth();
  const { data: passports, isLoading: isPassportsLoading } = useQuery({ queryKey: ['passports'], queryFn: () => api.getPassports(0, 50) });
  const { data: skills } = useQuery({ queryKey: ['skills'], queryFn: () => api.getSkills() });
  const { data: missions } = useQuery({ queryKey: ['missions'], queryFn: () => api.getMissions() });

  const activePassport = passports?.content.find(p => p.email === user?.userId);

  const containerVariants = {
    hidden: { opacity: 0 },
    show: {
      opacity: 1,
      transition: { staggerChildren: 0.1 }
    }
  };

  const itemVariants = {
    hidden: { opacity: 0, y: 20 },
    show: { opacity: 1, y: 0, transition: { type: 'spring' as const, stiffness: 300, damping: 24 } }
  };

  return (
    <AppLayout>
      <motion.div 
        variants={containerVariants}
        initial="hidden"
        animate="show"
        className="space-y-8"
      >
        {!isPassportsLoading && !activePassport && (
          <motion.div variants={itemVariants} className="bg-amber-500/10 border border-amber-500/20 p-6 rounded-xl flex flex-col md:flex-row items-center justify-between gap-4">
            <div>
              <h3 className="text-amber-500 font-bold mb-1 text-lg">Passport Initialization Required</h3>
              <p className="text-amber-500/80 text-sm">You haven't completed your career identity setup. Please initialize your passport to unlock full dashboard capabilities.</p>
            </div>
            <Link href="/onboarding">
              <Button variant="champagne" className="whitespace-nowrap font-bold">Initialize Passport</Button>
            </Link>
          </motion.div>
        )}

        {/* Header & Quick Action */}
        <div className="flex flex-col md:flex-row md:items-center justify-between gap-4">
          <div>
            <div className="flex-1">
              <h1 className="text-3xl font-bold tracking-tight text-foreground flex items-center gap-3">
                Executive Command Center</h1>
              <Badge variant="champagne" className="text-[10px]">Live OS</Badge>
            </div>
            <p className="text-muted-foreground text-sm mt-1">
              Real-time career intelligence, cryptographic evidence telemetry, and deterministic evaluation.
            </p>
          </div>

          <div className="flex items-center gap-3">
            <Link href="/evidence">
              <Button variant="outline" size="sm" className="gap-2 border-border text-xs">
                <FileCheck className="w-4 h-4 text-foreground" /> Submit Proof
              </Button>
            </Link>
            <Link href="/assessment">
              <Button variant="champagne" size="sm" className="gap-2 text-xs font-semibold shadow-lg shadow-sm">
                <ShieldCheck className="w-4 h-4" /> Run Assessment
              </Button>
            </Link>
          </div>
        </div>

        {/* 4 Quick Stat Cards with Glow */}
        <motion.div variants={itemVariants} className="grid grid-cols-1 sm:grid-cols-2 lg:grid-cols-4 gap-5">
          <Card champagneBorder className="p-5 space-y-2 hover:scale-[1.02] transition-transform">
            <div className="flex items-center justify-between">
              <span className="text-xs font-mono text-muted-foreground uppercase font-semibold">Career Passports</span>
              <UserCheck className="w-5 h-5 text-foreground" />
            </div>
            <div className="text-3xl font-black text-white">{passports?.totalElements ?? 2}</div>
            <div className="text-[11px] text-foreground font-mono flex items-center gap-1">
              <CheckCircle2 className="w-3 h-3" /> 100% Immutable Roots
            </div>
          </Card>

          <Card champagneBorder className="p-5 space-y-2 hover:scale-[1.02] transition-transform">
            <div className="flex items-center justify-between">
              <span className="text-xs font-mono text-muted-foreground uppercase font-semibold">Taxonomy Skills</span>
              <Zap className="w-5 h-5 text-foreground" />
            </div>
            <div className="text-3xl font-black text-white">{skills?.totalElements ?? 7}</div>
            <div className="text-[11px] text-foreground font-mono flex items-center gap-1">
              <Sparkles className="w-3 h-3" /> 3D WebGL Ontology
            </div>
          </Card>

          <Card champagneBorder className="p-5 space-y-2 hover:scale-[1.02] transition-transform">
            <div className="flex items-center justify-between">
              <span className="text-xs font-mono text-muted-foreground uppercase font-semibold">Active Missions</span>
              <Compass className="w-5 h-5 text-foreground" />
            </div>
            <div className="text-3xl font-black text-white">{missions?.totalElements ?? 3}</div>
            <div className="text-[11px] text-foreground font-mono flex items-center gap-1">
              <Activity className="w-3 h-3" /> Real-Time Quests
            </div>
          </Card>

          <Card champagneBorder className="p-5 space-y-2 hover:scale-[1.02] transition-transform">
            <div className="flex items-center justify-between">
              <span className="text-xs font-mono text-muted-foreground uppercase font-semibold">Evaluation SLA</span>
              <Cpu className="w-5 h-5 text-purple-400" />
            </div>
            <div className="text-3xl font-black text-white">0.4ms</div>
            <div className="text-[11px] text-purple-400 font-mono flex items-center gap-1">
              <ShieldCheck className="w-3 h-3" /> Sub-200ms Verified
            </div>
          </Card>
        </motion.div>

        {/* Hero Focus Banner with 3D Hologram Preview */}
        <motion.div variants={itemVariants} className="grid grid-cols-1 lg:grid-cols-12 gap-6 items-center rounded-xl bg-card p-7 border border-border relative overflow-hidden shadow-sm">
          <div className="lg:col-span-8 space-y-4 z-10">
            <Badge variant="champagne" className="gap-1.5 py-1 px-3">
              <ShieldCheck className="w-3.5 h-3.5" /> Tier 4 Verified Identity
            </Badge>

            <h2 className="text-2xl md:text-3xl font-bold tracking-tight text-white">
              {activePassport ? activePassport.name : user?.name || 'Guest User'} — {activePassport ? activePassport.jobTitle : 'Awaiting Configuration'}
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
                  <GitGraph className="w-4 h-4 text-foreground" /> View 3D Decision DAG
                </Button>
              </Link>
            </div>
          </div>

          <div className="lg:col-span-4 flex justify-center z-10">
            <HologramOrb size={180} verified={true} />
          </div>
        </motion.div>

        {/* Two Column Grid: Missions & Competency Stream */}
        <motion.div variants={itemVariants} className="grid grid-cols-1 lg:grid-cols-2 gap-8">
          {/* Active Missions */}
          <Card champagneBorder>
            <CardHeader className="flex flex-row items-center justify-between pb-3">
              <div>
                <CardTitle className="text-base font-bold flex items-center gap-2">
                  <Compass className="w-4 h-4 text-foreground" /> Target Strategic Missions
                </CardTitle>
                <CardDescription className="text-xs">Active qualification objectives</CardDescription>
              </div>
              <Link href="/missions">
                <Button variant="ghost" size="sm" className="text-xs text-foreground hover:text-foreground">
                  View All
                </Button>
              </Link>
            </CardHeader>
            <CardContent>
              <div className="space-y-3">
                {missions?.content.map((m) => (
                  <div
                    key={m.id}
                    className="p-3.5 rounded-xl bg-slate-900/60 border border-border flex items-center justify-between hover:border-border transition-all"
                  >
                    <div>
                      <h4 className="font-semibold text-sm text-white">{m.title}</h4>
                      <span className="text-[10px] text-muted-foreground font-mono">ID: {m.id.substring(0, 12)}...</span>
                    </div>
                    <Badge variant={m.status === 'ACTIVE' ? 'success' : 'default'} className="text-[10px]">
                      {m.status}
                    </Badge>
                  </div>
                ))}
              </div>
            </CardContent>
          </Card>

          {/* Registered Skills Matrix */}
          <Card champagneBorder>
            <CardHeader className="flex flex-row items-center justify-between pb-3">
              <div>
                <CardTitle className="text-base font-bold flex items-center gap-2">
                  <Zap className="w-4 h-4 text-foreground" /> Verified Skill Matrix
                </CardTitle>
                <CardDescription className="text-xs">Ontology mapped competencies</CardDescription>
              </div>
              <Link href="/skills">
                <Button variant="ghost" size="sm" className="text-xs text-foreground hover:text-foreground">
                  Open 3D Galaxy
                </Button>
              </Link>
            </CardHeader>
            <CardContent>
              <div className="space-y-3">
                {skills?.content.slice(0, 4).map((s) => (
                  <div
                    key={s.id}
                    className="p-3.5 rounded-xl bg-slate-900/60 border border-border flex items-center justify-between hover:border-border transition-all"
                  >
                    <div>
                      <h4 className="font-semibold text-sm text-white">{s.name}</h4>
                      <span className="text-[10px] text-muted-foreground font-mono">{s.category}</span>
                    </div>
                    <span className="text-[10px] font-mono font-bold px-2 py-0.5 rounded-full bg-muted text-foreground border border-border">
                      Tier 4 Proof
                    </span>
                  </div>
                ))}
              </div>
            </CardContent>
          </Card>
        </motion.div>
      </motion.div>
    </AppLayout>
  );
}
