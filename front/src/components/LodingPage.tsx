import { useEffect } from 'react';
import { useRouter } from 'next/router';
import styles from '../css/LoadingPage.module.css'; // CSS 모듈 사용

const LoadingPage: React.FC = () => {
  const router = useRouter();

  useEffect(() => {
    // 매칭이 완료되면 원래 페이지로 돌아가기 위한 로직
    // 3초 후에 원래 페이지로 돌아가는 예시 (실제 매칭 완료 여부에 따라 동작)
    const timeout = setTimeout(() => {
      router.push('/'); // 원래 경로로 리다이렉션 (필요시 수정)
    }, 3000);

    return () => clearTimeout(timeout); // 컴포넌트 언마운트 시 타이머 제거
  }, [router]);

  return (
    <div className={styles.loadingContainer}>
      <div className={styles.spinner}></div> {/* 스피너 애니메이션 */}
      <h1 className={styles.loadingText}>매칭 시스템이 작동 중입니다.</h1>
      <p className={styles.subText}>잠시만 기다려 주세요...</p>
    </div>
  );
};

export default LoadingPage;
