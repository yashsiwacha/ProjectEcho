'use client';

import Link from 'next/link';
import { usePathname } from 'next/navigation';
import {
  LayoutDashboard,
  UserCheck,
  FileCheck,
  Compass,
  Award,
  GitGraph,
  ShieldCheck,
  Zap,
  Layers,
  Sparkles,
  ChevronRight
} from 'lucide-react';

const NAV_ITEMS = [
  { label: 'Dashboard', href: '/dashboard', icon: LayoutDashboard, badge: 'Active' },
  { label: 'Career Passport', href: '/passport', icon: UserCheck, badge: 'Tier 4' },
  { label: 'Evidence Sandbox', href: '/evidence', icon: FileCheck, badge: 'Proof' },
  { label: 'Skills Galaxy', href: '/skills', icon: Zap, badge: '3D' },
  { label: 'Mission Explorer', href: '/missions', icon: Compass, badge: 'Roles' },
  { label: 'Readiness Engine', href: '/assessment', icon: ShieldCheck, badge: 'Score' },
  { label: 'Reasoning Cards', href: '/reasoning', icon: Award, badge: 'Explain' },
  { label: 'Decision Graph', href: '/graph', icon: GitGraph, badge: '3D DAG' },
  { label: 'Executive Profile', href: '/profile', icon: Layers, badge: 'Public' },
];

export default function Sidebar() {
  const pathname = usePathname();

  return (
    <aside className="w-64 border-r border-border/80 bg-background/90 backdrop-blur-xl flex flex-col justify-between p-4 sticky top-16 h-[calc(100vh-4rem)]">
      <div className="space-y-6">
        {/* Section Header */}
        <div className="px-3 pt-2">
          <span className="text-[10px] font-mono font-bold tracking-widest text-muted-foreground uppercase">
            Platform Modules
          </span>
        </div>

        {/* Navigation List */}
        <nav className="space-y-1.5">
          {NAV_ITEMS.map((item) => {
            const Icon = item.icon;
            const isActive = pathname === item.href;

            return (
              <Link
                key={item.href}
                href={item.href}
                className={`flex items-center justify-between px-3.5 py-2.5 rounded-xl text-sm font-medium transition-all group ${
                  isActive
                    ? 'bg-amber-500/15 text-amber-300 border border-amber-500/40 shadow-lg shadow-amber-500/10'
                    : 'text-muted-foreground hover:text-foreground hover:bg-muted/50 border border-transparent'
                }`}
              >
                <div className="flex items-center gap-3">
                  <Icon className={`w-4 h-4 transition-colors ${isActive ? 'text-amber-400' : 'text-muted-foreground group-hover:text-amber-400'}`} />
                  <span>{item.label}</span>
                </div>

                {item.badge && (
                  <span className={`text-[10px] font-mono px-2 py-0.5 rounded-full border ${
                    isActive
                      ? 'bg-amber-500/25 border-amber-500/40 text-amber-300 font-bold'
                      : 'bg-muted/60 border-border text-muted-foreground group-hover:border-amber-500/30'
                  }`}>
                    {item.badge}
                  </span>
                )}
              </Link>
            );
          })}
        </nav>
      </div>

      {/* Footer Security Badge */}
      <div className="p-3.5 rounded-xl bg-muted/40 border border-border/60 space-y-2">
        <div className="flex items-center gap-2 text-xs font-semibold text-foreground">
          <Sparkles className="w-3.5 h-3.5 text-amber-400" />
          <span>Deterministic AI Core</span>
        </div>
        <p className="text-[11px] text-muted-foreground leading-relaxed">
          Zero hallucinations. All reasoning backed by auditable domain events.
        </p>
      </div>
    </aside>
  );
}
