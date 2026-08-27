export default function OnboardingLayout({ children }: { children: React.ReactNode }) {
  return (
    <div className="min-h-screen bg-zinc-950 flex flex-col items-center py-20 relative overflow-hidden">
      {/* Background visual effects */}
      <div className="absolute top-0 w-full h-[500px] bg-accent/5 rounded-full blur-[150px] pointer-events-none" />
      <div className="absolute inset-0 bg-noise opacity-30 mix-blend-overlay pointer-events-none" />
      
      <div className="z-10 w-full max-w-2xl px-6">
        {children}
      </div>
    </div>
  );
}
