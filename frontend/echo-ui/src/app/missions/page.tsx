'use client';

import { useState } from 'react';
import { useQuery, useMutation, useQueryClient } from '@tanstack/react-query';
import { api } from '@/lib/api';
import AppLayout from '@/components/AppLayout';
import { Card, CardHeader, CardTitle, CardDescription, CardContent } from '@/components/ui/Card';
import { Button } from '@/components/ui/Button';
import { Input } from '@/components/ui/Input';
import { Badge } from '@/components/ui/Badge';
import { Compass, Plus, Play, Archive, AlertCircle } from 'lucide-react';

export default function MissionsPage() {
  const queryClient = useQueryClient();
  const [title, setTitle] = useState('');
  const [errorMsg, setErrorMsg] = useState('');

  const { data: missions, isLoading } = useQuery({
    queryKey: ['missions'],
    queryFn: () => api.getMissions(),
  });

  const createMutation = useMutation({
    mutationFn: api.createMission,
    onSuccess: () => {
      queryClient.invalidateQueries({ queryKey: ['missions'] });
      setTitle('');
      setErrorMsg('');
    },
    onError: (err: Error) => setErrorMsg(err.message),
  });

  const activateMutation = useMutation({
    mutationFn: api.activateMission,
    onSuccess: () => queryClient.invalidateQueries({ queryKey: ['missions'] }),
  });

  const handleSubmit = (e: React.FormEvent) => {
    e.preventDefault();
    if (!title) {
      setErrorMsg('Mission title is required');
      return;
    }
    createMutation.mutate({ title });
  };

  return (
    <AppLayout>
      <div className="space-y-8 max-w-5xl">
        <div>
          <h1 className="text-3xl font-bold tracking-tight">Mission Explorer</h1>
          <p className="text-muted-foreground mt-1">Configure and activate target career roles and readiness missions.</p>
        </div>

        <div className="grid grid-cols-1 md:grid-cols-3 gap-8">
          {/* Create Mission Form */}
          <Card className="md:col-span-1">
            <CardHeader>
              <CardTitle className="flex items-center gap-2">
                <Plus className="w-5 h-5 text-accent" /> Create Mission
              </CardTitle>
              <CardDescription>Define a new strategic role objective</CardDescription>
            </CardHeader>
            <CardContent>
              <form onSubmit={handleSubmit} className="space-y-4">
                <Input
                  label="Mission Title"
                  placeholder="e.g. Lead Systems Architect"
                  value={title}
                  onChange={(e) => setTitle(e.target.value)}
                />

                {errorMsg && (
                  <div className="p-3 rounded-lg bg-destructive/10 border border-destructive/20 text-destructive text-xs flex items-center gap-2">
                    <AlertCircle className="w-4 h-4 flex-shrink-0" />
                    {errorMsg}
                  </div>
                )}

                <Button type="submit" variant="champagne" className="w-full" disabled={createMutation.isPending}>
                  {createMutation.isPending ? 'Creating...' : 'Create Mission'}
                </Button>
              </form>
            </CardContent>
          </Card>

          {/* Missions List */}
          <Card className="md:col-span-2">
            <CardHeader>
              <CardTitle className="flex items-center gap-2">
                <Compass className="w-5 h-5 text-accent" /> Active & Draft Missions
              </CardTitle>
              <CardDescription>System target missions available for evaluation</CardDescription>
            </CardHeader>
            <CardContent>
              {isLoading ? (
                <div className="py-8 text-center text-sm text-muted-foreground">Loading missions...</div>
              ) : missions?.content.length === 0 ? (
                <div className="py-8 text-center text-sm text-muted-foreground">No missions available. Create one to begin.</div>
              ) : (
                <div className="space-y-4">
                  {missions?.content.map((mission) => (
                    <div
                      key={mission.id}
                      className="p-5 rounded-xl bg-card border border-border flex items-center justify-between gap-4"
                    >
                      <div className="space-y-1">
                        <div className="flex items-center gap-2">
                          <h3 className="font-semibold text-base">{mission.title}</h3>
                          <Badge variant={mission.status === 'ACTIVE' ? 'success' : 'default'}>
                            {mission.status}
                          </Badge>
                        </div>
                        <p className="text-xs text-muted-foreground font-mono">ID: {mission.id}</p>
                      </div>

                      {mission.status === 'DRAFT' && (
                        <Button
                          size="sm"
                          variant="outline"
                          className="gap-1.5"
                          onClick={() => activateMutation.mutate(mission.id)}
                          disabled={activateMutation.isPending}
                        >
                          <Play className="w-3.5 h-3.5" /> Activate
                        </Button>
                      )}
                    </div>
                  ))}
                </div>
              )}
            </CardContent>
          </Card>
        </div>
      </div>
    </AppLayout>
  );
}
