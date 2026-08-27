'use client';

import React, { createContext, useContext, useState, useEffect } from 'react';

interface AuthUser {
  userId: string;
  name: string;
}

interface AuthContextType {
  user: AuthUser | null;
  login: (data: AuthUser) => void;
  logout: () => void;
  isLoading: boolean;
}

const AuthContext = createContext<AuthContextType | undefined>(undefined);

const AUTH_STORAGE_KEY = 'echo_auth_user';

export function AuthProvider({ children }: { children: React.ReactNode }) {
  const [user, setUser] = useState<AuthUser | null>(null);
  const [isLoading, setIsLoading] = useState(true);

  useEffect(() => {
    // We only store non-sensitive UI state (userId, name) in localStorage.
    // The actual authentication credential (JWT) is stored exclusively in
    // an HttpOnly cookie set by the backend and is never accessible to JavaScript.
    const stored = localStorage.getItem(AUTH_STORAGE_KEY);
    if (stored) {
      try {
        const parsed = JSON.parse(stored) as AuthUser;
        setUser(parsed);
      } catch (e) {
        localStorage.removeItem(AUTH_STORAGE_KEY);
      }
    }
    setIsLoading(false);
  }, []);

  const login = (data: AuthUser) => {
    setUser(data);
    // Store only non-sensitive UI metadata, NOT the JWT.
    localStorage.setItem(AUTH_STORAGE_KEY, JSON.stringify(data));
  };

  const logout = () => {
    setUser(null);
    localStorage.removeItem(AUTH_STORAGE_KEY);
    // The HttpOnly JWT cookie is cleared by calling the backend /api/v1/auth/logout
    // which issues a Set-Cookie with maxAge=0.
  };

  return (
    <AuthContext.Provider value={{ user, login, logout, isLoading }}>
      {children}
    </AuthContext.Provider>
  );
}

export function useAuth() {
  const context = useContext(AuthContext);
  if (context === undefined) {
    throw new Error('useAuth must be used within an AuthProvider');
  }
  return context;
}
