'use client';

import { useEffect, useState, Suspense } from 'react';
import { Button } from '@/components/ui/Button';
import { MailCheck, XCircle, Loader2 } from 'lucide-react';
import Link from 'next/link';
import { useSearchParams } from 'next/navigation';

function VerifyEmailContent() {
  const searchParams = useSearchParams();
  const token = searchParams.get('token');
  
  const [status, setStatus] = useState<'loading' | 'success' | 'error'>('loading');
  const [errorMessage, setErrorMessage] = useState('');

  useEffect(() => {
    if (!token) {
      setStatus('error');
      setErrorMessage('Invalid verification link.');
      return;
    }

    const verifyToken = async () => {
      try {
        const response = await fetch('http://localhost:8080/api/v1/auth/email/verify', {
          method: 'POST',
          headers: { 'Content-Type': 'application/json' },
          body: JSON.stringify({ token }),
        });
        
        if (!response.ok) {
          throw new Error('Failed to verify email. The token may be expired or invalid.');
        }
        
        setStatus('success');
      } catch (err: any) {
        setStatus('error');
        setErrorMessage(err.message || 'Verification failed');
      }
    };

    verifyToken();
  }, [token]);

  return (
    <div className="text-center">
      {status === 'loading' && (
        <div className="py-8">
          <Loader2 className="w-12 h-12 text-amber-500 animate-spin mx-auto mb-6" />
          <h2 className="text-xl font-semibold text-white mb-2">Verifying Email...</h2>
          <p className="text-zinc-400">Please wait while we verify your email address.</p>
        </div>
      )}

      {status === 'success' && (
        <div className="py-4">
          <div className="w-16 h-16 bg-emerald-500/10 text-emerald-500 rounded-full flex items-center justify-center mx-auto mb-6 border border-emerald-500/20">
            <MailCheck className="w-8 h-8" />
          </div>
          <h2 className="text-2xl font-bold text-white mb-3">Email Verified!</h2>
          <p className="text-zinc-400 mb-8">
            Thank you for verifying your email address. Your account is now fully active.
          </p>
          <Link href="/dashboard" className="block">
            <Button className="w-full" variant="champagne">Continue to Dashboard</Button>
          </Link>
        </div>
      )}

      {status === 'error' && (
        <div className="py-4">
          <div className="w-16 h-16 bg-red-500/10 text-red-500 rounded-full flex items-center justify-center mx-auto mb-6 border border-red-500/20">
            <XCircle className="w-8 h-8" />
          </div>
          <h2 className="text-2xl font-bold text-white mb-3">Verification Failed</h2>
          <p className="text-red-200 mb-8 px-4">
            {errorMessage}
          </p>
          <Link href="/login" className="block">
            <Button className="w-full" variant="outline">Return to Login</Button>
          </Link>
        </div>
      )}
    </div>
  );
}

export default function VerifyEmailPage() {
  return (
    <div className="min-h-screen flex items-center justify-center bg-black p-4 relative overflow-hidden">
      {/* Background glow */}
      <div className="absolute top-1/2 left-1/2 -translate-x-1/2 -translate-y-1/2 w-[600px] h-[600px] bg-emerald-500/10 rounded-full blur-[100px] opacity-50" />
      
      <div className="w-full max-w-md bg-zinc-950/50 border border-white/10 rounded-2xl p-8 backdrop-blur-xl relative z-10 shadow-2xl">
        <Suspense fallback={<div className="text-center text-zinc-500 py-8"><Loader2 className="w-8 h-8 animate-spin mx-auto mb-4"/>Verifying...</div>}>
          <VerifyEmailContent />
        </Suspense>
      </div>
    </div>
  );
}
