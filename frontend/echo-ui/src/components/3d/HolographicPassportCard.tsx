'use client';

import { useState, useRef, MouseEvent } from 'react';
import { ShieldCheck, CheckCircle2, QrCode, Sparkles, Lock, Cpu, Globe } from 'lucide-react';
import { Badge } from '@/components/ui/Badge';
import { Button } from '@/components/ui/Button';

interface PassportProps {
  id: string;
  name: string;
  email: string;
  jobTitle: string;
  tier?: string;
  verifiedDate?: string;
}

export default function HolographicPassportCard({
  passport,
  onOpenQR,
}: {
  passport?: PassportProps;
  onOpenQR?: () => void;
}) {
  const cardRef = useRef<HTMLDivElement>(null);
  const [rotateX, setRotateX] = useState(0);
  const [rotateY, setRotateY] = useState(0);
  const [glareX, setGlareX] = useState(50);
  const [glareY, setGlareY] = useState(50);

  const data = passport || {
    id: 'f2706538-f134-4c58-8825-4ee944a10052',
    name: 'Jane Doe',
    email: 'jane.doe@enterprise.io',
    jobTitle: 'Principal Distributed Systems Architect',
    tier: 'Tier 4 Sovereign Proof',
    verifiedDate: '2026-08-08',
  };

  const handleMouseMove = (e: MouseEvent<HTMLDivElement>) => {
    const card = cardRef.current;
    if (!card) return;

    const rect = card.getBoundingClientRect();
    const x = e.clientX - rect.left;
    const y = e.clientY - rect.top;

    const centerX = rect.width / 2;
    const centerY = rect.height / 2;

    const rotX = -((y - centerY) / centerY) * 12;
    const rotY = ((x - centerX) / centerX) * 12;

    setRotateX(rotX);
    setRotateY(rotY);
    setGlareX((x / rect.width) * 100);
    setGlareY((y / rect.height) * 100);
  };

  const handleMouseLeave = () => {
    setRotateX(0);
    setRotateY(0);
  };

  return (
    <div
      style={{ perspective: '1200px' }}
      className="w-full flex items-center justify-center p-4"
    >
      <div
        ref={cardRef}
        onMouseMove={handleMouseMove}
        onMouseLeave={handleMouseLeave}
        style={{
          transform: `rotateX(${rotateX}deg) rotateY(${rotateY}deg)`,
          transition: 'transform 0.15s ease-out',
        }}
        className="relative w-full max-w-lg rounded-2xl bg-gradient-to-br from-slate-900 via-slate-950 to-neutral-950 p-7 text-foreground shadow-2xl border border-amber-500/40 overflow-hidden cursor-pointer group"
      >
        {/* Holographic Specular Glare Overlay */}
        <div
          style={{
            background: `radial-gradient(circle at ${glareX}% ${glareY}%, rgba(212, 175, 55, 0.25) 0%, rgba(6, 182, 212, 0.15) 30%, transparent 60%)`,
          }}
          className="absolute inset-0 pointer-events-none transition-opacity duration-300 opacity-90 group-hover:opacity-100"
        />

        {/* Shimmer Border Light */}
        <div className="absolute inset-0 rounded-2xl bg-gradient-to-r from-transparent via-amber-400/20 to-transparent -translate-x-full group-hover:animate-shimmer pointer-events-none" />

        {/* Security Watermark Background */}
        <div className="absolute right-4 bottom-2 text-white/5 font-black text-8xl select-none pointer-events-none font-mono">
          ECHO
        </div>

        {/* Card Header */}
        <div className="relative z-10 flex items-start justify-between">
          <div className="flex items-center gap-3">
            <div className="w-12 h-12 rounded-xl bg-gradient-to-br from-amber-400 to-amber-600 flex items-center justify-center text-black font-black text-2xl shadow-lg shadow-amber-500/20">
              E
            </div>
            <div>
              <div className="flex items-center gap-1.5 text-xs font-mono font-bold tracking-widest text-amber-400 uppercase">
                <Sparkles className="w-3.5 h-3.5" /> Project Echo Sovereign Passport
              </div>
              <h3 className="text-xl font-bold text-white tracking-tight mt-0.5">{data.name}</h3>
            </div>
          </div>

          <Badge variant="success" className="gap-1.5 py-1 px-3 bg-emerald-500/20 text-emerald-300 border-emerald-500/40">
            <CheckCircle2 className="w-3.5 h-3.5" /> Verified
          </Badge>
        </div>

        {/* Passport Body */}
        <div className="relative z-10 my-6 space-y-4">
          <div className="p-3.5 rounded-xl bg-slate-900/80 border border-slate-800 backdrop-blur-md">
            <span className="text-[10px] font-mono text-muted-foreground uppercase tracking-wider block">Official Position</span>
            <p className="text-sm font-semibold text-white mt-0.5">{data.jobTitle}</p>
          </div>

          <div className="grid grid-cols-2 gap-3 text-xs font-mono">
            <div className="p-3 rounded-xl bg-slate-900/60 border border-slate-800">
              <span className="text-[10px] text-muted-foreground block">TRUST TIER</span>
              <span className="text-amber-400 font-bold mt-0.5 block">{data.tier}</span>
            </div>
            <div className="p-3 rounded-xl bg-slate-900/60 border border-slate-800">
              <span className="text-[10px] text-muted-foreground block">VERIFIED DATE</span>
              <span className="text-emerald-400 font-bold mt-0.5 block">{data.verifiedDate}</span>
            </div>
          </div>
        </div>

        {/* Card Footer & QR Code Action */}
        <div className="relative z-10 flex items-center justify-between pt-4 border-t border-slate-800/80">
          <div className="flex items-center gap-2 text-xs font-mono text-slate-400">
            <Cpu className="w-4 h-4 text-cyan-400" />
            <span>ID: {data.id.substring(0, 13)}...</span>
          </div>

          <Button
            size="sm"
            variant="outline"
            onClick={onOpenQR}
            className="gap-1.5 text-xs font-mono border-amber-500/40 text-amber-300 hover:bg-amber-500/20"
          >
            <QrCode className="w-4 h-4" /> Cryptographic QR
          </Button>
        </div>
      </div>
    </div>
  );
}
