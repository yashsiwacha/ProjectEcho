import Link from 'next/link';
import { Button } from '@/components/ui/Button';
import { Card } from '@/components/ui/Card';
import { Badge } from '@/components/ui/Badge';
import { ShieldCheck, BrainCircuit, Award, ArrowRight, CheckCircle2 } from 'lucide-react';

export default function LandingPage() {
  return (
    <div className="min-h-screen bg-background text-foreground flex flex-col justify-between p-8 md:p-16 max-w-7xl mx-auto">
      {/* Header */}
      <header className="flex items-center justify-between">
        <div className="flex items-center gap-3">
          <div className="w-10 h-10 rounded-xl bg-primary flex items-center justify-center text-primary-foreground font-bold text-xl">
            E
          </div>
          <div>
            <h1 className="font-bold text-xl tracking-tight">ProjectEcho</h1>
            <p className="text-xs text-muted-foreground">Career Operating System</p>
          </div>
        </div>

        <div className="flex items-center gap-4">
          <Link href="/dashboard">
            <Button variant="ghost">Sign In</Button>
          </Link>
          <Link href="/dashboard">
            <Button variant="champagne">Launch App</Button>
          </Link>
        </div>
      </header>

      {/* Hero Section */}
      <main className="my-20 space-y-12 text-center md:text-left">
        <div className="space-y-6 max-w-3xl">
          <Badge variant="champagne" className="gap-1.5 py-1.5 px-4 text-sm">
            <ShieldCheck className="w-4 h-4" /> Evidence-Based Intelligence Platform
          </Badge>

          <h1 className="text-4xl md:text-6xl font-extrabold tracking-tight leading-tight">
            The Executive Career Operating System.
          </h1>

          <p className="text-lg md:text-xl text-muted-foreground leading-relaxed">
            ProjectEcho replaces traditional resume inflation with deterministic, evidence-backed competency verification and explainable AI reasoning.
          </p>

          <div className="flex flex-col sm:flex-row items-center gap-4 pt-4">
            <Link href="/dashboard">
              <Button size="lg" variant="champagne" className="gap-2 w-full sm:w-auto">
                Explore Executive Dashboard <ArrowRight className="w-5 h-5" />
              </Button>
            </Link>
            <Link href="/passport">
              <Button size="lg" variant="outline" className="w-full sm:w-auto">
                Initialize Passport
              </Button>
            </Link>
          </div>
        </div>

        {/* Feature Cards Grid */}
        <div className="grid grid-cols-1 md:grid-cols-3 gap-6 pt-12">
          <Card champagneBorder>
            <div className="space-y-3">
              <CheckCircle2 className="w-8 h-8 text-accent" />
              <h3 className="text-lg font-bold">Immutable Passports</h3>
              <p className="text-sm text-muted-foreground">
                Cryptographically verifiable career identity storing proof-backed skills and credentials.
              </p>
            </div>
          </Card>

          <Card champagneBorder>
            <div className="space-y-3">
              <BrainCircuit className="w-8 h-8 text-accent" />
              <h3 className="text-lg font-bold">Rule Engine Authority</h3>
              <p className="text-sm text-muted-foreground">
                Deterministic decision graphs replacing black-box AI with auditable evaluation rules.
              </p>
            </div>
          </Card>

          <Card champagneBorder>
            <div className="space-y-3">
              <Award className="w-8 h-8 text-accent" />
              <h3 className="text-lg font-bold">Explainable Reasoning</h3>
              <p className="text-sm text-muted-foreground">
                Automated Reasoning Cards offering transparent auditability for candidate readiness.
              </p>
            </div>
          </Card>
        </div>
      </main>

      {/* Footer */}
      <footer className="border-t border-border pt-8 flex items-center justify-between text-xs text-muted-foreground">
        <p>&copy; {new Date().getFullYear()} ProjectEcho Engineering Organization. All rights reserved.</p>
        <span className="font-mono">Backend RC1 Verified</span>
      </footer>
    </div>
  );
}
