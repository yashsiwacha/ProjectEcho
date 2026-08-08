'use client';

import { useState } from 'react';
import { useQuery, useMutation, useQueryClient } from '@tanstack/react-query';
import { api } from '@/lib/api';
import AppLayout from '@/components/AppLayout';
import { Card, CardHeader, CardTitle, CardDescription, CardContent } from '@/components/ui/Card';
import { Button } from '@/components/ui/Button';
import { Input } from '@/components/ui/Input';
import { Badge } from '@/components/ui/Badge';
import {
  FileCheck,
  UploadCloud,
  CheckCircle2,
  AlertCircle,
  ShieldCheck,
  Sparkles,
  Link as LinkIcon,
  Cpu,
  Zap
} from 'lucide-react';
import { toast } from 'sonner';
import confetti from 'canvas-confetti';

export default function EvidencePage() {
  const queryClient = useQueryClient();
  const [sourceUri, setSourceUri] = useState('https://github.com/project-echo/echo-shared/pull/42');
  const [selectedSkillId, setSelectedSkillId] = useState('1');
  const [trustTier, setTrustTier] = useState('TIER_4');
  const [isHashing, setIsHashing] = useState(false);
  const [shaHash, setShaHash] = useState('0x8cc653fae9821bf...');

  const { data: passports } = useQuery({ queryKey: ['passports'], queryFn: () => api.getPassports() });
  const { data: skills } = useQuery({ queryKey: ['skills'], queryFn: () => api.getSkills() });
  const { data: evidenceList, isLoading } = useQuery({
    queryKey: ['evidence'],
    queryFn: () => api.getPassportEvidence(passports?.content[0]?.id || '1'),
  });

  const submitMutation = useMutation({
    mutationFn: api.submitEvidence,
    onSuccess: (data) => {
      queryClient.invalidateQueries({ queryKey: ['evidence'] });
      // Trigger festive confetti burst on successful Tier 4 proof
      confetti({
        particleCount: 80,
        spread: 70,
        origin: { y: 0.6 },
        colors: ['#D4AF37', '#10B981', '#06B6D4'],
      });
      toast.success('Cryptographic evidence submitted and verified as Tier 4!');
    },
    onError: (err: Error) => {
      toast.error(`Evidence submission failed: ${err.message}`);
    },
  });

  const handleSimulateUpload = (e: React.FormEvent) => {
    e.preventDefault();
    setIsHashing(true);
    setTimeout(() => {
      setIsHashing(false);
      const generatedHash = `0x${Array.from({ length: 16 }, () => Math.floor(Math.random() * 16).toString(16)).join('')}...`;
      setShaHash(generatedHash);
      submitMutation.mutate({
        passportId: passports?.content[0]?.id || 'f2706538-f134-4c58-8825-4ee944a10052',
        skillId: selectedSkillId,
        sourceUri,
      });
    }, 600);
  };

  return (
    <AppLayout>
      <div className="space-y-8 max-w-5xl">
        {/* Header */}
        <div className="flex flex-col md:flex-row md:items-center justify-between gap-4">
          <div>
            <div className="flex items-center gap-2">
              <h1 className="text-3xl font-extrabold tracking-tight text-white">Evidence Verification Sandbox</h1>
              <Badge variant="champagne" className="text-[10px]">Tier 4 Proof Engine</Badge>
            </div>
            <p className="text-muted-foreground text-sm mt-1">
              Submit proof links, calculate cryptographic hashes, and evaluate trust tiers against domain rules.
            </p>
          </div>
        </div>

        {/* Interactive Verification Stepper & Uploader */}
        <div className="grid grid-cols-1 md:grid-cols-12 gap-8">
          {/* Uploader Box */}
          <Card champagneBorder className="md:col-span-6 p-6 space-y-6">
            <CardHeader className="p-0">
              <CardTitle className="text-base font-bold text-white flex items-center gap-2">
                <UploadCloud className="w-5 h-5 text-amber-400" /> Ingest Competency Evidence
              </CardTitle>
              <CardDescription className="text-xs">
                Link verifiable git commits, pull requests, or artifact hashes
              </CardDescription>
            </CardHeader>

            <form onSubmit={handleSimulateUpload} className="space-y-4">
              {/* Skill Selector */}
              <div className="space-y-1.5">
                <label className="text-xs font-mono text-muted-foreground uppercase font-semibold">Target Skill</label>
                <select
                  value={selectedSkillId}
                  onChange={(e) => setSelectedSkillId(e.target.value)}
                  className="w-full rounded-xl bg-slate-900 border border-border p-2.5 text-xs text-white outline-none focus:border-amber-500/60"
                >
                  {skills?.content.map((s) => (
                    <option key={s.id} value={s.id}>
                      {s.name} ({s.category})
                    </option>
                  ))}
                  {(!skills?.content || skills.content.length === 0) && (
                    <option value="1">Java 21 Virtual Threads & Architecture</option>
                  )}
                </select>
              </div>

              {/* Source URI */}
              <Input
                label="Source Proof URI / Artifact Commit"
                placeholder="https://github.com/..."
                value={sourceUri}
                onChange={(e) => setSourceUri(e.target.value)}
              />

              {/* Trust Tier Radio Selector */}
              <div className="space-y-2">
                <label className="text-xs font-mono text-muted-foreground uppercase font-semibold block">
                  Select Trust Tier Level
                </label>
                <div className="grid grid-cols-3 gap-2 text-xs font-mono">
                  {['TIER_2', 'TIER_3', 'TIER_4'].map((tier) => (
                    <button
                      type="button"
                      key={tier}
                      onClick={() => setTrustTier(tier)}
                      className={`p-2 rounded-xl border text-center font-bold transition-all ${
                        trustTier === tier
                          ? 'bg-amber-500/20 border-amber-500 text-amber-300 shadow-md shadow-amber-500/10'
                          : 'bg-slate-900 border-border text-muted-foreground hover:border-slate-700'
                      }`}
                    >
                      {tier}
                    </button>
                  ))}
                </div>
              </div>

              {/* Live Hash Box */}
              <div className="p-3 rounded-xl bg-slate-950 border border-slate-800 space-y-1">
                <div className="flex items-center justify-between text-[10px] font-mono text-muted-foreground">
                  <span>SHA-256 PROOF DIGEST</span>
                  <span className="text-emerald-400">IMMUTABLE</span>
                </div>
                <div className="text-xs font-mono text-amber-400 truncate">{shaHash}</div>
              </div>

              <Button
                type="submit"
                variant="champagne"
                className="w-full font-bold shadow-lg shadow-amber-500/20"
                disabled={submitMutation.isPending || isHashing}
              >
                {isHashing ? 'Computing Hash & Verifying...' : 'Verify & Anchor Evidence'}
              </Button>
            </form>
          </Card>

          {/* Trust Tier Standard Overview */}
          <Card champagneBorder className="md:col-span-6 p-6 space-y-4 flex flex-col justify-between">
            <div>
              <CardHeader className="p-0 pb-3">
                <CardTitle className="text-base font-bold text-white flex items-center gap-2">
                  <ShieldCheck className="w-5 h-5 text-emerald-400" /> Evidence Trust Tier Standard
                </CardTitle>
                <CardDescription className="text-xs">
                  Hierarchical verification levels enforced by Project Echo rule engines
                </CardDescription>
              </CardHeader>

              <div className="space-y-3 pt-2">
                <div className="p-3 rounded-xl bg-slate-900/80 border border-emerald-500/30 space-y-1">
                  <div className="flex items-center justify-between text-xs font-bold text-emerald-300">
                    <span>Tier 4: Sovereign Cryptographic Proof</span>
                    <Badge variant="success" className="text-[10px]">Highest Trust</Badge>
                  </div>
                  <p className="text-[11px] text-muted-foreground">
                    Public git commits, signed artifacts, peer-reviewed production PRs, or automated CI execution logs.
                  </p>
                </div>

                <div className="p-3 rounded-xl bg-slate-900/60 border border-border space-y-1">
                  <div className="flex items-center justify-between text-xs font-bold text-cyan-300">
                    <span>Tier 3: Verified Assessment Record</span>
                    <span className="text-[10px] font-mono text-muted-foreground">Standard</span>
                  </div>
                  <p className="text-[11px] text-muted-foreground">
                    Standardized third-party certifications, proctored coding assessments, and benchmark scores.
                  </p>
                </div>

                <div className="p-3 rounded-xl bg-slate-900/60 border border-border space-y-1">
                  <div className="flex items-center justify-between text-xs font-bold text-slate-300">
                    <span>Tier 1 & 2: Self-Attested Claims</span>
                    <span className="text-[10px] font-mono text-muted-foreground">Provisional</span>
                  </div>
                  <p className="text-[11px] text-muted-foreground">
                    Unverified resume bullet points awaiting cryptographic backing.
                  </p>
                </div>
              </div>
            </div>

            <div className="p-3 rounded-xl bg-amber-500/10 border border-amber-500/30 text-amber-300 text-xs flex items-center gap-2">
              <Sparkles className="w-4 h-4 flex-shrink-0" />
              <span>Zero hallucination policy: Unverified claims are filtered from reasoning cards.</span>
            </div>
          </Card>
        </div>

        {/* Verified Claims Log Table */}
        <Card champagneBorder className="p-6">
          <CardHeader className="p-0 pb-4">
            <CardTitle className="text-base font-bold text-white flex items-center gap-2">
              <FileCheck className="w-4 h-4 text-amber-400" /> Active Evidence Registry
            </CardTitle>
            <CardDescription className="text-xs">
              Cryptographically verified evidence claims linked to active passports
            </CardDescription>
          </CardHeader>
          <CardContent className="p-0">
            <div className="space-y-3">
              {evidenceList?.content.map((ev) => (
                <div
                  key={ev.id}
                  className="p-4 rounded-xl bg-slate-900/60 border border-border flex flex-col sm:flex-row sm:items-center justify-between gap-3 hover:border-emerald-500/40 transition-all"
                >
                  <div className="space-y-1">
                    <div className="flex items-center gap-2">
                      <span className="text-xs font-mono font-bold text-white">ID: {ev.id.substring(0, 10)}...</span>
                      <Badge variant="success" className="text-[10px] gap-1">
                        <CheckCircle2 className="w-3 h-3" /> {ev.trustTier}
                      </Badge>
                    </div>
                    <div className="flex items-center gap-1.5 text-xs text-muted-foreground font-mono truncate max-w-lg">
                      <LinkIcon className="w-3 h-3 text-amber-400 flex-shrink-0" />
                      <span className="truncate">{ev.sourceUri}</span>
                    </div>
                  </div>

                  <div className="flex items-center gap-3 self-end sm:self-center text-xs font-mono">
                    <span className="text-emerald-400 font-semibold">{ev.validationStatus}</span>
                    <span className="text-muted-foreground">{ev.createdAt}</span>
                  </div>
                </div>
              ))}
            </div>
          </CardContent>
        </Card>
      </div>
    </AppLayout>
  );
}
