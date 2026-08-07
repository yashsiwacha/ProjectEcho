'use client';

import { useQuery } from '@tanstack/react-query';
import { api } from '@/lib/api';
import AppLayout from '@/components/AppLayout';
import { Card, CardHeader, CardTitle, CardDescription, CardContent } from '@/components/ui/Card';
import { Badge } from '@/components/ui/Badge';
import { User, ShieldCheck, Mail, Briefcase, Key } from 'lucide-react';

export default function ProfilePage() {
  const { data: passports } = useQuery({ queryKey: ['passports'], queryFn: () => api.getPassports() });
  const activePassport = passports?.content[0];

  return (
    <AppLayout>
      <div className="space-y-8 max-w-4xl">
        <div>
          <h1 className="text-3xl font-bold tracking-tight">Executive Profile</h1>
          <p className="text-muted-foreground mt-1">Account credentials, system permissions, and verified profile metadata.</p>
        </div>

        <Card champagneBorder>
          <CardHeader>
            <div className="flex items-center gap-4">
              <div className="w-16 h-16 rounded-full bg-accent/20 border-2 border-accent flex items-center justify-center text-accent text-2xl font-bold">
                {activePassport ? activePassport.name.charAt(0) : 'E'}
              </div>
              <div>
                <CardTitle className="text-xl">{activePassport?.name ?? 'ProjectEcho User'}</CardTitle>
                <CardDescription>{activePassport?.jobTitle ?? 'Software Executive'}</CardDescription>
              </div>
            </div>
          </CardHeader>
          <CardContent className="space-y-6 pt-4">
            <div className="grid grid-cols-1 md:grid-cols-2 gap-6">
              <div className="p-4 rounded-xl bg-muted/30 border border-border space-y-1">
                <span className="text-xs text-muted-foreground flex items-center gap-1.5 font-semibold uppercase tracking-wider">
                  <Mail className="w-3.5 h-3.5 text-accent" /> Email Address
                </span>
                <p className="text-sm font-medium">{activePassport?.email ?? 'user@projectecho.io'}</p>
              </div>

              <div className="p-4 rounded-xl bg-muted/30 border border-border space-y-1">
                <span className="text-xs text-muted-foreground flex items-center gap-1.5 font-semibold uppercase tracking-wider">
                  <Briefcase className="w-3.5 h-3.5 text-accent" /> Role Designation
                </span>
                <p className="text-sm font-medium">{activePassport?.jobTitle ?? 'System Contributor'}</p>
              </div>
            </div>

            <div className="p-5 rounded-xl bg-card border border-border flex items-center justify-between">
              <div className="flex items-center gap-3">
                <ShieldCheck className="w-5 h-5 text-emerald-500" />
                <div>
                  <h4 className="font-semibold text-sm">Security & Access Tier</h4>
                  <p className="text-xs text-muted-foreground">Backend RC1 Connected - Full Admin Privileges</p>
                </div>
              </div>
              <Badge variant="success">Active</Badge>
            </div>
          </CardContent>
        </Card>
      </div>
    </AppLayout>
  );
}
