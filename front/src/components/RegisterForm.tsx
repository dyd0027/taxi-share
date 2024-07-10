'use client';

import { useState } from 'react';
import { useRouter } from 'next/navigation';
import { useMutation } from '@tanstack/react-query';

interface FormData {
  userId: string;
  userPassword: string;
  userName: string;
  phoneNum: string;
  userSex: 'M' | 'F';
}

const registerUser = async (formData: FormData): Promise<FormData> => {
  const response = await fetch('/api/users/register', {
    method: 'POST',
    headers: {
      'Content-Type': 'application/json',
    },
    body: JSON.stringify(formData),
  });

  if (!response.ok) {
    throw new Error('Network response was not ok');
  }

  return response.json();
};

const RegisterForm = () => {
  const [formData, setFormData] = useState<FormData>({
    userId: '',
    userPassword: '',
    userName: '',
    phoneNum: '',
    userSex: 'M',
  });

  const router = useRouter();

  const mutation = useMutation<FormData, Error, FormData>({
    mutationFn: registerUser,
    onSuccess: () => {
      router.push('/success'); // 회원가입 성공 시 리다이렉트
    },
    onError: (error: Error) => {
      console.error('회원가입 실패:', error);
    },
  });

  const handleChange = (e: React.ChangeEvent<HTMLInputElement | HTMLSelectElement>) => {
    setFormData({
      ...formData,
      [e.target.name]: e.target.value,
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
      >
        <option value="M">남성</option>
        <option value="F">여성</option>
      </select>
      <button type="submit" className="bg-blue-500 text-white p-2 rounded w-full">
        회원가입
      </button>
    </form>
  );
};

export default RegisterForm;
