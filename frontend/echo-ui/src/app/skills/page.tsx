'use client';

import { useState } from 'react';
import { useQuery, useMutation, useQueryClient } from '@tanstack/react-query';
import { api } from '@/lib/api';
import AppLayout from '@/components/AppLayout';
import { Card, CardHeader, CardTitle, CardDescription, CardContent } from '@/components/ui/Card';
import { Button } from '@/components/ui/Button';
import { Input } from '@/components/ui/Input';
import { Badge } from '@/components/ui/Badge';
import { Zap, Plus, Sparkles, Filter, CheckCircle2 } from 'lucide-react';
import { toast } from 'sonner';
import ThreeSkillGalaxy from '@/components/3d/ThreeSkillGalaxy';

const CATEGORIES = ['All', 'Backend', 'Architecture', 'Frontend', 'Security', 'Database', 'Intelligence'];

export default function SkillsPage() {
  const queryClient = useQueryClient();
  const [skillName, setSkillName] = useState('');
  const [selectedCategory, setSelectedCategory] = useState('Backend');
  const [activeFilter, setActiveFilter] = useState('All');

  const { data: skills, isLoading } = useQuery({
    queryKey: ['skills'],
    queryFn: () => api.getSkills(),
  });

  const createMutation = useMutation({
    mutationFn: api.createSkill,
    onSuccess: (data) => {
      queryClient.invalidateQueries({ queryKey: ['skills'] });
      setSkillName('');
      toast.success(`Skill "${data.name}" added to Taxonomy Galaxy!`);
    },
    onError: (err: Error) => {
      toast.error(`Failed to register skill: ${err.message}`);
    },
  });

  const handleCreateSkill = (e: React.FormEvent) => {
    e.preventDefault();
    if (!skillName) {
      toast.error('Skill name is required');
      return;
    }
    createMutation.mutate({ name: skillName, category: selectedCategory });
  };

  const filteredSkills = skills?.content.filter((s) =>
    activeFilter === 'All' ? true : s.category.toLowerCase() === activeFilter.toLowerCase()
  );

  return (
    <AppLayout>
      <div className="space-y-8 max-w-5xl">
        {/* Header */}
        <div className="flex flex-col md:flex-row md:items-center justify-between gap-4">
          <div>
            <div className="flex items-center gap-2">
              <h1 className="text-3xl font-extrabold tracking-tight text-white">Skills Taxonomy Galaxy</h1>
              <Badge variant="champagne" className="text-[10px]">3D WebGL Constellation</Badge>
            </div>
            <p className="text-muted-foreground text-sm mt-1">
              Standardized skill ontologies, relational competency graphs, and verified trust nodes.
            </p>
          </div>
        </div>

        {/* 3D WebGL Skill Universe Canvas */}
        <div className="space-y-3">
          <div className="flex items-center justify-between px-2">
            <span className="text-xs font-mono text-muted-foreground uppercase tracking-wider font-semibold">
              Interactive 3D Competency Orbit (Rotate & Inspect)
            </span>
            <span className="text-xs font-mono text-cyan-400">
              ● 60 FPS WebGL
            </span>
          </div>

          <ThreeSkillGalaxy />
        </div>

        {/* Category Filters */}
        <div className="flex flex-wrap items-center gap-2 pt-2">
          <span className="text-xs font-mono text-muted-foreground mr-2 flex items-center gap-1">
            <Filter className="w-3.5 h-3.5" /> Filter by Domain:
          </span>
          {CATEGORIES.map((cat) => (
            <button
              key={cat}
              onClick={() => setActiveFilter(cat)}
              className={`px-3 py-1 rounded-full text-xs font-mono font-medium transition-all ${
                activeFilter === cat
                  ? 'bg-amber-500 text-black font-bold shadow-md shadow-amber-500/20'
                  : 'bg-slate-900 text-muted-foreground hover:text-white border border-border'
              }`}
            >
              {cat}
            </button>
          ))}
        </div>

        {/* Two Column Grid: Form & List */}
        <div className="grid grid-cols-1 md:grid-cols-12 gap-8">
          {/* Create Skill Form */}
          <Card champagneBorder className="md:col-span-5 p-6">
            <CardHeader className="p-0 pb-4">
              <CardTitle className="flex items-center gap-2 text-base font-bold text-white">
                <Plus className="w-4 h-4 text-amber-400" /> Register Skill in Ontology
              </CardTitle>
              <CardDescription className="text-xs">
                Add a new verified capability to the platform taxonomy
              </CardDescription>
            </CardHeader>
            <CardContent className="p-0">
              <form onSubmit={handleCreateSkill} className="space-y-4">
                <Input
                  label="Standard Competency Name"
                  placeholder="e.g. Distributed Consensus (Raft)"
                  value={skillName}
                  onChange={(e) => setSkillName(e.target.value)}
                />

                <div className="space-y-1.5">
                  <label className="text-xs font-mono text-muted-foreground uppercase font-semibold">Domain Category</label>
                  <select
                    value={selectedCategory}
                    onChange={(e) => setSelectedCategory(e.target.value)}
                    className="w-full rounded-xl bg-slate-900 border border-border p-2.5 text-xs text-white outline-none focus:border-amber-500/60"
                  >
                    {CATEGORIES.filter((c) => c !== 'All').map((c) => (
                      <option key={c} value={c}>
                        {c}
                      </option>
                    ))}
                  </select>
                </div>

                <Button
                  type="submit"
                  variant="champagne"
                  className="w-full font-bold shadow-lg shadow-amber-500/20"
                  disabled={createMutation.isPending}
                >
                  {createMutation.isPending ? 'Registering...' : 'Add to Taxonomy Galaxy'}
                </Button>
              </form>
            </CardContent>
          </Card>

          {/* Skill List */}
          <Card champagneBorder className="md:col-span-7 p-6">
            <CardHeader className="p-0 pb-4">
              <CardTitle className="flex items-center gap-2 text-base font-bold text-white">
                <Zap className="w-4 h-4 text-cyan-400" /> Active Taxonomy Directory
              </CardTitle>
              <CardDescription className="text-xs">
                Ontological nodes registered in the database
              </CardDescription>
            </CardHeader>
            <CardContent className="p-0">
              <div className="space-y-3 max-h-96 overflow-y-auto pr-1">
                {filteredSkills?.map((s) => (
                  <div
                    key={s.id}
                    className="p-3.5 rounded-xl bg-slate-900/60 border border-border flex items-center justify-between hover:border-cyan-500/40 transition-all"
                  >
                    <div>
                      <h4 className="font-semibold text-sm text-white">{s.name}</h4>
                      <span className="text-[10px] text-muted-foreground font-mono">{s.category}</span>
                    </div>
                    <Badge variant="champagne" className="text-[10px] gap-1">
                      <CheckCircle2 className="w-3 h-3" /> Tier 4 Match
                    </Badge>
                  </div>
                ))}
              </div>
            </CardContent>
          </Card>
        </div>
      </div>
    </AppLayout>
  );
}
