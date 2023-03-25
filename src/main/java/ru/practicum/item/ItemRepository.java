package ru.practicum.item;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import javax.persistence.ColumnResult;
import javax.persistence.ConstructorResult;
import javax.persistence.Convert;
import javax.persistence.SqlResultSetMapping;
import java.time.LocalDate;
import java.util.List;

public interface ItemRepository extends JpaRepository<Item, Long>, ItemRepositoryCustom {
    List<Item> findByUserId(long userId);
    List<ItemInfo> findUrlByUserId(long userId);
    void deleteByUserIdAndId(long userId, long id);

    @Query("select new ru.practicum.item.ItemCountByUser(it.userId, count(it.id))" +
            "from Item as it "+
            "where it.url like %?1% "+
            "group by it.userId "+
            "order by count(it.id) desc")
    List<ItemCountByUser> countItemsByUser(String urlPart);

    @Query(value = "select it.user_id, count(it.id) as count "+
            "from items as it left join users as us on it.user_id = us.id "+
            "where (cast(us.registration_date as date)) between ?1 and ?2 "+
            "group by it.user_id", nativeQuery = true)
    List<Object[]> countByUserRegistered(LocalDate dateFrom, LocalDate dateTo);
}