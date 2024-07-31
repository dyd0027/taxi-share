'use client';

import dynamic from 'next/dynamic';
import { QueryClient, QueryClientProvider } from '@tanstack/react-query';

const LoginForm = dynamic(() => import('../../components/LoginForm'), {
  ssr: false,
});

// const queryClient = new QueryClient();

const LoginPage = () => {
  return (
    // <QueryClientProvider client={queryClient}>
      <div className="min-h-screen flex items-center justify-center bg-gray-100">
        <LoginForm />
      </div>
    // </QueryClientProvider>
  );
};

export default LoginPage;
