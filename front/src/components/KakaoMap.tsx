"use client";

import { useEffect } from "react";

declare global {
  interface Window {
    kakao: any;
  }
}

interface KakaoMapProps {
  origin?: string;
  destination?: string;
}

const KakaoMap = ({ origin, destination }: KakaoMapProps) => {
  useEffect(() => {
    const kakaoMapScript = document.createElement('script');
    kakaoMapScript.async = true; // 스크립트를 비동기로 로드
    kakaoMapScript.src = `//dapi.kakao.com/v2/maps/sdk.js?appkey=${process.env.NEXT_PUBLIC_KAKAO_MAP_API_KEY}&autoload=false`;
    document.head.appendChild(kakaoMapScript);

    kakaoMapScript.onload = () => {
      window.kakao.maps.load(() => {
        const container = document.getElementById('map');
        const options = {
          center: new window.kakao.maps.LatLng(33.450701, 126.570667),
          level: 3,
        };

        new window.kakao.maps.Map(container, options);
      });
    };

    // Cleanup script to prevent memory leaks
    return () => {
      if (kakaoMapScript) {
        kakaoMapScript.onload = null;
        document.head.removeChild(kakaoMapScript);
      }
    };
  }, []);

  return <div id="map" style={{ width: "100%", height: "500px" }}></div>;
};

export default KakaoMap;
