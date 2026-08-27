'use client';

import * as React from 'react';
import { motion } from 'framer-motion';

export function ReadinessRadar() {
  return (
    <div className="relative w-full aspect-square max-w-[250px] mx-auto flex items-center justify-center">
      <svg viewBox="0 0 100 100" className="w-full h-full overflow-visible">
        {/* Radar grid lines */}
        <polygon points="50,5 95,27 95,73 50,95 5,73 5,27" fill="none" stroke="currentColor" strokeWidth="0.5" className="text-border" />
        <polygon points="50,20 80,35 80,65 50,80 20,65 20,35" fill="none" stroke="currentColor" strokeWidth="0.5" className="text-border opacity-50" />
        <polygon points="50,35 65,42 65,58 50,65 35,58 35,42" fill="none" stroke="currentColor" strokeWidth="0.5" className="text-border opacity-30" />
        
        {/* Crosshairs */}
        <line x1="50" y1="5" x2="50" y2="95" stroke="currentColor" strokeWidth="0.5" className="text-border opacity-50" />
        <line x1="5" y1="27" x2="95" y2="73" stroke="currentColor" strokeWidth="0.5" className="text-border opacity-50" />
        <line x1="95" y1="27" x2="5" y2="73" stroke="currentColor" strokeWidth="0.5" className="text-border opacity-50" />

        {/* Data Polygon with Framer Motion */}
        <motion.polygon 
          initial={{ opacity: 0, scale: 0 }}
          animate={{ opacity: 1, scale: 1 }}
          transition={{ duration: 1.2, type: 'spring' }}
          points="50,15 85,32 70,60 50,85 20,60 15,35" 
          fill="currentColor" 
          stroke="currentColor" 
          strokeWidth="2"
          className="text-primary fill-primary/20 drop-shadow-md" 
        />
        
        {/* Data points */}
        <circle cx="50" cy="15" r="2.5" className="fill-primary" />
        <circle cx="85" cy="32" r="2.5" className="fill-primary" />
        <circle cx="70" cy="60" r="2.5" className="fill-primary" />
        <circle cx="50" cy="85" r="2.5" className="fill-primary" />
        <circle cx="20" cy="60" r="2.5" className="fill-primary" />
        <circle cx="15" cy="35" r="2.5" className="fill-primary" />
      </svg>
    </div>
  );
}
