'use client';

import { useEffect, useState } from 'react';
import { useRouter } from 'next/navigation';
import { Command } from 'cmdk';
import { motion, AnimatePresence } from 'framer-motion';
import {
  Search, UserCheck, FileCheck, Compass, Award, GitGraph, ShieldCheck, Zap, Activity, Layers
} from 'lucide-react';
import { springTransition } from '@/lib/motion';

interface CommandItem {
  title: string;
  category: string;
  href: string;
  icon: React.ElementType;
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

  const navigate = (href: string) => {
    setIsOpen(false);
    router.push(href);
  };

  return (
    <AnimatePresence>
      {isOpen && (
        <Command.Dialog 
          open={isOpen} 
          onOpenChange={setIsOpen} 
          label="Global Command Palette"
          className="fixed inset-0 z-50 flex items-start justify-center pt-32 sm:pt-48"
        >
          {/* Backdrop */}
          <motion.div 
            initial={{ opacity: 0 }}
            animate={{ opacity: 1 }}
            exit={{ opacity: 0 }}
            transition={{ duration: 0.2 }}
            className="fixed inset-0 bg-background/50 backdrop-blur-md" 
            onClick={() => setIsOpen(false)}
          />

          <motion.div
            initial={{ opacity: 0, scale: 0.95, y: -20 }}
            animate={{ opacity: 1, scale: 1, y: 0 }}
            exit={{ opacity: 0, scale: 0.95, y: 10 }}
            transition={springTransition}
            className="relative w-full max-w-xl mx-4 rounded-2xl glass-panel-glow border-border-highlight overflow-hidden shadow-2xl flex flex-col z-10"
          >
            <div className="flex items-center px-4 border-b border-border/50">
              <Search className="w-5 h-5 text-muted-foreground mr-2 shrink-0" />
              <Command.Input 
                autoFocus 
                placeholder="Type a command, module, or feature..." 
                className="w-full h-14 bg-transparent text-sm text-foreground placeholder:text-muted-foreground outline-none font-medium"
              />
              <button onClick={() => setIsOpen(false)} className="text-[10px] text-muted-foreground font-mono bg-muted px-1.5 py-0.5 rounded border border-border">
                ESC
              </button>
            </div>

            <Command.List className="max-h-[60vh] overflow-y-auto p-2 scrollbar-none">
              <Command.Empty className="py-8 text-center text-sm text-muted-foreground font-mono">
                No matching modules found.
              </Command.Empty>

              {['Overview', 'Identity', 'Trust', 'Competency', 'Strategy', 'Rules', 'Audit', 'DAG', 'Public'].map((category) => {
                const items = COMMANDS.filter(cmd => cmd.category === category);
                if (items.length === 0) return null;
                return (
                  <Command.Group key={category} heading={<span className="text-[10px] font-mono text-muted-foreground px-2 py-1 uppercase tracking-wider">{category}</span>}>
                    {items.map((cmd) => {
                      const Icon = cmd.icon;
                      return (
                        <Command.Item
                          key={cmd.title}
                          onSelect={() => navigate(cmd.href)}
                          className="flex items-center justify-between p-3 rounded-xl hover:bg-muted/80 aria-selected:bg-muted/80 aria-selected:text-foreground text-muted-foreground cursor-pointer transition-colors group mt-1"
                        >
                          <div className="flex items-center gap-3">
                            <div className="p-2 rounded-lg bg-card border border-border text-foreground group-aria-selected:bg-primary group-aria-selected:text-primary-foreground group-aria-selected:border-primary transition-colors">
                              <Icon className="w-4 h-4" />
                            </div>
                            <span className="text-sm font-semibold text-foreground">{cmd.title}</span>
                          </div>
                          {cmd.badge && (
                            <span className="text-[10px] font-mono px-2 py-0.5 rounded-full bg-card border border-border text-muted-foreground group-aria-selected:bg-primary group-aria-selected:text-primary-foreground">
                              {cmd.badge}
                            </span>
                          )}
                        </Command.Item>
                      );
                    })}
                  </Command.Group>
                );
              })}
            </Command.List>

            <div className="px-4 py-3 border-t border-border/50 bg-muted/20 flex items-center gap-4 text-[11px] text-muted-foreground font-mono">
              <span className="flex items-center gap-1">Press <kbd className="bg-muted px-1 rounded border border-border text-foreground">↵</kbd> to select</span>
              <span className="flex items-center gap-1">Press <kbd className="bg-muted px-1 rounded border border-border text-foreground">↑↓</kbd> to navigate</span>
            </div>
          </motion.div>
        </Command.Dialog>
      )}
    </AnimatePresence>
  );
}
