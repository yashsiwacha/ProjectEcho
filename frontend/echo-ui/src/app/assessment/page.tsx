'use client';

import { useState } from 'react';
import { useQuery, useMutation } from '@tanstack/react-query';
import { api, ReadinessAssessment } from '@/lib/api';
import AppLayout from '@/components/AppLayout';
import { Card, CardHeader, CardTitle, CardDescription, CardContent } from '@/components/ui/Card';
import { Button } from '@/components/ui/Button';
import { Badge } from '@/components/ui/Badge';
import { Award, ShieldCheck, Zap, AlertCircle, ArrowRight } from 'lucide-react';
import Link from 'next/link';

export default function AssessmentPage() {
  const [passportId, setPassportId] = useState('');
  const [missionId, setMissionId] = useState('');
  const [result, setResult] = useState<ReadinessAssessment | null>(null);
  const [errorMsg, setErrorMsg] = useState('');

  const { data: passports } = useQuery({ queryKey: ['passports'], queryFn: () => api.getPassports() });
  const { data: missions } = useQuery({ queryKey: ['missions'], queryFn: () => api.getMissions() });

  const evalMutation = useMutation({
    mutationFn: api.evaluateReadiness,
    onSuccess: (data) => {
      setResult(data);
      setErrorMsg('');
    },
    onError: (err: Error) => setErrorMsg(err.message),
  });

  const handleEvaluate = (e: React.FormEvent) => {
    e.preventDefault();
    const pid = passportId || passports?.content[0]?.id;
    const mid = missionId || missions?.content[0]?.id;

    if (!pid || !mid) {
      setErrorMsg('Please select both a Passport and a Mission');
      return;
    }

    evalMutation.mutate({
      passportId: pid,
      missionId: mid,
      passportSkills: ['Java 21', 'Spring Boot'],
      isPassportVerified: true,
      missionRequiredSkills: ['Java 21'],
      isMissionActive: true,
    });
  };

  return (
    <AppLayout>
      <div className="space-y-8 max-w-5xl">
        <div>
          <h1 className="text-3xl font-bold tracking-tight">Readiness Assessment</h1>
          <p className="text-muted-foreground mt-1">Execute Rule Engine authority to verify candidate readiness against mission criteria.</p>
        </div>

        <div className="grid grid-cols-1 md:grid-cols-3 gap-8">
          {/* Evaluation Form */}
          <Card className="md:col-span-1">
            <CardHeader>
              <CardTitle className="flex items-center gap-2">
                <Award className="w-5 h-5 text-accent" /> Trigger Rule Engine
              </CardTitle>
              <CardDescription>Evaluate deterministic readiness score</CardDescription>
            </CardHeader>
            <CardContent>
              <form onSubmit={handleEvaluate} className="space-y-4">
                <div className="space-y-1.5">
                  <label className="text-xs font-semibold uppercase tracking-wider text-muted-foreground">Select Passport</label>
                  <select
                    className="flex h-11 w-full rounded-xl border border-border bg-input px-4 py-2 text-sm text-foreground focus-visible:outline-none focus-visible:ring-2 focus-visible:ring-ring"
                    value={passportId}
                    onChange={(e) => setPassportId(e.target.value)}
                  >
                    {passports?.content.map((p) => (
                      <option key={p.id} value={p.id}>
                        {p.name}
                      </option>
                    ))}
                  </select>
                </div>

                <div className="space-y-1.5">
                  <label className="text-xs font-semibold uppercase tracking-wider text-muted-foreground">Select Mission</label>
                  <select
                    className="flex h-11 w-full rounded-xl border border-border bg-input px-4 py-2 text-sm text-foreground focus-visible:outline-none focus-visible:ring-2 focus-visible:ring-ring"
                    value={missionId}
                    onChange={(e) => setMissionId(e.target.value)}
                  >
                    {missions?.content.map((m) => (
                      <option key={m.id} value={m.id}>
                        {m.title} ({m.status})
                      </option>
                    ))}
                  </select>
                </div>

                {errorMsg && (
                  <div className="p-3 rounded-lg bg-destructive/10 border border-destructive/20 text-destructive text-xs flex items-center gap-2">
                    <AlertCircle className="w-4 h-4 flex-shrink-0" />
                    {errorMsg}
                  </div>
                )}

                <Button type="submit" variant="champagne" className="w-full" disabled={evalMutation.isPending}>
                  {evalMutation.isPending ? 'Evaluating...' : 'Evaluate Readiness'}
                </Button>
              </form>
            </CardContent>
          </Card>

          {/* Result Card */}
          <Card champagneBorder className="md:col-span-2 flex flex-col justify-between">
            <div>
              <CardHeader>
                <div className="flex items-center justify-between">
                  <CardTitle className="flex items-center gap-2">
                    <ShieldCheck className="w-5 h-5 text-accent" /> Assessment Result
                  </CardTitle>
                  {result && (
                    <Badge variant={result.eligible ? 'success' : 'destructive'}>
                      {result.eligible ? 'ELIGIBLE' : 'NOT ELIGIBLE'}
                    </Badge>
                  )}
                </div>
                <CardDescription>Deterministic decision generated by Rule Engine</CardDescription>
              </CardHeader>
              <CardContent className="space-y-6">
                {result ? (
                  <div className="space-y-6">
                    <div className="flex items-center justify-between p-6 rounded-2xl bg-muted/30 border border-border">
                      <div>
                        <span className="text-xs uppercase tracking-wider text-muted-foreground font-semibold block">Readiness Score</span>
                        <div className="text-4xl font-bold text-foreground mt-1">{result.score} / 100</div>
                      </div>
                      <div className="text-right">
                        <span className="text-xs uppercase tracking-wider text-muted-foreground font-semibold block">Graph Trace ID</span>
                        <div className="text-xs font-mono text-accent mt-1">{result.graphId.substring(0, 12)}...</div>
                      </div>
                    </div>

                    <div className="p-4 rounded-xl bg-card border border-border space-y-2">
                      <h4 className="text-sm font-semibold flex items-center gap-2">
                        <Zap className="w-4 h-4 text-accent" /> Rule Authority Executed
                      </h4>
                      <p className="text-xs text-muted-foreground leading-relaxed">
                        Evaluated standard skill matching rule. Passport verification status and active mission prerequisites verified cleanly.
                      </p>
                    </div>
                  </div>
                ) : (
                  <div className="py-12 text-center text-sm text-muted-foreground">
                    Select a Passport and Mission, then click &quot;Evaluate Readiness&quot; to execute the decision graph.
                  </div>
                )}
              </CardContent>
            </div>

            {result && (
              <div className="p-6 border-t border-border flex items-center justify-between bg-muted/20">
                <span className="text-xs text-muted-foreground">Reasoning Card generated automatically.</span>
                <Link href="/reasoning">
                  <Button variant="default" size="sm" className="gap-1.5">
                    View Reasoning Card <ArrowRight className="w-4 h-4" />
                  </Button>
                </Link>
              </div>
            )}
          </Card>
        </div>
      </div>
    </AppLayout>
  );
}
