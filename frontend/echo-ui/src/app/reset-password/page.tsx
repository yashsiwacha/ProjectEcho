'use client';

import { useState, Suspense } from 'react';
import { Button } from '@/components/ui/Button';
import { Lock, ArrowRight, AlertCircle, CheckCircle2 } from 'lucide-react';
import Link from 'next/link';
import { useSearchParams, useRouter } from 'next/navigation';

function ResetPasswordForm() {
  const searchParams = useSearchParams();
  const router = useRouter();
  const token = searchParams.get('token');
  
  const [password, setPassword] = useState('');
  const [confirmPassword, setConfirmPassword] = useState('');
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState('');
  const [success, setSuccess] = useState(false);

  if (!token) {
    return (
      <div className="text-center">
        <AlertCircle className="w-12 h-12 text-red-500 mx-auto mb-4" />
        <h2 className="text-xl font-semibold text-white mb-2">Invalid Link</h2>
        <p className="text-zinc-400 mb-6">This password reset link is invalid or missing the token.</p>
        <Link href="/forgot-password">
          <Button variant="outline">Request New Link</Button>
        </Link>
      </div>
    );
  }

  const handleSubmit = async (e: React.FormEvent) => {
    e.preventDefault();
    if (password !== confirmPassword) {
      setError('Passwords do not match');
      return;
    }
    setLoading(true);
    setError('');
    try {
      const response = await fetch('http://localhost:8080/api/v1/auth/password/reset', {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify({ token, newPassword: password }),
      });
      if (!response.ok) throw new Error('Failed to reset password. The token may be expired.');
      setSuccess(true);
    } catch (err: any) {
      setError(err.message || 'An error occurred');
    } finally {
      setLoading(false);
    }
  };

  if (success) {
    return (
      <div className="text-center space-y-6">
        <div className="w-12 h-12 bg-emerald-500/10 text-emerald-500 rounded-xl flex items-center justify-center mx-auto mb-4 border border-emerald-500/20">
          <CheckCircle2 className="w-6 h-6" />
        </div>
        <h2 className="text-2xl font-bold text-white mb-2">Password Reset Successful</h2>
        <p className="text-zinc-400 text-sm mb-6">Your password has been securely updated.</p>
        <Link href="/login" className="block">
          <Button className="w-full" variant="champagne">Go to Login</Button>
        </Link>
      </div>
    );
  }

  return (
    <>
      <div className="text-center mb-8">
        <div className="w-12 h-12 bg-amber-500/10 text-amber-500 rounded-xl flex items-center justify-center mx-auto mb-4 border border-amber-500/20">
          <Lock className="w-6 h-6" />
        </div>
        <h1 className="text-2xl font-bold text-white mb-2">Create New Password</h1>
        <p className="text-zinc-400 text-sm">
          Please enter your new secure password below.
        </p>
      </div>

      {error && (
        <div className="mb-6 p-4 bg-red-500/10 border border-red-500/20 rounded-xl flex items-start gap-3">
          <AlertCircle className="w-5 h-5 text-red-400 shrink-0 mt-0.5" />
          <p className="text-sm text-red-200">{error}</p>
        </div>
      )}

      <form onSubmit={handleSubmit} className="space-y-6">
        <div className="space-y-4">
          <div className="space-y-2">
            <label className="text-sm font-medium text-zinc-300">New Password</label>
            <div className="relative">
              <Lock className="absolute left-3 top-1/2 -translate-y-1/2 w-5 h-5 text-zinc-500" />
              <input
                type="password"
                value={password}
                onChange={(e) => setPassword(e.target.value)}
                className="w-full bg-zinc-900/50 border border-zinc-800 rounded-xl py-2.5 pl-10 pr-4 text-white focus:outline-none focus:ring-2 focus:ring-amber-500/50 focus:border-amber-500 transition-all"
                placeholder="••••••••"
                required
                minLength={8}
              />
            </div>
          </div>
          
          <div className="space-y-2">
            <label className="text-sm font-medium text-zinc-300">Confirm Password</label>
            <div className="relative">
              <Lock className="absolute left-3 top-1/2 -translate-y-1/2 w-5 h-5 text-zinc-500" />
              <input
                type="password"
                value={confirmPassword}
                onChange={(e) => setConfirmPassword(e.target.value)}
                className="w-full bg-zinc-900/50 border border-zinc-800 rounded-xl py-2.5 pl-10 pr-4 text-white focus:outline-none focus:ring-2 focus:ring-amber-500/50 focus:border-amber-500 transition-all"
                placeholder="••••••••"
                required
                minLength={8}
              />
            </div>
          </div>
        </div>

        <Button
          type="submit"
          className="w-full"
          variant="champagne"
          disabled={loading || !password || !confirmPassword}
        >
          {loading ? 'Updating...' : 'Update Password'}
          {!loading && <ArrowRight className="w-4 h-4 ml-2" />}
        </Button>
      </form>
    </>
  );
}

export default function ResetPasswordPage() {
  return (
    <div className="min-h-screen flex items-center justify-center bg-black p-4 relative overflow-hidden">
      {/* Background glow */}
      <div className="absolute top-1/2 left-1/2 -translate-x-1/2 -translate-y-1/2 w-[600px] h-[600px] bg-amber-500/10 rounded-full blur-[100px] opacity-50" />
      
      <div className="w-full max-w-md bg-zinc-950/50 border border-white/10 rounded-2xl p-8 backdrop-blur-xl relative z-10 shadow-2xl">
        <Suspense fallback={<div className="text-center text-zinc-500">Loading form...</div>}>
          <ResetPasswordForm />
        </Suspense>
      </div>
    </div>
  );
}
