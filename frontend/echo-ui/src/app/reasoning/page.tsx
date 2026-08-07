'use client';

import { useQuery } from '@tanstack/react-query';
import { api } from '@/lib/api';
import AppLayout from '@/components/AppLayout';
import { Card, CardHeader, CardTitle, CardDescription, CardContent } from '@/components/ui/Card';
import { Badge } from '@/components/ui/Badge';
import { Button } from '@/components/ui/Button';
import { BrainCircuit, GitGraph, ShieldCheck, Zap, ArrowRight } from 'lucide-react';
import Link from 'next/link';

export default function ReasoningPage() {
  const { data: reasoningData, isLoading } = useQuery({
    queryKey: ['reasoningCards'],
    queryFn: () => api.getReasoningCards(),
  });

  return (
    <AppLayout>
      <div className="space-y-8 max-w-5xl">
        <div>
          <h1 className="text-3xl font-bold tracking-tight">Reasoning Cards</h1>
          <p className="text-muted-foreground mt-1">Explainable AI reasoning cards generated automatically from rule evaluations.</p>
        </div>

        <div className="space-y-6">
          {isLoading ? (
            <div className="py-8 text-center text-sm text-muted-foreground">Loading reasoning cards...</div>
          ) : reasoningData?.content.length === 0 ? (
            <Card className="p-8 text-center">
              <BrainCircuit className="w-10 h-10 text-muted-foreground mx-auto mb-3" />
              <CardTitle>No Reasoning Cards Found</CardTitle>
              <CardDescription className="mt-1">
                Reasoning Cards are generated automatically when a Readiness Assessment is evaluated.
              </CardDescription>
              <Link href="/assessment" className="inline-block mt-4">
                <Button variant="champagne">Go to Assessment</Button>
              </Link>
            </Card>
          ) : (
            reasoningData?.content.map((card) => (
              <Card key={card.id} champagneBorder className="relative overflow-hidden">
                <div className="flex flex-col md:flex-row md:items-center justify-between gap-6">
                  <div className="space-y-3 max-w-2xl">
                    <div className="flex items-center gap-3">
                      <Badge variant="champagne" className="gap-1">
                        <Zap className="w-3 h-3" /> {card.confidenceScore}% Confidence
                      </Badge>
                      <Badge variant="default" className="font-mono text-[10px]">
                        Card ID: {card.id.substring(0, 8)}...
                      </Badge>
                    </div>

                    <h3 className="text-xl font-bold text-foreground">AI Evidence Summary</h3>
                    <p className="text-sm text-muted-foreground leading-relaxed">{card.summary}</p>

                    <div className="flex items-center gap-6 pt-2 text-xs text-muted-foreground">
                      <span>Passport ID: <strong className="text-foreground font-mono">{card.passportId.substring(0, 8)}...</strong></span>
                      <span>Mission ID: <strong className="text-foreground font-mono">{card.missionId.substring(0, 8)}...</strong></span>
                    </div>
                  </div>

                  <div className="flex flex-col gap-3">
                    <Link href="/graph">
                      <Button variant="outline" className="w-full gap-2">
                        <GitGraph className="w-4 h-4 text-accent" /> Inspect Decision Graph <ArrowRight className="w-4 h-4" />
                      </Button>
                    </Link>
                  </div>
                </div>
              </Card>
            ))
          )}
        </div>
      </div>
    </AppLayout>
  );
}
