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

export const login = async (loginFormData: LoginFormData): Promise<FormData> => {
  try {
    // param으로 보낼 때
    // const params = new URLSearchParams();
    //     params.append('userId', loginFormData.userId);
    //     params.append('userPassword', loginFormData.userPassword);

    //     const response = await axios.post(`${process.env.NEXT_PUBLIC_API_BASE_URL}/login`, params, {
    //         headers: {
    //             'Content-Type': 'application/x-www-form-urlencoded',
    //         },
    //     });
    const response = await axios.post(`${process.env.NEXT_PUBLIC_API_BASE_URL}/api/users/login`, loginFormData, {
        headers: {
            'Content-Type': 'application/json',
        },
    });
    return response.data;
  } catch (error: any) {
      if (error.response) {
          // 서버 응답이 있는 경우
          console.error('Error response:', error.response.data);
          console.error('Error status:', error.response.status);
          console.error('Error headers:', error.response.headers);
      } else if (error.request) {
          // 요청이 전송되었으나 응답이 없는 경우
          console.error('Error request:', error.request);
      } else {
          // 요청 설정 중에 발생한 에러
          console.error('Error message:', error.message);
      }
      throw error;
  }
};