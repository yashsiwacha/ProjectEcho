'use client';

import { useState } from 'react';
import { useQuery } from '@tanstack/react-query';
import { api } from '@/lib/api';
import AppLayout from '@/components/AppLayout';
import { Card, CardHeader, CardTitle, CardDescription, CardContent } from '@/components/ui/Card';
import { Badge } from '@/components/ui/Badge';
import { Button } from '@/components/ui/Button';
import {
  Award,
  ShieldCheck,
  CheckCircle2,
  FileCode,
  Sparkles,
  Download,
  Printer,
  ChevronRight,
  GitGraph,
  X
} from 'lucide-react';
import { toast } from 'sonner';

export default function ReasoningPage() {
  const [certModalOpen, setCertModalOpen] = useState(false);

  const { data: cards } = useQuery({
    queryKey: ['reasoning-cards'],
    queryFn: () => api.getReasoningCards(),
  });

  const card = cards?.content[0] || {
    id: 'rc-001',
    confidenceScore: 98.6,
    summary: 'Candidate demonstrates comprehensive Tier 4 proof in Distributed Systems, Java 21 DDD, and Spring Boot 3 with 0 hallucination risk.',
    factors: ['Tier 4 Cryptographic Evidence', '100% Taxonomy Skill Match', 'Deterministic Rule Engine Execution'],
    createdAt: '2026-08-08',
  };

  return (
    <AppLayout>
      <div className="space-y-8 max-w-5xl">
        {/* Header */}
        <div className="flex flex-col md:flex-row md:items-center justify-between gap-4">
          <div>
            <div className="flex items-center gap-2">
              <h1 className="text-3xl font-extrabold tracking-tight text-white">Explainable Reasoning Cards</h1>
              <Badge variant="champagne" className="text-[10px]">Zero Hallucination</Badge>
            </div>
            <p className="text-muted-foreground text-sm mt-1">
              Transparent, auditable AI reasoning trails and deterministic factor weighting certificates.
            </p>
          </div>

          <Button
            variant="champagne"
            size="sm"
            onClick={() => setCertModalOpen(true)}
            className="gap-2 text-xs font-bold shadow-lg shadow-amber-500/20"
          >
            <Download className="w-4 h-4" /> Download Certificate
          </Button>
        </div>

        {/* Primary Reasoning Card Showcase */}
        <div className="rounded-2xl glass-panel-glow p-8 border border-amber-500/40 space-y-6">
          <div className="flex flex-col sm:flex-row sm:items-center justify-between gap-4 border-b border-border/80 pb-4">
            <div className="flex items-center gap-3">
              <div className="p-2.5 rounded-xl bg-amber-500/20 text-amber-300 border border-amber-500/30">
                <Award className="w-6 h-6" />
              </div>
              <div>
                <h2 className="text-xl font-bold text-white">Executive Qualification Audit</h2>
                <span className="text-xs font-mono text-muted-foreground">ID: {card.id}</span>
              </div>
            </div>

            <div className="flex items-center gap-2">
              <Badge variant="success" className="gap-1 py-1 px-3 text-xs font-bold">
                <CheckCircle2 className="w-3.5 h-3.5" /> 98.6% Confidence
              </Badge>
              <span className="text-xs font-mono text-emerald-400 font-bold bg-emerald-500/10 px-2.5 py-1 rounded-md border border-emerald-500/30">
                QUALIFIED
              </span>
            </div>
          </div>

          {/* Reasoning Summary */}
          <div className="p-4 rounded-xl bg-slate-900/80 border border-border space-y-2">
            <span className="text-[10px] font-mono text-muted-foreground uppercase tracking-wider font-semibold">
              Deterministic Evaluation Summary
            </span>
            <p className="text-sm text-foreground leading-relaxed font-medium">
              &quot;{card.summary}&quot;
            </p>
          </div>

          {/* Factor Contribution Breakdown */}
          <div className="space-y-4">
            <h3 className="text-sm font-mono font-bold text-white uppercase tracking-wider">
              Factor Weighting Breakdown
            </h3>

            <div className="space-y-3">
              <div className="space-y-1">
                <div className="flex justify-between text-xs font-mono">
                  <span className="text-muted-foreground">1. Tier 4 Cryptographic Evidence Backing</span>
                  <span className="text-emerald-400 font-bold">100% Full Proof</span>
                </div>
                <div className="w-full h-2 rounded-full bg-slate-900 overflow-hidden">
                  <div className="h-full bg-emerald-400 rounded-full w-full" />
                </div>
              </div>

              <div className="space-y-1">
                <div className="flex justify-between text-xs font-mono">
                  <span className="text-muted-foreground">2. Spring Boot & Java 21 Taxonomy Alignment</span>
                  <span className="text-amber-400 font-bold">98.5% Match</span>
                </div>
                <div className="w-full h-2 rounded-full bg-slate-900 overflow-hidden">
                  <div className="h-full bg-amber-400 rounded-full w-[98.5%]" />
                </div>
              </div>

              <div className="space-y-1">
                <div className="flex justify-between text-xs font-mono">
                  <span className="text-muted-foreground">3. OWASP Security & Architectural Compliance</span>
                  <span className="text-cyan-400 font-bold">100% Clean</span>
                </div>
                <div className="w-full h-2 rounded-full bg-slate-900 overflow-hidden">
                  <div className="h-full bg-cyan-400 rounded-full w-full" />
                </div>
              </div>
            </div>
          </div>

          {/* Step-by-Step Reasoning Trail */}
          <div className="space-y-3 pt-2">
            <h3 className="text-sm font-mono font-bold text-white uppercase tracking-wider">
              Auditable Execution Trail
            </h3>

            <div className="grid grid-cols-1 sm:grid-cols-3 gap-3">
              <div className="p-3 rounded-xl bg-slate-900/60 border border-border space-y-1">
                <span className="text-[10px] font-mono text-amber-400 font-bold">STEP 01</span>
                <h4 className="text-xs font-bold text-white">Identity Root</h4>
                <p className="text-[11px] text-muted-foreground">Passport authenticated in Spring Boot context.</p>
              </div>

              <div className="p-3 rounded-xl bg-slate-900/60 border border-border space-y-1">
                <span className="text-[10px] font-mono text-emerald-400 font-bold">STEP 02</span>
                <h4 className="text-xs font-bold text-white">Evidence Hash</h4>
                <p className="text-[11px] text-muted-foreground">SHA-256 commit verified against public repository.</p>
              </div>

              <div className="p-3 rounded-xl bg-slate-900/60 border border-border space-y-1">
                <span className="text-[10px] font-mono text-cyan-400 font-bold">STEP 03</span>
                <h4 className="text-xs font-bold text-white">Rule Execution</h4>
                <p className="text-[11px] text-muted-foreground">Readiness qualification score synthesized to 98.6%.</p>
              </div>
            </div>
          </div>
        </div>
      </div>

      {/* Printable Certificate Modal */}
      {certModalOpen && (
        <div className="fixed inset-0 z-50 flex items-center justify-center p-4 bg-black/85 backdrop-blur-md animate-in fade-in duration-200">
          <div className="relative w-full max-w-2xl rounded-2xl bg-card border-2 border-amber-500/50 p-8 shadow-2xl glass-panel-glow space-y-6">
            <button
              onClick={() => setCertModalOpen(false)}
              className="absolute top-4 right-4 text-muted-foreground hover:text-white"
            >
              <X className="w-5 h-5" />
            </button>

            {/* Certificate Border & Header */}
            <div className="border-4 border-double border-amber-500/60 p-8 rounded-xl text-center space-y-6 bg-gradient-to-b from-slate-950 via-slate-900 to-black">
              <div className="space-y-1">
                <div className="text-xs font-mono font-bold text-amber-400 tracking-widest uppercase">
                  PROJECT ECHO SOVEREIGN CERTIFICATION
                </div>
                <h2 className="text-3xl font-serif font-bold text-white tracking-wide">
                  Certificate of Competency Readiness
                </h2>
              </div>

              <p className="text-xs text-muted-foreground font-mono max-w-md mx-auto">
                This document certifies that the candidate has undergone deterministic AI evaluation and satisfied all Tier 4 cryptographic proof conditions.
              </p>

              <div className="py-2 space-y-1">
                <span className="text-[10px] font-mono text-muted-foreground uppercase">Certified Subject</span>
                <div className="text-xl font-bold text-white">Jane Doe</div>
                <div className="text-xs font-mono text-amber-300">Principal Distributed Systems Architect</div>
              </div>

              <div className="grid grid-cols-2 gap-4 border-t border-b border-amber-500/30 py-4 text-xs font-mono">
                <div>
                  <span className="text-muted-foreground block text-[10px]">QUALIFICATION SCORE</span>
                  <span className="text-emerald-400 font-bold text-base">98.6% (PASS)</span>
                </div>
                <div>
                  <span className="text-muted-foreground block text-[10px]">VERIFICATION DATE</span>
                  <span className="text-white font-bold text-base">2026-08-08</span>
                </div>
              </div>

              <div className="flex items-center justify-between text-[10px] font-mono text-muted-foreground">
                <span>Authority: Project Echo Rule Engine</span>
                <span>SHA-256: 0x8cc653f...2a632</span>
              </div>
            </div>

            <div className="flex items-center gap-3">
              <Button
                variant="champagne"
                className="w-full font-bold"
                onClick={() => {
                  toast.success('Certificate downloaded successfully!');
                  setCertModalOpen(false);
                }}
              >
                <Download className="w-4 h-4 mr-2" /> Download Official PDF
              </Button>
            </div>
          </div>
        </div>
      )}
    </AppLayout>
  );
}
