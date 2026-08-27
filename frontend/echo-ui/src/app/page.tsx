'use client';

import Link from 'next/link';
import { motion } from 'framer-motion';
import { Button } from '@/components/ui/Button';
import { Card } from '@/components/ui/Card';
import { Badge } from '@/components/ui/Badge';
import { ThemeToggle } from '@/components/ThemeToggle';
import {
  ShieldCheck, BrainCircuit, Award, ArrowRight,
  Zap, Layers, Sparkles, GitGraph, UserCheck, FileCheck
} from 'lucide-react';
import ThreeSkillGalaxy from '@/components/3d/ThreeSkillGalaxy';
import HologramOrb from '@/components/3d/HologramOrb';
import { containerVariants, itemVariants, pageVariants } from '@/lib/motion';

export default function LandingPage() {
  return (
    <div className="relative min-h-screen bg-background overflow-hidden">
      {/* Animated cyber grid background layer */}
      <div className="absolute inset-0 bg-cyber-grid animate-cyber-grid opacity-10 pointer-events-none" />
      {/* Ambient background glows */}
      <div className="absolute top-1/4 left-1/4 w-[500px] h-[500px] bg-glow-indigo rounded-full pointer-events-none blur-3xl" />
      <div className="absolute bottom-1/4 right-1/4 w-[500px] h-[500px] bg-glow-amber rounded-full pointer-events-none blur-3xl" />

      <motion.div 
        variants={pageVariants}
        initial="initial"
        animate="animate"
        exit="exit"
        className="min-h-screen bg-transparent text-foreground flex flex-col justify-between p-6 md:p-12 max-w-7xl mx-auto space-y-16 overflow-hidden relative z-10"
      >
      {/* Header */}
      <motion.header 
        variants={itemVariants}
        className="flex items-center justify-between border-b border-border/50 pb-6"
      >
        <div className="flex items-center gap-3.5 group">
          <motion.div 
            whileHover={{ scale: 1.05, rotate: -2 }}
            whileTap={{ scale: 0.95 }}
            className="w-11 h-11 rounded-2xl bg-gradient-to-br from-amber-400 to-amber-600 text-amber-950 font-black text-2xl flex items-center justify-center shadow-lg group-hover:shadow-amber-500/20 transition-shadow"
          >
            E
          </motion.div>
          <div>
            <div className="flex items-center gap-2">
              <h1 className="font-extrabold text-xl tracking-tight text-foreground transition-colors">ProjectEcho</h1>
              <span className="text-[10px] font-mono px-2 py-0.5 rounded-full bg-muted text-foreground font-bold border border-border">
                RC1 Certified
              </span>
            </div>
            <p className="text-xs text-muted-foreground font-mono">Evidence-Driven Career Intelligence OS</p>
          </div>
        </div>

        <div className="flex items-center gap-4">
          <ThemeToggle />
          <Link href="/dashboard">
            <Button variant="outline" size="sm" className="hidden sm:inline-flex border-border">
              Sign In
            </Button>
          </Link>
          <Link href="/dashboard">
            <Button variant="champagne" size="sm" className="gap-2">
              <Zap className="w-4 h-4" /> Launch Platform
            </Button>
          </Link>
        </div>
      </motion.header>

      {/* Hero Section */}
      <main className="space-y-24">
        <div className="grid grid-cols-1 lg:grid-cols-12 gap-12 items-center">
          {/* Left Column: Vision & CTA */}
          <motion.div 
            variants={containerVariants}
            initial="hidden"
            animate="show"
            className="lg:col-span-7 space-y-8 text-center lg:text-left"
          >
            <motion.div variants={itemVariants} className="inline-flex items-center gap-2 py-1.5 px-3.5 rounded-full bg-muted/50 border border-border text-foreground text-xs font-mono font-semibold backdrop-blur-md">
              <Sparkles className="w-3.5 h-3.5 text-amber-500 animate-pulse" />
              <span>Deterministic AI Reasoning & Sovereign Proof</span>
            </motion.div>

            <motion.h1 variants={itemVariants} className="text-5xl md:text-7xl font-black tracking-tighter leading-[1.1] text-foreground">
              The Career OS <br />
              <span className="text-transparent bg-clip-text bg-gradient-to-r from-amber-400 to-amber-500 neon-glow-gold">Backed by Proof.</span>
            </motion.h1>

            <motion.p variants={itemVariants} className="text-lg md:text-xl text-muted-foreground max-w-2xl leading-relaxed mx-auto lg:mx-0">
              ProjectEcho replaces static, inflated resumes with cryptographically verifiable competency proof, auditable decision graphs, and zero-hallucination AI evaluation.
            </motion.p>

            <motion.div variants={itemVariants} className="flex flex-col sm:flex-row items-center gap-4 pt-4 justify-center lg:justify-start">
              <Link href="/dashboard" className="w-full sm:w-auto">
                <Button size="lg" variant="champagne" className="gap-2.5 w-full sm:w-auto text-base">
                  Enter Executive Dashboard <ArrowRight className="w-4 h-4" />
                </Button>
              </Link>
              <Link href="/passport" className="w-full sm:w-auto">
                <Button size="lg" variant="outline" className="w-full sm:w-auto text-base">
                  <UserCheck className="w-4 h-4 mr-2" /> Initialize Passport
                </Button>
              </Link>
            </motion.div>

            {/* Quick Metrics Bar */}
            <motion.div variants={itemVariants} className="grid grid-cols-3 gap-8 pt-8 border-t border-border/50 text-left">
              <div>
                <div className="text-3xl font-black text-foreground">100%</div>
                <div className="text-xs text-muted-foreground font-mono mt-1 uppercase tracking-wider">Proof Determinism</div>
              </div>
              <div>
                <div className="text-3xl font-black text-foreground text-gradient-champagne">Tier 4</div>
                <div className="text-xs text-muted-foreground font-mono mt-1 uppercase tracking-wider">Cryptographic Trust</div>
              </div>
              <div>
                <div className="text-3xl font-black text-foreground">&lt;200ms</div>
                <div className="text-xs text-muted-foreground font-mono mt-1 uppercase tracking-wider">Evaluation SLA</div>
              </div>
            </motion.div>
          </motion.div>

          {/* Right Column: 3D Holographic Visualizer */}
          <motion.div 
            initial={{ opacity: 0, scale: 0.9, filter: 'blur(20px)' }}
            animate={{ opacity: 1, scale: 1, filter: 'blur(0px)' }}
            transition={{ duration: 1, ease: [0.16, 1, 0.3, 1], delay: 0.2 }}
            className="lg:col-span-5 relative flex items-center justify-center"
          >
            <div className="relative w-full max-w-md aspect-square rounded-3xl p-8 bg-zinc-950 border border-amber-500/30 shadow-[0_0_32px_-8px_rgba(245,158,11,0.15)] flex flex-col items-center justify-between text-center overflow-hidden transition-all duration-700 hover:shadow-2xl hover:shadow-amber-500/20 group">
              <div className="absolute inset-0 bg-gradient-to-b from-white/5 to-transparent opacity-0 group-hover:opacity-100 transition-opacity duration-700 pointer-events-none" />
              
              <div className="flex items-center justify-between w-full z-10">
                <Badge variant="champagne" className="text-[10px] shadow-sm">Tier 4 Verified</Badge>
                <span className="text-[10px] font-mono text-muted-foreground">3D WebGL Core</span>
              </div>

              {/* 3D Hologram Orb */}
              <div className="my-auto z-10 transition-transform duration-700 group-hover:scale-110">
                <HologramOrb size={220} verified={true} />
              </div>

              <div className="z-10 space-y-1.5">
                <div className="text-xs font-mono text-foreground font-bold tracking-widest uppercase">Cryptographic Anchor Active</div>
                <div className="text-[11px] text-muted-foreground">Immutable Aggregate Roots & Spring Boot 3 Engine</div>
              </div>
            </div>
          </motion.div>
        </div>

        {/* 3D Interactive Galaxy Canvas Showcase */}
        <motion.div 
          initial="hidden"
          whileInView="show"
          viewport={{ once: true, margin: "-100px" }}
          variants={containerVariants}
          className="space-y-6 pt-12"
        >
          <div className="flex flex-col md:flex-row md:items-end justify-between gap-4">
            <motion.div variants={itemVariants} className="space-y-2">
              <h2 className="text-3xl font-bold tracking-tight">Interactive 3D Competency Universe</h2>
              <p className="text-base text-muted-foreground">Real-time WebGL particle constellation mapping verified domain skills.</p>
            </motion.div>
            <motion.div variants={itemVariants}>
              <Link href="/skills">
                <Button variant="outline" className="gap-2">
                  <Zap className="w-4 h-4" /> Explore Full 3D Galaxy
                </Button>
              </Link>
            </motion.div>
          </div>
          <motion.div variants={itemVariants} className="rounded-2xl overflow-hidden border border-border/50 relative">
             <div className="absolute inset-0 bg-noise pointer-events-none z-10 opacity-50 mix-blend-overlay" />
             <ThreeSkillGalaxy />
          </motion.div>
        </motion.div>

        {/* 9 Core Business Modules Showcase Grid */}
        <div className="space-y-12 pt-12">
          <motion.div 
            initial={{ opacity: 0, y: 20 }}
            whileInView={{ opacity: 1, y: 0 }}
            viewport={{ once: true }}
            className="text-center max-w-2xl mx-auto space-y-4"
          >
            <Badge variant="champagne" className="px-3 py-1 text-xs">9 Sovereign Modules</Badge>
            <h2 className="text-4xl font-extrabold tracking-tight">Enterprise Architecture Matrix</h2>
            <p className="text-base text-muted-foreground leading-relaxed">
              Clean Hexagonal separation between Domain Models, Rule Evaluation, and Cryptographic Evidence.
            </p>
          </motion.div>

          <motion.div 
            variants={containerVariants}
            initial="hidden"
            whileInView="show"
            viewport={{ once: true, margin: "-50px" }}
            className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-6"
          >
            {[
              { title: '1. Career Passport', href: '/passport', icon: UserCheck, desc: 'Cryptographically immutable career profile storing sovereign identities and proof-backed skills.', highlight: false },
              { title: '2. Evidence Verification', href: '/evidence', icon: FileCheck, desc: 'Multi-tier evidence trust assessment pipeline evaluating SHA-256 commits, PRs, and certifications.', highlight: false },
              { title: '3. Taxonomy Galaxy', href: '/skills', icon: Zap, desc: 'Standardized skill ontologies with 3D constellation visualization and relational skill hierarchies.', highlight: false },
              { title: '4. Mission Explorer', href: '/missions', icon: Layers, desc: 'Target executive career quests with required competency checklists and real-time state transitions.', highlight: false },
              { title: '5. Readiness Engine', href: '/assessment', icon: ShieldCheck, desc: 'Deterministic rule engine evaluating qualification scores and eligibility against live job criteria.', highlight: false },
              { title: '6. Reasoning Cards', href: '/reasoning', icon: Award, desc: 'Explainable AI audit trails providing clear factor breakdowns and downloadable certificates.', highlight: true },
              { title: '7. Decision Graph', href: '/graph', icon: GitGraph, desc: '3D DAG trace connecting passports, evidence claims, rule evaluations, and final scoring.', highlight: false },
              { title: '8. Executive Profile', href: '/profile', icon: Sparkles, desc: 'Public-facing verified profile with 3D Holographic Passport Badge and shareable proof link.', highlight: false },
              { title: '9. Executive Dashboard', href: '/dashboard', icon: BrainCircuit, desc: 'Real-time command center aggregating passports, active missions, and enterprise quality metrics.', highlight: true },
            ].map((module, i) => {
              const Icon = module.icon;
              return (
                <Link key={i} href={module.href} className="group block h-full outline-none">
                  <motion.div variants={itemVariants} className="h-full">
                    <Card champagneBorder={module.highlight} className="h-full flex flex-col gap-4">
                      <div className={`w-12 h-12 rounded-xl flex items-center justify-center transition-colors ${module.highlight ? 'bg-amber-500/10 text-amber-500' : 'bg-muted text-foreground'}`}>
                         <Icon className="w-6 h-6 transition-transform group-hover:scale-110 group-hover:rotate-3" />
                      </div>
                      <div>
                        <h3 className="text-lg font-bold text-foreground mb-2 group-hover:text-primary transition-colors">{module.title}</h3>
                        <p className="text-sm text-muted-foreground leading-relaxed">{module.desc}</p>
                      </div>
                    </Card>
                  </motion.div>
                </Link>
              );
            })}
          </motion.div>
        </div>
      </main>

      {/* Footer */}
      <footer className="border-t border-border/50 mt-24 pt-8 pb-4 flex flex-col md:flex-row items-center justify-between gap-4 text-xs text-muted-foreground font-mono">
        <p>&copy; {new Date().getFullYear()} ProjectEcho Engineering Organization. All rights reserved.</p>
        <div className="flex items-center gap-6">
          <span className="flex items-center gap-2"><span className="w-1.5 h-1.5 rounded-full bg-emerald-500 animate-pulse" /> Spring Boot 3.3.0 Engine UP</span>
          <span className="flex items-center gap-2"><span className="w-1.5 h-1.5 rounded-full bg-blue-500" /> Next.js 16.3 Turbopack</span>
          <span className="flex items-center gap-2"><span className="w-1.5 h-1.5 rounded-full bg-purple-500" /> WCAG 2.1 AA Compliant</span>
        </div>
      </footer>
    </motion.div>
    </div>
  );
}
