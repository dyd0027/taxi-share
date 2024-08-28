"use client";

import { useEffect, useState } from 'react';
import Link from "next/link";
import useUserStore from '@/store/useUserStore';
import { useSession } from '@/hooks/useSession';
import dynamic from 'next/dynamic';
import { useMutation } from '@tanstack/react-query';
import { route } from '@/api/map'; // 수정된 route 함수 가져오기
import { RouteData } from '@/types/routeData'; // RouteData 타입 가져오기

// KakaoMap 컴포넌트를 동적으로 불러옵니다. 이때 SSR을 비활성화합니다.
const KakaoMap = dynamic(() => import('@/components/KakaoMap'), {
  ssr: false,
});
const AddressSearch = dynamic(() => import('@/components/AddressSearch'), {
  ssr: false,
});
export default function Home() {
  const [isHydrated, setIsHydrated] = useState(false);
  const user = useUserStore((state) => state.user);
  const [origin, setOrigin] = useState<string | undefined>();
  const [destination, setDestination] = useState<string | undefined>();
  useSession();

  useEffect(() => {
    setIsHydrated(true);
  }, []);

  const { mutate, data: routeData, isError, error } = useMutation({
    mutationFn: (data: RouteData) => route(data), // RouteData 객체를 전달
  });

  const handleFindRoute = () => {
    if (origin && destination) {
      mutate({ origin, destination }); // RouteData 객체로 전달
    } else {
      console.error('Origin and destination must be provided');
    }
  };

  if (!isHydrated) {
    return null; // 클라이언트 측에서 초기화될 때까지 아무것도 렌더링하지 않음
  }

  return (
    <main className="flex min-h-screen flex-col items-center justify-between p-24">
      <div className="min-h-screen flex flex-col items-center justify-center">
        <h1 className="text-3xl font-bold mb-4">Welcome to the Home Page</h1>
        {user ? (
          <>
            <div>
              {origin && origin}
              <AddressSearch btn={"출발지 검색"} setPlace={setOrigin} />
            </div>
            <div>
              {destination && destination}
              <AddressSearch btn={"도착지 검색"} setPlace={setDestination} />
            </div>
            <button onClick={handleFindRoute}>경로 찾기</button>

            {isError && <p>Error: {error.message}</p>}
            {routeData && (
              <div>
                <h3>경로 정보:</h3>
                <pre>{JSON.stringify(routeData, null, 2)}</pre>
              </div>
            )}
            <KakaoMap origin={origin} destination={destination} />
          </>
        ) : (
          <>
            <Link href="/signup">
              <span className="text-blue-500 underline hover:text-blue-700">Go to Signup</span>
            </Link>
            <Link href="/login">
              <span className="text-blue-500 underline hover:text-blue-700">Go to Login</span>
            </Link>
          </>
        )}
      </div>
    </main>
  );
}
