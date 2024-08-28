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
    kakaoMapScript.async = true;
    kakaoMapScript.src = `//dapi.kakao.com/v2/maps/sdk.js?appkey=${process.env.NEXT_PUBLIC_KAKAO_MAP_API_KEY}&libraries=services&autoload=false`;
    document.head.appendChild(kakaoMapScript);

    kakaoMapScript.onload = () => {
      window.kakao.maps.load(() => {
        const container = document.getElementById('map');
        const options = {
          center: new window.kakao.maps.LatLng(37.5665, 126.9780), // 초기 중심 좌표를 서울로 설정
          level: 3,
        };

        const map = new window.kakao.maps.Map(container, options);

        // services 객체가 존재하는지 확인 후 Geocoder 사용
        if (window.kakao.maps.services) {
          const geocoder = new window.kakao.maps.services.Geocoder();

          // 주소를 좌표로 변환하고 마커를 생성하는 함수
          const setMarker = (address: string, title: string) => {
            geocoder.addressSearch(address, (result: any, status: any) => {
              if (status === window.kakao.maps.services.Status.OK) {
                const coords = new window.kakao.maps.LatLng(result[0].y, result[0].x);
                console.log('위도>>>',Number(result[0].y));
                console.log('경도>>>',Number(result[0].x));
                const marker = new window.kakao.maps.Marker({
                  map: map,
                  position: coords,
                  title: title, // 마커에 제목을 설정
                });

                // 지도 중심을 마커 위치로 이동
                map.setCenter(coords);
              }
            });
          };

          // origin과 destination에 대해 마커를 설정
          if (origin) {
            setMarker(origin, "출발지");
          }
          if (destination) {
            setMarker(destination, "도착지");
          }
        } else {
          console.error("Kakao Map services are not available.");
        }
      });
    };

    return () => {
      if (kakaoMapScript) {
        kakaoMapScript.onload = null;
        document.head.removeChild(kakaoMapScript);
      }
    };
  }, [origin, destination]); // origin과 destination이 변경될 때마다 useEffect 실행

  return <div id="map" style={{ width: "100%", height: "500px" }}></div>;
};

export default KakaoMap;
