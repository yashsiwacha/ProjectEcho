'use client';

import { useState } from 'react';
import { useQuery, useMutation, useQueryClient } from '@tanstack/react-query';
import { api } from '@/lib/api';
import AppLayout from '@/components/AppLayout';
import { Card, CardHeader, CardTitle, CardDescription, CardContent } from '@/components/ui/Card';
import { Button } from '@/components/ui/Button';
import { Input } from '@/components/ui/Input';
import { Badge } from '@/components/ui/Badge';
import { Compass, Plus, Play, CheckCircle2, Award, Zap, Layers } from 'lucide-react';
import { toast } from 'sonner';

export default function MissionsPage() {
  const queryClient = useQueryClient();
  const [missionTitle, setMissionTitle] = useState('');

  const { data: missions, isLoading } = useQuery({
    queryKey: ['missions'],
    queryFn: () => api.getMissions(),
  });

  const createMutation = useMutation({
    mutationFn: api.createMission,
    onSuccess: (data) => {
      queryClient.invalidateQueries({ queryKey: ['missions'] });
      setMissionTitle('');
      toast.success(`Mission "${data.title}" established!`);
    },
    onError: (err: Error) => {
      toast.error(`Failed to create mission: ${err.message}`);
    },
  });

  const activateMutation = useMutation({
    mutationFn: api.activateMission,
    onSuccess: () => {
      queryClient.invalidateQueries({ queryKey: ['missions'] });
      toast.success('Mission activated for readiness qualification!');
    },
  });

  const handleCreateMission = (e: React.FormEvent) => {
    e.preventDefault();
    if (!missionTitle) {
      toast.error('Mission title is required');
      return;
    }
    createMutation.mutate({ title: missionTitle });
  };

  return (
    <AppLayout>
      <div className="space-y-8 max-w-5xl">
        {/* Header */}
        <div className="flex flex-col md:flex-row md:items-center justify-between gap-4">
          <div>
            <div className="flex items-center gap-2">
              <h1 className="text-3xl font-extrabold tracking-tight text-white">Mission Explorer</h1>
              <Badge variant="champagne" className="text-[10px]">Strategic Quests</Badge>
            </div>
            <p className="text-muted-foreground text-sm mt-1">
              Target executive roles, required competency checklists, and multi-state activation pathways.
            </p>
          </div>
        </div>

        {/* Two Column Grid: Creation & Quest Board */}
        <div className="grid grid-cols-1 md:grid-cols-12 gap-8">
          {/* Create Mission Form */}
          <Card champagneBorder className="md:col-span-5 p-6">
            <CardHeader className="p-0 pb-4">
              <CardTitle className="flex items-center gap-2 text-base font-bold text-white">
                <Plus className="w-4 h-4 text-amber-400" /> Create Executive Mission
              </CardTitle>
              <CardDescription className="text-xs">
                Define a target career objective or strategic engineering role
              </CardDescription>
            </CardHeader>
            <CardContent className="p-0">
              <form onSubmit={handleCreateMission} className="space-y-4">
                <Input
                  label="Mission Role Title"
                  placeholder="e.g. Chief Systems Architect"
                  value={missionTitle}
                  onChange={(e) => setMissionTitle(e.target.value)}
                />

                <div className="p-3 rounded-xl bg-slate-900/80 border border-border text-xs text-muted-foreground space-y-1.5">
                  <div className="font-semibold text-white">Standard Requirements Evaluated:</div>
                  <ul className="list-disc pl-4 space-y-0.5 font-mono text-[11px]">
                    <li>Java 21 / Spring Boot 3 DDD (Tier 4)</li>
                    <li>Distributed Event Streaming & Kafka</li>
                    <li>Zero-Trust OWASP Security Compliance</li>
                  </ul>
                </div>

                <Button
                  type="submit"
                  variant="champagne"
                  className="w-full font-bold shadow-lg shadow-amber-500/20"
                  disabled={createMutation.isPending}
                >
                  {createMutation.isPending ? 'Establishing...' : 'Publish Mission'}
                </Button>
              </form>
            </CardContent>
          </Card>

          {/* Missions Quest Board */}
          <Card champagneBorder className="md:col-span-7 p-6">
            <CardHeader className="p-0 pb-4">
              <CardTitle className="flex items-center gap-2 text-base font-bold text-white">
                <Compass className="w-4 h-4 text-emerald-400" /> Active Career Quests
              </CardTitle>
              <CardDescription className="text-xs">
                Evaluate your verified passport against active mission criteria
              </CardDescription>
            </CardHeader>
            <CardContent className="p-0">
              <div className="space-y-4">
                {missions?.content.map((m) => {
                  const isActive = m.status === 'ACTIVE';
                  return (
                    <div
                      key={m.id}
                      className="p-5 rounded-2xl bg-slate-900/60 border border-border hover:border-amber-500/40 transition-all space-y-3"
                    >
                      <div className="flex items-start justify-between gap-4">
                        <div>
                          <div className="flex items-center gap-2">
                            <h4 className="font-bold text-base text-white">{m.title}</h4>
                            <Badge variant={isActive ? 'success' : 'default'} className="text-[10px]">
                              {m.status}
                            </Badge>
                          </div>
                          <span className="text-[10px] font-mono text-muted-foreground block mt-0.5">
                            ID: {m.id}
                          </span>
                        </div>

                        {!isActive && (
                          <Button
                            size="sm"
                            variant="champagne"
                            onClick={() => activateMutation.mutate(m.id)}
                            disabled={activateMutation.isPending}
                            className="text-xs font-bold gap-1"
                          >
                            <Play className="w-3.5 h-3.5" /> Activate
                          </Button>
                        )}
                      </div>

                      <div className="flex items-center justify-between pt-2 border-t border-border/60 text-xs font-mono">
                        <div className="flex items-center gap-1.5 text-emerald-400">
                          <CheckCircle2 className="w-3.5 h-3.5" /> Ready for Rule Engine
                        </div>
                        <span className="text-muted-foreground">{m.createdAt}</span>
                      </div>
                    </div>
                  );
                })}
              </div>
            </CardContent>
          </Card>
        </div>
      </div>
    </AppLayout>
  );
}
