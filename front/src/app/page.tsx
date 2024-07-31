// src/app/page.tsx
"use client";

import { useEffect, useState } from 'react';
import { useQueryClient } from '@tanstack/react-query';
import Link from "next/link";
import { FormData } from '../api/user';

export default function Home() {
  const queryClient = useQueryClient();
  const [user, setUser] = useState<FormData | undefined>(undefined);

  useEffect(() => {
    const fetchUser = async () => {
      const userData = queryClient.getQueryData<FormData>(['user']);
      console.log("user >>>>> ", userData);
      setUser(userData);
    };

    fetchUser();
  }, [queryClient]); // queryClient가 변경될 때마다 fetchUser를 호출

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
