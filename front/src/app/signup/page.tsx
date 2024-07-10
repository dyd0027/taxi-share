'use client';

import RegisterForm from '../../components/RegisterForm';
import { QueryClient, QueryClientProvider } from '@tanstack/react-query';

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