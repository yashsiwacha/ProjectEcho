import Providers from './providers';
import './globals.css';

export const metadata = {
  title: 'ProjectEcho — Career Operating System',
  description: 'Evidence-based career intelligence platform backed by deterministic Rule Engine authority.',
};

export default function RootLayout({ children }: { children: React.ReactNode }) {
  return (
    <html lang="en">
      <body>
        <Providers>{children}</Providers>
      </body>
    </html>
  );
}
