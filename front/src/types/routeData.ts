export interface RouteData {
    routeNo?: number;
    origin: string;
    destination: string;
    originLatitude: number;
    originLongitude: number;
    destinationLatitude: number;
    destinationLongitude: number;
    fare?: number;
    toll?: number;
    status?: number;
    personCnt?: number;
}