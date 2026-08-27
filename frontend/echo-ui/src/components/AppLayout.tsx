'use client';

import { useEffect } from 'react';
import { useRouter } from 'next/navigation';
import { useAuth } from '@/lib/AuthContext';
import Sidebar from '@/components/Sidebar';
import Navbar from '@/components/Navbar';
import { motion } from 'framer-motion';
import { pageVariants } from '@/lib/motion';

export default function AppLayout({ children }: { children: React.ReactNode }) {
  const router = useRouter();
  const { user, isLoading } = useAuth();

  useEffect(() => {
    if (!isLoading && !user) {
      router.push('/auth/sign-in');
    }
  }, [user, isLoading, router]);

  if (isLoading || !user) {
    return <div className="min-h-screen bg-transparent flex items-center justify-center"><div className="w-8 h-8 rounded-full border-4 border-accent border-t-transparent animate-spin" /></div>;
  }

  return (
    <div className="relative min-h-screen bg-background overflow-hidden">
      {/* Animated cyber grid background layer */}
      <div className="absolute inset-0 bg-cyber-grid animate-cyber-grid opacity-5 pointer-events-none" />
      {/* Ambient background glows */}
      <div className="absolute top-1/3 left-1/4 w-[400px] h-[400px] bg-glow-indigo rounded-full pointer-events-none blur-3xl" />
      <div className="absolute bottom-1/3 right-1/4 w-[400px] h-[400px] bg-glow-amber rounded-full pointer-events-none blur-3xl" />

      <div className="min-h-screen bg-transparent flex flex-col relative z-10">
        <Navbar />
        <div className="flex flex-1 max-w-7xl mx-auto w-full relative z-10 pt-6">
          <Sidebar />
          <motion.main 
            initial="initial"
            animate="animate"
            exit="exit"
            variants={pageVariants}
            className="flex-1 p-6 md:p-8 overflow-y-auto max-w-5xl w-full"
          >
            {children}
          </motion.main>
        </div>
      </div>
    </div>
  );
}
