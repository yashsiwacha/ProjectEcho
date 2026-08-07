'use client';

import Link from 'next/link';
import { usePathname } from 'next/navigation';
import { 
  LayoutDashboard, 
  UserCheck, 
  FileCheck, 
  Compass, 
  Award, 
  BrainCircuit, 
  GitGraph, 
  User 
} from 'lucide-react';
import { cn } from '@/lib/utils';

const navItems = [
  { name: 'Dashboard', href: '/dashboard', icon: LayoutDashboard },
  { name: 'Career Passport', href: '/passport', icon: UserCheck },
  { name: 'Evidence Upload', href: '/evidence', icon: FileCheck },
  { name: 'Mission Explorer', href: '/missions', icon: Compass },
  { name: 'Readiness Assessment', href: '/assessment', icon: Award },
  { name: 'Reasoning Card', href: '/reasoning', icon: BrainCircuit },
  { name: 'Decision Graph', href: '/graph', icon: GitGraph },
  { name: 'Profile', href: '/profile', icon: User },
];

export function Sidebar() {
  const pathname = usePathname();

  return (
    <aside className="w-70 border-r border-border bg-card/60 backdrop-blur-md h-screen sticky top-0 flex flex-col justify-between p-6">
      <div>
        <div className="flex items-center gap-3 mb-10 px-2">
          <div className="w-9 h-9 rounded-lg bg-primary flex items-center justify-center text-primary-foreground font-bold text-lg">
            E
          </div>
          <div>
            <h1 className="font-bold text-foreground tracking-tight text-lg">ProjectEcho</h1>
            <p className="text-xs text-muted-foreground">Career Operating System</p>
          </div>
        </div>

        <nav className="space-y-1">
          {navItems.map((item) => {
            const isActive = pathname === item.href;
            const Icon = item.icon;

            return (
              <Link
                key={item.href}
                href={item.href}
                className={cn(
                  'flex items-center gap-3 px-4 py-3 rounded-lg text-sm font-medium transition-all duration-200',
                  isActive
                    ? 'bg-secondary/20 text-secondary-foreground font-semibold border-l-4 border-accent pl-3'
                    : 'text-muted-foreground hover:bg-muted hover:text-foreground'
                )}
              >
                <Icon className={cn('w-5 h-5', isActive ? 'text-accent' : 'text-muted-foreground')} />
                {item.name}
              </Link>
            );
          })}
        </nav>
      </div>

      <div className="p-4 rounded-xl bg-muted/50 border border-border">
        <div className="flex items-center justify-between text-xs text-muted-foreground">
          <span>Backend Status</span>
          <span className="flex items-center gap-1.5 font-medium text-emerald-600 dark:text-emerald-400">
            <span className="w-2 h-2 rounded-full bg-emerald-500 animate-pulse"></span>
            RC1 Connected
          </span>
        </div>
      </div>
    </aside>
  );
}
