import React, { useEffect, useState } from "react";
import WebSocketService from "../service/WebSocketService";

const MatchPage: React.FC = () => {
  const [message, setMessage] = useState<string | null>(null);

  useEffect(() => {
    // 메시지를 수신했을 때 처리하는 콜백
    const handleMessage = (msg: string) => {
      setMessage(msg); // 메시지를 상태로 설정
    };

    // WebSocket 연결 시작
    WebSocketService.connect(handleMessage);

    // 컴포넌트가 언마운트될 때 WebSocket 연결 종료
    return () => {
      WebSocketService.disconnect();
    };
  }, []);

  return (
    <div>
      <h1>실시간 매칭</h1>
      {message ? (
        <p>수신된 메시지: {message}</p>
      ) : (
        <p>매칭 중입니다. 잠시만 기다려 주세요...</p>
      )}
    </div>
  );
};

export default MatchPage;
