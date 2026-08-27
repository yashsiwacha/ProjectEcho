'use client';

import { useState } from 'react';
import AppLayout from '@/components/AppLayout';
import { motion } from 'framer-motion';
import { Card, CardHeader, CardTitle, CardDescription, CardContent } from '@/components/ui/Card';
import { Badge } from '@/components/ui/Badge';
import { Button } from '@/components/ui/Button';
import {
  GitGraph,
  ShieldCheck,
  CheckCircle2,
  FileCode,
  Terminal,
  Download,
  Sparkles,
  Layers,
  Cpu
} from 'lucide-react';
import { toast } from 'sonner';
import ThreeDecisionGraph from '@/components/3d/ThreeDecisionGraph';

export default function GraphPage() {
  const [selectedNode, setSelectedNode] = useState<unknown>(null);

  const handleExportJson = () => {
    toast.success('Decision DAG AST exported as JSON!');
  };

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
        className="space-y-8 max-w-5xl"
      >
        {/* Header */}
        <div className="flex flex-col md:flex-row md:items-center justify-between gap-4">
          <div>
            <div className="flex items-center gap-2">
              <h1 className="text-3xl font-extrabold tracking-tight text-white">Decision Graph Traceability</h1>
              <Badge variant="champagne" className="text-[10px]">3D Interactive DAG</Badge>
            </div>
            <p className="text-muted-foreground text-sm mt-1">
              Full cryptographically linked lineage connecting Passports, Evidence Claims, Rule Engines, and Outputs.
            </p>
          </div>

          <Button
            variant="outline"
            size="sm"
            onClick={handleExportJson}
            className="gap-2 text-xs border-border text-foreground hover:bg-muted"
          >
            <Download className="w-4 h-4" /> Export DAG AST
          </Button>
        </div>

        {/* 3D WebGL Decision Graph Canvas */}
        <motion.div variants={itemVariants} className="space-y-3">
          <div className="flex items-center justify-between px-2">
            <span className="text-xs font-mono text-muted-foreground uppercase tracking-wider font-semibold">
              Interactive 3D WebGL Execution Lineage (Drag to rotate)
            </span>
            <span className="text-xs font-mono text-foreground">
              ● Particle Pulses Active
            </span>
          </div>

          <ThreeDecisionGraph onSelectNode={(node) => setSelectedNode(node)} />
        </motion.div>

        {/* Two Column Grid: Execution Logs & Provenance */}
        <motion.div variants={itemVariants} className="grid grid-cols-1 md:grid-cols-12 gap-8">
          {/* Live Execution Stream Terminal */}
          <Card champagneBorder className="md:col-span-7 p-6 space-y-4">
            <CardHeader className="p-0 pb-2">
              <CardTitle className="text-base font-bold text-white flex items-center gap-2">
                <Terminal className="w-4 h-4 text-foreground" /> Real-Time Decision Graph Stream
              </CardTitle>
              <CardDescription className="text-xs">
                Domain event dispatching and deterministic evaluation log
              </CardDescription>
            </CardHeader>
            <CardContent className="p-0">
              <div className="p-4 rounded-xl bg-slate-950 border border-slate-800 font-mono text-xs text-slate-300 space-y-2 max-h-56 overflow-y-auto">
                <div className="text-foreground font-semibold">[00:00.012] IDENTITY_ROOT_INITIALIZED: f2706538...</div>
                <div className="text-foreground">[00:00.045] EVIDENCE_CLAIM_INGESTED: fdfe527d (Tier 4)</div>
                <div className="text-foreground">[00:00.089] TAXONOMY_SKILL_LINKED: Java 21 / Spring Boot 3</div>
                <div className="text-purple-400">[00:00.124] RULE_ENGINE_EVALUATE: Rule #104 -&gt; ELIGIBLE</div>
                <div className="text-foreground font-bold">[00:00.158] REASONING_CARD_GENERATED: rc-001 (Score: 98.6%)</div>
                <div className="text-slate-500">[00:00.160] DAG_EXECUTION_COMPLETED_SUCCESSFULLY</div>
              </div>
            </CardContent>
          </Card>

          {/* Audit Traceability Card */}
          <Card champagneBorder className="md:col-span-5 p-6 space-y-4 flex flex-col justify-between">
            <div>
              <CardHeader className="p-0 pb-2">
                <CardTitle className="text-base font-bold text-white flex items-center gap-2">
                  <FileCode className="w-4 h-4 text-foreground" /> Cryptographic Provenance
                </CardTitle>
                <CardDescription className="text-xs">
                  Zero black-box opacity
                </CardDescription>
              </CardHeader>
              <p className="text-xs text-muted-foreground leading-relaxed pt-2">
                Every node in the 3D Decision Graph is backed by immutable domain entities. Any changes to skills or evidence automatically trigger a deterministic re-evaluation.
              </p>
            </div>

            <div className="p-3 rounded-xl bg-muted border border-border text-foreground text-xs font-mono flex items-center gap-2">
              <CheckCircle2 className="w-4 h-4 flex-shrink-0" />
              <span>DAG Hash: 0x98f4...112e Verified</span>
            </div>
          </Card>
        </motion.div>
      </motion.div>
    </AppLayout>
  );
}
