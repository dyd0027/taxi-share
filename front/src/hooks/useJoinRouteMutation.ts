// src/hooks/useFindRouteMutation.ts
import { useMutation } from '@tanstack/react-query';
import { joinRoute } from '@/api/map';
import { RouteData } from '@/types/routeData';

const useJoinRouteMutation = ({
  setMatchResult,
  setLoading,
  setError,
}: {
  setMatchResult: (data: any) => void;
  setLoading: (loading: boolean) => void;
  setError: (error: string | null) => void;
}) => {
  return useMutation({
    mutationFn: ({ sendData }: { sendData: RouteData; }) => joinRoute(sendData),
    onSuccess: (data) => {
      setMatchResult(data); // 성공 시 경로 데이터를 저장
      setLoading(false); // 로딩 상태 해제
    },
    onError: () => {
      setLoading(false); // 로딩 상태 해제
      setError('매칭 시스템에 문제가 발생했습니다.'); // 에러 메시지 설정
    },
  });
};

export default useJoinRouteMutation;