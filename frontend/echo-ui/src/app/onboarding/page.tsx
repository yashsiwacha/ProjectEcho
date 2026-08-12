'use client';

import { useState } from 'react';
import { Button } from '@/components/ui/Button';
import { UserCheck, ArrowRight, ArrowLeft, Target, Rocket, Briefcase } from 'lucide-react';
import { useRouter } from 'next/navigation';
import { Card } from '@/components/ui/Card';

export default function OnboardingPage() {
  const router = useRouter();
  const [step, setStep] = useState(1);
  const [role, setRole] = useState<string | null>(null);

  const handleNext = () => {
    if (step < 3) setStep(step + 1);
    else router.push('/dashboard');
  };

  const handleBack = () => {
    if (step > 1) setStep(step - 1);
  };

  return (
    <div className="min-h-screen bg-black text-white flex flex-col items-center justify-center p-6 relative overflow-hidden">
      {/* Dynamic Background Glow */}
      <div className={`absolute top-1/2 left-1/2 -translate-x-1/2 -translate-y-1/2 w-[800px] h-[800px] rounded-full blur-[120px] opacity-30 transition-colors duration-1000 ${
        step === 1 ? 'bg-amber-500' : step === 2 ? 'bg-cyan-500' : 'bg-emerald-500'
      }`} />

      <div className="w-full max-w-3xl z-10">
        {/* Progress Tracker */}
        <div className="flex items-center justify-center gap-4 mb-12">
          {[1, 2, 3].map((i) => (
            <div key={i} className="flex items-center gap-4">
              <div
                className={`w-10 h-10 rounded-full flex items-center justify-center font-bold font-mono transition-colors ${
                  step === i
                    ? 'bg-white text-black ring-4 ring-white/20'
                    : step > i
                    ? 'bg-white/20 text-white'
                    : 'bg-white/5 text-white/30 border border-white/10'
                }`}
              >
                {i}
              </div>
              {i < 3 && (
                <div className={`w-16 h-0.5 rounded-full ${step > i ? 'bg-white/50' : 'bg-white/10'}`} />
              )}
            </div>
          ))}
        </div>

        {/* Step 1: Welcome & Role */}
        {step === 1 && (
          <div className="animate-in fade-in slide-in-from-bottom-8 duration-500 text-center">
            <h1 className="text-4xl font-extrabold mb-4 tracking-tight">Welcome to ProjectEcho</h1>
            <p className="text-zinc-400 text-lg mb-10 max-w-xl mx-auto">
              The world's first deterministic Career Intelligence OS. How do you plan to use the platform?
            </p>
            
            <div className="grid grid-cols-1 md:grid-cols-2 gap-6 max-w-2xl mx-auto text-left">
              <Card
                className={`p-6 cursor-pointer border-2 transition-all hover:scale-105 ${role === 'candidate' ? 'border-amber-500 bg-amber-500/10' : 'border-border bg-slate-900/50'}`}
                onClick={() => setRole('candidate')}
              >
                <div className="w-12 h-12 bg-amber-500/20 text-amber-500 rounded-xl flex items-center justify-center mb-4">
                  <Target className="w-6 h-6" />
                </div>
                <h3 className="text-xl font-bold mb-2">Candidate</h3>
                <p className="text-zinc-400 text-sm">I want to build my cryptographic career passport and prove my skills.</p>
              </Card>

              <Card
                className={`p-6 cursor-pointer border-2 transition-all hover:scale-105 ${role === 'recruiter' ? 'border-cyan-500 bg-cyan-500/10' : 'border-border bg-slate-900/50'}`}
                onClick={() => setRole('recruiter')}
              >
                <div className="w-12 h-12 bg-cyan-500/20 text-cyan-500 rounded-xl flex items-center justify-center mb-4">
                  <Briefcase className="w-6 h-6" />
                </div>
                <h3 className="text-xl font-bold mb-2">Recruiter / Admin</h3>
                <p className="text-zinc-400 text-sm">I want to evaluate candidates based on deterministic evidence.</p>
              </Card>
            </div>
          </div>
        )}

        {/* Step 2: Interests / Sync */}
        {step === 2 && (
          <div className="animate-in fade-in slide-in-from-bottom-8 duration-500 text-center">
            <h1 className="text-4xl font-extrabold mb-4 tracking-tight">Sync Your Data</h1>
            <p className="text-zinc-400 text-lg mb-10 max-w-xl mx-auto">
              ProjectEcho works best when it can analyze your existing professional footprint.
            </p>
            
            <div className="max-w-md mx-auto space-y-4">
              <div className="p-4 rounded-xl border border-border bg-slate-900/50 flex items-center justify-between">
                <div className="flex items-center gap-3">
                  <div className="w-10 h-10 bg-[#0A66C2]/20 text-[#0A66C2] rounded-lg flex items-center justify-center">
                    <UserCheck className="w-5 h-5" />
                  </div>
                  <div className="text-left">
                    <h4 className="font-semibold">LinkedIn Profile</h4>
                    <p className="text-xs text-zinc-400">Import work history</p>
                  </div>
                </div>
                <Button variant="outline" size="sm">Connect</Button>
              </div>

              <div className="p-4 rounded-xl border border-border bg-slate-900/50 flex items-center justify-between">
                <div className="flex items-center gap-3">
                  <div className="w-10 h-10 bg-white/10 text-white rounded-lg flex items-center justify-center">
                    <Rocket className="w-5 h-5" />
                  </div>
                  <div className="text-left">
                    <h4 className="font-semibold">GitHub</h4>
                    <p className="text-xs text-zinc-400">Import repositories & commits</p>
                  </div>
                </div>
                <Button variant="outline" size="sm">Connect</Button>
              </div>
            </div>
          </div>
        )}

        {/* Step 3: Completion */}
        {step === 3 && (
          <div className="animate-in fade-in slide-in-from-bottom-8 duration-500 text-center">
            <div className="w-24 h-24 bg-emerald-500/20 text-emerald-500 rounded-3xl flex items-center justify-center mx-auto mb-6 border border-emerald-500/30">
              <Rocket className="w-12 h-12" />
            </div>
            <h1 className="text-4xl font-extrabold mb-4 tracking-tight">You're all set!</h1>
            <p className="text-zinc-400 text-lg mb-10 max-w-xl mx-auto">
              Your profile is initialized. Welcome to the future of deterministic career growth.
            </p>
          </div>
        )}

        {/* Navigation Controls */}
        <div className="flex items-center justify-between mt-12 max-w-2xl mx-auto">
          <Button 
            variant="ghost" 
            onClick={handleBack} 
            disabled={step === 1}
            className={`gap-2 ${step === 1 ? 'opacity-0' : 'opacity-100'}`}
          >
            <ArrowLeft className="w-4 h-4" /> Back
          </Button>
          
          <Button 
            variant="champagne" 
            onClick={handleNext} 
            disabled={step === 1 && !role}
            className="gap-2 px-8"
          >
            {step === 3 ? 'Launch Dashboard' : 'Continue'} 
            {step !== 3 && <ArrowRight className="w-4 h-4" />}
          </Button>
        </div>
      </div>
    </div>
  );
}
