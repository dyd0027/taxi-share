// src/app/page.tsx
"use client";

import { useEffect, useState } from 'react';
import Link from "next/link";
import useUserStore from '@/store/useUserStore';
import { useSession } from '@/hooks/useSession'

export default function Home() {
  const [isHydrated, setIsHydrated] = useState(false);
  const user = useUserStore((state) => state.user);
  useSession();
  // 클라이언트 측에서만 실행
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
          <p className="text-xl">Hello, {user.userName}!</p>
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
