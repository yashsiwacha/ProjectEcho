'use client';

import Link from 'next/link';
import { Search, Activity, UserCheck } from 'lucide-react';
import { Button } from '@/components/ui/Button';
import { motion } from 'framer-motion';
import { ThemeToggle } from './ThemeToggle';

export default function Navbar() {
  const triggerCommandPalette = () => {
    window.dispatchEvent(new KeyboardEvent('keydown', { key: 'k', metaKey: true }));
  };

  return (
    <motion.header 
      initial={{ y: -20, opacity: 0 }}
      animate={{ y: 0, opacity: 1 }}
      transition={{ duration: 0.5, ease: [0.16, 1, 0.3, 1] }}
      className="sticky top-0 z-40 w-full border-b border-border/50 bg-background/60 backdrop-blur-xl transition-all"
    >
      <div className="flex h-16 items-center justify-between px-6 max-w-7xl mx-auto">
        {/* Brand */}
        <div className="flex items-center gap-4">
          <Link href="/" className="flex items-center gap-3 group">
            <motion.div 
              whileHover={{ scale: 1.05, rotate: -2 }}
              whileTap={{ scale: 0.95 }}
              className="w-10 h-10 rounded-xl bg-gradient-to-br from-amber-400 to-amber-600 flex items-center justify-center text-amber-950 font-black text-xl shadow-lg group-hover:shadow-amber-500/20"
            >
              E
            </motion.div>
            <div>
              <div className="flex items-center gap-1.5">
                <span className="font-extrabold text-lg tracking-tight text-foreground transition-colors">
                  ProjectEcho
                </span>
                <span className="text-[10px] font-mono font-bold px-1.5 py-0.5 rounded bg-muted text-foreground border border-border">
                  RC1
                </span>
              </div>
              <p className="text-[10px] font-mono text-muted-foreground">Career Intelligence OS</p>
            </div>
          </Link>
        </div>

        {/* Search / Command Palette Bar */}
        <div className="hidden md:flex items-center flex-1 max-w-md mx-8">
          <motion.button
            whileHover={{ scale: 1.01 }}
            whileTap={{ scale: 0.99 }}
            onClick={triggerCommandPalette}
            className="w-full flex items-center justify-between px-3.5 py-2 rounded-xl bg-muted/30 border border-border hover:bg-muted/50 text-xs text-muted-foreground transition-all group shadow-sm hover:shadow"
          >
            <div className="flex items-center gap-2">
              <Search className="w-3.5 h-3.5 text-muted-foreground group-hover:text-foreground transition-colors" />
              <span>Search modules, skills, missions, DAG trace...</span>
            </div>
            <kbd className="font-mono text-[10px] px-1.5 py-0.5 rounded bg-background border border-border text-muted-foreground shadow-sm">
              ⌘K
            </kbd>
          </motion.button>
        </div>

        {/* Live System Status & Action Buttons */}
        <div className="flex items-center gap-3">
          <ThemeToggle />
          <div className="hidden lg:flex items-center gap-2 px-3 py-1.5 rounded-full bg-muted/50 border border-border text-foreground text-xs font-mono shadow-sm">
            <span className="w-2 h-2 rounded-full bg-emerald-500 animate-pulse shadow-[0_0_8px_rgba(16,185,129,0.8)]" />
            <span>Backend 8080 UP</span>
          </div>

          <Link href="/passport">
            <Button variant="outline" size="sm" className="gap-1.5 text-xs font-medium">
              <UserCheck className="w-3.5 h-3.5" /> Passport
            </Button>
          </Link>

          <Link href="/dashboard">
            <Button variant="champagne" size="sm" className="gap-1.5 text-xs font-semibold">
              <Activity className="w-3.5 h-3.5" /> Dashboard
            </Button>
          </Link>
        </div>
      </div>
    </motion.header>
  );
}
