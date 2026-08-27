import PageTransition from '@/components/motion/PageTransition';
import { ReactNode } from 'react';

export default function Template({ children }: { children: ReactNode }) {
  // In Next.js App Router, templates create a new instance for each route,
  // making them perfect for triggering framer-motion page transitions.
  return <PageTransition>{children}</PageTransition>;
}
