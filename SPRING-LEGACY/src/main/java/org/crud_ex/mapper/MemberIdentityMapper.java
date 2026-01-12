package org.crud_ex.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.crud_ex.domain.MemberIdentityVO;

@Mapper
public interface MemberIdentityMapper {

    void insert(MemberIdentityVO identity);

    MemberIdentityVO findByProviderAndUserId(
            @Param("provider") String provider,
            @Param("providerUserId") String providerUserId
    );
}