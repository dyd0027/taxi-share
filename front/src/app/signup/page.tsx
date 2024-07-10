'use client';

import dynamic from 'next/dynamic';
import { QueryClient, QueryClientProvider } from '@tanstack/react-query';

const RegisterForm = dynamic(() => import('../../components/RegisterForm'), {
  ssr: false,
});

const queryClient = new QueryClient();

const RegisterPage = () => {
  return (
    <QueryClientProvider client={queryClient}>
      <div className="min-h-screen flex items-center justify-center bg-gray-100">
        <RegisterForm />
      </div>
    </QueryClientProvider>
  );
};

export default RegisterPage;
