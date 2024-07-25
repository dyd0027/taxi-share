// src/api/user.ts
import axios from 'axios';

export interface FormData {
    userId: string;
    userPassword: string;
    userName: string;
    phoneNum: string;
    userSex: 'M' | 'F';
    userType: number;
}
export interface LoginFormData {
  userId: string;
  userPassword: string;
}

// axios같은 경우 fetch보다 좀 더 유연하게 백엔드와의 통신을 할 수 있어서 axios를 많이 사용 함.
export const user = async (formData: FormData): Promise<FormData> => {
  const response = await axios.post(`${process.env.NEXT_PUBLIC_API_BASE_URL}/api/users/register`, formData, {
    headers: {
      'Content-Type': 'application/json',
    },
  });
  return response.data;
};

export const login = async (loginFormData: LoginFormData): Promise<LoginFormData> => {
  const response = await axios.post(`${process.env.NEXT_PUBLIC_API_BASE_URL}/api/users/login`, loginFormData, {
    headers: {
      'Content-Type': 'application/json',
    },
  });
  console.log(response.data);
  return response.data;
};