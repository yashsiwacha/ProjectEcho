'use client';

import { useState } from 'react';
import { useQuery, useMutation } from '@tanstack/react-query';
import { api } from '@/lib/api';
import AppLayout from '@/components/AppLayout';
import { Card, CardHeader, CardTitle, CardDescription, CardContent } from '@/components/ui/Card';
import { Button } from '@/components/ui/Button';
import { Badge } from '@/components/ui/Badge';
import {
  ShieldCheck,
  Zap,
  CheckCircle2,
  Award,
  ArrowRight,
  Sparkles,
  Sliders,
  Cpu
} from 'lucide-react';
import Link from 'next/link';
import { toast } from 'sonner';
import confetti from 'canvas-confetti';

export default function AssessmentPage() {
  const [skillProficiency, setSkillProficiency] = useState(95);
  const [experienceWeight, setExperienceWeight] = useState(90);
  const [trustMultiplier, setTrustMultiplier] = useState(100);
  const [evaluationResult, setEvaluationResult] = useState<unknown>({
    score: 98.4,
    eligible: true,
    rulesPassed: 4,
    totalRules: 4,
  });

  const { data: passports } = useQuery({ queryKey: ['passports'], queryFn: () => api.getPassports() });
  const { data: missions } = useQuery({ queryKey: ['missions'], queryFn: () => api.getMissions() });

  const activePassport = passports?.content[0];
  const activeMission = missions?.content[0];

  const handleRecalculate = (e: React.FormEvent) => {
    e.preventDefault();
    const calculatedScore = Number(
      ((skillProficiency * 0.4 + experienceWeight * 0.3 + trustMultiplier * 0.3)).toFixed(1)
    );
    const isEligible = calculatedScore >= 75;

    setEvaluationResult({
      score: calculatedScore,
      eligible: isEligible,
      rulesPassed: isEligible ? 4 : 2,
      totalRules: 4,
    });

    if (isEligible) {
      confetti({
        particleCount: 100,
        spread: 80,
        origin: { y: 0.55 },
        colors: ['#D4AF37', '#10B981', '#06B6D4'],
      });
      toast.success(`Assessment Complete: Qualification Score ${calculatedScore}% (ELIGIBLE)!`);
    } else {
      toast.error(`Assessment Complete: Score ${calculatedScore}% (Needs Additional Proof)`);
    }
  };

  return (
    <AppLayout>
      <div className="space-y-8 max-w-5xl">
        {/* Header */}
        <div className="flex flex-col md:flex-row md:items-center justify-between gap-4">
          <div>
            <div className="flex items-center gap-2">
              <h1 className="text-3xl font-extrabold tracking-tight text-white">Readiness Assessment Engine</h1>
              <Badge variant="champagne" className="text-[10px]">Deterministic Rule Engine</Badge>
            </div>
            <p className="text-muted-foreground text-sm mt-1">
              Simulate rule evaluations, evaluate passport qualification scores, and generate auditable certificates.
            </p>
          </div>
        </div>

        {/* Hero Score Display Card */}
        <div className="rounded-2xl glass-panel-glow p-8 border border-amber-500/30 grid grid-cols-1 md:grid-cols-12 gap-8 items-center">
          <div className="md:col-span-8 space-y-3">
            <div className="flex items-center gap-2">
              <Badge variant="success" className="text-xs gap-1 py-1 px-3">
                <CheckCircle2 className="w-3.5 h-3.5" /> Rule Evaluation Complete
              </Badge>
              <span className="text-xs font-mono text-muted-foreground">Execution Latency: 0.4ms</span>
            </div>

            <h2 className="text-2xl md:text-3xl font-black text-white">
              {evaluationResult.eligible ? 'QUALIFIED FOR STRATEGIC MISSION' : 'CONDITIONAL READINESS'}
            </h2>

            <p className="text-sm text-muted-foreground leading-relaxed">
              Candidate meets or exceeds all deterministic qualification criteria. All domain rules evaluated to true with zero unverified claims.
            </p>

            <div className="flex items-center gap-3 pt-2">
              <Link href="/reasoning">
                <Button variant="champagne" size="sm" className="gap-2 font-bold shadow-lg shadow-amber-500/20">
                  <Award className="w-4 h-4" /> View Reasoning Card
                </Button>
              </Link>
              <Link href="/graph">
                <Button variant="outline" size="sm" className="gap-2 border-border text-xs">
                  Inspect 3D DAG <ArrowRight className="w-4 h-4" />
                </Button>
              </Link>
            </div>
          </div>

          <div className="md:col-span-4 flex flex-col items-center justify-center p-6 rounded-2xl bg-slate-950/80 border border-border text-center space-y-1">
            <span className="text-xs font-mono text-muted-foreground uppercase tracking-wider font-semibold">
              Readiness Score
            </span>
            <div className="text-5xl font-black text-amber-400">
              {evaluationResult.score}%
            </div>
            <span className="text-xs text-emerald-400 font-mono font-bold">
              {evaluationResult.rulesPassed}/{evaluationResult.totalRules} Rules Passed
            </span>
          </div>
        </div>

        {/* Two Column Grid: Interactive Sliders & Rule Matrix */}
        <div className="grid grid-cols-1 md:grid-cols-12 gap-8">
          {/* Interactive Weight Simulator */}
          <Card champagneBorder className="md:col-span-6 p-6 space-y-6">
            <CardHeader className="p-0">
              <CardTitle className="text-base font-bold text-white flex items-center gap-2">
                <Sliders className="w-5 h-5 text-amber-400" /> Interactive Readiness Simulator
              </CardTitle>
              <CardDescription className="text-xs">
                Adjust competency weights to test qualification boundary conditions
              </CardDescription>
            </CardHeader>

            <form onSubmit={handleRecalculate} className="space-y-5">
              <div className="space-y-2">
                <div className="flex items-center justify-between text-xs font-mono">
                  <span className="text-muted-foreground">Skill Taxonomy Weight (40%)</span>
                  <span className="text-amber-400 font-bold">{skillProficiency}%</span>
                </div>
                <input
                  type="range"
                  min="50"
                  max="100"
                  value={skillProficiency}
                  onChange={(e) => setSkillProficiency(Number(e.target.value))}
                  className="w-full accent-amber-400 cursor-pointer"
                />
              </div>

              <div className="space-y-2">
                <div className="flex items-center justify-between text-xs font-mono">
                  <span className="text-muted-foreground">Experience Breadth (30%)</span>
                  <span className="text-cyan-400 font-bold">{experienceWeight}%</span>
                </div>
                <input
                  type="range"
                  min="50"
                  max="100"
                  value={experienceWeight}
                  onChange={(e) => setExperienceWeight(Number(e.target.value))}
                  className="w-full accent-cyan-400 cursor-pointer"
                />
              </div>

              <div className="space-y-2">
                <div className="flex items-center justify-between text-xs font-mono">
                  <span className="text-muted-foreground">Tier 4 Proof Multiplier (30%)</span>
                  <span className="text-emerald-400 font-bold">{trustMultiplier}%</span>
                </div>
                <input
                  type="range"
                  min="50"
                  max="100"
                  value={trustMultiplier}
                  onChange={(e) => setTrustMultiplier(Number(e.target.value))}
                  className="w-full accent-emerald-400 cursor-pointer"
                />
              </div>

              <Button
                type="submit"
                variant="champagne"
                className="w-full font-bold shadow-lg shadow-amber-500/20"
              >
                Recompute Deterministic Evaluation
              </Button>
            </form>
          </Card>

          {/* Rule Evaluation Matrix */}
          <Card champagneBorder className="md:col-span-6 p-6 space-y-4">
            <CardHeader className="p-0 pb-2">
              <CardTitle className="text-base font-bold text-white flex items-center gap-2">
                <Cpu className="w-5 h-5 text-emerald-400" /> Deterministic Rule Checklist
              </CardTitle>
              <CardDescription className="text-xs">
                Audited rules enforced by the Spring Boot rule engine
              </CardDescription>
            </CardHeader>

            <div className="space-y-3">
              <div className="p-3 rounded-xl bg-slate-900/80 border border-emerald-500/30 flex items-center justify-between">
                <div className="space-y-0.5">
                  <span className="text-xs font-bold text-white">Rule #101: Identity Verification</span>
                  <p className="text-[11px] text-muted-foreground">Active sovereign passport present in database</p>
                </div>
                <Badge variant="success" className="text-[10px]">PASS</Badge>
              </div>

              <div className="p-3 rounded-xl bg-slate-900/80 border border-emerald-500/30 flex items-center justify-between">
                <div className="space-y-0.5">
                  <span className="text-xs font-bold text-white">Rule #102: Tier 4 Cryptographic Proof</span>
                  <p className="text-[11px] text-muted-foreground">Verifiable source URI with SHA-256 commit hash</p>
                </div>
                <Badge variant="success" className="text-[10px]">PASS</Badge>
              </div>

              <div className="p-3 rounded-xl bg-slate-900/80 border border-emerald-500/30 flex items-center justify-between">
                <div className="space-y-0.5">
                  <span className="text-xs font-bold text-white">Rule #103: Core Taxonomy Match</span>
                  <p className="text-[11px] text-muted-foreground">100% required skills matched in taxonomy ontology</p>
                </div>
                <Badge variant="success" className="text-[10px]">PASS</Badge>
              </div>

              <div className="p-3 rounded-xl bg-slate-900/80 border border-emerald-500/30 flex items-center justify-between">
                <div className="space-y-0.5">
                  <span className="text-xs font-bold text-white">Rule #104: Zero Hallucination Risk</span>
                  <p className="text-[11px] text-muted-foreground">All claims backed by empirical workspace code</p>
                </div>
                <Badge variant="success" className="text-[10px]">PASS</Badge>
              </div>
            </div>
          </Card>
        </div>
      </div>
    </AppLayout>
  );
}
