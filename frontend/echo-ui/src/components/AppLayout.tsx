'use client';

import Sidebar from '@/components/Sidebar';
import Navbar from '@/components/Navbar';
import CommandPalette from '@/components/CommandPalette';

export default function AppLayout({ children }: { children: React.ReactNode }) {
  return (
    <div className="min-h-screen bg-background text-foreground bg-cyber-grid flex flex-col">
      <Navbar />
      <div className="flex flex-1 max-w-7xl mx-auto w-full">
        <Sidebar />
        <main className="flex-1 p-6 md:p-8 overflow-y-auto max-w-5xl w-full">
          {children}
        </main>
      </div>
      <CommandPalette />
    </div>
  );
}
