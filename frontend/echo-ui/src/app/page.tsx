'use client';

import Link from 'next/link';
import { Button } from '@/components/ui/Button';
import { Card } from '@/components/ui/Card';
import { Badge } from '@/components/ui/Badge';
import {
  ShieldCheck,
  BrainCircuit,
  Award,
  ArrowRight,
  CheckCircle2,
  Zap,
  Layers,
  Sparkles,
  GitGraph,
  UserCheck,
  FileCheck
} from 'lucide-react';
import ThreeSkillGalaxy from '@/components/3d/ThreeSkillGalaxy';
import HologramOrb from '@/components/3d/HologramOrb';

export default function LandingPage() {
  return (
    <div className="min-h-screen bg-background text-foreground bg-cyber-grid flex flex-col justify-between p-6 md:p-12 max-w-7xl mx-auto space-y-16">
      {/* Header */}
      <header className="flex items-center justify-between border-b border-border/60 pb-6">
        <div className="flex items-center gap-3.5">
          <div className="w-11 h-11 rounded-2xl bg-gradient-to-br from-amber-400 to-amber-600 flex items-center justify-center text-black font-black text-2xl shadow-xl shadow-amber-500/20">
            E
          </div>
          <div>
            <div className="flex items-center gap-2">
              <h1 className="font-extrabold text-xl tracking-tight text-white">ProjectEcho</h1>
              <span className="text-[10px] font-mono px-2 py-0.5 rounded-full bg-amber-500/20 text-amber-300 font-bold border border-amber-500/40">
                RC1 Certified
              </span>
            </div>
            <p className="text-xs text-muted-foreground font-mono">Evidence-Driven Career Intelligence OS</p>
          </div>
        </div>

        <div className="flex items-center gap-3">
          <Link href="/dashboard">
            <Button variant="outline" size="sm" className="hidden sm:inline-flex border-border">
              Sign In
            </Button>
          </Link>
          <Link href="/dashboard">
            <Button variant="champagne" size="sm" className="gap-2 shadow-lg shadow-amber-500/20">
              <Zap className="w-4 h-4" /> Launch Platform
            </Button>
          </Link>
        </div>
      </header>

      {/* Hero Section */}
      <main className="space-y-16">
        <div className="grid grid-cols-1 lg:grid-cols-12 gap-12 items-center">
          {/* Left Column: Vision & CTA */}
          <div className="lg:col-span-7 space-y-6 text-center lg:text-left">
            <div className="inline-flex items-center gap-2 py-1.5 px-3.5 rounded-full bg-amber-500/10 border border-amber-500/30 text-amber-300 text-xs font-mono font-semibold">
              <Sparkles className="w-3.5 h-3.5 text-amber-400 animate-pulse" />
              <span>Deterministic AI Reasoning & Sovereign Proof</span>
            </div>

            <h1 className="text-4xl md:text-6xl font-black tracking-tight leading-tight">
              The Career OS <br />
              <span className="text-gradient-champagne">Backed by Proof.</span>
            </h1>

            <p className="text-base md:text-lg text-muted-foreground max-w-2xl leading-relaxed">
              ProjectEcho replaces static, inflated resumes with cryptographically verifiable competency proof, auditable decision graphs, and zero-hallucination AI evaluation.
            </p>

            <div className="flex flex-col sm:flex-row items-center gap-4 pt-2">
              <Link href="/dashboard" className="w-full sm:w-auto">
                <Button size="lg" variant="champagne" className="gap-2.5 w-full sm:w-auto font-bold shadow-xl shadow-amber-500/20">
                  Enter Executive Dashboard <ArrowRight className="w-4 h-4" />
                </Button>
              </Link>
              <Link href="/passport" className="w-full sm:w-auto">
                <Button size="lg" variant="outline" className="w-full sm:w-auto font-medium border-border hover:border-amber-500/40">
                  <UserCheck className="w-4 h-4 text-amber-400 mr-2" /> Initialize Passport
                </Button>
              </Link>
            </div>

            {/* Quick Metrics Bar */}
            <div className="grid grid-cols-3 gap-4 pt-6 border-t border-border/80 text-left">
              <div>
                <div className="text-2xl font-black text-white">100%</div>
                <div className="text-xs text-muted-foreground font-mono">Proof Determinism</div>
              </div>
              <div>
                <div className="text-2xl font-black text-amber-400">Tier 4</div>
                <div className="text-xs text-muted-foreground font-mono">Cryptographic Trust</div>
              </div>
              <div>
                <div className="text-2xl font-black text-emerald-400">&lt;200ms</div>
                <div className="text-xs text-muted-foreground font-mono">Evaluation SLA</div>
              </div>
            </div>
          </div>

          {/* Right Column: 3D Holographic Visualizer */}
          <div className="lg:col-span-5 relative flex items-center justify-center">
            <div className="relative w-full max-w-md aspect-square rounded-3xl p-6 glass-panel-glow flex flex-col items-center justify-between text-center overflow-hidden">
              <div className="absolute inset-0 bg-gradient-to-b from-amber-500/10 via-transparent to-cyan-500/10 pointer-events-none" />
              
              <div className="flex items-center justify-between w-full z-10">
                <Badge variant="champagne" className="text-[10px]">Tier 4 Verified</Badge>
                <span className="text-[10px] font-mono text-muted-foreground">3D WebGL Core</span>
              </div>

              {/* 3D Hologram Orb */}
              <div className="my-auto z-10">
                <HologramOrb size={200} verified={true} />
              </div>

              <div className="z-10 space-y-1">
                <div className="text-xs font-mono text-amber-300 font-bold">Cryptographic Anchor Active</div>
                <div className="text-[11px] text-muted-foreground">Immutable Aggregate Roots & Spring Boot 3 Engine</div>
              </div>
            </div>
          </div>
        </div>

        {/* 3D Interactive Galaxy Canvas Showcase */}
        <div className="space-y-4">
          <div className="flex items-center justify-between">
            <div>
              <h2 className="text-2xl font-bold tracking-tight">Interactive 3D Competency Universe</h2>
              <p className="text-sm text-muted-foreground">Real-time WebGL particle constellation mapping verified domain skills.</p>
            </div>
            <Link href="/skills">
              <Button variant="outline" size="sm" className="gap-2 text-xs border-border">
                <Zap className="w-3.5 h-3.5 text-amber-400" /> Explore Full 3D Galaxy
              </Button>
            </Link>
          </div>
          <ThreeSkillGalaxy />
        </div>

        {/* 9 Core Business Modules Showcase Grid */}
        <div className="space-y-6">
          <div className="text-center max-w-2xl mx-auto space-y-2">
            <Badge variant="champagne">9 Sovereign Modules</Badge>
            <h2 className="text-3xl font-extrabold tracking-tight">Enterprise Architecture Matrix</h2>
            <p className="text-sm text-muted-foreground">
              Clean Hexagonal separation between Domain Models, Rule Evaluation, and Cryptographic Evidence.
            </p>
          </div>

          <div className="grid grid-cols-1 md:grid-cols-3 gap-6">
            <Link href="/passport" className="group">
              <Card champagneBorder className="h-full p-6 space-y-3 group-hover:border-amber-500/60 transition-all">
                <UserCheck className="w-8 h-8 text-amber-400" />
                <h3 className="text-lg font-bold text-white group-hover:text-amber-300">1. Career Passport</h3>
                <p className="text-xs text-muted-foreground leading-relaxed">
                  Cryptographically immutable career profile storing sovereign identities and proof-backed skills.
                </p>
              </Card>
            </Link>

            <Link href="/evidence" className="group">
              <Card champagneBorder className="h-full p-6 space-y-3 group-hover:border-amber-500/60 transition-all">
                <FileCheck className="w-8 h-8 text-emerald-400" />
                <h3 className="text-lg font-bold text-white group-hover:text-emerald-300">2. Evidence Verification</h3>
                <p className="text-xs text-muted-foreground leading-relaxed">
                  Multi-tier evidence trust assessment pipeline evaluating SHA-256 commits, PRs, and certifications.
                </p>
              </Card>
            </Link>

            <Link href="/skills" className="group">
              <Card champagneBorder className="h-full p-6 space-y-3 group-hover:border-amber-500/60 transition-all">
                <Zap className="w-8 h-8 text-cyan-400" />
                <h3 className="text-lg font-bold text-white group-hover:text-cyan-300">3. Taxonomy Galaxy</h3>
                <p className="text-xs text-muted-foreground leading-relaxed">
                  Standardized skill ontologies with 3D constellation visualization and relational skill hierarchies.
                </p>
              </Card>
            </Link>

            <Link href="/missions" className="group">
              <Card champagneBorder className="h-full p-6 space-y-3 group-hover:border-amber-500/60 transition-all">
                <Layers className="w-8 h-8 text-amber-400" />
                <h3 className="text-lg font-bold text-white group-hover:text-amber-300">4. Mission Explorer</h3>
                <p className="text-xs text-muted-foreground leading-relaxed">
                  Target executive career quests with required competency checklists and real-time state transitions.
                </p>
              </Card>
            </Link>

            <Link href="/assessment" className="group">
              <Card champagneBorder className="h-full p-6 space-y-3 group-hover:border-amber-500/60 transition-all">
                <ShieldCheck className="w-8 h-8 text-emerald-400" />
                <h3 className="text-lg font-bold text-white group-hover:text-emerald-300">5. Readiness Engine</h3>
                <p className="text-xs text-muted-foreground leading-relaxed">
                  Deterministic rule engine evaluating qualification scores and eligibility against live job criteria.
                </p>
              </Card>
            </Link>

            <Link href="/reasoning" className="group">
              <Card champagneBorder className="h-full p-6 space-y-3 group-hover:border-amber-500/60 transition-all">
                <Award className="w-8 h-8 text-purple-400" />
                <h3 className="text-lg font-bold text-white group-hover:text-purple-300">6. Reasoning Cards</h3>
                <p className="text-xs text-muted-foreground leading-relaxed">
                  Explainable AI audit trails providing clear factor breakdowns and downloadable certificates.
                </p>
              </Card>
            </Link>

            <Link href="/graph" className="group">
              <Card champagneBorder className="h-full p-6 space-y-3 group-hover:border-amber-500/60 transition-all">
                <GitGraph className="w-8 h-8 text-cyan-400" />
                <h3 className="text-lg font-bold text-white group-hover:text-cyan-300">7. Decision Graph</h3>
                <p className="text-xs text-muted-foreground leading-relaxed">
                  3D DAG trace connecting passports, evidence claims, rule evaluations, and final scoring.
                </p>
              </Card>
            </Link>

            <Link href="/profile" className="group">
              <Card champagneBorder className="h-full p-6 space-y-3 group-hover:border-amber-500/60 transition-all">
                <Sparkles className="w-8 h-8 text-amber-400" />
                <h3 className="text-lg font-bold text-white group-hover:text-amber-300">8. Executive Profile</h3>
                <p className="text-xs text-muted-foreground leading-relaxed">
                  Public-facing verified profile with 3D Holographic Passport Badge and shareable proof link.
                </p>
              </Card>
            </Link>

            <Link href="/dashboard" className="group">
              <Card champagneBorder className="h-full p-6 space-y-3 group-hover:border-amber-500/60 transition-all">
                <BrainCircuit className="w-8 h-8 text-blue-400" />
                <h3 className="text-lg font-bold text-white group-hover:text-blue-300">9. Executive Dashboard</h3>
                <p className="text-xs text-muted-foreground leading-relaxed">
                  Real-time command center aggregating passports, active missions, and enterprise quality metrics.
                </p>
              </Card>
            </Link>
          </div>
        </div>
      </main>

      {/* Footer */}
      <footer className="border-t border-border/80 pt-8 flex flex-col md:flex-row items-center justify-between gap-4 text-xs text-muted-foreground font-mono">
        <p>&copy; {new Date().getFullYear()} ProjectEcho Engineering Organization. All rights reserved.</p>
        <div className="flex items-center gap-4">
          <span className="text-emerald-400">● Spring Boot 3.3.0 Engine UP</span>
          <span className="text-amber-400">● Next.js 16.3 Turbopack</span>
          <span>● WCAG 2.1 AA Compliant</span>
        </div>
      </footer>
    </div>
  );
}
