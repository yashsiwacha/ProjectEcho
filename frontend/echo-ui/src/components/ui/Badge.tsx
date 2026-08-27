import { cn } from '@/lib/utils';

interface BadgeProps extends React.HTMLAttributes<HTMLSpanElement> {
  variant?: 'default' | 'success' | 'warning' | 'destructive' | 'champagne';
}

export function Badge({ className, variant = 'default', ...props }: BadgeProps) {
  const variantStyles = {
    default: 'bg-muted text-muted-foreground border-border',
    success: 'bg-muted text-foreground border-border dark:text-foreground',
    warning: 'bg-muted text-foreground border-border dark:text-foreground',
    destructive: 'bg-destructive/10 text-destructive border-destructive/20',
    champagne: 'bg-accent/10 text-accent border-accent/20 font-semibold',
  };

  return (
    <span
      className={cn(
        'inline-flex items-center rounded-full border px-3 py-1 text-xs font-medium transition-colors',
        variantStyles[variant],
        className
      )}
      {...props}
    />
  );
}
