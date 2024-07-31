// src/components/RegisterForm.tsx
'use client';

import { useState } from 'react';
import { useRouter } from 'next/navigation';
import { useMutation } from '@tanstack/react-query';
import { FormData, user } from '../api/user';

const RegisterForm = () => {
  const [formData, setFormData] = useState<FormData>({
    userId: '',
    userPassword: '',
    userName: '',
    phoneNum: '',
    userSex: 'M',
    userType: 1,
  });

  const router = useRouter();

  const mutation = useMutation<FormData, Error, FormData>({
    mutationFn: user,
    onSuccess: () => {
      router.push('/'); // 회원가입 성공 시 리다이렉트
    },
    onError: (error: Error) => {
      console.error('회원가입 실패:', error);
    },
  });

  const handleChange = (e: React.ChangeEvent<HTMLInputElement | HTMLSelectElement>) => {
    const { name, value } = e.target;

    const numericFields = ['userType']; // 숫자형으로 변환해야 할 필드 목록
    
    setFormData({
      ...formData,
      [name]: numericFields.includes(name) ? Number(value) : value,
    });

    
  };

  const handleSubmit = (e: React.FormEvent) => {
    e.preventDefault();
    mutation.mutate(formData);
  };

  return (
    <form onSubmit={handleSubmit} className="bg-white p-6 rounded shadow-md w-full max-w-md mx-auto">
      <h2 className="text-2xl mb-4">회원가입</h2>
      <input
        type="text"
        name="userId"
        placeholder="아이디"
        value={formData.userId}
        onChange={handleChange}
        className="border p-2 w-full mb-4"
        required
      />
      <input
        type="password"
        name="userPassword"
        placeholder="비밀번호"
        value={formData.userPassword}
        onChange={handleChange}
        className="border p-2 w-full mb-4"
        required
      />
      <input
        type="text"
        name="userName"
        placeholder="이름"
        value={formData.userName}
        onChange={handleChange}
        className="border p-2 w-full mb-4"
        required
      />
      <input
        type="text"
        name="phoneNum"
        placeholder="전화번호 (예: 010-1111-1111)"
        value={formData.phoneNum}
        onChange={handleChange}
        className="border p-2 w-full mb-4"
        required
      />
      <select
        name="userSex"
        value={formData.userSex}
        onChange={handleChange}
        className="border p-2 w-full mb-4"
        aria-label="userSex"
      >
        <option value="M">남성</option>
        <option value="F">여성</option>
      </select>
      <select
        name="userType"
        value={formData.userType}
        onChange={handleChange}
        className="border p-2 w-full mb-4"
        aria-label="userType"
      >
        <option value={1}>이용자</option>
        <option value={2}>운전자</option>
      </select>
      <button type="submit" className="bg-blue-500 text-white p-2 rounded w-full">
        회원가입
      </button>
    </form>
  );
};

export default RegisterForm;
