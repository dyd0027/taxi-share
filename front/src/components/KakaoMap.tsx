import { RouteData } from "@/types/routeData";
import { useEffect, useRef } from "react";

declare global {
  interface Window {
    kakao: any;
  }
}

interface KakaoMapProps {
  origin?: string;
  destination?: string;
  setSendData: (sendData: RouteData | ((prevState: RouteData) => RouteData)) => void;
  routeData?: any;
}

const KakaoMap = ({ origin, destination, setSendData, routeData }: KakaoMapProps) => {
  
  const mapRef = useRef<any>(null);
  const prevOriginRef = useRef<string | undefined>();
  const prevDestinationRef = useRef<string | undefined>();

  useEffect(() => {
    const kakaoMapScript = document.createElement("script");
    kakaoMapScript.async = true;
    kakaoMapScript.src = `//dapi.kakao.com/v2/maps/sdk.js?appkey=${process.env.NEXT_PUBLIC_KAKAO_MAP_API_KEY}&libraries=services&autoload=false`;
    document.head.appendChild(kakaoMapScript);

    kakaoMapScript.onload = () => {
      window.kakao.maps.load(() => {
        const container = document.getElementById("map");
        const options = {
          center: new window.kakao.maps.LatLng(37.5665, 126.9780), // 초기 중심 좌표를 서울로 설정
          level: 3,
        };

        const map = new window.kakao.maps.Map(container, options);
        mapRef.current = map; // map 객체를 ref에 저장

        if (window.kakao.maps.services) {
          const geocoder = new window.kakao.maps.services.Geocoder();
          const setMarker = (address: string, title: string) => {
            geocoder.addressSearch(address, (result: any, status: any) => {
              if (status === window.kakao.maps.services.Status.OK) {
                const coords = new window.kakao.maps.LatLng(result[0].y, result[0].x);
                if(title === "출발지"){
                  setSendData((prevState) => ({
                    ...prevState,
                    origin: origin || "",
                    originLatitude: Number(result[0].y),
                    originLongitude: Number(result[0].x),
                  }));
                }else{
                  setSendData((prevState) => ({
                    ...prevState,
                    destination: destination || "",
                    destinationLatitude: Number(result[0].y),
                    destinationLongitude: Number(result[0].x),
                  }));
                }
                const marker = new window.kakao.maps.Marker({
                  map: map,
                  position: coords,
                  title: title,
                });
                map.setCenter(coords);
              }
            });
          };

          if (origin !== prevOriginRef.current) {
            setMarker(origin || "", "출발지");
          }

          if (destination !== prevDestinationRef.current) {
            setMarker(destination || "", "도착지");
          }

          // 이전 값을 업데이트
          prevOriginRef.current = origin;
          prevDestinationRef.current = destination;
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
  }, [origin, destination]);


  useEffect(() => {
    if (routeData && mapRef.current) {
      const map = mapRef.current;
      const linePath: any[] = [];

      
      console.log('map -> ');
      console.log(map);

      routeData.routes[0].sections[0].roads.forEach((road: any) => {
        road.vertexes.forEach((vertex: any, index: number) => {
          // x,y 좌표가 우르르 들어옵니다. 그래서 인덱스가 짝수일 때만 linePath에 넣어봅시다.
          // lat이 y이고 lng이 x입니다.
          if (index % 2 === 0) {
            linePath.push(new window.kakao.maps.LatLng(road.vertexes[index + 1], road.vertexes[index]));
          }
        });
      });

      const polyline = new window.kakao.maps.Polyline({
        path: linePath,
        strokeWeight: 5,
        strokeColor: '#000000',
        strokeOpacity: 0.7,
        strokeStyle: 'solid',
      });

      polyline.setMap(map); // 지도에 경로를 그립니다.
    }
  }, [routeData]);

  return <div id="map" style={{ width: "100%", height: "500px" }}></div>;
};

export default KakaoMap;
