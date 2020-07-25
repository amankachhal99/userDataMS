package com.aman.userdata.userdatams.repository;

import com.aman.userdata.userdatams.model.UserData;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface JPAUserDataRepository extends JpaRepository<UserData, Integer> {

    @Query(value = "SELECT * FROM USER_DATA " +
            "WHERE (?1 IS NULL OR UPPER(USER_NAME) LIKE %?1%) " +
            "AND (?2 IS NULL OR AGE = ?2) " +
            "AND (?3 IS NULL OR SALE_AMOUNT = ?3) " +
            "AND (?4 IS NULL OR LAST_PURCHASE_DATE > ?4) " +
            "AND (?5 IS NULL OR LAST_PURCHASE_DATE <= ?5) " +
            "AND (?6 IS NULL OR height = ?6) " +
            "AND (?7 IS NULL OR UPPER(gender) = ?7) " +
            "ORDER BY LAST_PURCHASE_DATE DESC",
            countQuery = "SELECT count(*) FROM USER_DATA " +
                    "WHERE (?1 IS NULL OR UPPER(USER_NAME) LIKE %?1%) " +
                    "AND (?2 IS NULL OR AGE = ?2) " +
                    "AND (?3 IS NULL OR SALE_AMOUNT = ?3) " +
                    "AND (?4 IS NULL OR LAST_PURCHASE_DATE > ?4) " +
                    "AND (?5 IS NULL OR LAST_PURCHASE_DATE <= ?5) " +
                    "AND (?6 IS NULL OR height = ?6) " +
                    "AND (?7 IS NULL OR UPPER(gender) = ?7) " +
                    "ORDER BY LAST_PURCHASE_DATE DESC",
            nativeQuery = true)
    Page<UserData> findByCriteria(String userName,String age,String saleAmount,String purchaseDateFrom,
                                  String purchaseDateTo, String height, String gender, Pageable pageable);

}
