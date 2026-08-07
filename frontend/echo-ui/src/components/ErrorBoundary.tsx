'use client';

import React, { Component, ErrorInfo, ReactNode } from 'react';
import { Card, CardHeader, CardTitle, CardDescription, CardContent } from '@/components/ui/Card';
import { Button } from '@/components/ui/Button';
import { AlertCircle, RotateCcw } from 'lucide-react';

interface Props {
  children: ReactNode;
}

interface State {
  hasError: boolean;
  error?: Error;
}

export class ErrorBoundary extends Component<Props, State> {
  public state: State = {
    hasError: false,
  };

  public static getDerivedStateFromError(error: Error): State {
    return { hasError: true, error };
  }

  public componentDidCatch(error: Error, errorInfo: ErrorInfo) {
    console.error('Uncaught error in ErrorBoundary:', error, errorInfo);
  }

  public handleReset = () => {
    this.setState({ hasError: false, error: undefined });
  };

  public render() {
    if (this.state.hasError) {
      return (
        <div className="min-h-screen flex items-center justify-center p-6 bg-background">
          <Card className="max-w-md w-full" champagneBorder>
            <CardHeader>
              <div className="flex items-center gap-3">
                <AlertCircle className="w-6 h-6 text-destructive" />
                <CardTitle>Application Error</CardTitle>
              </div>
              <CardDescription>An unexpected error occurred in this view.</CardDescription>
            </CardHeader>
            <CardContent className="space-y-4">
              <p className="text-xs font-mono p-3 rounded-lg bg-muted/50 border border-border text-muted-foreground overflow-x-auto">
                {this.state.error?.message || 'Unknown Application Exception'}
              </p>
              <Button onClick={this.handleReset} variant="champagne" className="w-full gap-2">
                <RotateCcw className="w-4 h-4" /> Try Again
              </Button>
            </CardContent>
          </Card>
        </div>
      );
    }

    return this.props.children;
  }
}
