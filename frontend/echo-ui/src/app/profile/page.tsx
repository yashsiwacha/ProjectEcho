'use client';

import { useQuery } from '@tanstack/react-query';
import { api } from '@/lib/api';
import AppLayout from '@/components/AppLayout';
import { Card, CardHeader, CardTitle, CardDescription, CardContent } from '@/components/ui/Card';
import { Badge } from '@/components/ui/Badge';
import { Button } from '@/components/ui/Button';
import {
  Share2,
  CheckCircle2,
  ShieldCheck,
  Award,
  Link as LinkIcon,
  Sparkles,
  Zap,
  Layers,
  Copy
} from 'lucide-react';
import { toast } from 'sonner';
import HolographicPassportCard from '@/components/3d/HolographicPassportCard';

export default function ProfilePage() {
  const { data: passports } = useQuery({ queryKey: ['passports'], queryFn: () => api.getPassports() });
  const { data: skills } = useQuery({ queryKey: ['skills'], queryFn: () => api.getSkills() });

  const activePassport = passports?.content[0] || {
    id: 'f2706538-f134-4c58-8825-4ee944a10052',
    name: 'Jane Doe',
    email: 'jane.doe@enterprise.io',
    jobTitle: 'Principal Distributed Systems Architect',
    createdAt: '2026-08-08',
  };

  const handleCopyShareLink = () => {
    navigator.clipboard?.writeText(window.location.href);
    toast.success('Public executive profile link copied to clipboard!');
  };

  return (
    <AppLayout>
      <div className="space-y-8 max-w-5xl">
        {/* Header */}
        <div className="flex flex-col md:flex-row md:items-center justify-between gap-4">
          <div>
            <div className="flex items-center gap-2">
              <h1 className="text-3xl font-extrabold tracking-tight text-white">Executive Profile Showcase</h1>
              <Badge variant="champagne" className="text-[10px]">Public Sovereign Record</Badge>
            </div>
            <p className="text-muted-foreground text-sm mt-1">
              Publicly shareable verified career profile backed by cryptographic aggregate roots.
            </p>
          </div>

          <Button
            variant="champagne"
            size="sm"
            onClick={handleCopyShareLink}
            className="gap-2 font-bold shadow-lg shadow-amber-500/20 text-xs"
          >
            <Share2 className="w-4 h-4" /> Share Verified Profile
          </Button>
        </div>

        {/* 3D Holographic Passport Card Showcase */}
        <div className="space-y-3">
          <div className="flex items-center justify-between px-2">
            <span className="text-xs font-mono text-muted-foreground uppercase tracking-wider font-semibold">
              Verified Sovereign Credential
            </span>
            <span className="text-xs font-mono text-emerald-400">
              ● 100% Cryptographic Backing
            </span>
          </div>

          <HolographicPassportCard
            passport={{
              id: activePassport.id,
              name: activePassport.name,
              email: activePassport.email,
              jobTitle: activePassport.jobTitle,
              tier: 'Tier 4 Sovereign Proof',
              verifiedDate: activePassport.createdAt ? activePassport.createdAt.split('T')[0] : '2026-08-08',
            }}
          />
        </div>

        {/* Two Column Grid: Verified Skills & Public Provenance */}
        <div className="grid grid-cols-1 md:grid-cols-12 gap-8">
          {/* Verified Skills */}
          <Card champagneBorder className="md:col-span-7 p-6 space-y-4">
            <CardHeader className="p-0 pb-2">
              <CardTitle className="text-base font-bold text-white flex items-center gap-2">
                <Zap className="w-4 h-4 text-cyan-400" /> Cryptographically Verified Skills
              </CardTitle>
              <CardDescription className="text-xs">
                Capabilities evaluated and verified with Tier 4 proof
              </CardDescription>
            </CardHeader>
            <CardContent className="p-0">
              <div className="space-y-3">
                {skills?.content.map((s) => (
                  <div
                    key={s.id}
                    className="p-3.5 rounded-xl bg-slate-900/60 border border-border flex items-center justify-between hover:border-amber-500/40 transition-all"
                  >
                    <div>
                      <h4 className="font-semibold text-sm text-white">{s.name}</h4>
                      <span className="text-[10px] text-muted-foreground font-mono">{s.category}</span>
                    </div>
                    <Badge variant="success" className="text-[10px] gap-1">
                      <CheckCircle2 className="w-3 h-3" /> Tier 4 Verified
                    </Badge>
                  </div>
                ))}
              </div>
            </CardContent>
          </Card>

          {/* Public Verification Link */}
          <Card champagneBorder className="md:col-span-5 p-6 space-y-4 flex flex-col justify-between">
            <div className="space-y-3">
              <CardHeader className="p-0 pb-2">
                <CardTitle className="text-base font-bold text-white flex items-center gap-2">
                  <ShieldCheck className="w-4 h-4 text-emerald-400" /> Shareable Provenance Link
                </CardTitle>
                <CardDescription className="text-xs">
                  For recruiters, boards, and strategic partners
                </CardDescription>
              </CardHeader>

              <div className="p-3 rounded-xl bg-slate-950 border border-slate-800 text-xs font-mono text-slate-300 break-all">
                https://project-echo.io/p/{activePassport.id}
              </div>

              <p className="text-xs text-muted-foreground leading-relaxed">
                Recruiters and hiring boards can directly inspect the underlying 3D Decision DAG and verify git commit proofs in real time.
              </p>
            </div>

            <Button
              variant="outline"
              size="sm"
              onClick={handleCopyShareLink}
              className="w-full gap-2 text-xs border-amber-500/40 text-amber-300 hover:bg-amber-500/10"
            >
              <Copy className="w-4 h-4" /> Copy Direct URL
            </Button>
          </Card>
        </div>
      </div>
    </AppLayout>
  );
}
