'use client';

import { motion } from 'framer-motion';
import { ReactNode } from 'react';

// Emil Kowalski inspired snappy spring physics
export const springTransition = {
  type: "spring" as const,
  stiffness: 260,
  damping: 20,
  mass: 1
};

export default function PageTransition({ children }: { children: ReactNode }) {
  return (
    <motion.div
      initial={{ opacity: 0, y: 12, filter: 'blur(4px)' }}
      animate={{ opacity: 1, y: 0, filter: 'blur(0px)' }}
      exit={{ opacity: 0, y: -12, filter: 'blur(4px)' }}
      transition={springTransition}
      className="w-full h-full"
    >
      {children}
    </motion.div>
  );
}
