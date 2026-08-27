import { Variants } from 'framer-motion';

export const EASE = [0.16, 1, 0.3, 1] as const;

export const springTransition = {
  type: 'spring' as const,
  stiffness: 300,
  damping: 24,
};

export const pageVariants: Variants = {
  initial: { opacity: 0, y: 10, filter: 'blur(10px)' },
  animate: { 
    opacity: 1, 
    y: 0, 
    filter: 'blur(0px)',
    transition: {
      duration: 0.6,
      ease: EASE
    }
  },
  exit: { 
    opacity: 0, 
    y: -10, 
    filter: 'blur(5px)',
    transition: {
      duration: 0.3,
      ease: EASE
    }
  }
};

export const containerVariants: Variants = {
  hidden: { opacity: 0 },
  show: {
    opacity: 1,
    transition: {
      staggerChildren: 0.1,
      ease: EASE
    }
  }
};

export const itemVariants: Variants = {
  hidden: { opacity: 0, y: 15, filter: 'blur(8px)' },
  show: { 
    opacity: 1, 
    y: 0, 
    filter: 'blur(0px)',
    transition: {
      duration: 0.5,
      ease: EASE
    }
  }
};

export const magneticHover = {
  scale: 1.02,
  y: -2,
  transition: springTransition
};
