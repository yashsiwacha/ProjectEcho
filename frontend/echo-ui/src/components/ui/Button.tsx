import React from 'react';
import { cva, type VariantProps } from 'class-variance-authority';
import { motion, HTMLMotionProps } from 'framer-motion';
import { cn } from '@/lib/utils';
import { magneticHover } from '@/lib/motion';

const buttonVariants = cva(
  'inline-flex items-center justify-center rounded-xl text-sm font-medium transition-all focus-visible:outline-none focus-visible:ring-2 focus-visible:ring-ring disabled:pointer-events-none disabled:opacity-50 h-11 px-5 py-2.5 cursor-pointer relative overflow-hidden',
  {
    variants: {
      variant: {
        default: 'bg-primary text-primary-foreground shadow-[inset_0_1px_1px_rgba(255,255,255,0.2)] hover:bg-primary/90',
        champagne: 'bg-accent text-accent-foreground font-bold shadow-[inset_0_1px_1px_rgba(255,255,255,0.4),0_0_20px_rgba(251,191,36,0.3)] hover:shadow-[inset_0_1px_1px_rgba(255,255,255,0.5),0_0_24px_rgba(251,191,36,0.5)]',
        outline: 'border border-border bg-transparent hover:bg-muted text-foreground backdrop-blur-md',
        secondary: 'bg-secondary/80 text-secondary-foreground hover:bg-secondary backdrop-blur-md',
        ghost: 'hover:bg-muted/50 text-foreground',
        destructive: 'bg-destructive text-destructive-foreground hover:bg-destructive/90 shadow-[inset_0_1px_1px_rgba(255,255,255,0.2)]',
      },
      size: {
        default: 'h-11 px-5 py-2.5',
        sm: 'h-9 rounded-lg px-3 text-xs',
        lg: 'h-12 rounded-xl px-8 text-base',
      },
    },
    defaultVariants: {
      variant: 'default',
      size: 'default',
    },
  }
);

export interface ButtonProps
  extends Omit<HTMLMotionProps<"button">, "color" | "translate" | "children">,
    VariantProps<typeof buttonVariants> {
    asChild?: boolean;
    children?: React.ReactNode;
}

export const Button = React.forwardRef<HTMLButtonElement, ButtonProps>(
  ({ className, variant, size, children, ...props }, ref) => {
    return (
      <motion.button
        className={cn(buttonVariants({ variant, size, className }))}
        ref={ref}
        whileHover={magneticHover}
        whileTap={{ scale: 0.98 }}
        {...props}
      >
        <span className="relative z-10 flex items-center justify-center gap-2">{children}</span>
        {variant === 'champagne' && (
          <div className="absolute inset-0 bg-gradient-to-r from-transparent via-white/10 to-transparent -translate-x-full hover:animate-shimmer" />
        )}
      </motion.button>
    );
  }
);
Button.displayName = 'Button';
