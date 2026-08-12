'use client';

import { Loader2 } from 'lucide-react';

export default function LoadingFallback() {
  return (
    <div className="min-h-screen bg-background text-foreground bg-cyber-grid flex flex-col items-center justify-center p-6 space-y-4">
      <div className="relative flex items-center justify-center">
        <div className="w-16 h-16 rounded-full border-4 border-amber-500/10 border-t-amber-500 animate-spin" />
        <Loader2 className="w-6 h-6 text-amber-400 absolute animate-pulse" />
      </div>
      <div className="space-y-1 text-center">
        <span className="text-[10px] font-mono text-amber-300 uppercase tracking-widest font-bold">
          Synchronizing State
        </span>
        <p className="text-xs text-muted-foreground">Loading interactive 3D components...</p>
      </div>
    </div>
  );
}
