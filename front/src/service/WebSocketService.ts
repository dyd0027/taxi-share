import { Client, Frame, IStompSocket } from "@stomp/stompjs";
import SockJS from "sockjs-client";

class WebSocketService {
  private client: Client;
  constructor() {
    this.client = new Client({
      webSocketFactory: () =>
        new SockJS("http://localhost:8080/ws", null, { withCredentials: true } as any) as unknown as IStompSocket,
      reconnectDelay: 5000,
      debug: (msg) => console.log(msg),
    });
  }

  connect(userId: String, onMessageReceived: (message: string) => void) {
    this.client.onConnect = (frame: Frame) => {
      console.log("Connected to WebSocket");
      this.client.subscribe(`/user/${userId}/queue/match`, (message) => {
        const body = message.body;
        onMessageReceived(body);
      });
    };

    this.client.activate(); // WebSocket 연결 시작
  }

  disconnect() {
    if (this.client.active) {
      this.client.deactivate();
      console.log("Disconnected from WebSocket");
    }
  }
}

export default new WebSocketService();
