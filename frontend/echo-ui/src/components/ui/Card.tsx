'use client';

import React, { useRef, useState } from 'react';
import { cn } from '@/lib/utils';
import { motion, useMotionValue } from 'framer-motion';

interface CardProps extends React.HTMLAttributes<HTMLDivElement> {
  champagneBorder?: boolean;
}

export const Card = React.forwardRef<HTMLDivElement, CardProps>(
  ({ className, champagneBorder, children, ...props }, ref) => {
    const cardRef = useRef<HTMLDivElement>(null);
    const [isHovered, setIsHovered] = useState(false);

    // Dynamic mouse position for border lighting / glow tracking
    const mouseX = useMotionValue(0);
    const mouseY = useMotionValue(0);

    const handleMouseMove = (e: React.MouseEvent<HTMLDivElement>) => {
      if (!cardRef.current) return;
      const { left, top } = cardRef.current.getBoundingClientRect();
      const x = e.clientX - left;
      const y = e.clientY - top;
      
      mouseX.set(x);
      mouseY.set(y);
    };

    return (
      <motion.div
        ref={(node) => {
          (cardRef as any).current = node;
          if (typeof ref === 'function') ref(node);
          else if (ref) (ref as any).current = node;
        }}
        onMouseMove={handleMouseMove}
        onMouseEnter={() => setIsHovered(true)}
        onMouseLeave={() => setIsHovered(false)}
        whileHover={{ 
          scale: 1.015,
          y: -4,
          transition: { type: 'spring', stiffness: 400, damping: 25 }
        }}
        className={cn(
          'relative rounded-2xl bg-card/60 backdrop-blur-md border border-border p-6 shadow-xl transition-colors duration-300 overflow-hidden',
          champagneBorder ? 'border-accent/30 shadow-[0_0_24px_-8px_rgba(245,158,11,0.1)]' : 'hover:border-border/80',
          className
        )}
        {...(props as any)}
      >
        {/* Dynamic Interactive Border Light Glow Overlay */}
        {isHovered && (
          <motion.div
            className="absolute inset-0 z-0 pointer-events-none rounded-2xl opacity-100 transition-opacity duration-300"
            style={{
              background: `radial-gradient(400px circle at ${mouseX.get()}px ${mouseY.get()}px, ${champagneBorder ? 'rgba(245, 158, 11, 0.08)' : 'rgba(99, 102, 241, 0.08)'}, transparent 80%)`,
            }}
          />
        )}

        {/* Hover Highlight Border Gradient (Dynamic Radial Glow border helper) */}
        {isHovered && (
          <motion.div
            className="absolute inset-0 z-0 pointer-events-none rounded-2xl opacity-100 transition-opacity duration-300"
            style={{
              padding: '1px',
              background: `radial-gradient(180px circle at ${mouseX.get()}px ${mouseY.get()}px, ${champagneBorder ? 'hsl(43 96% 56% / 0.5)' : 'hsl(244 47% 49% / 0.4)'}, transparent 80%)`,
              WebkitMask: 'linear-gradient(#fff 0 0) content-box, linear-gradient(#fff 0 0)',
              WebkitMaskComposite: 'xor',
              maskComposite: 'exclude',
            }}
          />
        )}

        <div className="relative z-10">{children}</div>
      </motion.div>
    );
  }
);
Card.displayName = 'Card';

export function CardHeader({ className, ...props }: React.HTMLAttributes<HTMLDivElement>) {
  return <div className={cn('flex flex-col space-y-1.5 mb-4', className)} {...props} />;
}

export function CardTitle({ className, ...props }: React.HTMLAttributes<HTMLHeadingElement>) {
  return <h3 className={cn('text-lg font-bold tracking-tight text-foreground', className)} {...props} />;
}

export function CardDescription({ className, ...props }: React.HTMLAttributes<HTMLParagraphElement>) {
  return <p className={cn('text-sm text-muted-foreground leading-relaxed', className)} {...props} />;
}

export function CardContent({ className, ...props }: React.HTMLAttributes<HTMLDivElement>) {
  return <div className={cn('pt-0', className)} {...props} />;
}
