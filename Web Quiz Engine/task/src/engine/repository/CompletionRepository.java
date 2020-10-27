package engine.repository;

import engine.entity.CompletionEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CompletionRepository extends PagingAndSortingRepository<CompletionEntity, Long> {
    Page<CompletionEntity> findAllByUserEmail(String userEmail, Pageable pageable);
}
