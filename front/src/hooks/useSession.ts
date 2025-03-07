// src/hooks/useSession.ts
import { useEffect } from 'react';
import { checkSession } from '@/api/user';
import useUserStore from '@/store/useUserStore';
import useRouteStore from '@/store/useRouteStore'

export const useSession = () => {
  const logout = useUserStore((state) => state.logout);
  const clear = useRouteStore((state) => state.clear);
  useEffect(() => {
    const verifySession = async () => {
      try {
        const loggedIn = await checkSession();
        if (!loggedIn) {
          logout(); // 세션이 유효하지 않으면 로그아웃
          clear();
        }
      } catch (error) {
        console.error('Error verifying session:', error);
        logout(); // 에러 발생 시에도 로그아웃
        clear();
      }
    };

    verifySession();
  }, [logout]);
};
