'use client';

import AppLayout from '@/components/AppLayout';
import { Card, CardHeader, CardTitle, CardDescription, CardContent } from '@/components/ui/Card';
import { Badge } from '@/components/ui/Badge';
import { GitGraph, ShieldCheck, CheckCircle2, FileCode } from 'lucide-react';

export default function GraphPage() {
  return (
    <AppLayout>
      <div className="space-y-8 max-w-5xl">
        <div>
          <h1 className="text-3xl font-bold tracking-tight">Decision Graph Traceability</h1>
          <p className="text-muted-foreground mt-1">Full audit lineage of evidence, rules, and AI decision graphs.</p>
        </div>

        {/* Visual Graph Representation */}
        <Card champagneBorder className="p-8 space-y-8">
          <div className="flex items-center justify-between border-b border-border pb-4">
            <div>
              <Badge variant="champagne" className="gap-1.5">
                <ShieldCheck className="w-3.5 h-3.5" /> Immutable Audit Graph
              </Badge>
              <h2 className="text-xl font-bold mt-2">Graph Execution Lineage</h2>
            </div>
            <Badge variant="success">Deterministic Engine</Badge>
          </div>

          <div className="grid grid-cols-1 md:grid-cols-4 gap-4 relative">
            <div className="p-4 rounded-xl bg-card border border-border space-y-2">
              <span className="text-[10px] font-mono text-accent uppercase tracking-wider block">Node 1: Identity</span>
              <h4 className="font-semibold text-sm">Career Passport</h4>
              <p className="text-xs text-muted-foreground">Passport ID verified</p>
              <Badge variant="success" className="text-[10px]">Verified</Badge>
            </div>

            <div className="p-4 rounded-xl bg-card border border-border space-y-2">
              <span className="text-[10px] font-mono text-accent uppercase tracking-wider block">Node 2: Evidence</span>
              <h4 className="font-semibold text-sm">Proof Claim</h4>
              <p className="text-xs text-muted-foreground">Source URI & Tier 4</p>
              <Badge variant="champagne" className="text-[10px]">Tier 4 Proof</Badge>
            </div>

            <div className="p-4 rounded-xl bg-card border border-border space-y-2">
              <span className="text-[10px] font-mono text-accent uppercase tracking-wider block">Node 3: Rule Engine</span>
              <h4 className="font-semibold text-sm">Skill Match Rule</h4>
              <p className="text-xs text-muted-foreground">Standard Assessment</p>
              <Badge variant="default" className="text-[10px]">Score 100</Badge>
            </div>

            <div className="p-4 rounded-xl bg-card border border-border space-y-2">
              <span className="text-[10px] font-mono text-accent uppercase tracking-wider block">Node 4: Output</span>
              <h4 className="font-semibold text-sm">Reasoning Card</h4>
              <p className="text-xs text-muted-foreground">Explainable AI Summary</p>
              <Badge variant="success" className="text-[10px]">Eligible</Badge>
            </div>
          </div>

          <div className="p-5 rounded-xl bg-muted/40 border border-border space-y-3">
            <div className="flex items-center gap-2">
              <FileCode className="w-4 h-4 text-accent" />
              <h3 className="font-semibold text-sm">Audit Trace & Provenance</h3>
            </div>
            <p className="text-xs text-muted-foreground leading-relaxed">
              Every decision graph produced by ProjectEcho is cryptographically traceable back to its originating evidence claim and aggregate root domain events.
            </p>
          </div>
        </Card>
      </div>
    </AppLayout>
  );
}
