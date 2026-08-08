'use client';

import { useEffect, useState } from 'react';
import { useRouter } from 'next/navigation';
import {
  Search,
  UserCheck,
  FileCheck,
  Compass,
  Award,
  GitGraph,
  ShieldCheck,
  Zap,
  Activity,
  Layers,
  Terminal,
  X
} from 'lucide-react';

interface CommandItem {
  title: string;
  category: string;
  href: string;
  icon: any;
  badge?: string;
}

const COMMANDS: CommandItem[] = [
  { title: 'Executive Dashboard', category: 'Overview', href: '/dashboard', icon: Activity, badge: 'Active' },
  { title: 'Career Passport Studio', category: 'Identity', href: '/passport', icon: UserCheck, badge: 'Tier 4' },
  { title: 'Evidence Verification Sandbox', category: 'Trust', href: '/evidence', icon: FileCheck, badge: 'Proof' },
  { title: 'Skills Taxonomy Galaxy', category: 'Competency', href: '/skills', icon: Zap, badge: 'Ontology' },
  { title: 'Mission Explorer', category: 'Strategy', href: '/missions', icon: Compass, badge: 'Roles' },
  { title: 'Readiness Assessment Engine', category: 'Rules', href: '/assessment', icon: ShieldCheck, badge: 'Score 100' },
  { title: 'Explainable Reasoning Cards', category: 'Audit', href: '/reasoning', icon: Award, badge: 'Explain' },
  { title: 'Decision Graph Traceability', category: 'DAG', href: '/graph', icon: GitGraph, badge: '3D DAG' },
  { title: 'Executive Profile Showcase', category: 'Public', href: '/profile', icon: Layers, badge: 'Live' },
];

export default function CommandPalette() {
  const [isOpen, setIsOpen] = useState(false);
  const [query, setQuery] = useState('');
  const router = useRouter();

  useEffect(() => {
    const onKeyDown = (e: KeyboardEvent) => {
      if ((e.metaKey || e.ctrlKey) && e.key === 'k') {
        e.preventDefault();
        setIsOpen((prev) => !prev);
      }
      if (e.key === 'Escape') {
        setIsOpen(false);
      }
    };

    window.addEventListener('keydown', onKeyDown);
    return () => window.removeEventListener('keydown', onKeyDown);
  }, []);

  if (!isOpen) return null;

  const filtered = COMMANDS.filter((cmd) =>
    cmd.title.toLowerCase().includes(query.toLowerCase()) ||
    cmd.category.toLowerCase().includes(query.toLowerCase())
  );

  const navigate = (href: string) => {
    setIsOpen(false);
    setQuery('');
    router.push(href);
  };

  return (
    <div className="fixed inset-0 z-50 flex items-start justify-center pt-24 bg-black/75 backdrop-blur-md animate-in fade-in duration-200">
      <div className="relative w-full max-w-xl rounded-2xl bg-card border border-amber-500/30 shadow-2xl overflow-hidden glass-panel-glow">
        {/* Search Input Bar */}
        <div className="flex items-center px-4 py-3.5 border-b border-border gap-3">
          <Search className="w-5 h-5 text-amber-400 flex-shrink-0" />
          <input
            autoFocus
            type="text"
            placeholder="Type a command, module, or feature (e.g. Passport, Evidence, 3D Graph)..."
            value={query}
            onChange={(e) => setQuery(e.target.value)}
            className="w-full bg-transparent text-sm text-foreground placeholder:text-muted-foreground outline-none font-medium"
          />
          <button
            onClick={() => setIsOpen(false)}
            className="text-xs text-muted-foreground hover:text-foreground font-mono bg-muted/60 px-2 py-1 rounded"
          >
            ESC
          </button>
        </div>

        {/* Command Results */}
        <div className="max-h-80 overflow-y-auto p-2 space-y-1">
          {filtered.length === 0 ? (
            <div className="py-8 text-center text-xs text-muted-foreground font-mono">
              No matching modules found for "{query}".
            </div>
          ) : (
            filtered.map((cmd) => {
              const Icon = cmd.icon;
              return (
                <button
                  key={cmd.href}
                  onClick={() => navigate(cmd.href)}
                  className="w-full flex items-center justify-between p-3 rounded-xl hover:bg-amber-500/10 hover:border-amber-500/30 border border-transparent transition-all text-left group"
                >
                  <div className="flex items-center gap-3">
                    <div className="p-2 rounded-lg bg-muted text-amber-400 group-hover:bg-amber-500/20 transition-colors">
                      <Icon className="w-4 h-4" />
                    </div>
                    <div>
                      <div className="text-sm font-semibold text-foreground group-hover:text-amber-300">
                        {cmd.title}
                      </div>
                      <div className="text-[10px] text-muted-foreground font-mono">
                        {cmd.category}
                      </div>
                    </div>
                  </div>
                  {cmd.badge && (
                    <span className="text-[10px] font-mono px-2 py-0.5 rounded-full bg-muted border border-border text-muted-foreground group-hover:text-amber-300 group-hover:border-amber-500/40">
                      {cmd.badge}
                    </span>
                  )}
                </button>
              );
            })
          )}
        </div>

        {/* Footer */}
        <div className="p-3 border-t border-border bg-muted/40 flex items-center justify-between text-[11px] text-muted-foreground font-mono">
          <span>Navigation Quicklinks</span>
          <span>Press ↵ to select</span>
        </div>
      </div>
    </div>
  );
}
