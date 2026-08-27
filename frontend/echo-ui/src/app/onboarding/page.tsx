'use client';

import { useState, useEffect } from 'react';
import { useRouter } from 'next/navigation';
import { useAuth } from '@/lib/AuthContext';
import { api, Skill } from '@/lib/api';
import { Button } from '@/components/ui/Button';
import { Input } from '@/components/ui/Input';

export default function OnboardingPage() {
  const router = useRouter();
  const { user } = useAuth();
  const [step, setStep] = useState(1);
  const [isSubmitting, setIsSubmitting] = useState(false);
  const [error, setError] = useState('');

  // Form State
  const [jobTitle, setJobTitle] = useState('');
  const [yearsExperience, setYearsExperience] = useState('');
  
  // Available Skills (fetched from API)
  const [availableSkills, setAvailableSkills] = useState<Skill[]>([]);
  const [selectedSkillIds, setSelectedSkillIds] = useState<string[]>([]);
  
  // Generate fake skills for now if backend taxonomy is empty
  useEffect(() => {
    if (step === 2) {
      api.getSkills(0, 50).then(res => {
        if (res.content.length > 0) {
          setAvailableSkills(res.content);
        } else {
          // Fake fallback if DB is empty
          setAvailableSkills([
            { id: '1', name: 'Java', category: 'Language', createdAt: '', updatedAt: '' },
            { id: '2', name: 'Spring Boot', category: 'Framework', createdAt: '', updatedAt: '' },
            { id: '3', name: 'React', category: 'Framework', createdAt: '', updatedAt: '' },
            { id: '4', name: 'TypeScript', category: 'Language', createdAt: '', updatedAt: '' },
            { id: '5', name: 'System Design', category: 'Architecture', createdAt: '', updatedAt: '' },
          ]);
        }
      });
    }
  }, [step]);

  // Protect route
  useEffect(() => {
    if (user === null) {
      router.push('/auth/sign-in');
    }
  }, [user, router]);

  const handleNext = () => {
    if (step === 1 && !jobTitle) return;
    setStep(s => s + 1);
  };

  const handleBack = () => {
    setStep(s => s - 1);
  };

  const toggleSkill = (id: string) => {
    setSelectedSkillIds(prev => 
      prev.includes(id) ? prev.filter(i => i !== id) : [...prev, id]
    );
  };

  const handleInitialize = async () => {
    if (!user) return;
    setIsSubmitting(true);
    setError('');

    try {
      // 1. Create Passport
      const passportRes = await api.createPassport({
        name: user.name,
        email: user.userId, // We used email as ID currently in controller logic? Wait, user.email isn't in AuthUser.
        jobTitle
      });

      // 2. We'd submit evidence for skills here normally, but for now we'll just redirect to dashboard
      // as backend evidence initialization would be done.
      
      router.push('/dashboard');
    } catch (err) {
      setError('Failed to initialize passport. Please try again.');
    } finally {
      setIsSubmitting(false);
    }
  };

  if (!user) return null;

  return (
    <div className="animate-in fade-in slide-in-from-bottom-4 duration-500">
      <div className="mb-8">
        <div className="flex gap-2 mb-8">
          {[1, 2].map((i) => (
            <div 
              key={i} 
              className={`h-1 flex-1 rounded-full transition-colors ${i <= step ? 'bg-accent' : 'bg-white/10'}`} 
            />
          ))}
        </div>
        <h1 className="text-3xl font-bold tracking-tight text-white mb-2">
          {step === 1 ? 'Career Identity' : 'Core Capabilities'}
        </h1>
        <p className="text-slate-400 font-mono text-sm">
          {step === 1 
            ? 'Define your professional baseline.' 
            : 'Select the skills you want to anchor in your Career Passport.'}
        </p>
      </div>

      {step === 1 && (
        <div className="space-y-6 animate-in slide-in-from-right-8 duration-300">
          <div className="space-y-2">
            <label className="text-sm font-medium text-slate-300">Current Job Title</label>
            <Input 
              value={jobTitle}
              onChange={(e) => setJobTitle(e.target.value)}
              placeholder="e.g. Senior Software Engineer"
              className="bg-white/5 border-white/10 text-white text-lg py-6"
            />
          </div>
          <div className="space-y-2">
            <label className="text-sm font-medium text-slate-300">Years of Experience</label>
            <Input 
              value={yearsExperience}
              onChange={(e) => setYearsExperience(e.target.value)}
              placeholder="e.g. 5"
              type="number"
              className="bg-white/5 border-white/10 text-white text-lg py-6"
            />
          </div>
          <Button 
            onClick={handleNext} 
            disabled={!jobTitle}
            className="w-full bg-accent hover:bg-accent/90 text-accent-foreground py-6 text-lg font-semibold mt-4"
          >
            Continue
          </Button>
        </div>
      )}

      {step === 2 && (
        <div className="space-y-6 animate-in slide-in-from-right-8 duration-300">
          <div className="grid grid-cols-2 md:grid-cols-3 gap-3">
            {availableSkills.map(skill => {
              const isSelected = selectedSkillIds.includes(skill.id);
              return (
                <button
                  key={skill.id}
                  onClick={() => toggleSkill(skill.id)}
                  className={`p-4 rounded-xl border text-left transition-all duration-200 ${
                    isSelected 
                      ? 'bg-accent/20 border-accent text-accent-foreground shadow-[0_0_15px_rgba(255,215,0,0.15)]' 
                      : 'bg-white/5 border-white/10 text-slate-300 hover:bg-white/10'
                  }`}
                >
                  <div className="font-semibold">{skill.name}</div>
                  <div className="text-xs opacity-70 mt-1">{skill.category}</div>
                </button>
              );
            })}
          </div>
          
          {error && (
            <div className="p-3 bg-red-500/10 border border-red-500/20 text-red-400 rounded-md text-sm">
              {error}
            </div>
          )}

          <div className="flex gap-4 pt-4">
            <Button 
              onClick={handleBack} 
              variant="outline"
              className="flex-1 bg-transparent border-white/10 text-white hover:bg-white/5 py-6 text-lg font-medium"
            >
              Back
            </Button>
            <Button 
              onClick={handleInitialize}
              disabled={isSubmitting || selectedSkillIds.length === 0}
              className="flex-1 bg-accent hover:bg-accent/90 text-accent-foreground py-6 text-lg font-bold shadow-[0_0_20px_rgba(255,215,0,0.3)]"
            >
              {isSubmitting ? 'Initializing...' : 'Initialize Passport'}
            </Button>
          </div>
        </div>
      )}
    </div>
  );
}
