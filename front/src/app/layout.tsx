// src/app/layout.tsx
import type { Metadata } from "next";
import "./globals.css";
import QueryProvider from './QueryProvider';

export const metadata: Metadata = {
  title: "Taxi Share",
  description: "경로가 비슷한 유저들끼리 택시비를 나눠내는 서비스",
};

export default function RootLayout({
  children,
}: Readonly<{
  children: React.ReactNode;
}>) {
  return (
    <html lang="en">
      <body>
        <header>
          <h1 className="flex text-3xl font-bold justify-center">택시 경로 공유하기</h1>
        </header>
        <QueryProvider>
          {children}
        </QueryProvider>
      </body>
    </html>
  );
}
