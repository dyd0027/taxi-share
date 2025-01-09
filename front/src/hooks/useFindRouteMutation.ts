// src/hooks/useFindRouteMutation.ts
import { useMutation } from '@tanstack/react-query';
import { route } from '@/api/map';
import { RouteData } from '@/types/routeData';
import { UserFormData } from '@/types/userFormData';

const useFindRouteMutation = ({
  setRouteData,
  setLoading,
  setError,
}: {
  setRouteData: (data: any) => void;
  setLoading: (loading: boolean) => void;
  setError: (error: string | null) => void;
}) => {
  return useMutation({
    mutationFn: ({ sendData, user }: { sendData: RouteData; user: UserFormData }) => route(sendData, user),
    onSuccess: (data) => {
      setRouteData(data); // 성공 시 경로 데이터를 저장
      setLoading(false); // 로딩 상태 해제
    },
    onError: () => {
      setLoading(false); // 로딩 상태 해제
      setError('경로 찾기에 실패했습니다.'); // 에러 메시지 설정
    },
  });
};

export default useFindRouteMutation;