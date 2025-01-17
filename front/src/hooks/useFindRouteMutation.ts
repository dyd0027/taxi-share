// src/hooks/useFindRouteMutation.ts
import { useMutation } from '@tanstack/react-query';
import { route } from '@/api/map';
import { RouteData } from '@/types/routeData';
import { UserFormData } from '@/types/userFormData';
import RouteStore from '@/store/useRouteStore';

// 함수형 매개변수 방식으로 코드가 간결하나 매개변수 순서에 민감함.
const useFindRouteMutation = (
  setRouteData: (data: any) => void,
  setLoading: (loading: boolean) => void,
  setError: (error: string | null) => void
) => {
  const setRouteStore = RouteStore((state) => state.setRoute);
    // 매개변수가 한 개가 아닌경우에는 이렇게 모든 매개변수명을 다 적어주고, 타입도 지정해야 하는 번거로움이 있어서 웬만하면 타입 하나로 관리하는게 좋음.
  return useMutation<any, Error, {sendData: RouteData; user: UserFormData}>({
    mutationFn: ({ sendData, user }) => route(sendData, user),
    onSuccess: (data) => {
      setRouteStore(data.routeObj);
      setRouteData(data); // 성공 시 경로 데이터를 저장
      setLoading(false); // 로딩 상태 해제
    },
    onError: () => {
      setLoading(false);
      setError('경로 찾기에 실패했습니다.');
    },
  });
};

export default useFindRouteMutation;