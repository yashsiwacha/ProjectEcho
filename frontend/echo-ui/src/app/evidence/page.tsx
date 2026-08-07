'use client';

import { useState } from 'react';
import { useQuery, useMutation, useQueryClient } from '@tanstack/react-query';
import { api } from '@/lib/api';
import AppLayout from '@/components/AppLayout';
import { Card, CardHeader, CardTitle, CardDescription, CardContent } from '@/components/ui/Card';
import { Button } from '@/components/ui/Button';
import { Input } from '@/components/ui/Input';
import { Badge } from '@/components/ui/Badge';
import { FileCheck, ShieldCheck, CheckCircle2, XCircle, AlertCircle } from 'lucide-react';

export default function EvidencePage() {
  const queryClient = useQueryClient();
  const [passportId, setPassportId] = useState('');
  const [skillId, setSkillId] = useState('');
  const [sourceUri, setSourceUri] = useState('');
  const [errorMsg, setErrorMsg] = useState('');

  const { data: passports } = useQuery({ queryKey: ['passports'], queryFn: () => api.getPassports() });
  const { data: skills } = useQuery({ queryKey: ['skills'], queryFn: () => api.getSkills() });

  const selectedPassportId = passportId || (passports?.content[0]?.id ?? '');

  const { data: claims, isLoading } = useQuery({
    queryKey: ['evidence', selectedPassportId],
    queryFn: () => api.getPassportEvidence(selectedPassportId),
    enabled: Boolean(selectedPassportId),
  });

  const submitMutation = useMutation({
    mutationFn: api.submitEvidence,
    onSuccess: () => {
      queryClient.invalidateQueries({ queryKey: ['evidence'] });
      setSourceUri('');
      setErrorMsg('');
    },
    onError: (err: Error) => setErrorMsg(err.message),
  });

  const verifyMutation = useMutation({
    mutationFn: ({ id, trustTier }: { id: string; trustTier: string }) => api.verifyEvidence(id, trustTier),
    onSuccess: () => queryClient.invalidateQueries({ queryKey: ['evidence'] }),
  });

  const handleSubmit = (e: React.FormEvent) => {
    e.preventDefault();
    const pid = passportId || passports?.content[0]?.id;
    const sid = skillId || skills?.content[0]?.id;
    if (!pid || !sid || !sourceUri) {
      setErrorMsg('Passport, Skill, and Source URI are required');
      return;
    }
    submitMutation.mutate({ passportId: pid, skillId: sid, sourceUri });
  };

  return (
    <AppLayout>
      <div className="space-y-8 max-w-5xl">
        <div>
          <h1 className="text-3xl font-bold tracking-tight">Evidence Upload & Verification</h1>
          <p className="text-muted-foreground mt-1">Submit proof sources and verify trust tiers for competency claims.</p>
        </div>

        <div className="grid grid-cols-1 md:grid-cols-3 gap-8">
          {/* Submit Claim Form */}
          <Card className="md:col-span-1">
            <CardHeader>
              <CardTitle className="flex items-center gap-2">
                <FileCheck className="w-5 h-5 text-accent" /> Submit Evidence
              </CardTitle>
              <CardDescription>Link verified artifact or repository proof</CardDescription>
            </CardHeader>
            <CardContent>
              <form onSubmit={handleSubmit} className="space-y-4">
                <div className="space-y-1.5">
                  <label className="text-xs font-semibold uppercase tracking-wider text-muted-foreground">Passport</label>
                  <select
                    className="flex h-11 w-full rounded-xl border border-border bg-input px-4 py-2 text-sm text-foreground focus-visible:outline-none focus-visible:ring-2 focus-visible:ring-ring"
                    value={passportId}
                    onChange={(e) => setPassportId(e.target.value)}
                  >
                    {passports?.content.map((p) => (
                      <option key={p.id} value={p.id}>
                        {p.name} ({p.jobTitle})
                      </option>
                    ))}
                  </select>
                </div>

                <div className="space-y-1.5">
                  <label className="text-xs font-semibold uppercase tracking-wider text-muted-foreground">Skill</label>
                  <select
                    className="flex h-11 w-full rounded-xl border border-border bg-input px-4 py-2 text-sm text-foreground focus-visible:outline-none focus-visible:ring-2 focus-visible:ring-ring"
                    value={skillId}
                    onChange={(e) => setSkillId(e.target.value)}
                  >
                    {skills?.content.map((s) => (
                      <option key={s.id} value={s.id}>
                        {s.name} ({s.category})
                      </option>
                    ))}
                  </select>
                </div>

                <Input
                  label="Source URI"
                  placeholder="https://github.com/org/repo"
                  value={sourceUri}
                  onChange={(e) => setSourceUri(e.target.value)}
                />

                {errorMsg && (
                  <div className="p-3 rounded-lg bg-destructive/10 border border-destructive/20 text-destructive text-xs flex items-center gap-2">
                    <AlertCircle className="w-4 h-4 flex-shrink-0" />
                    {errorMsg}
                  </div>
                )}

                <Button type="submit" variant="champagne" className="w-full" disabled={submitMutation.isPending}>
                  {submitMutation.isPending ? 'Submitting...' : 'Submit Claim'}
                </Button>
              </form>
            </CardContent>
          </Card>

          {/* Evidence Claims Table */}
          <Card className="md:col-span-2">
            <CardHeader>
              <CardTitle className="flex items-center gap-2">
                <ShieldCheck className="w-5 h-5 text-accent" /> Submitted Evidence Claims
              </CardTitle>
              <CardDescription>Track verification status and trust tier assessments</CardDescription>
            </CardHeader>
            <CardContent>
              {isLoading ? (
                <div className="py-8 text-center text-sm text-muted-foreground">Loading evidence claims...</div>
              ) : claims?.content.length === 0 ? (
                <div className="py-8 text-center text-sm text-muted-foreground">No evidence claims submitted for this passport.</div>
              ) : (
                <div className="space-y-4">
                  {claims?.content.map((claim) => (
                    <div
                      key={claim.id}
                      className="p-5 rounded-xl bg-card border border-border flex flex-col md:flex-row md:items-center justify-between gap-4"
                    >
                      <div className="space-y-1">
                        <div className="flex items-center gap-2">
                          <Badge
                            variant={
                              claim.validationStatus === 'VERIFIED'
                                ? 'success'
                                : claim.validationStatus === 'REJECTED'
                                ? 'destructive'
                                : 'default'
                            }
                          >
                            {claim.validationStatus}
                          </Badge>
                          <Badge variant="champagne">{claim.trustTier}</Badge>
                        </div>
                        <p className="text-sm font-mono text-foreground font-medium truncate max-w-md">{claim.sourceUri}</p>
                        <p className="text-xs text-muted-foreground">Claim ID: {claim.id.substring(0, 8)}...</p>
                      </div>

                      {claim.validationStatus === 'PENDING' && (
                        <div className="flex items-center gap-2">
                          <Button
                            size="sm"
                            variant="champagne"
                            onClick={() => verifyMutation.mutate({ id: claim.id, trustTier: 'TIER_4' })}
                            disabled={verifyMutation.isPending}
                          >
                            Verify (Tier 4)
                          </Button>
                        </div>
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
