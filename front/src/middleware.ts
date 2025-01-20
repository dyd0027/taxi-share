import { NextResponse } from 'next/server';
import type { NextRequest } from 'next/server';

export function middleware(req: NextRequest) {
  const token = req.cookies.get('jwt-token'); // HttpOnly 쿠키에서 JWT 가져오기
  const excludedPaths = ['/', '/login', '/signup']; // 제외할 경로
  // 제외할 경로는 리디렉션을 건너뜀
  if (excludedPaths.includes(req.nextUrl.pathname)) {
    return token
      ? NextResponse.redirect(new URL('/share', req.url)) // 쿠키가 있으면 '/share'로 리디렉션
      : NextResponse.next(); // 쿠키가 없으면 진행
  }
  
  return token
    ? NextResponse.next() // 쿠키가 있으면 진행
    : NextResponse.redirect(new URL('/', req.url)); // 쿠키가 없으면 '/'로 리디렉션
}

export const config = {
  matcher: ['/((?!_next|favicon.ico).*)'], // 모든 경로를 포함하지만, Next.js 내부 경로는 제외
};