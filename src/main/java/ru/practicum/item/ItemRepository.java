package ru.practicum.item;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.user.User;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@RepositoryRestResource
public interface ItemRepository extends JpaRepository<Item, Long>, ItemRepositoryCustom,
        QuerydslPredicateExecutor<Item> {
    List<Item> findByUserId(long userId);

    List<ItemInfo> findUrlByUserId(long userId);

    void deleteByUserIdAndId(long userId, long id);

    Optional<Item> findByUserAndResolvedUrl(User user, String resolvedUrl);

    @Query("select new ru.practicum.item.ItemCountByUser(it.user.id, count(it.id))" +
            "from Item as it " +
            "where it.url like %?1% " +
            "group by it.user.id " +
            "order by count(it.id) desc")
    List<ItemCountByUser> countItemsByUser(String urlPart);

    @Query(value = "select us.id, count(it.id) as count " +
            "from items as it left join users as us on it.id = us.id " +
            "where (cast(us.registration_date as date)) between ?1 and ?2 " +
            "group by us.id", nativeQuery = true)
    List<Object[]> countByUserRegistered(LocalDate dateFrom, LocalDate dateTo);

    @Query("select it " +
            "from Item as it " +
            "join it.user as u " +
            "where u.lastName like concat(?1, '%') ")
    List<Item> findItemsByLastNamePrefix(String lastNamePrefix);

    //NOTE: Below is alternative to above
    List<Item> findAllByUserLastNameStartingWith(String lastNamePrefix);

    @Modifying(clearAutomatically = true)
    @Transactional
    @Query("update Item i set i.unread = ?2 where i.id = ?1")
    void modifyReadStatus(Long itemId, Boolean unread);
}