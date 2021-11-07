package com.sgu.agency.dal.repository;

import com.sgu.agency.dal.entity.GrantPermission;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface IGrantPermissionRepository extends JpaRepository<GrantPermission, String> {
    @Query("select u from GrantPermission u where u.role.id = ?1")
    List<GrantPermission> getRoleId(String roleId);

    @Modifying
    @Query("delete from GrantPermission g where (g.role.id in :roleIdList) and (g.permission.code in :permissionCodeList)")
    void deleteByPermissionListAndRoleList(@Param("permissionCodeList") List<String> permissionCodeList, @Param("roleIdList") List<String> roleIdList);
}
