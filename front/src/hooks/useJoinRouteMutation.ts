// src/hooks/useFindRouteMutation.ts
import { useMutation } from '@tanstack/react-query';
import { joinRoute } from '@/api/map';
import { RouteData } from '@/types/routeData';

// 객체 매개변수 방식으로 코드가 길어지나 매개변수가 객체로 넘어가서 매개변수 순서에 민감하지 않고, 매개변수 명과 일치시켜야함.
const useJoinRouteMutation = ({
  setMatchResult,
  setLoading,
  setError
}: {
  setMatchResult: (data: any) => void;
  setLoading: (loading: boolean) => void;
  setError: (error: string | null) => void;
}) => {
  // 매개변수가 한 개인 경우에는 타입만 지정해주면 되기 때문에 메소드명만 적어줘도 됨 -> 코드 간결해짐. 비교: useFindRouteMutation.ts
  return useMutation<String, Error, RouteData>({
    mutationFn: joinRoute,
    onSuccess: (data) => {
      setMatchResult(data); // 성공 시 경로 데이터를 저장
      setLoading(false); // 로딩 상태 해제
    },
    onError: () => {
      setLoading(false);
      setError('매칭 시스템에 문제가 발생했습니다.');
    },
  });
};

export default useJoinRouteMutation;