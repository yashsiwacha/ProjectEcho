import Providers from './providers';
import './globals.css';
import CommandPalette from '@/components/CommandPalette';

export const metadata = {
  title: 'ProjectEcho — Career Operating System',
  description: 'Evidence-based career intelligence platform backed by deterministic Rule Engine authority.',
};

export default function RootLayout({ children }: { children: React.ReactNode }) {
  return (
    <html lang="en" suppressHydrationWarning>
      <body className="antialiased selection:bg-accent/20 selection:text-foreground">
        <Providers>
          <div className="fixed inset-0 z-[-1] bg-noise" />
          <CommandPalette />
          <div className="max-w-[1600px] mx-auto w-full relative">
            {children}
          </div>
        </Providers>
      </body>
    </html>
  );
}
