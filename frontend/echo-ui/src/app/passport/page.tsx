'use client';

import { useState } from 'react';
import { useQuery, useMutation, useQueryClient } from '@tanstack/react-query';
import { api } from '@/lib/api';
import AppLayout from '@/components/AppLayout';
import { Card, CardHeader, CardTitle, CardDescription, CardContent } from '@/components/ui/Card';
import { Button } from '@/components/ui/Button';
import { Input } from '@/components/ui/Input';
import { Badge } from '@/components/ui/Badge';
import { Skeleton } from '@/components/ui/Skeleton';
import { UserCheck, Plus, CheckCircle2, AlertCircle } from 'lucide-react';
import { toast } from 'sonner';

export default function PassportPage() {
  const queryClient = useQueryClient();
  const [name, setName] = useState('');
  const [email, setEmail] = useState('');
  const [jobTitle, setJobTitle] = useState('');
  const [errorMsg, setErrorMsg] = useState('');

  const { data: passports, isLoading } = useQuery({
    queryKey: ['passports'],
    queryFn: () => api.getPassports(),
  });

  const createMutation = useMutation({
    mutationFn: api.createPassport,
    onSuccess: (data) => {
      queryClient.invalidateQueries({ queryKey: ['passports'] });
      setName('');
      setEmail('');
      setJobTitle('');
      setErrorMsg('');
      toast.success(`Career Passport initialized for ${data.name}`);
    },
    onError: (err: Error) => {
      setErrorMsg(err.message);
      toast.error(`Failed to initialize passport: ${err.message}`);
    },
  });

  const handleSubmit = (e: React.FormEvent) => {
    e.preventDefault();
    if (!name || !email || !jobTitle) {
      setErrorMsg('All fields are required');
      toast.error('Please fill out all required fields');
      return;
    }
    createMutation.mutate({ name, email, jobTitle });
  };

  return (
    <AppLayout>
      <div className="space-y-8 max-w-5xl">
        <div>
          <h1 className="text-3xl font-bold tracking-tight">Career Passport</h1>
          <p className="text-muted-foreground mt-1">Manage your immutable career identity and verified competency profile.</p>
        </div>

        <div className="grid grid-cols-1 md:grid-cols-3 gap-8">
          {/* Create Passport Form */}
          <Card className="md:col-span-1">
            <CardHeader>
              <CardTitle className="flex items-center gap-2">
                <Plus className="w-5 h-5 text-accent" /> Initialize Passport
              </CardTitle>
              <CardDescription>Create a new verified career identity</CardDescription>
            </CardHeader>
            <CardContent>
              <form onSubmit={handleSubmit} className="space-y-4">
                <Input
                  label="Full Name"
                  placeholder="e.g. Jane Doe"
                  value={name}
                  onChange={(e) => setName(e.target.value)}
                />
                <Input
                  label="Email Address"
                  type="email"
                  placeholder="e.g. jane@example.com"
                  value={email}
                  onChange={(e) => setEmail(e.target.value)}
                />
                <Input
                  label="Job Title"
                  placeholder="e.g. Principal Architect"
                  value={jobTitle}
                  onChange={(e) => setJobTitle(e.target.value)}
                />

                {errorMsg && (
                  <div className="p-3 rounded-lg bg-destructive/10 border border-destructive/20 text-destructive text-xs flex items-center gap-2">
                    <AlertCircle className="w-4 h-4 flex-shrink-0" />
                    {errorMsg}
                  </div>
                )}

                <Button
                  type="submit"
                  variant="champagne"
                  className="w-full"
                  disabled={createMutation.isPending}
                >
                  {createMutation.isPending ? 'Initializing...' : 'Initialize Passport'}
                </Button>
              </form>
            </CardContent>
          </Card>

          {/* Passports List */}
          <Card className="md:col-span-2">
            <CardHeader>
              <CardTitle className="flex items-center gap-2">
                <UserCheck className="w-5 h-5 text-accent" /> Registered Passports
              </CardTitle>
              <CardDescription>Active verified career passports in the system</CardDescription>
            </CardHeader>
            <CardContent>
              {isLoading ? (
                <div className="space-y-3">
                  <Skeleton className="h-20 w-full" />
                  <Skeleton className="h-20 w-full" />
                </div>
              ) : passports?.content.length === 0 ? (
                <div className="py-8 text-center text-sm text-muted-foreground">No passports created yet. Use the form to initialize one.</div>
              ) : (
                <div className="space-y-4">
                  {passports?.content.map((passport) => (
                    <div
                      key={passport.id}
                      className="p-5 rounded-xl bg-card border border-border flex items-center justify-between hover:border-accent/50 transition-all"
                    >
                      <div className="space-y-1">
                        <div className="flex items-center gap-2">
                          <h3 className="font-semibold text-base">{passport.name}</h3>
                          <Badge variant="success" className="gap-1 text-[10px]">
                            <CheckCircle2 className="w-3 h-3" /> Verified
                          </Badge>
                        </div>
                        <p className="text-sm text-muted-foreground">{passport.jobTitle}</p>
                        <p className="text-xs text-muted-foreground font-mono">{passport.email}</p>
                      </div>
                      <div className="text-right">
                        <span className="text-[10px] text-muted-foreground block">ID</span>
                        <span className="text-xs font-mono text-foreground font-medium">{passport.id.substring(0, 8)}...</span>
                      </div>
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
