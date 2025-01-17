export interface RouteData {
    routeNo?: number;
    rtUserNo?: number;
    origin: string;
    destination: string;
    originLatitude: number;
    originLongitude: number;
    destinationLatitude: number;
    destinationLongitude: number;
    distanceM?: number;
    startTime?: string;
    endTime?: string;
    fare?: number;
    toll?: number;
    paidFare?: number;
    status?: number;
    personCnt?: number;
}