package taxi.share.back.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import taxi.share.back.model.Routes;
import taxi.share.back.repository.RoutesRepository;

@Slf4j
@RequiredArgsConstructor
@Service
public class RouteService {
    private final RoutesRepository routesRepository;

    public Routes save(Routes routes){
      return routesRepository.save(routes);
    };
    @Cacheable(value = "routeCache::", key = "#p[0]")
    public Routes findByRoutesByRouteNo(int routeNo) throws Exception  {
        return routesRepository.findByRouteNo(routeNo)
                .orElseThrow(() -> new Exception("Route not found"));
    }
}
