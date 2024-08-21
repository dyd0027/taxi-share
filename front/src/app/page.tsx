"use client";

import { useEffect, useState } from 'react';
import Link from "next/link";
import useUserStore from '@/store/useUserStore';
import { useSession } from '@/hooks/useSession';
import dynamic from 'next/dynamic';
import AddressSearch from '@/components/AddressSearch';

// KakaoMap 컴포넌트를 동적으로 불러옵니다. 이때 SSR을 비활성화합니다.
const KakaoMap = dynamic(() => import('@/components/KakaoMap'), {
  ssr: false,
});

export default function Home() {
  const [isHydrated, setIsHydrated] = useState(false);
  const user = useUserStore((state) => state.user);
  const [origin, setOrigin] = useState<string | undefined>();
  const [destination, setDestination] = useState<string | undefined>();
  const [isSelectingOrigin, setIsSelectingOrigin] = useState<boolean>(false);

  const handleSelectAddress = (address: string) => {
    if (isSelectingOrigin) {
      setOrigin(address);
    } else {
      setDestination(address);
    }
  };

  useSession();

  useEffect(() => {
    setIsHydrated(true);
  }, []);

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
              <p>출발지: {origin}</p>
              <button
                onClick={() => setIsSelectingOrigin(true)}
                className="bg-blue-500 text-white p-2 m-2"
              >
                출발지 검색
              </button>
            </div>
            <div>
              <p>도착지: {destination}</p>
              <button
                onClick={() => setIsSelectingOrigin(false)}
                className="bg-blue-500 text-white p-2 m-2"
              >
                도착지 검색
              </button>
            </div>
            <AddressSearch onSelectAddress={handleSelectAddress} />
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
