import { useMutation } from '@tanstack/react-query';
import { useRouter } from 'next/navigation';
import { login } from '@/api/user';
import useUserStore from '@/store/useUserStore';
import { UserFormData } from '@/types/userFormData';
import { LoginFormData } from '@/types/loginFormData';

const useLoginMutation = () => {
  const router = useRouter();
  const setUserStore = useUserStore((state) => state.setUser);

  return useMutation<UserFormData, Error, LoginFormData>({
    mutationFn: login,
    onSuccess: (data) => {
      setUserStore(data);
      router.push('/'); // 성공 시 리다이렉트
    },
    onError: (error: Error) => {
      console.error('로그인 실패:', error.message);
    },
  });
};

export default useLoginMutation;
