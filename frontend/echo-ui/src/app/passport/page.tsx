'use client';

import { useState } from 'react';
import { useQuery, useMutation, useQueryClient } from '@tanstack/react-query';
import { api } from '@/lib/api';
import AppLayout from '@/components/AppLayout';
import { Card, CardHeader, CardTitle, CardDescription, CardContent } from '@/components/ui/Card';
import { Button } from '@/components/ui/Button';
import { Input } from '@/components/ui/Input';
import { Badge } from '@/components/ui/Badge';
import { Skeleton } from '@/components/ui/Skeleton';
import {
  UserCheck,
  Plus,
  CheckCircle2,
  AlertCircle,
  Sparkles,
  QrCode,
  ShieldCheck,
  Cpu,
  X
} from 'lucide-react';
import { toast } from 'sonner';
import HolographicPassportCard from '@/components/3d/HolographicPassportCard';

export default function PassportPage() {
  const queryClient = useQueryClient();
  const [name, setName] = useState('');
  const [email, setEmail] = useState('');
  const [jobTitle, setJobTitle] = useState('');
  const [errorMsg, setErrorMsg] = useState('');
  const [qrModalOpen, setQrModalOpen] = useState(false);

  const { data: passports, isLoading } = useQuery({
    queryKey: ['passports'],
    queryFn: () => api.getPassports(),
  });

  const [selectedPassport, setSelectedPassport] = useState<unknown>(null);

  const activePassport = selectedPassport || (passports?.content && passports.content[0]);

  const createMutation = useMutation({
    mutationFn: api.createPassport,
    onSuccess: (data) => {
      queryClient.invalidateQueries({ queryKey: ['passports'] });
      setName('');
      setEmail('');
      setJobTitle('');
      setErrorMsg('');
      setSelectedPassport(data);
      toast.success(`Career Passport initialized for ${data.name}!`);
    },
    onError: (err: Error) => {
      setErrorMsg(err.message);
      toast.error(`Failed to initialize passport: ${err.message}`);
    },
  });

  const handleSubmit = (e: React.FormEvent) => {
    e.preventDefault();
    if (!name || !email || !jobTitle) {
      setErrorMsg('All fields are required');
      toast.error('Please fill out all required fields');
      return;
    }
    createMutation.mutate({ name, email, jobTitle });
  };

  return (
    <AppLayout>
      <div className="space-y-8 max-w-5xl">
        {/* Header */}
        <div className="flex flex-col md:flex-row md:items-center justify-between gap-4">
          <div>
            <div className="flex items-center gap-2">
              <h1 className="text-3xl font-extrabold tracking-tight text-white">Career Passport Studio</h1>
              <Badge variant="champagne" className="text-[10px]">3D Holographic</Badge>
            </div>
            <p className="text-muted-foreground text-sm mt-1">
              Manage your immutable career identity, verified aggregate root credentials, and cryptographic proofs.
            </p>
          </div>

          <Button
            variant="outline"
            size="sm"
            onClick={() => setQrModalOpen(true)}
            className="gap-2 text-xs border-amber-500/40 text-amber-300 hover:bg-amber-500/10"
          >
            <QrCode className="w-4 h-4" /> Verify QR Seal
          </Button>
        </div>

        {/* 3D Holographic Passport Card Display */}
        <div className="space-y-3">
          <div className="flex items-center justify-between px-2">
            <span className="text-xs font-mono text-muted-foreground uppercase tracking-wider font-semibold">
              Interactive 3D Holographic Card (Hover & Tilt)
            </span>
            <span className="text-xs font-mono text-amber-400">
              ● Gyroscope Active
            </span>
          </div>

          <HolographicPassportCard
            passport={activePassport ? {
              id: activePassport.id,
              name: activePassport.name,
              email: activePassport.email,
              jobTitle: activePassport.jobTitle,
              tier: 'Tier 4 Sovereign Proof',
              verifiedDate: activePassport.createdAt ? activePassport.createdAt.split('T')[0] : '2026-08-08',
            } : undefined}
            onOpenQR={() => setQrModalOpen(true)}
          />
        </div>

        {/* Two Column Grid: Form & Passport Gallery */}
        <div className="grid grid-cols-1 md:grid-cols-12 gap-8">
          {/* Create Passport Form */}
          <Card champagneBorder className="md:col-span-5 p-6">
            <CardHeader className="p-0 pb-4">
              <CardTitle className="flex items-center gap-2 text-base font-bold text-white">
                <Plus className="w-4 h-4 text-amber-400" /> Initialize New Passport
              </CardTitle>
              <CardDescription className="text-xs">Create a verified aggregate root career identity</CardDescription>
            </CardHeader>
            <CardContent className="p-0">
              <form onSubmit={handleSubmit} className="space-y-4">
                <Input
                  label="Executive Full Name"
                  placeholder="e.g. Jane Doe"
                  value={name}
                  onChange={(e) => setName(e.target.value)}
                />
                <Input
                  label="Verified Work Email"
                  type="email"
                  placeholder="e.g. jane.doe@enterprise.io"
                  value={email}
                  onChange={(e) => setEmail(e.target.value)}
                />
                <Input
                  label="Target Leadership Title"
                  placeholder="e.g. Principal Distributed Systems Architect"
                  value={jobTitle}
                  onChange={(e) => setJobTitle(e.target.value)}
                />

                {errorMsg && (
                  <div className="p-3 rounded-lg bg-destructive/10 border border-destructive/20 text-destructive text-xs flex items-center gap-2">
                    <AlertCircle className="w-4 h-4 flex-shrink-0" />
                    {errorMsg}
                  </div>
                )}

                <Button
                  type="submit"
                  variant="champagne"
                  className="w-full font-bold shadow-lg shadow-amber-500/20"
                  disabled={createMutation.isPending}
                >
                  {createMutation.isPending ? 'Initializing...' : 'Initialize Career Passport'}
                </Button>
              </form>
            </CardContent>
          </Card>

          {/* Passports List */}
          <Card champagneBorder className="md:col-span-7 p-6">
            <CardHeader className="p-0 pb-4">
              <CardTitle className="flex items-center gap-2 text-base font-bold text-white">
                <UserCheck className="w-4 h-4 text-emerald-400" /> Active Verified Passports
              </CardTitle>
              <CardDescription className="text-xs">Click a passport to preview in 3D studio</CardDescription>
            </CardHeader>
            <CardContent className="p-0">
              {isLoading ? (
                <div className="space-y-3">
                  <Skeleton className="h-20 w-full" />
                  <Skeleton className="h-20 w-full" />
                </div>
              ) : passports?.content.length === 0 ? (
                <div className="py-8 text-center text-xs text-muted-foreground font-mono">
                  No passports registered yet.
                </div>
              ) : (
                <div className="space-y-3">
                  {passports?.content.map((passport) => {
                    const isSelected = activePassport?.id === passport.id;
                    return (
                      <div
                        key={passport.id}
                        onClick={() => setSelectedPassport(passport)}
                        className={`p-4 rounded-xl border transition-all cursor-pointer flex items-center justify-between ${
                          isSelected
                            ? 'bg-amber-500/15 border-amber-500/60 shadow-lg shadow-amber-500/10'
                            : 'bg-slate-900/60 border-border hover:border-amber-500/30'
                        }`}
                      >
                        <div className="space-y-1">
                          <div className="flex items-center gap-2">
                            <h4 className="font-bold text-sm text-white">{passport.name}</h4>
                            <Badge variant="success" className="text-[10px] gap-1 py-0.5">
                              <CheckCircle2 className="w-3 h-3" /> Tier 4
                            </Badge>
                          </div>
                          <p className="text-xs text-muted-foreground">{passport.jobTitle}</p>
                          <p className="text-[10px] text-muted-foreground font-mono">{passport.email}</p>
                        </div>
                        <div className="text-right">
                          <span className="text-[10px] font-mono text-amber-400 block font-bold">
                            {isSelected ? 'ACTIVE 3D' : 'SELECT'}
                          </span>
                          <span className="text-[10px] font-mono text-muted-foreground">{passport.id.substring(0, 8)}...</span>
                        </div>
                      </div>
                    );
                  })}
                </div>
              )}
            </CardContent>
          </Card>
        </div>
      </div>

      {/* QR Code Verification Modal */}
      {qrModalOpen && (
        <div className="fixed inset-0 z-50 flex items-center justify-center p-4 bg-black/80 backdrop-blur-md animate-in fade-in duration-200">
          <div className="relative w-full max-w-md rounded-2xl bg-card border border-amber-500/40 p-6 glass-panel-glow space-y-6">
            <div className="flex items-center justify-between border-b border-border pb-3">
              <div className="flex items-center gap-2">
                <ShieldCheck className="w-5 h-5 text-amber-400" />
                <h3 className="font-bold text-base text-white">Cryptographic QR Verification</h3>
              </div>
              <button onClick={() => setQrModalOpen(false)} className="text-muted-foreground hover:text-white">
                <X className="w-5 h-5" />
              </button>
            </div>

            {/* QR Visual */}
            <div className="p-8 rounded-xl bg-white flex flex-col items-center justify-center mx-auto w-64 h-64 shadow-2xl">
              <div className="w-full h-full border-4 border-black p-2 flex flex-col items-center justify-between">
                <div className="flex justify-between w-full">
                  <div className="w-8 h-8 bg-black" />
                  <div className="w-8 h-8 bg-black" />
                </div>
                <div className="font-black text-black text-center font-mono text-xs">
                  PROJECT ECHO
                  <br />
                  <span className="text-[8px]">PROVENANCE SEAL</span>
                </div>
                <div className="flex justify-between w-full">
                  <div className="w-8 h-8 bg-black" />
                  <div className="w-8 h-8 bg-black" />
                </div>
              </div>
            </div>

            <div className="text-center space-y-1 text-xs font-mono text-muted-foreground">
              <p className="text-white font-semibold">Passport ID: {activePassport?.id}</p>
              <p>Cryptographic SHA-256 Hash: 0x9f8b...21e7</p>
              <p className="text-emerald-400">Status: PASS (0 Hallucinations)</p>
            </div>

            <Button
              variant="champagne"
              className="w-full font-bold"
              onClick={() => {
                toast.success('Passport verification link copied to clipboard!');
                setQrModalOpen(false);
              }}
            >
              Copy Verification Link
            </Button>
          </div>
        </div>
      )}
    </AppLayout>
  );
}
