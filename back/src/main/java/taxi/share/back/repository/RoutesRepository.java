package taxi.share.back.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import taxi.share.back.model.Routes;

import java.util.Optional;

@Repository
public interface RoutesRepository extends JpaRepository<Routes, Integer> {

}