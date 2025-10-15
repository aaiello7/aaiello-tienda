package tienda.repository;

import tienda.domain.Producto;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductoRepository extends JpaRepository<Producto,Integer>{
    public List<Producto> findByActivoTrue();

}

