// src/api/user.ts
import axios from 'axios';
import { RouteData } from '@/types/routeData';

axios.defaults.withCredentials = true;

export const route = async (routeData: RouteData): Promise<RouteData> => {
  try {
    const response = await axios.get(`${process.env.NEXT_PUBLIC_API_BASE_URL}/api/map/route`, {
        headers: {
            'Content-Type': 'application/json',
        },
        params: routeData,
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