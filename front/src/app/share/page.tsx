"use client";

import { useEffect, useState } from 'react';
import useUserStore from '@/store/useUserStore';
import useRouteStore from '@/store/useRouteStore';
import { useSession } from '@/hooks/useSession';
import dynamic from 'next/dynamic'; // 수정된 route 함수 가져오기
import { RouteData } from '@/types/routeData'; // RouteData 타입 가져오기
import useFindRouteMutation from '@/hooks/useFindRouteMutation';
import useJoinRouteMutation from '@/hooks/useJoinRouteMutation';
import MatchPage from '@/components/MatchPage';

// KakaoMap 컴포넌트를 동적으로 불러옵니다. 이때 SSR을 비활성화합니다.
const KakaoMap = dynamic(() => import('@/components/KakaoMap'), {
  ssr: false,
});

// AddressSearch 컴포넌트를 동적으로 불러옵니다.
const AddressSearch = dynamic(() => import('@/components/AddressSearch'), {
  ssr: false,
});

// LoadingPage 컴포넌트를 동적으로 불러옵니다.
const LoadingPage = dynamic(() => import('@/components/LodingPage'), {
  ssr: false,
});

export default function SharePage() {
  const [isHydrated, setIsHydrated] = useState(false);
  const user = useUserStore((state) => state.user);
  const route = useRouteStore((state) => state.route);
  const [origin, setOrigin] = useState<string | undefined>();
  const [destination, setDestination] = useState<string | undefined>();
  const initialState: RouteData = {
    origin: '',
    destination: '',
    originLatitude: 0,
    originLongitude: 0,
    destinationLatitude: 0,
    destinationLongitude: 0,
  };
  const [sendData, setSendData] = useState<RouteData>(initialState);
  const [kakaoRouteData, setKaKaoRouteData] = useState<any>(null); // 경로 데이터를 저장할 상태
  const [loading, setLoading] = useState(false); // 로딩 상태 추가
  const [matchResult, setMatchResult] = useState<any>(null); // 매칭 결과 저장
  const [error, setError] = useState<string | null>(null); // 에러 처리
  useSession();

  useEffect(() => {
    setIsHydrated(true);
  }, []);

  const findRouteMutation = useFindRouteMutation(
    setKaKaoRouteData,
    setLoading,
    setError,
  );

  const joinRouteMutation = useJoinRouteMutation({
    setMatchResult,
    setLoading,
    setError,
  });

  const handleFindRoute = () => {
    if (!user) {
      console.error('User must be logged in to find route.');
      return;
    }
    if (origin && destination) {
      setLoading(true); // 로딩 상태 시작
      setError(null); // 에러 초기화
      findRouteMutation.mutate({ sendData, user }); // 경로 찾기 로직 실행
    } else {
      alert('Origin and destination must be provided');
      console.error('Origin and destination must be provided');
    }
  };

  const handleShareRoute = () => {
    if (route) {
      setLoading(true); // 로딩 상태 시작
      setError(null); // 에러 초기화
      joinRouteMutation.mutate(route); // RouteData 객체로 전달
    } else {
      alert('오류발생. 재시도 부탁바랍니다.');
      window.location.reload();
    }
  };

  const handleResetRoute = () => {
    setKaKaoRouteData(null);
    setOrigin(undefined);
    setDestination(undefined);
    setSendData(initialState);
  }

  if (!isHydrated) {
    return null; // 클라이언트 측에서 초기화될 때까지 아무것도 렌더링하지 않음
  }

  return (
    <main className="flex min-h-screen flex-col items-center justify-between px-24">
      <div className="min-h-screen flex flex-col items-center justify-center">
          {user && <MatchPage userId={user.userId}/>}
          <div>
            {origin && (<p>출발지: {origin}</p> )}
            {!kakaoRouteData&&<AddressSearch btn={"출발지 검색"} setPlace={setOrigin} />}
          </div>
          <div>
            {destination && (<p>도착지: {destination}</p>)}
            {!kakaoRouteData&&<AddressSearch btn={"도착지 검색"} setPlace={setDestination} />}
          </div>

          {loading ? ( // 로딩 상태일 때 로딩 화면을 표시
            <LoadingPage /> // 동적 로딩 페이지 표시
          ) : (
            kakaoRouteData ? (
              <>
                <button onClick={handleShareRoute} className="bg-blue-500 text-white p-2 m-2">택시 공유하기</button>
                <button onClick={handleResetRoute} className="bg-blue-500 text-white p-2 m-2">재탐색</button>
              </>
            ) : (
              <>
                <button onClick={handleFindRoute} className="bg-blue-500 text-white p-2 m-2">경로 찾기</button>
              </>
            )
          )}

          {/* 매칭 결과가 있을 경우 표시 */}
          {matchResult && (
            <div>
              <h2>매칭 완료!</h2>
              <p>상대방과의 거리: {matchResult.distance} km</p>
              <p>상대방 정보: {matchResult.partnerName}</p>
            </div>
          )}

          <KakaoMap origin={origin} destination={destination} setSendData={setSendData} kakaoRouteData={kakaoRouteData} />
      </div>
    </main>
  );
}
