'use client';

import React, { useState } from 'react';
import Link from 'next/link';
import { useRouter } from 'next/navigation';
import { Button } from '@/components/ui/Button';
import { Input } from '@/components/ui/Input';
import { Card, CardHeader, CardTitle, CardDescription, CardContent } from '@/components/ui/Card';
import { ShieldCheck, Lock, Mail, KeyRound, Sparkles } from 'lucide-react';
import { api } from '@/lib/api';
import { toast } from 'sonner';

export default function LoginPage() {
  const router = useRouter();
  const [email, setEmail] = useState('');
  const [password, setPassword] = useState('');
  const [mfaCode, setMfaCode] = useState('');
  const [mfaRequired, setMfaRequired] = useState(false);
  const [loading, setLoading] = useState(false);

  const handleLoginSubmit = async (e: React.FormEvent) => {
    e.preventDefault();
    if (!email || !password) {
      toast.error('Please enter email and password');
      return;
    }

    setLoading(true);
    try {
      const response = await api.login({ email, password });
      if (response.mfaRequired) {
        setMfaRequired(true);
        toast.info('Multi-Factor Authentication required. Please enter your TOTP code.');
      } else {
        if (response.accessToken) {
          localStorage.setItem('accessToken', response.accessToken);
          if (response.refreshToken) {
            localStorage.setItem('refreshToken', response.refreshToken);
          }
          localStorage.setItem('userEmail', response.email);
          toast.success('Successfully logged in!');
          router.push('/dashboard');
        } else {
          toast.error('Login failed: Token not received');
        }
      }
    } catch (err: any) {
      toast.error(err.message || 'Authentication failed. Please check credentials.');
    } finally {
      setLoading(false);
    }
  };

  const handleMfaSubmit = async (e: React.FormEvent) => {
    e.preventDefault();
    if (!mfaCode || mfaCode.length < 6) {
      toast.error('Please enter a valid 6-digit passcode');
      return;
    }

    setLoading(true);
    try {
      const response = await api.verifyMfa({ email, code: parseInt(mfaCode, 10) });
      if (response.accessToken) {
        localStorage.setItem('accessToken', response.accessToken);
        if (response.refreshToken) {
          localStorage.setItem('refreshToken', response.refreshToken);
        }
        localStorage.setItem('userEmail', response.email);
        toast.success('MFA verification successful!');
        router.push('/dashboard');
      } else {
        toast.error('Verification failed');
      }
    } catch (err: any) {
      toast.error(err.message || 'MFA validation failed. Check your token.');
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
            {!mfaRequired ? 'Authentication Gateway' : 'Sovereign Verification'}
            <Sparkles className="w-5 h-5 text-amber-400" />
          </CardTitle>
          <CardDescription className="text-muted-foreground text-sm">
            {!mfaRequired 
              ? 'Enter credentials to authorize access to your Career Passport' 
              : 'Enter the 6-digit TOTP code from your authenticator app'}
          </CardDescription>
        </CardHeader>

        <CardContent className="mt-6">
          {!mfaRequired ? (
            <form onSubmit={handleLoginSubmit} className="space-y-4">
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
                  label="Password"
                  placeholder="••••••••"
                  value={password}
                  onChange={(e) => setPassword(e.target.value)}
                  className="pl-12 bg-input/40"
                  required
                />
              </div>

              <Button
                type="submit"
                variant="champagne"
                className="w-full font-bold shadow-lg shadow-amber-500/10 mt-6 h-12"
                disabled={loading}
              >
                {loading ? 'Authorizing...' : 'Sign In'}
              </Button>
            </form>
          ) : (
            <form onSubmit={handleMfaSubmit} className="space-y-5">
              <div className="relative">
                <KeyRound className="absolute left-4 top-3.5 h-4.5 w-4.5 text-amber-400 z-10" />
                <Input
                  type="text"
                  maxLength={6}
                  label="6-Digit Verification Code"
                  placeholder="000 000"
                  value={mfaCode}
                  onChange={(e) => setMfaCode(e.target.value.replace(/\D/g, ''))}
                  className="pl-12 text-center text-lg font-mono tracking-widest bg-input/40"
                  required
                />
              </div>

              <Button
                type="submit"
                variant="champagne"
                className="w-full font-bold shadow-lg shadow-amber-500/10 h-12"
                disabled={loading}
              >
                {loading ? 'Verifying...' : 'Verify Code'}
              </Button>

              <button
                type="button"
                onClick={() => setMfaRequired(false)}
                className="w-full text-center text-xs font-mono text-muted-foreground hover:text-white transition-all underline"
              >
                Return to Login Credentials
              </button>
            </form>
          )}

          {!mfaRequired && (
            <div className="mt-8 pt-6 border-t border-border/80 text-center text-xs font-mono text-muted-foreground space-y-3">
              <p>
                Don't have a secure identity profile?{' '}
                <Link href="/register" className="text-amber-400 hover:underline">
                  Create Profile
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
