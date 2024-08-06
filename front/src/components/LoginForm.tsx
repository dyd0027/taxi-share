// src/components/LoginForm.tsx
'use client';

import { useState } from 'react';
import { useRouter } from 'next/navigation';
import { useMutation } from '@tanstack/react-query';
import { login } from '../api/user';
import { FormData } from '@/types/formData';
import { LoginFormData } from '@/types/loginFormData';
import useUserStore from '@/store/useUserStore';

const LoginForm = () => {
  const [loginFormData, setLoginFormData] = useState<LoginFormData>({
    userId: '',
    userPassword: '',
  });

  const router = useRouter();
  const setUserStore = useUserStore((state) => state.setUser);
  const mutation = useMutation<FormData, Error, LoginFormData>({
    mutationFn: login,
    onSuccess: (data) => {
      setUserStore(data);
      router.push('/'); // 회원가입 성공 시 리다이렉트
    },
    onError: (error: Error) => {
      console.error('로그인 실패:', error);
    },
  });

  const handleChange = (e: React.ChangeEvent<HTMLInputElement | HTMLSelectElement>) => {
    const { name, value } = e.target;
    setLoginFormData({
      ...loginFormData,
      [name]: value,
    });
  };

  const handleSubmit = (e: React.FormEvent) => {
    e.preventDefault();
    mutation.mutate(loginFormData);
  };

  return (
    <form onSubmit={handleSubmit} className="bg-white p-6 rounded shadow-md w-full max-w-md mx-auto">
      <h2 className="text-2xl mb-4">로그인</h2>
      <input
        type="text"
        name="userId"
        placeholder="아이디"
        value={loginFormData.userId}
        onChange={handleChange}
        className="border p-2 w-full mb-4"
        required
      />
      <input
        type="password"
        name="userPassword"
        placeholder="비밀번호"
        value={loginFormData.userPassword}
        onChange={handleChange}
        className="border p-2 w-full mb-4"
        required
      />
      <button type="submit" className="bg-blue-500 text-white p-2 rounded w-full">
        로그인
      </button>
    </form>
  );
};

export default LoginForm;
