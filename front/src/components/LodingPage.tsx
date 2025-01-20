import { useEffect, useState } from 'react';
import { useRouter } from 'next/navigation'; // useRouter를 상단에서 가져옵니다.
import styles from '../css/LoadingPage.module.css'; // CSS 모듈 사용

const LoadingPage: React.FC = () => {
  const router = useRouter(); // useRouter를 여기서 호출
  const [isClient, setIsClient] = useState(false); // 클라이언트 여부 상태

  useEffect(() => {
    // 클라이언트 렌더링 여부 확인
    setIsClient(true);
  }, []);

  useEffect(() => {
    if (isClient) {
      // 매칭이 완료되면 원래 페이지로 돌아가기 위한 로직
      const timeout = setTimeout(() => {
        router.push('/'); // 원래 경로로 리다이렉션 (필요시 수정)
      }, 3000);

      return () => clearTimeout(timeout); // 컴포넌트 언마운트 시 타이머 제거
    }
  }, [isClient, router]); // router를 의존성 배열에 추가

  if (!isClient) {
    return null; // 클라이언트가 아닌 경우 아무것도 렌더링하지 않음
  }

  return (
    <div className={styles.loadingContainer}>
      <div className={styles.spinner}></div> {/* 스피너 애니메이션 */}
      <h1 className={styles.loadingText}>매칭 시스템이 작동 중입니다.</h1>
      <p className={styles.subText}>잠시만 기다려 주세요...</p>
    </div>
  );
};

export default LoadingPage;
