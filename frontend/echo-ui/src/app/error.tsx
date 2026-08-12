'use client';

import { useEffect } from 'react';
import { Button } from '@/components/ui/Button';
import { AlertTriangle, RefreshCcw } from 'lucide-react';

export default function ErrorBoundary({
  error,
  reset,
}: {
  error: Error & { digest?: string };
  reset: () => void;
}) {
  useEffect(() => {
    console.error('Unhandled app error:', error);
  }, [error]);

  return (
    <div className="min-h-screen bg-background text-foreground bg-cyber-grid flex flex-col items-center justify-center p-6 text-center space-y-6">
      <div className="p-4 rounded-full bg-destructive/10 text-destructive border border-destructive/20 animate-pulse">
        <AlertTriangle className="w-12 h-12" />
      </div>

      <div className="space-y-2 max-w-md">
        <h1 className="text-2xl font-extrabold tracking-tight text-white">System Error Encountered</h1>
        <p className="text-sm text-muted-foreground">
          An unexpected exception occurred in the client application scope. The execution context has been halted.
        </p>
        {error.digest && (
          <div className="text-[10px] font-mono bg-slate-950/80 px-3 py-1.5 rounded-lg border border-border mt-2 inline-block text-cyan-400">
            DIGEST: {error.digest}
          </div>
        )}
      </div>

      <Button
        variant="champagne"
        size="sm"
        onClick={() => reset()}
        className="gap-2 font-bold shadow-lg shadow-amber-500/20"
      >
        <RefreshCcw className="w-4 h-4" /> Reset Execution Context
      </Button>
    </div>
  );
}
