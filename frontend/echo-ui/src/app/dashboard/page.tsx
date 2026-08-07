'use client';

import { useQuery } from '@tanstack/react-query';
import { api } from '@/lib/api';
import AppLayout from '@/components/AppLayout';
import { Card, CardHeader, CardTitle, CardDescription, CardContent } from '@/components/ui/Card';
import { Badge } from '@/components/ui/Badge';
import { Button } from '@/components/ui/Button';
import { UserCheck, FileCheck, Compass, Award, ArrowRight, ShieldCheck, Zap } from 'lucide-react';
import Link from 'next/link';

export default function DashboardPage() {
  const { data: passports } = useQuery({ queryKey: ['passports'], queryFn: () => api.getPassports() });
  const { data: skills } = useQuery({ queryKey: ['skills'], queryFn: () => api.getSkills() });
  const { data: missions } = useQuery({ queryKey: ['missions'], queryFn: () => api.getMissions() });

  const activePassport = passports?.content[0];

  return (
    <AppLayout>
      <div className="space-y-8">
        {/* Header */}
        <div className="flex items-center justify-between">
          <div>
            <h1 className="text-3xl font-bold tracking-tight">Executive Dashboard</h1>
            <p className="text-muted-foreground mt-1">Real-time career intelligence and evidence verification status.</p>
          </div>
          <Link href="/evidence">
            <Button variant="champagne" className="gap-2">
              <FileCheck className="w-4 h-4" /> Submit Evidence
            </Button>
          </Link>
        </div>

        {/* Quick Stats Grid */}
        <div className="grid grid-cols-1 md:grid-cols-4 gap-6">
          <Card>
            <CardHeader className="flex flex-row items-center justify-between pb-2">
              <CardTitle className="text-sm font-semibold text-muted-foreground">Career Passports</CardTitle>
              <UserCheck className="w-5 h-5 text-accent" />
            </CardHeader>
            <CardContent>
              <div className="text-3xl font-bold">{passports?.totalElements ?? 0}</div>
              <p className="text-xs text-muted-foreground mt-1">Active verified profiles</p>
            </CardContent>
          </Card>

          <Card>
            <CardHeader className="flex flex-row items-center justify-between pb-2">
              <CardTitle className="text-sm font-semibold text-muted-foreground">Taxonomy Skills</CardTitle>
              <Zap className="w-5 h-5 text-amber-500" />
            </CardHeader>
            <CardContent>
              <div className="text-3xl font-bold">{skills?.totalElements ?? 0}</div>
              <p className="text-xs text-muted-foreground mt-1">Registered competencies</p>
            </CardContent>
          </Card>

          <Card>
            <CardHeader className="flex flex-row items-center justify-between pb-2">
              <CardTitle className="text-sm font-semibold text-muted-foreground">Active Missions</CardTitle>
              <Compass className="w-5 h-5 text-emerald-500" />
            </CardHeader>
            <CardContent>
              <div className="text-3xl font-bold">{missions?.totalElements ?? 0}</div>
              <p className="text-xs text-muted-foreground mt-1">Target career roles</p>
            </CardContent>
          </Card>

          <Card>
            <CardHeader className="flex flex-row items-center justify-between pb-2">
              <CardTitle className="text-sm font-semibold text-muted-foreground">Rule Engine Authority</CardTitle>
              <ShieldCheck className="w-5 h-5 text-blue-500" />
            </CardHeader>
            <CardContent>
              <div className="text-3xl font-bold">100%</div>
              <p className="text-xs text-muted-foreground mt-1">Deterministic AI Verification</p>
            </CardContent>
          </Card>
        </div>

        {/* Primary Focus Card: Evidence-Based AI Reasoning */}
        <Card champagneBorder className="relative overflow-hidden">
          <div className="flex flex-col md:flex-row items-start md:items-center justify-between gap-6">
            <div className="space-y-2 max-w-2xl">
              <Badge variant="champagne">AI Career OS Active</Badge>
              <h2 className="text-2xl font-bold tracking-tight">Verified Career Passport Status</h2>
              <p className="text-muted-foreground text-sm leading-relaxed">
                {activePassport ? (
                  <>Current active passport for <span className="font-semibold text-foreground">{activePassport.name}</span> ({activePassport.jobTitle}). All evidence claims are backed by verifiable proof graphs.</>
                ) : (
                  <>No active Passport initialized yet. Initialize your Career Passport to unlock automated readiness assessments.</>
                )}
              </p>
            </div>
            <div className="flex items-center gap-4">
              <Link href="/passport">
                <Button variant="default">Manage Passport</Button>
              </Link>
              <Link href="/assessment">
                <Button variant="outline" className="gap-2">
                  Assess Readiness <ArrowRight className="w-4 h-4" />
                </Button>
              </Link>
            </div>
          </div>
        </Card>

        {/* Two Column Grid */}
        <div className="grid grid-cols-1 md:grid-cols-2 gap-8">
          {/* Recent Missions */}
          <Card>
            <CardHeader>
              <CardTitle>Recent Missions</CardTitle>
              <CardDescription>Target strategic career opportunities</CardDescription>
            </CardHeader>
            <CardContent>
              <div className="space-y-4">
                {missions?.content.slice(0, 3).map((m) => (
                  <div key={m.id} className="flex items-center justify-between p-3.5 rounded-xl bg-muted/40 border border-border/50">
                    <div>
                      <h4 className="font-semibold text-sm">{m.title}</h4>
                      <span className="text-xs text-muted-foreground">ID: {m.id.substring(0, 8)}...</span>
                    </div>
                    <Badge variant={m.status === 'ACTIVE' ? 'success' : 'default'}>{m.status}</Badge>
                  </div>
                ))}
                {(!missions?.content || missions.content.length === 0) && (
                  <p className="text-sm text-muted-foreground py-4 text-center">No missions created yet.</p>
                )}
              </div>
            </CardContent>
          </Card>

          {/* Registered Skills */}
          <Card>
            <CardHeader>
              <CardTitle>Registered Skills</CardTitle>
              <CardDescription>Competencies in the taxonomy database</CardDescription>
            </CardHeader>
            <CardContent>
              <div className="space-y-4">
                {skills?.content.slice(0, 3).map((s) => (
                  <div key={s.id} className="flex items-center justify-between p-3.5 rounded-xl bg-muted/40 border border-border/50">
                    <div>
                      <h4 className="font-semibold text-sm">{s.name}</h4>
                      <span className="text-xs text-muted-foreground">{s.category}</span>
                    </div>
                    <Badge variant="champagne">Taxonomy</Badge>
                  </div>
                ))}
                {(!skills?.content || skills.content.length === 0) && (
                  <p className="text-sm text-muted-foreground py-4 text-center">No skills registered yet.</p>
                )}
              </div>
            </CardContent>
          </Card>
        </div>
      </div>
    </AppLayout>
  );
}
