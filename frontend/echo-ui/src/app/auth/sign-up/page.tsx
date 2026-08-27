'use client';

import { useState } from 'react';
import { useRouter } from 'next/navigation';
import Link from 'next/link';
import { useAuth } from '@/lib/AuthContext';
import { api } from '@/lib/api';
import { Button } from '@/components/ui/Button';
import { Input } from '@/components/ui/Input';
import { Label } from '@/components/ui/Label';

export default function SignUpPage() {
  const router = useRouter();
  const { login } = useAuth();
  
  const [name, setName] = useState('');
  const [email, setEmail] = useState('');
  const [password, setPassword] = useState('');
  const [error, setError] = useState('');
  const [isLoading, setIsLoading] = useState(false);

  const handleSubmit = async (e: React.FormEvent) => {
    e.preventDefault();
    setIsLoading(true);
    setError('');

    try {
      const res = await api.signup({ name, email, password });
      login({ userId: res.userId, name: res.name });
      router.push('/onboarding');
    } catch (err: any) {
      setError('Registration failed. Please try a different email.');
    } finally {
      setIsLoading(false);
    }
  };

  return (
    <div className="animate-in fade-in slide-in-from-bottom-4 duration-500">
      <div className="mb-10">
        <h2 className="text-3xl font-bold tracking-tight text-foreground mb-2">Create an account</h2>
        <p className="text-sm text-muted-foreground font-mono">Initialize your Career Passport</p>
      </div>

      <form onSubmit={handleSubmit} className="space-y-6">
        <div className="space-y-2">
          <Label htmlFor="name" className="text-muted-foreground">Full Name</Label>
          <Input 
            id="name" 
            type="text" 
            required 
            value={name}
            onChange={(e) => setName(e.target.value)}
            className="bg-card border-border text-foreground placeholder:text-muted-foreground/60 focus-visible:ring-accent/50"
            placeholder="Ada Lovelace"
          />
        </div>

        <div className="space-y-2">
          <Label htmlFor="email" className="text-muted-foreground">Email Address</Label>
          <Input 
            id="email" 
            type="email" 
            required 
            value={email}
            onChange={(e) => setEmail(e.target.value)}
            className="bg-card border-border text-foreground placeholder:text-muted-foreground/60 focus-visible:ring-accent/50"
            placeholder="you@example.com"
          />
        </div>
        
        <div className="space-y-2">
          <Label htmlFor="password" className="text-muted-foreground">Password</Label>
          <Input 
            id="password" 
            type="password" 
            required 
            value={password}
            onChange={(e) => setPassword(e.target.value)}
            className="bg-card border-border text-foreground focus-visible:ring-accent/50"
          />
        </div>

        {error && (
          <div className="p-3 rounded-lg bg-red-500/10 border border-red-500/20 text-red-400 text-sm font-medium">
            {error}
          </div>
        )}

        <Button 
          type="submit" 
          disabled={isLoading}
          className="w-full bg-accent text-accent-foreground hover:bg-accent/90 transition-all font-semibold"
        >
          {isLoading ? 'Creating account...' : 'Continue to Onboarding'}
        </Button>

        <div className="text-center mt-6">
          <span className="text-sm text-muted-foreground">
            Already have an account?{' '}
            <Link href="/auth/sign-in" className="text-accent hover:underline font-medium">
              Sign in
            </Link>
          </span>
        </div>
      </form>
    </div>
  );
}
