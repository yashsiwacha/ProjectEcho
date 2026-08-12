'use client';

import React, { useState } from 'react';
import Link from 'next/link';
import { useRouter } from 'next/navigation';
import { Button } from '@/components/ui/Button';
import { Input } from '@/components/ui/Input';
import { Card, CardHeader, CardTitle, CardDescription, CardContent } from '@/components/ui/Card';
import { ShieldCheck, Mail, Lock, Sparkles, UserCheck, KeyRound } from 'lucide-react';
import { api } from '@/lib/api';
import { toast } from 'sonner';

export default function RegisterPage() {
  const router = useRouter();
  const [email, setEmail] = useState('');
  const [password, setPassword] = useState('');
  const [role, setRole] = useState('ROLE_CANDIDATE');
  const [loading, setLoading] = useState(false);
  
  // MFA setup flow
  const [mfaSecret, setMfaSecret] = useState('');
  const [mfaConfigured, setMfaConfigured] = useState(false);

  const handleRegisterSubmit = async (e: React.FormEvent) => {
    e.preventDefault();
    if (!email || !password) {
      toast.error('Please enter email and password');
      return;
    }

    setLoading(true);
    try {
      // 1. Register User
      await api.register({ email, password, role });
      toast.success('Secure credentials registered successfully!');

      // 2. Set up MFA right away to secure the profile
      const setupResp = await api.setupMfa({ email });
      if (setupResp && setupResp.secret) {
        setMfaSecret(setupResp.secret);
        setMfaConfigured(true);
        toast.success('MFA secret key generated!');
      } else {
        router.push('/login');
      }
    } catch (err: any) {
      toast.error(err.message || 'Registration failed. Email may already be registered.');
    } finally {
      setLoading(false);
    }
  };

  return (
    <div className="min-h-screen bg-background bg-cyber-grid flex items-center justify-center p-6 relative overflow-hidden">
      {/* Dynamic Background Glows */}
      <div className="absolute top-1/4 left-1/4 w-96 h-96 bg-amber-500/10 rounded-full blur-3xl pointer-events-none" />
      <div className="absolute bottom-1/4 right-1/4 w-96 h-96 bg-cyan-500/10 rounded-full blur-3xl pointer-events-none" />

      <Card className="w-full max-w-md border border-border bg-card/80 backdrop-blur-xl relative z-10 p-8 rounded-3xl shadow-2xl glass-panel-glow">
        <CardHeader className="text-center space-y-3">
          <div className="mx-auto w-12 h-12 rounded-2xl bg-gradient-to-br from-amber-400 to-amber-600 flex items-center justify-center text-black font-black text-2xl shadow-xl shadow-amber-500/20">
            E
          </div>
          <CardTitle className="text-2xl font-black tracking-tight text-white flex items-center justify-center gap-2">
            {!mfaConfigured ? 'Create Sovereign Profile' : 'Configure Authentication'}
            <Sparkles className="w-5 h-5 text-amber-400" />
          </CardTitle>
          <CardDescription className="text-muted-foreground text-sm">
            {!mfaConfigured
              ? 'Establish your verifiable identity inside Yash Engineering Systems'
              : 'Add security with standard TOTP Google/Microsoft Authenticator'}
          </CardDescription>
        </CardHeader>

        <CardContent className="mt-6">
          {!mfaConfigured ? (
            <form onSubmit={handleRegisterSubmit} className="space-y-4">
              <div className="relative">
                <Mail className="absolute left-4 top-3.5 h-4.5 w-4.5 text-muted-foreground z-10" />
                <Input
                  type="email"
                  label="Email Address"
                  placeholder="name@enterprise.io"
                  value={email}
                  onChange={(e) => setEmail(e.target.value)}
                  className="pl-12 bg-input/40"
                  required
                />
              </div>

              <div className="relative">
                <Lock className="absolute left-4 top-3.5 h-4.5 w-4.5 text-muted-foreground z-10" />
                <Input
                  type="password"
                  label="Secure Password"
                  placeholder="••••••••"
                  value={password}
                  onChange={(e) => setPassword(e.target.value)}
                  className="pl-12 bg-input/40"
                  required
                />
              </div>

              <div className="space-y-1.5 w-full">
                <label className="text-xs font-semibold uppercase tracking-wider text-muted-foreground">Select Profile Role</label>
                <div className="grid grid-cols-2 gap-3.5 pt-1">
                  <button
                    type="button"
                    onClick={() => setRole('ROLE_CANDIDATE')}
                    className={`flex items-center justify-center gap-2 py-3 px-4 rounded-xl border text-sm font-semibold transition-all ${
                      role === 'ROLE_CANDIDATE'
                        ? 'border-amber-500/60 bg-amber-500/10 text-white shadow-md'
                        : 'border-border bg-input/20 text-muted-foreground hover:bg-input/40'
                    }`}
                  >
                    Candidate
                  </button>
                  <button
                    type="button"
                    onClick={() => setRole('ROLE_RECRUITER')}
                    className={`flex items-center justify-center gap-2 py-3 px-4 rounded-xl border text-sm font-semibold transition-all ${
                      role === 'ROLE_RECRUITER'
                        ? 'border-cyan-500/60 bg-cyan-500/10 text-white shadow-md'
                        : 'border-border bg-input/20 text-muted-foreground hover:bg-input/40'
                    }`}
                  >
                    Recruiter
                  </button>
                </div>
              </div>

              <Button
                type="submit"
                variant="champagne"
                className="w-full font-bold shadow-lg shadow-amber-500/10 mt-6 h-12"
                disabled={loading}
              >
                {loading ? 'Registering...' : 'Register Identity'}
              </Button>
            </form>
          ) : (
            <div className="space-y-6">
              <div className="p-5 rounded-2xl bg-amber-500/5 border border-amber-500/20 text-center space-y-4">
                <div className="flex justify-center">
                  <KeyRound className="w-10 h-10 text-amber-400" />
                </div>
                <div className="space-y-1">
                  <div className="text-sm font-bold text-white">Sovereign Key Generated</div>
                  <div className="text-xs text-muted-foreground">Scan this key or enter it manually in your device:</div>
                </div>
                <div className="p-3 bg-card rounded-xl border border-border select-all select-text break-all font-mono text-sm text-amber-300 font-bold tracking-wider">
                  {mfaSecret}
                </div>
                <div className="text-[10px] text-muted-foreground">
                  Important: Save this key. It is required to log in to your dashboard.
                </div>
              </div>

              <Link href="/login" className="w-full">
                <Button variant="champagne" className="w-full font-bold h-12 shadow-lg">
                  Proceed to Sign In
                </Button>
              </Link>
            </div>
          )}

          {!mfaConfigured && (
            <div className="mt-8 pt-6 border-t border-border/80 text-center text-xs font-mono text-muted-foreground space-y-3">
              <p>
                Already registered your profile?{' '}
                <Link href="/login" className="text-amber-400 hover:underline">
                  Sign In
                </Link>
              </p>
              <div className="flex items-center justify-center gap-1.5 text-[10px] text-emerald-400">
                <ShieldCheck className="w-3.5 h-3.5" />
                <span>Zero-Trust Verification Enabled</span>
              </div>
            </div>
          )}
        </CardContent>
      </Card>
    </div>
  );
}
