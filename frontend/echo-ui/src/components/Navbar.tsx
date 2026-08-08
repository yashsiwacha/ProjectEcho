'use client';

import Link from 'next/link';
import { ShieldCheck, Search, Activity, Cpu, Sparkles, UserCheck } from 'lucide-react';
import { Button } from '@/components/ui/Button';

export default function Navbar() {
  const triggerCommandPalette = () => {
    window.dispatchEvent(new KeyboardEvent('keydown', { key: 'k', metaKey: true }));
  };

  return (
    <header className="sticky top-0 z-40 w-full border-b border-border/80 bg-background/85 backdrop-blur-xl transition-all">
      <div className="flex h-16 items-center justify-between px-6 max-w-7xl mx-auto">
        {/* Brand */}
        <div className="flex items-center gap-4">
          <Link href="/" className="flex items-center gap-3 group">
            <div className="w-10 h-10 rounded-xl bg-gradient-to-br from-amber-400 to-amber-600 flex items-center justify-center text-black font-black text-xl shadow-lg shadow-amber-500/20 group-hover:scale-105 transition-transform">
              E
            </div>
            <div>
              <div className="flex items-center gap-1.5">
                <span className="font-extrabold text-lg tracking-tight text-white group-hover:text-amber-300 transition-colors">
                  ProjectEcho
                </span>
                <span className="text-[10px] font-mono font-bold px-1.5 py-0.5 rounded bg-amber-500/20 text-amber-300 border border-amber-500/30">
                  RC1
                </span>
              </div>
              <p className="text-[10px] font-mono text-muted-foreground">Career Intelligence OS</p>
            </div>
          </Link>
        </div>

        {/* Search / Command Palette Bar */}
        <div className="hidden md:flex items-center flex-1 max-w-md mx-8">
          <button
            onClick={triggerCommandPalette}
            className="w-full flex items-center justify-between px-3.5 py-2 rounded-xl bg-muted/50 border border-border hover:border-amber-500/40 text-xs text-muted-foreground transition-all group"
          >
            <div className="flex items-center gap-2">
              <Search className="w-3.5 h-3.5 text-muted-foreground group-hover:text-amber-400" />
              <span>Search modules, skills, missions, DAG trace...</span>
            </div>
            <kbd className="font-mono text-[10px] px-1.5 py-0.5 rounded bg-background border border-border text-muted-foreground">
              ⌘K
            </kbd>
          </button>
        </div>

        {/* Live System Status & Action Buttons */}
        <div className="flex items-center gap-3">
          <div className="hidden lg:flex items-center gap-2 px-3 py-1.5 rounded-full bg-emerald-500/10 border border-emerald-500/30 text-emerald-400 text-xs font-mono">
            <span className="w-2 h-2 rounded-full bg-emerald-400 animate-pulse" />
            <span>Backend 8080 UP</span>
          </div>

          <Link href="/passport">
            <Button variant="outline" size="sm" className="gap-1.5 text-xs font-medium border-border hover:border-amber-500/40">
              <UserCheck className="w-3.5 h-3.5 text-amber-400" /> Passport
            </Button>
          </Link>

          <Link href="/dashboard">
            <Button variant="champagne" size="sm" className="gap-1.5 text-xs font-semibold">
              <Activity className="w-3.5 h-3.5" /> Dashboard
            </Button>
          </Link>
        </div>
      </div>
    </header>
  );
}
