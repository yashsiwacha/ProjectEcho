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
        className="relative w-full max-w-[500px] aspect-[1.586/1] rounded-2xl bg-gradient-to-br from-slate-900 via-slate-950 to-neutral-950 p-6 text-foreground shadow-2xl border border-border overflow-hidden cursor-pointer group flex flex-col justify-between"
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
            <div className="w-12 h-12 rounded-xl bg-gradient-to-br from-amber-400 to-amber-600 flex items-center justify-center text-amber-950 font-black text-2xl shadow-lg shadow-sm">
              E
            </div>
            <div>
              <div className="flex items-center gap-1.5 text-xs font-mono font-bold tracking-widest text-slate-300 uppercase">
                <Sparkles className="w-3.5 h-3.5" /> Project Echo Sovereign Passport
              </div>
              <h3 className="text-xl font-bold text-white tracking-tight mt-0.5">{data.name}</h3>
            </div>
          </div>

          <Badge variant="success" className="gap-1.5 py-1 px-3 bg-slate-800 text-white border-slate-700">
            <CheckCircle2 className="w-3.5 h-3.5" /> Verified
          </Badge>
        </div>

        {/* Passport Body */}
        <div className="relative z-10 space-y-2">
          <div className="flex gap-2 w-full">
            <div className="flex-1 p-2.5 rounded-xl bg-white/5 border border-white/10 backdrop-blur-md min-w-0">
              <span className="text-[9px] font-mono text-slate-400 uppercase tracking-wider block truncate">Official Position</span>
              <p className="text-sm font-semibold text-white mt-0.5 truncate">{data.jobTitle}</p>
            </div>
            
            <div className="flex-1 p-2.5 rounded-xl bg-white/5 border border-white/10 backdrop-blur-md min-w-0">
              <span className="text-[9px] font-mono text-slate-400 uppercase tracking-wider block truncate">Verified Email Signature</span>
              <p className="text-xs font-semibold text-white mt-1 flex items-center gap-1.5 truncate">
                <Globe className="w-3.5 h-3.5 text-slate-400 shrink-0" /> <span className="truncate">{data.email}</span>
              </p>
            </div>
          </div>

          <div className="grid grid-cols-4 gap-2 text-[10px] font-mono">
            <div className="p-2.5 rounded-xl bg-white/5 border border-white/10 min-w-0 shadow-inner">
              <span className="text-[8px] text-slate-400 block truncate">TRUST TIER</span>
              <span className="text-white font-bold mt-1 block truncate">{data.tier?.split(' ')[0] || data.tier}</span>
            </div>
            <div className="p-2.5 rounded-xl bg-white/5 border border-white/10 min-w-0 shadow-inner">
              <span className="text-[8px] text-slate-400 block truncate">ISSUED</span>
              <span className="text-white font-bold mt-1 block truncate">{data.verifiedDate}</span>
            </div>
            <div className="p-2.5 rounded-xl bg-white/5 border border-white/10 min-w-0 shadow-inner">
              <span className="text-[8px] text-slate-400 block truncate">NETWORK STATUS</span>
              <span className="text-emerald-400 font-bold mt-1 flex items-center gap-1 truncate">
                <span className="w-1.5 h-1.5 rounded-full bg-emerald-400 animate-pulse shrink-0" /> ACTIVE
              </span>
            </div>
            <div className="p-2.5 rounded-xl bg-white/5 border border-white/10 min-w-0 shadow-inner">
              <span className="text-[8px] text-slate-400 block truncate">CLEARANCE</span>
              <span className="text-white font-bold mt-1 flex items-center gap-1 truncate">
                <Lock className="w-3 h-3 text-slate-400 shrink-0" /> LVL 5
              </span>
            </div>
          </div>
        </div>

        {/* Card Footer & QR Code Action */}
        <div className="relative z-10 flex flex-col gap-2">
          {/* Aesthetic Barcode */}
          <div className="w-full h-3 flex items-center justify-between opacity-30 px-1">
            {[...Array(60)].map((_, i) => (
              <div 
                key={i} 
                className="bg-slate-300 h-full" 
                style={{ width: `${Math.random() * 2 + 1}px`, opacity: Math.random() * 0.5 + 0.5 }} 
              />
            ))}
          </div>

          <div className="flex items-center justify-between pt-2 border-t border-white/10">
            <div className="flex items-center gap-1.5 text-[10px] font-mono text-slate-400">
              <Cpu className="w-3.5 h-3.5 text-slate-300" />
              <span>ID: {data.id.substring(0, 18)}...</span>
            </div>

            <Button
              size="sm"
              variant="outline"
              onClick={onOpenQR}
              className="h-7 px-2.5 gap-1.5 text-[10px] font-mono border-white/20 text-white bg-white/5 hover:bg-white/10"
            >
              <QrCode className="w-3.5 h-3.5" /> QR
            </Button>
          </div>
        </div>
      </div>
    </div>
  );
}
