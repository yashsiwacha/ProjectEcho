export default function AuthLayout({ children }: { children: React.ReactNode }) {
  return (
    <div className="min-h-screen grid lg:grid-cols-2">
      <div className="flex flex-col justify-center items-center p-8 lg:p-24 relative overflow-hidden">
        {/* Animated background subtle glow */}
        <div className="absolute top-0 left-0 w-[500px] h-[500px] bg-accent/10 rounded-full blur-[100px] -translate-x-1/2 -translate-y-1/2 pointer-events-none" />
        
        <div className="w-full max-w-sm z-10">
          {children}
        </div>
      </div>

      <div className="hidden lg:flex flex-col justify-center p-24 bg-zinc-950 border-l border-white/5 relative overflow-hidden">
        <div className="absolute inset-0 bg-noise opacity-30 mix-blend-overlay pointer-events-none" />
        
        <div className="z-10 max-w-md">
          <div className="w-12 h-12 bg-white/10 rounded-xl flex items-center justify-center mb-8 border border-white/10 backdrop-blur-md">
            <svg className="w-6 h-6 text-accent" fill="none" viewBox="0 0 24 24" stroke="currentColor">
              <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={1.5} d="M13 10V3L4 14h7v7l9-11h-7z" />
            </svg>
          </div>
          <h1 className="text-4xl font-bold tracking-tight text-white mb-6 leading-tight">
            The Evidence-Based Career Intelligence Platform.
          </h1>
          <p className="text-lg text-slate-400 font-mono tracking-tight leading-relaxed">
            ProjectEcho anchors your verified skills to deterministic domain proofs, creating an incontrovertible Career Passport.
          </p>
        </div>
      </div>
    </div>
  );
}
