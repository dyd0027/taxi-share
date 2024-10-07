'use client';

import dynamic from 'next/dynamic';
import { QueryClient, QueryClientProvider } from '@tanstack/react-query';

const RegisterForm = dynamic(() => import('../../components/RegisterForm'), {
  ssr: false,
});

// const queryClient = new QueryClient();

const SharePage = () => {
  return (
    // <QueryClientProvider client={queryClient}>
      <div className="min-h-screen flex items-center justify-center bg-gray-100">
        매칭 될 때 까지 기다려주세욥!!!
      </div>
    // </QueryClientProvider>
  );
};

export default SharePage;
